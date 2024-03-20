package com.irostec.boardgamemanager.configuration.security.user.boundary.persistence;

import java.util.Set;

import com.irostec.boardgamemanager.configuration.security.user.output.BGMRole;
import com.irostec.boardgamemanager.configuration.security.user.output.BGMUser;
import lombok.Data;

/**
 * DynamoDbUser
 * A User, as represented in DynamoDb
 */
@Data
final class DynamoDbUser implements BGMUser {

    private String pk;
    private String sk;
    private String username;
    private String password;
    private String email;
    private Set<BGMRole> roles;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

}
