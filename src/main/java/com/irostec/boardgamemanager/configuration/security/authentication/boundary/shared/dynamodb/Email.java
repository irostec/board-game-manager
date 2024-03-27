package com.irostec.boardgamemanager.configuration.security.authentication.boundary.shared.dynamodb;

import lombok.Data;

/**
 * Email
 * An Email, as represented in DynamoDb
 */
@Data
public final class Email {

    private String pk;
    private String sk;

}
