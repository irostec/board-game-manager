package com.irostec.boardgamemanager.configuration.security.workflow;

import com.google.common.collect.ImmutableSet;
import com.irostec.boardgamemanager.configuration.security.authentication.core.GetRolesAndPrivilegesService;
import com.irostec.boardgamemanager.configuration.security.authentication.core.ParseTokenService;
import com.irostec.boardgamemanager.configuration.security.authentication.core.parsetoken.error.ExpiredToken;
import com.irostec.boardgamemanager.configuration.security.authentication.core.parsetoken.error.InvalidToken;
import com.irostec.boardgamemanager.configuration.security.authentication.core.parsetoken.output.TokenData;
import com.irostec.boardgamemanager.configuration.security.authentication.helper.BGMRoleMapper;
import com.irostec.boardgamemanager.configuration.security.authentication.core.getrolesandprivileges.output.RolesAndPrivileges;
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

import static com.google.common.base.Defaults.defaultValue;

import java.io.IOException;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * JwtAuthenticationFilter
 * A customized AuthenticationFilter
 */
@AllArgsConstructor
final class JwtAuthenticationFilter extends BGMAuthenticationFilter {

    private static final String AUTHORIZATION_HEADER_PREFIX = "Bearer ";
    private final int AUTHORIZATION_HEADER_PREFIX_LENGTH = AUTHORIZATION_HEADER_PREFIX.length();

    private final ParseTokenService parseTokenService;
    private final GetRolesAndPrivilegesService getRolesAndPrivilegesService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain
    ) throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        if (Objects.nonNull(authorizationHeader)
                && authorizationHeader.length() > AUTHORIZATION_HEADER_PREFIX_LENGTH
                && authorizationHeader.startsWith(AUTHORIZATION_HEADER_PREFIX)) {

            final String jwtToken = authorizationHeader.substring(AUTHORIZATION_HEADER_PREFIX_LENGTH);

            final String username = parseTokenService.execute(jwtToken)
                        .map(TokenData::username)
                        .getOrElseThrow(
                                error ->
                                        switch (error) {
                                            case InvalidToken invalidToken ->
                                                    new BadCredentialsException("Invalid token", invalidToken.cause());
                                            case ExpiredToken expiredToken ->
                                                new BadCredentialsException("Expired token");
                                        }
                        );

            final Set<GrantedAuthority> authorities =
                    this.getRolesAndPrivilegesService.execute(username)
                            .getOrElseThrow(
                                    error -> new BadCredentialsException("Couldn't get the roles and privileges from the database")
                            )
                            .map(RolesAndPrivileges::roles)
                            .orElse(Collections.emptySet())
                            .stream()
                            .map(BGMRoleMapper.INSTANCE::toGrantedAuthority)
                            .collect(ImmutableSet.toImmutableSet());

            final Authentication authentication =
                    new UsernamePasswordAuthenticationToken(username, defaultValue(Object.class), authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);

        }

        filterChain.doFilter(request, response);

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getServletPath().equals("/v1/users/authenticate");
    }

}
