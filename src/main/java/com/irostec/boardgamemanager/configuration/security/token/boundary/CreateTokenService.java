package com.irostec.boardgamemanager.configuration.security.token.boundary;

import com.irostec.boardgamemanager.configuration.security.token.CreateToken;
import com.irostec.boardgamemanager.configuration.security.user.input.ValidatedPassword;
import com.irostec.boardgamemanager.configuration.security.user.input.ValidatedUsername;
import com.irostec.boardgamemanager.configuration.security.exception.UserAuthenticationException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

/**
 * CreateTokenService
 * Implementation of CreateToken that uses the AuthenticationProvider configured for Spring Security to verify the provided password
 */
@AllArgsConstructor
final class CreateTokenService implements CreateToken {

    private final String usernameClaimIdentifier;
    private final SecretKey key;
    private final AuthenticationProvider authenticationProvider;

    @Override
    public String execute(ValidatedUsername username, ValidatedPassword password) throws UserAuthenticationException {

        try {
            authenticationProvider.authenticate(
                    new UsernamePasswordAuthenticationToken(username.value(), password.value())
            );
        }
        catch (AuthenticationException ex) {
            throw new UserAuthenticationException(ex);
        }

        return createNewToken(username.value());

    }

    private String createNewToken(String username) {

        final Map<String, Object> claims = Map.of(this.usernameClaimIdentifier, username);
        final Instant current = Instant.now();
        final Instant expiration = current.plus(5, ChronoUnit.MINUTES);

        return Jwts.builder()
                .subject(username)
                .claims(claims)
                .issuedAt(Date.from(current))
                .expiration(Date.from(expiration))
                .signWith(this.key)
                .compact();

    }

}
