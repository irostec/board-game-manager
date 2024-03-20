package com.irostec.boardgamemanager.configuration.security.user.boundary.persistence;

import lombok.Data;

/**
 * Email
 * An Email, as represented in DynamoDb
 */
@Data
final class Email {

    private String pk;
    private String sk;

}
