package com.irostec.boardgamemanager.configuration.security.authentication.boundary.createtoken.dependency;

import com.irostec.boardgamemanager.configuration.security.authentication.application.createtoken.dependency.TokenFactory;
import com.irostec.boardgamemanager.configuration.security.authentication.application.createtoken.error.Unauthorized;
import io.jsonwebtoken.Jwts;
import io.vavr.control.Either;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

/**
 * JwtTokenFactory
 * Creates tokens for valid user credentials
 */
@AllArgsConstructor
public final class JwtTokenFactory implements TokenFactory {

    private final String usernameClaimIdentifier;
    private final SecretKey key;
    private final AuthenticationProvider authenticationProvider;

    @Override
    public Either<Unauthorized, String> buildNewToken(String username, String password) {

        return Try.ofCallable(() -> {
                        authenticationProvider.authenticate(
                                new UsernamePasswordAuthenticationToken(username, password)
                        );

                        return createNewToken(username);
                }
        )
        .toEither()
        .mapLeft(Unauthorized::new);

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
