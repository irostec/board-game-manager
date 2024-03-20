package com.irostec.boardgamemanager.configuration.security.user.boundary.persistence;

import com.irostec.boardgamemanager.common.utility.ExceptionUtils;
import com.irostec.boardgamemanager.common.utility.LoggingUtils;
import com.irostec.boardgamemanager.configuration.security.user.CreateUser;
import com.irostec.boardgamemanager.configuration.security.user.input.ValidatedUserCreationData;
import com.irostec.boardgamemanager.configuration.security.user.output.BGMRole;
import io.atlassian.fugue.*;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.model.TransactPutItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.TransactWriteItemsEnhancedRequest;

import java.util.Set;

/**
 * CreateUserService
 * Implementation of CreateUser that relies on DynamoDb to persist the user data
 */
@Component
@AllArgsConstructor
final class CreateUserService implements CreateUser {

    private static final Set<BGMRole> DEFAULT_ROLES = Set.of(BGMRole.USER);

    private final Logger logger = LogManager.getLogger(CreateUserService.class);

    private final DynamoDbUserTable dynamoDbUserTable;
    private final DynamoDbEnhancedClient enhancedClient;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Try<Unit> execute(ValidatedUserCreationData userData) {

        DynamoDbUser dynamoDbUser = this.buildNewUser(userData);
        Email email = this.buildNewEmail(userData.email().value());

        final String conditionExpression = String.format("attribute_not_exists(%s)", DynamoDbUserTable.Attributes.PARTITION_KEY);

        LoggingUtils.info(logger, "execute", "Attempting to create a user in DynamoDB.", dynamoDbUser, email);

        return ExceptionUtils.toTry(() ->
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
        );

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
