package com.irostec.boardgamemanager.configuration.aws.client;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.irostec.boardgamemanager.configuration.ApplicationProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AwsClientConfig
 * The standard configuration for AWS clients (as opposed to the one used with LocalStack.
 */
@Configuration
@ConditionalOnProperty(value = ApplicationProperties.LOCALSTACK_ENABLED_FLAG, havingValue = "false")
public class AwsClientConfig {

    @Bean
    public AWSSimpleSystemsManagement simpleSystemsManagementClient() {
        return AWSSimpleSystemsManagementClientBuilder.defaultClient();
    }

}
