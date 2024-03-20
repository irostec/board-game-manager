package com.irostec.boardgamemanager.configuration.security.user.boundary.persistence;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.irostec.boardgamemanager.configuration.security.user.GetRolesAndPrivileges;
import com.irostec.boardgamemanager.configuration.security.user.input.ValidatedUsername;
import com.irostec.boardgamemanager.configuration.security.exception.UserDatabaseException;
import com.irostec.boardgamemanager.configuration.security.user.output.RolesAndPrivileges;
import io.atlassian.fugue.Checked;
import io.atlassian.fugue.Eithers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

/**
 * GetRolesAndPrivilegesService
 * Implementation of GetRolesAndPrivileges that relies on DynamoDb to retrieve the user data
 */
@Service
final class GetRolesAndPrivilegesService implements GetRolesAndPrivileges {

    private final Logger logger = LogManager.getLogger(GetRolesAndPrivilegesService.class);

    private final ObjectMapper objectMapper;
    private final DynamoDbUserTable dynamoDbUserTable;

    public GetRolesAndPrivilegesService(DynamoDbUserTable dynamoDbUserTable) {
        this.objectMapper = new ObjectMapper();
        this.dynamoDbUserTable = dynamoDbUserTable;
    }

    @Override
    public Optional<RolesAndPrivileges> execute(ValidatedUsername username) throws UserDatabaseException {

        final String partitionKeyForUser = dynamoDbUserTable.keyFactory().buildPartitionKeyForUser(username.value());
        final String sortKeyForUser = dynamoDbUserTable.keyFactory().buildSortKeyForUser(username.value());

        final String projectionExpression = String.join(", ", DynamoDbUserTable.Attributes.ROLE_SET);

        final GetItemSpec spec = new GetItemSpec()
                .withPrimaryKey(DynamoDbUserTable.Attributes.PARTITION_KEY, partitionKeyForUser, DynamoDbUserTable.Attributes.SORT_KEY, sortKeyForUser)
                .withProjectionExpression(projectionExpression)
                .withConsistentRead(false);

        logger.info("Attempting to get the roles and privileges of a user from DynamoDB.", spec);

        final Item item = Eithers.getOrThrow(
                Checked.of(() -> dynamoDbUserTable.table().getItem(spec))
                        .toEither()
                        .leftMap(ex -> new UserDatabaseException("Error retrieving user authorities", ex))
        );

        return Objects.isNull(item) ?
                Optional.empty() :
                Optional.of(
                        Eithers.getOrThrow(
                                Checked.of(() -> objectMapper.readValue(item.toJSON(), SerializableRolesAndPrivileges.class))
                                        .toEither()
                                        .leftMap(exception -> new UserDatabaseException("Couldn't map the DynamoDB item", exception))
                        )
                );

    }

}
