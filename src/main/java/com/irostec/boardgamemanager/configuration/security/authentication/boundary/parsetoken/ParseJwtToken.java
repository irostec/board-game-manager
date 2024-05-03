package com.irostec.boardgamemanager.configuration.security.authentication.boundary.parsetoken;

import com.irostec.boardgamemanager.configuration.security.authentication.core.parsetoken.error.ExpiredToken;
import com.irostec.boardgamemanager.configuration.security.authentication.core.parsetoken.error.ValidateTokenError;
import com.irostec.boardgamemanager.configuration.security.authentication.core.parsetoken.output.TokenData;
import com.irostec.boardgamemanager.configuration.security.authentication.core.ParseTokenService;
import io.vavr.control.Either;
import io.vavr.control.Try;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Objects;

import com.irostec.boardgamemanager.configuration.security.authentication.core.parsetoken.error.InvalidToken;

/**
 * ParseJwtToken
 * Extracts the information from a JWT token
 */
@AllArgsConstructor
public final class ParseJwtToken implements ParseTokenService {

    private static final String INVALID_TOKEN_MESSAGE = "Invalid token";

    private final String usernameClaimIdentifier;
    private final SecretKey key;

    @Override
    public Either<ValidateTokenError, TokenData> execute(String jwtToken) {

        final Either<ValidateTokenError, Claims> claimsValidation =
                Try.of(() -> Jwts.parser()
                        .verifyWith(key)
                        .build()
                        .parseSignedClaims(jwtToken)
                        .getPayload())
                .toEither()
                .mapLeft(InvalidToken::new);


        return claimsValidation.flatMap(this::validateClaims);


    }

    private Either<ValidateTokenError, TokenData> validateClaims(Claims claims) {

        final boolean isExpired = Objects.isNull(claims.getExpiration()) || new Date().after(claims.getExpiration());

        return isExpired ?
                Either.left(new ExpiredToken()) :
                Either.narrow(
                        Try.of(() -> claims.get(usernameClaimIdentifier))
                                .map(String::valueOf)
                                .toEither()
                                .mapLeft(InvalidToken::new)
                                .map(TokenData::new)
                );

    }

}
