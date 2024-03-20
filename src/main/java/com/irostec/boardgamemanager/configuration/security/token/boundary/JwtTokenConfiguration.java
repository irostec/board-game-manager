package com.irostec.boardgamemanager.configuration.security.token.boundary;

import com.irostec.boardgamemanager.configuration.security.token.CreateToken;
import com.irostec.boardgamemanager.configuration.security.token.ValidateToken;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

/**
 * JwtTokenConfiguration
 * Configures the components used to create and validate the JWT tokens used during authentication
 */
@Configuration
class JwtTokenConfiguration {

    private static final String USERNAME_CLAIM_IDENTIFIER = "username";

    private final SecretKey key;

    JwtTokenConfiguration(@Value("${jwt.signing.key}") String signingKey) {
        this.key = Keys.hmacShaKeyFor(signingKey.getBytes(StandardCharsets.UTF_8));
    }

    @Bean
    CreateToken createToken(AuthenticationProvider authenticationProvider) {
        return new CreateTokenService(USERNAME_CLAIM_IDENTIFIER, key, authenticationProvider);
    }

    @Bean
    ValidateToken validateToken() {
        return new ValidateTokenService(USERNAME_CLAIM_IDENTIFIER, key);
    }

}
