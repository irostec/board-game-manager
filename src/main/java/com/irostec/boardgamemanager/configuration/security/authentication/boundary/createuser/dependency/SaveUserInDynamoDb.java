package com.irostec.boardgamemanager.configuration.security.authentication.boundary.createuser.dependency;

import com.irostec.boardgamemanager.common.utility.LoggingUtils;
import com.irostec.boardgamemanager.configuration.security.authentication.core.createuser.dependency.SaveUser;
import com.irostec.boardgamemanager.configuration.security.authentication.boundary.shared.dynamodb.DynamoDbUser;
import com.irostec.boardgamemanager.configuration.security.authentication.boundary.shared.dynamodb.DynamoDbUserTable;
import com.irostec.boardgamemanager.configuration.security.authentication.boundary.shared.dynamodb.Email;
import com.irostec.boardgamemanager.configuration.security.authentication.core.createuser.error.PersistenceFailure;
import com.irostec.boardgamemanager.configuration.security.authentication.core.createuser.dependency.ValidatedUserCreationData;
import com.irostec.boardgamemanager.configuration.security.authentication.core.getrolesandprivileges.output.BGMRole;
import io.vavr.control.Either;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.model.TransactPutItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.TransactWriteItemsEnhancedRequest;

import java.util.Set;

/**
 * SaveUserInDynamoDb
 * Implementation of SaveUser backed by DynamoDB
 */
@AllArgsConstructor
public final class SaveUserInDynamoDb implements SaveUser {

    private static final Set<BGMRole> DEFAULT_ROLES = Set.of(BGMRole.USER);

    private final Logger logger = LogManager.getLogger(SaveUserInDynamoDb.class);

    private final DynamoDbUserTable dynamoDbUserTable;
    private final DynamoDbEnhancedClient enhancedClient;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Either<PersistenceFailure, Void> execute(ValidatedUserCreationData userData) {

        DynamoDbUser dynamoDbUser = this.buildNewUser(userData);
        Email email = this.buildNewEmail(userData.email().value());

        final String conditionExpression = String.format("attribute_not_exists(%s)", DynamoDbUserTable.Attributes.PARTITION_KEY);

        LoggingUtils.info(logger, "execute", "Attempting to create a user in DynamoDB.", dynamoDbUser, email);

        return Try.runRunnable(() ->
                enhancedClient.transactWriteItems(
                    TransactWriteItemsEnhancedRequest.builder()
                            .addPutItem(
                                    dynamoDbUserTable.projections().userProjection(),
                                    TransactPutItemEnhancedRequest.builder(DynamoDbUser.class)
                                            .item(dynamoDbUser)
                                            .conditionExpression(Expression.builder().expression(conditionExpression).build())
                                            .build()
                            )
                            .addPutItem(
                                    dynamoDbUserTable.projections().emailProjection(),
                                    TransactPutItemEnhancedRequest.builder(Email.class)
                                            .item(email)
                                            .conditionExpression(Expression.builder().expression(conditionExpression).build())
                                            .build()
                            )
                            .build()
                )
        )
        .toEither()
        .mapLeft(PersistenceFailure::new);

    }

    private DynamoDbUser buildNewUser(ValidatedUserCreationData userData) {

        final String username = userData.username().value();
        final String hashedPassword = passwordEncoder.encode(userData.password().value());
        final String partitionKeyForUser = dynamoDbUserTable.keyFactory().buildPartitionKeyForUser(username);
        final String sortKeyForUser = dynamoDbUserTable.keyFactory().buildSortKeyForUser(username);

        DynamoDbUser dynamoDbUser = new DynamoDbUser();
        dynamoDbUser.setPk(partitionKeyForUser);
        dynamoDbUser.setSk(sortKeyForUser);
        dynamoDbUser.setUsername(username);
        dynamoDbUser.setPassword(hashedPassword);
        dynamoDbUser.setEmail(userData.email().value());
        dynamoDbUser.setRoles(DEFAULT_ROLES);
        dynamoDbUser.setAccountNonExpired(true);
        dynamoDbUser.setAccountNonLocked(true);
        dynamoDbUser.setCredentialsNonExpired(true);
        dynamoDbUser.setEnabled(true);

        return dynamoDbUser;

    }

    private Email buildNewEmail(String email) {

        final String partitionKeyForEmail = dynamoDbUserTable.keyFactory().buildKey("USEREMAIL", email);
        final String sortKeyForEmail = partitionKeyForEmail;

        Email result = new Email();
        result.setPk(partitionKeyForEmail);
        result.setSk(sortKeyForEmail);

        return result;

    }

}
