package com.irostec.boardgamemanager.configuration.security.authentication.boundary.shared.dynamodb;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;

import com.irostec.boardgamemanager.configuration.security.authentication.application.getrolesandprivileges.output.BGMRole;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.internal.converter.attribute.SetAttributeConverter;

import static software.amazon.awssdk.enhanced.dynamodb.EnhancedType.setOf;
import static software.amazon.awssdk.enhanced.dynamodb.mapper.StaticAttributeTags.primaryPartitionKey;
import static software.amazon.awssdk.enhanced.dynamodb.mapper.StaticAttributeTags.primarySortKey;

/**
 * DynamoDbUserTable
 * Contains convenient objects and data used to represent the user information in DynamoDb
 */
@Service
public class DynamoDbUserTable {

    private final Table table;
    private final KeyFactory keyFactory;
    private final Projections projections;

    DynamoDbUserTable(@Value("${aws.dynamodb.user.table}") String userTableName,
                                 AmazonDynamoDB client,
                                 DynamoDbEnhancedClient enhancedClient) {

        final TableSchema<DynamoDbUser> userTableSchema = TableSchema.builder(DynamoDbUser.class).newItemSupplier(DynamoDbUser::new)
                .addAttribute(String.class, a -> a.name(Attributes.PARTITION_KEY)
                        .getter(DynamoDbUser::getPk)
                        .setter(DynamoDbUser::setPk)
                        .tags(primaryPartitionKey()))
                .addAttribute(String.class, a -> a.name(Attributes.SORT_KEY)
                        .getter(DynamoDbUser::getSk)
                        .setter(DynamoDbUser::setSk)
                        .tags(primarySortKey()))
                .addAttribute(String.class, a -> a.name(Attributes.PASSWORD)
                        .getter(DynamoDbUser::getPassword)
                        .setter(DynamoDbUser::setPassword))
                .addAttribute(String.class, a -> a.name("Email")
                        .getter(DynamoDbUser::getEmail)
                        .setter(DynamoDbUser::setEmail))
                .addAttribute(setOf(BGMRole.class), a -> a.name(Attributes.ROLE_SET)
                        .getter(DynamoDbUser::getRoles)
                        .setter(DynamoDbUser::setRoles)
                        .attributeConverter(SetAttributeConverter.setConverter(EnumAttributeConverter.create(BGMRole.class))))
                .addAttribute(Boolean.class, a -> a.name("AccountNonExpired")
                        .getter(DynamoDbUser::isAccountNonExpired)
                        .setter(DynamoDbUser::setAccountNonExpired))
                .addAttribute(Boolean.class, a -> a.name("AccountNonLocked")
                        .getter(DynamoDbUser::isAccountNonLocked)
                        .setter(DynamoDbUser::setAccountNonLocked))
                .addAttribute(Boolean.class, a -> a.name("CredentialsNonExpired")
                        .getter(DynamoDbUser::isCredentialsNonExpired)
                        .setter(DynamoDbUser::setCredentialsNonExpired))
                .addAttribute(Boolean.class, a -> a.name("Enabled")
                        .getter(DynamoDbUser::isEnabled)
                        .setter(DynamoDbUser::setEnabled))
                .build();

        final TableSchema<Email> emailTableSchema = TableSchema.builder(Email.class).newItemSupplier(Email::new)
                .addAttribute(String.class, a -> a.name(Attributes.PARTITION_KEY)
                        .getter(Email::getPk)
                        .setter(Email::setPk)
                        .tags(primaryPartitionKey()))
                .addAttribute(String.class, a -> a.name(Attributes.SORT_KEY)
                        .getter(Email::getSk)
                        .setter(Email::setSk)
                        .tags(primarySortKey()))
                .build();

        this.table = new DynamoDB(client).getTable(userTableName);
        this.keyFactory = new KeyFactory("USER", "#");
        this.projections = new Projections(
                enhancedClient.table(userTableName, userTableSchema),
                enhancedClient.table(userTableName, emailTableSchema)
        );

    }

    public Table table() { return this.table; }
    public KeyFactory keyFactory() { return this.keyFactory; }
    public Projections projections() { return this.projections; }

    public static final class Attributes {

        private Attributes() {}

        public static final String PARTITION_KEY = "PK";
        public static final String SORT_KEY = "SK";
        public static final String PASSWORD = "Password";
        public static final String ROLE_SET = "RoleSet";

    }

    public record Projections(DynamoDbTable<DynamoDbUser> userProjection,
                              DynamoDbTable<Email> emailProjection) {}

    @AllArgsConstructor
    public static class KeyFactory {

        private final String userPrefix;
        private final String prefixSeparator;

        public String buildPartitionKeyForUser(String username) {
            return this.buildKey(this.userPrefix, username);
        }

        public String buildSortKeyForUser(String username) {
            return buildPartitionKeyForUser(username);
        }

        public String buildKey(String prefix, String value) {
            return prefix + this.prefixSeparator + value;
        }

    }

}
