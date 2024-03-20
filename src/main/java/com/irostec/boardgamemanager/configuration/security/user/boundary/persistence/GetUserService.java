package com.irostec.boardgamemanager.configuration.security.user.boundary.persistence;

import com.irostec.boardgamemanager.common.utility.LoggingUtils;
import com.irostec.boardgamemanager.configuration.security.exception.UserDatabaseException;
import com.irostec.boardgamemanager.configuration.security.user.GetUser;
import com.irostec.boardgamemanager.configuration.security.user.output.BGMUser;
import io.atlassian.fugue.Checked;
import io.atlassian.fugue.Eithers;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.util.Optional;

/**
 * GetUserService
 * Implementation of GetUser that relies on DynamoDb to retrieve the user data
 */
@Component
@AllArgsConstructor
public final class GetUserService implements GetUser {

    private final Logger logger = LogManager.getLogger(GetUserService.class);

    private final DynamoDbUserTable dynamoDbUserTable;

    @Override
    public Optional<? extends BGMUser> execute(String username) throws UserDatabaseException {

        final String partitionKeyForUser = dynamoDbUserTable.keyFactory().buildPartitionKeyForUser(username);
        final String sortKeyForUser = dynamoDbUserTable.keyFactory().buildSortKeyForUser(username);

        LoggingUtils.info(logger,
                "execute",
                String.format("Attempting to get user with username '%s' from DynamoDB.", username));

        DynamoDbUser dynamoDbUser = Eithers.getOrThrow(
                Checked.of(() -> dynamoDbUserTable.projections().userProjection()
                                .getItem(
                                        Key.builder()
                                                .partitionValue(partitionKeyForUser)
                                                .sortValue(sortKeyForUser)
                                                .build()
                                )
                        )
                        .toEither()
                        .leftMap(
                                ex -> new UserDatabaseException("Error retrieving user", ex)
                        )
        );

        final Optional<DynamoDbUser> result = Optional.ofNullable(dynamoDbUser);
        result.ifPresent(user -> user.setUsername(username));

        return result;

    }

}
