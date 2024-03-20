package com.irostec.boardgamemanager.configuration.security.token.boundary;

import com.irostec.boardgamemanager.configuration.security.token.output.ValidatedToken;
import com.irostec.boardgamemanager.configuration.security.exception.InvalidTokenException;
import com.irostec.boardgamemanager.configuration.security.token.ValidateToken;
import com.irostec.boardgamemanager.configuration.security.user.input.ValidatedUsername;
import io.atlassian.fugue.Checked;
import io.atlassian.fugue.Either;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * ValidateTokenService
 * Basic implementation of ValidateToken
 */
@AllArgsConstructor
final class ValidateTokenService implements ValidateToken {

    private static final String INVALID_TOKEN_MESSAGE = "Invalid token";

    private final String usernameClaimIdentifier;
    private final SecretKey key;

    @Override
    public Either<InvalidTokenException, ValidatedToken> execute(String jwtToken) {

        return Checked.of(() -> Jwts.parser()
                        .verifyWith(key)
                        .build()
                        .parseSignedClaims(jwtToken)
                        .getPayload())
                .toEither()
                .leftMap(exception -> new InvalidTokenException(INVALID_TOKEN_MESSAGE))
                .flatMap(this::validateClaims);

    }

    private Either<InvalidTokenException, ValidatedToken> validateClaims(Claims claims) {

        final boolean isExpired = claims.getExpiration() == null || new Date().after(claims.getExpiration());

        return isExpired ?
                Either.left(new InvalidTokenException("The authentication token has expired.")) :
                Checked.of(() -> claims.get(usernameClaimIdentifier))
                        .map(String::valueOf)
                        .flatMap(username -> Checked.of(() -> new ValidatedUsername(username)))
                        .map(ValidatedToken::new)
                        .toEither()
                        .leftMap(exception -> new InvalidTokenException(INVALID_TOKEN_MESSAGE));

    }

}
