package com.irostec.boardgamemanager.configuration.security.core.workflow;

import com.google.common.collect.ImmutableSet;
import com.irostec.boardgamemanager.configuration.security.token.ValidateToken;
import com.irostec.boardgamemanager.configuration.security.token.output.ValidatedToken;
import com.irostec.boardgamemanager.configuration.security.user.helper.BGMRoleMapper;
import com.irostec.boardgamemanager.configuration.security.user.GetRolesAndPrivileges;
import com.irostec.boardgamemanager.configuration.security.user.input.ValidatedUsername;
import com.irostec.boardgamemanager.configuration.security.user.output.RolesAndPrivileges;
import io.atlassian.fugue.Checked;
import io.atlassian.fugue.Eithers;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Set;

/**
 * JwtAuthenticationFilter
 * A customized AuthenticationFilter
 */
@AllArgsConstructor
final class JwtAuthenticationFilter extends BGMAuthenticationFilter {

    private static final String AUTHORIZATION_HEADER_PREFIX = "Bearer ";
    private final int AUTHORIZATION_HEADER_PREFIX_LENGTH = AUTHORIZATION_HEADER_PREFIX.length();

    private final ValidateToken validateToken;
    private final GetRolesAndPrivileges getRolesAndPrivileges;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain
    ) throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null
                && authorizationHeader.length() > AUTHORIZATION_HEADER_PREFIX_LENGTH
                && authorizationHeader.startsWith(AUTHORIZATION_HEADER_PREFIX)) {

            final String jwtToken = authorizationHeader.substring(AUTHORIZATION_HEADER_PREFIX_LENGTH);

            final ValidatedUsername validatedUsername = Eithers.getOrThrow(
                    validateToken.execute(jwtToken)
                        .map(ValidatedToken::username)
                        .leftMap(exception -> new BadCredentialsException("Invalid token", exception))
            );

            final RolesAndPrivileges rolesAndPrivileges =
                    Checked.of(() -> this.getRolesAndPrivileges.execute(validatedUsername))
                            .fold(
                                    exception -> { throw new BadCredentialsException("Error retrieving user authorities", exception); },
                                    optionalRolesAndPrivileges -> optionalRolesAndPrivileges.orElseThrow(() -> new BadCredentialsException("User authorities not found"))
                            );

            final Set<GrantedAuthority> authorities = rolesAndPrivileges.roles()
                                .stream()
                                .map(BGMRoleMapper.INSTANCE::toGrantedAuthority)
                                .collect(ImmutableSet.toImmutableSet());

            final Authentication authentication =
                    new UsernamePasswordAuthenticationToken(validatedUsername.value(), null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);

        }

        filterChain.doFilter(request, response);

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getServletPath().equals("/v1/users/authenticate");
    }

}
