package com.irostec.boardgamemanager.configuration.security.authentication.boundary.getrolesandprivileges;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.irostec.boardgamemanager.configuration.security.authentication.core.GetRolesAndPrivilegesService;
import com.irostec.boardgamemanager.configuration.security.authentication.core.getrolesandprivileges.error.GetRolesAndPrivilegesError;
import com.irostec.boardgamemanager.configuration.security.authentication.boundary.shared.dynamodb.DynamoDbUserTable;
import com.irostec.boardgamemanager.configuration.security.authentication.core.getrolesandprivileges.output.RolesAndPrivileges;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import com.irostec.boardgamemanager.configuration.security.authentication.core.getrolesandprivileges.error.DatabaseFailure;
import com.irostec.boardgamemanager.configuration.security.authentication.core.getrolesandprivileges.error.InvalidFormat;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * GetRolesAndPrivilegesFromDynamoDb
 * Implementation of GetRolesAndPrivileges that relies on DynamoDb to retrieve the user data
 */
@Component
final class GetRolesAndPrivilegesServiceFromDynamoDb implements GetRolesAndPrivilegesService {

    private final Logger logger = LogManager.getLogger(GetRolesAndPrivilegesServiceFromDynamoDb.class);

    private final ObjectMapper objectMapper;
    private final DynamoDbUserTable dynamoDbUserTable;

    public GetRolesAndPrivilegesServiceFromDynamoDb(DynamoDbUserTable dynamoDbUserTable) {
        this.objectMapper = new ObjectMapper();
        this.dynamoDbUserTable = dynamoDbUserTable;
    }

    @Override
    public Either<GetRolesAndPrivilegesError, Optional<RolesAndPrivileges>> execute(String username) {

        final String partitionKeyForUser = dynamoDbUserTable.keyFactory().buildPartitionKeyForUser(username);
        final String sortKeyForUser = dynamoDbUserTable.keyFactory().buildSortKeyForUser(username);

        final String projectionExpression = String.join(", ", DynamoDbUserTable.Attributes.ROLE_SET);

        final GetItemSpec spec = new GetItemSpec()
                .withPrimaryKey(DynamoDbUserTable.Attributes.PARTITION_KEY, partitionKeyForUser, DynamoDbUserTable.Attributes.SORT_KEY, sortKeyForUser)
                .withProjectionExpression(projectionExpression)
                .withConsistentRead(false);

        logger.info("Attempting to get the roles and privileges of a user from DynamoDB.", spec);

        Function<Item, Either<GetRolesAndPrivilegesError, Optional<RolesAndPrivileges>>> itemToRolesAndPrivileges = item ->
            Objects.isNull(item) ?
                    Either.right(Optional.empty()) :
                    Either.narrow(
                            Try.of(() -> objectMapper.readValue(item.toJSON(), SerializableRolesAndPrivileges.class))
                            .toEither()
                            .mapLeft(InvalidFormat::new)
                            .map(Optional::of)
                    );

        Function<Throwable, GetRolesAndPrivilegesError> throwableToError = DatabaseFailure::new;

        return Try.of(() -> dynamoDbUserTable.table().getItem(spec))
                .toEither()
                .mapLeft(throwableToError)
                .flatMap(itemToRolesAndPrivileges);

    }

}
