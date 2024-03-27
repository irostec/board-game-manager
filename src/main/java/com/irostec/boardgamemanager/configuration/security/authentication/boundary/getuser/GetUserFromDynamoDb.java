package com.irostec.boardgamemanager.configuration.security.authentication.boundary.getuser;

import com.irostec.boardgamemanager.common.utility.LoggingUtils;
import com.irostec.boardgamemanager.configuration.security.authentication.application.GetUserService;
import com.irostec.boardgamemanager.configuration.security.authentication.application.getuser.error.GetUserError;
import com.irostec.boardgamemanager.configuration.security.authentication.application.getuser.output.BGMUser;
import com.irostec.boardgamemanager.configuration.security.authentication.boundary.shared.dynamodb.DynamoDbUser;
import com.irostec.boardgamemanager.configuration.security.authentication.boundary.shared.dynamodb.DynamoDbUserTable;
import io.vavr.control.Either;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * GetUserFromDynamoDb
 * Implementation of GetUser that relies on DynamoDb to retrieve the user data
 */
@Component
@AllArgsConstructor
public final class GetUserFromDynamoDb implements GetUserService {

    private final Logger logger = LogManager.getLogger(GetUserFromDynamoDb.class);

    private final DynamoDbUserTable dynamoDbUserTable;

    @Override
    public Either<GetUserError, Optional<BGMUser>> execute(String username) {

        final String partitionKeyForUser = dynamoDbUserTable.keyFactory().buildPartitionKeyForUser(username);
        final String sortKeyForUser = dynamoDbUserTable.keyFactory().buildSortKeyForUser(username);

        LoggingUtils.info(logger,
                "execute",
                String.format("Attempting to get user with username '%s' from DynamoDB.", username));

        Function<DynamoDbUser, Optional<BGMUser>> identity = Optional::ofNullable;

        return Try.of(
                    () -> dynamoDbUserTable.projections().userProjection()
                                    .getItem(
                                            Key.builder()
                                                    .partitionValue(partitionKeyForUser)
                                                    .sortValue(sortKeyForUser)
                                                    .build()
                                    )
            )
            .toEither()
            .mapLeft(GetUserError::new)
            .map(dynamoDbUser -> {

                final Optional<BGMUser> result;

                if (Objects.isNull(dynamoDbUser)) {
                    result = Optional.empty();
                } else {
                    dynamoDbUser.setUsername(username);
                    result = Optional.of(dynamoDbUser);
                }

                return result;
            });

    }

}
