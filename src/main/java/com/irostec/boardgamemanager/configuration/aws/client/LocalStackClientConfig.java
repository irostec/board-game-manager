package com.irostec.boardgamemanager.configuration.aws.client;

import com.irostec.boardgamemanager.configuration.ApplicationProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.regions.DefaultAwsRegionProviderChain;

/**
 * LocalStackClientConfig
 * The AWS configuration used with LocalStack.
 */
@Configuration
@ConditionalOnProperty(value = ApplicationProperties.LOCALSTACK_ENABLED_FLAG, havingValue = "true")
class LocalStackClientConfig {

    @Bean
    public AWSCredentialsProvider credentialsProvider() {
        return DefaultAWSCredentialsProviderChain.getInstance();
    }

    @Bean
    public AwsClientBuilder.EndpointConfiguration endpointConfiguration(
        @Value("${aws.localStack.endpoint.url}") String awsEndpointUrl) {

        return new AwsClientBuilder.EndpointConfiguration(awsEndpointUrl,
                new DefaultAwsRegionProviderChain().getRegion());

    }

    @Bean
    public AWSSimpleSystemsManagement simpleSystemsManagementClient(
        AWSCredentialsProvider credentialsProvider,
        AwsClientBuilder.EndpointConfiguration endpointConfiguration
    ) {

        return AWSSimpleSystemsManagementClientBuilder.standard()
                .withCredentials(credentialsProvider)
                .withEndpointConfiguration(endpointConfiguration)
                .build();

    }

}
