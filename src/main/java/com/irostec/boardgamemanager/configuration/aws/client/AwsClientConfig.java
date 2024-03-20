package com.irostec.boardgamemanager.configuration.aws.client;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.irostec.boardgamemanager.configuration.ApplicationProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

/**
 * AwsClientConfig
 * The standard configuration for AWS clients (as opposed to the one used with LocalStack.
 */
@Configuration
@ConditionalOnProperty(value = ApplicationProperties.LOCALSTACK_ENABLED_FLAG, havingValue = "false")
public class AwsClientConfig {

    /* AWS Systems Manager Parameter Store configuration - Start */

    @Bean
    public AWSSimpleSystemsManagement getSimpleSystemsManagementClient() {
        return AWSSimpleSystemsManagementClientBuilder.defaultClient();
    }

    /* AWS Systems Manager Parameter Store configuration - End */

    /* Amazon DynamoDB configuration - Start */

    @Bean
    public DynamoDbClient getDynamoDbClient() {
        return DynamoDbClient.builder().build();
    }

    @Bean
    public DynamoDbEnhancedClient getDynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
        return DynamoDbEnhancedClient.builder().dynamoDbClient(dynamoDbClient).build();
    }

    @Bean
    public AmazonDynamoDB getAmazonDynamoDBClient(
            AWSCredentialsProvider credentialsProvider,
            AwsClientBuilder.EndpointConfiguration endpointConfiguration) {

        return AmazonDynamoDBClient.builder().build();

    }

    /* Amazon DynamoDB configuration - End */

}
