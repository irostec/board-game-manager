package com.irostec.boardgamemanager.configuration.security.authentication.helper;

import com.irostec.boardgamemanager.configuration.security.authentication.core.getrolesandprivileges.output.BGMRole;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.GrantedAuthority;

/**
 * BGMRoleMapper
 * Maps BGMRole instances to GrantedAuthority.
 * Since Spring Security expects the roles to be prefixed by "ROLE_", in practice all this mapper does is to add such prefix to the name() of the provided BGMRole.
 */
@Mapper
public interface BGMRoleMapper {

    BGMRoleMapper INSTANCE = Mappers.getMapper(BGMRoleMapper.class);

    default GrantedAuthority toGrantedAuthority(BGMRole role) {
        return () -> "ROLE_" + role.name();
    }

}
