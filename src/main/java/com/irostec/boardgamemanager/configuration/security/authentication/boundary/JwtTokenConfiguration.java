package com.irostec.boardgamemanager.configuration.security.authentication.boundary;

import com.irostec.boardgamemanager.configuration.security.authentication.application.CreateTokenService;
import com.irostec.boardgamemanager.configuration.security.authentication.application.ParseTokenService;
import com.irostec.boardgamemanager.configuration.security.authentication.application.createtoken.dependency.TokenFactory;
import com.irostec.boardgamemanager.configuration.security.authentication.boundary.createtoken.dependency.JwtTokenFactory;
import com.irostec.boardgamemanager.configuration.security.authentication.boundary.parsetoken.ParseJwtToken;
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
    TokenFactory tokenFactory(AuthenticationProvider authenticationProvider) {
        return new JwtTokenFactory(USERNAME_CLAIM_IDENTIFIER, key, authenticationProvider);
    }

    @Bean
    CreateTokenService createToken(TokenFactory tokenFactory) {
        return new CreateTokenService(tokenFactory);
    }

    @Bean
    ParseTokenService validateToken() {
        return new ParseJwtToken(USERNAME_CLAIM_IDENTIFIER, key);
    }

}
