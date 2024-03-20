package com.irostec.boardgamemanager.configuration.security.core.workflow;

import com.irostec.boardgamemanager.configuration.security.user.GetUser;
import com.irostec.boardgamemanager.configuration.security.user.helper.BGMRoleMapper;
import com.irostec.boardgamemanager.configuration.security.user.output.BGMUser;
import io.atlassian.fugue.Checked;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.stream.Collectors;

/**
 * BGMUserDetailsService
 * A customized UserDetailsService
 */
@AllArgsConstructor
final class BGMUserDetailsService implements UserDetailsService {

    private final GetUser getUser;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        final BGMUser bgmUser = Checked.of(() -> getUser.execute(username)).fold(
                exception -> { throw new UsernameNotFoundException("Error retrieving user data", exception); },
                optionalUser -> optionalUser.orElseThrow(() -> new UsernameNotFoundException("User not found"))
        );

        return new User(
                bgmUser.getUsername(),
                bgmUser.getPassword(),
                bgmUser.isEnabled(),
                bgmUser.isAccountNonExpired(),
                bgmUser.isCredentialsNonExpired(),
                bgmUser.isAccountNonLocked(),
                bgmUser.getRoles().stream()
                        .map(BGMRoleMapper.INSTANCE::toGrantedAuthority)
                        .collect(Collectors.toList())
        );

    }

}
