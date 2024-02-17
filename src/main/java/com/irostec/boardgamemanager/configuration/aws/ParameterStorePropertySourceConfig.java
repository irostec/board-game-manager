package com.irostec.boardgamemanager.configuration.aws;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.irostec.boardgamemanager.configuration.aws.dataclass.ParameterStoreProperties;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * ParameterStorePropertySourceConfig
 * Initializes a ParameterStorePropertySource and adds it to the Environment.
 * @see org.springframework.core.env.Environment
 * @see com.irostec.boardgamemanager.configuration.aws.ParameterStorePropertySource
 */
@Configuration
public class ParameterStorePropertySourceConfig {

    private final ConfigurableEnvironment env;

    private final AWSSimpleSystemsManagement simpleSystemsManagement;

    private final ParameterStoreProperties parameterStoreProperties;

    public ParameterStorePropertySourceConfig(
        ConfigurableEnvironment env,
        AWSSimpleSystemsManagement simpleSystemsManagement,
        @Value("${aws.paramstore.prefix}") String parameterStorePrefix,
        @Value("${aws.paramstore.defaultContext}") String parameterStoreDefaultContext,
        @Value("${aws.paramstore.profileSeparator}") String parameterStoreProfileSeparator) {

        this.env = env;
        this.simpleSystemsManagement = simpleSystemsManagement;

        this.parameterStoreProperties = new ParameterStoreProperties(parameterStorePrefix,
                parameterStoreDefaultContext,
                parameterStoreProfileSeparator);

    }

    @PostConstruct
    public void init() {

        ParameterStorePropertySource propertySource =
                new ParameterStorePropertySource(env,
                        "ParameterStorePropertySource",
                        simpleSystemsManagement,
                        parameterStoreProperties);

        env.getPropertySources().addLast(propertySource);

    }

}
