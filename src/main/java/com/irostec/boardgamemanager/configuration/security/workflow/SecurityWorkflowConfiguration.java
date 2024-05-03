package com.irostec.boardgamemanager.configuration.security.workflow;

import com.irostec.boardgamemanager.configuration.security.authentication.core.GetRolesAndPrivilegesService;
import com.irostec.boardgamemanager.configuration.security.authentication.core.ParseTokenService;
import com.irostec.boardgamemanager.configuration.security.authentication.core.GetUserService;
import com.irostec.boardgamemanager.configuration.security.errorhandling.DelegatedAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import  org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

import java.util.Map;

/**
 * SecurityWorkflowConfiguration
 * Configuration of all the components used by Spring Security
 */
@Configuration
@EnableMethodSecurity
class SecurityWorkflowConfiguration {

    @Bean
    UserDetailsService userDetailsService(GetUserService getUserService) {
        return new BGMUserDetailsService(getUserService);
    }

    @Bean
    PasswordEncoder passwordEncoder() {

        final String defaultEncoderId = "bcrypt";

        Map<String, PasswordEncoder> encoders = Map.of(
                defaultEncoderId, new BCryptPasswordEncoder(),
                "scrypt", SCryptPasswordEncoder.defaultsForSpringSecurity_v5_8()
        );

        return new DelegatingPasswordEncoder(defaultEncoderId, encoders);
    }

    @Bean
    AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
                                                         PasswordEncoder passwordEncoder) {
        return new BGMAuthenticationProvider(userDetailsService, passwordEncoder);
    }

    @Bean
    BGMAuthenticationFilter jwtAuthenticationFilter(ParseTokenService parseTokenService,
                                                    GetRolesAndPrivilegesService getRolesAndPrivilegesService) {
        return new JwtAuthenticationFilter(parseTokenService, getRolesAndPrivilegesService);
    }

    @Bean
    SecurityFilterChain filterChain(
            HttpSecurity http,
            BGMAuthenticationFilter authenticationFilter,
            DelegatedAuthenticationEntryPoint authenticationEntryPoint) throws Exception {

        final String actuatorEndpointsMatcher = "/actuator/**";
        final String[] exposedMutatingEndpointMatchers = {"/v1/users", "/v1/users/authenticate"};

        // Defer Loading CsrfToken
        // https://docs.spring.io/spring-security/reference/5.8/migration/servlet/exploits.html#_defer_loading_csrftoken
        final CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();

        /*
            Ideally, the CSRF Header name would have its default value "X-XSRF-TOKEN".
            However, Postman uses NodeJS to send requests.
            There's a well-known bug of NodeJS that cause custom header names to be lowercased.
            Consequently, since the search of the CSRF Header is case-sensitive, it's never found.
            I must confess, finding the reason for the mismatch was painful.
            I had to debug significant portions of Spring Security's code to find out what was happening.
            About 12 continuous hours of my time went to waste on this,
            shared between testing every suggestion I could find in the internet and finally diving into the framework's code.
            For more information:

            https://github.com/postmanlabs/postman-app-support/issues/6394
            https://github.com/postmanlabs/postman-app-support/issues/5372
        */
        final CookieCsrfTokenRepository csrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        csrfTokenRepository.setHeaderName("x-xsrf-token");

        return http
            .addFilterAt(authenticationFilter, BasicAuthenticationFilter.class)
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(actuatorEndpointsMatcher).permitAll()
                .requestMatchers(HttpMethod.POST, exposedMutatingEndpointMatchers).permitAll()
                .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf
                .csrfTokenRequestHandler(requestHandler)
                .csrfTokenRepository(csrfTokenRepository)
            )
            .exceptionHandling(exceptionHandlingCustomizer ->
                    exceptionHandlingCustomizer.authenticationEntryPoint(authenticationEntryPoint)
            )
            .build();

    }

}
