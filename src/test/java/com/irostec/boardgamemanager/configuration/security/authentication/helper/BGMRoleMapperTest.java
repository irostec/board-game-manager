package com.irostec.boardgamemanager.configuration.security.authentication.helper;

import com.irostec.boardgamemanager.configuration.security.authentication.core.getrolesandprivileges.output.BGMRole;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.security.core.GrantedAuthority;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * BGMRoleMapperTest
 */
class BGMRoleMapperTest {

    @ParameterizedTest
    @EnumSource(BGMRole.class)
    void testRoleMapping(BGMRole role) {

        GrantedAuthority result = BGMRoleMapper.INSTANCE.toGrantedAuthority(role);

        assertEquals("ROLE_" + role.name(), result.getAuthority());

    }

}
