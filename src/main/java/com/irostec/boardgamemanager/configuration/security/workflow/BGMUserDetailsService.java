package com.irostec.boardgamemanager.configuration.security.workflow;

import com.irostec.boardgamemanager.configuration.security.authentication.core.GetUserService;
import com.irostec.boardgamemanager.configuration.security.authentication.helper.BGMRoleMapper;
import com.irostec.boardgamemanager.configuration.security.authentication.core.getuser.output.BGMUser;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * BGMUserDetailsService
 * A customized UserDetailsService
 */
@AllArgsConstructor
final class BGMUserDetailsService implements UserDetailsService {

    private final GetUserService getUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        final BGMUser bgmUser = getUserService.execute(username)
                .getOrElseThrow(error -> new UsernameNotFoundException("Error retrieving the user from the database", error.cause()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new User(
                bgmUser.getUsername(),
                bgmUser.getPassword(),
                bgmUser.isEnabled(),
                bgmUser.isAccountNonExpired(),
                bgmUser.isCredentialsNonExpired(),
                bgmUser.isAccountNonLocked(),
                bgmUser.getRoles().stream()
                    .map(BGMRoleMapper.INSTANCE::toGrantedAuthority)
                    .toList()
        );

    }

}
