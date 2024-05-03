package com.irostec.boardgamemanager.configuration.security.authentication.core.getuser.output;

import com.irostec.boardgamemanager.configuration.security.authentication.core.getrolesandprivileges.output.BGMRole;

import java.util.Set;

/**
 * BGMUser
 * A generic representation of a user
 */
public interface BGMUser {

    String getUsername();
    String getPassword();
    String getEmail();
    Set<BGMRole> getRoles();
    boolean isAccountNonExpired();
    boolean isAccountNonLocked();
    boolean isCredentialsNonExpired();
    boolean isEnabled();

}
