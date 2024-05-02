package com.irostec.boardgamemanager.configuration.aws.client;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.irostec.boardgamemanager.configuration.ApplicationProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.DefaultAwsRegionProviderChain;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * LocalStackClientConfig
 * The AWS configuration used with LocalStack.
 */
@Configuration
@ConditionalOnProperty(value = ApplicationProperties.LOCALSTACK_ENABLED_FLAG, havingValue = "true")
class LocalStackClientConfig {

    @Value("${AWS_ENDPOINT}")
    private String awsEndpoint;

    @Bean
    public AWSCredentialsProvider getAWScredentialsProvider() {
        return DefaultAWSCredentialsProviderChain.getInstance();
    }

    @Bean
    public AwsClientBuilder.EndpointConfiguration getEndpointConfiguration() {

        return new AwsClientBuilder.EndpointConfiguration(
                awsEndpoint,
                new DefaultAwsRegionProviderChain().getRegion()
        );

    }


    /* Amazon DynamoDB configuration - Start */

    @Bean
    public AwsCredentialsProvider getAwsCredentialsProvider() {
        return DefaultCredentialsProvider.builder().build();
    }

    @Bean
    public DynamoDbClient getDynamoDbClient(AwsCredentialsProvider credentialsProvider) throws URISyntaxException {
        return DynamoDbClient.builder()
                .credentialsProvider(credentialsProvider)
                .endpointOverride(new URI(awsEndpoint))
                .build();
    }

    @Bean
    public DynamoDbEnhancedClient getDynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
        return DynamoDbEnhancedClient.builder().dynamoDbClient(dynamoDbClient).build();
    }

    @Bean
    public AmazonDynamoDB getAmazonDynamoDBClient(
            AWSCredentialsProvider credentialsProvider,
            AwsClientBuilder.EndpointConfiguration endpointConfiguration) {

        return AmazonDynamoDBClient.builder()
                .withCredentials(credentialsProvider)
                .withEndpointConfiguration(endpointConfiguration)
                .build();

    }

    /* Amazon DynamoDB configuration - End */

}
