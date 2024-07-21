package com.irostec.boardgamemanager.configuration.security.authentication.boundary.createuser;

import com.irostec.boardgamemanager.application.boundary.api.kafka.service.UserMessagingService;
import com.irostec.boardgamemanager.configuration.security.authentication.core.CreateUserService;
import com.irostec.boardgamemanager.configuration.security.authentication.core.createuser.dependency.SaveUser;
import com.irostec.boardgamemanager.configuration.security.authentication.boundary.createuser.dependency.SaveUserInDynamoDb;
import com.irostec.boardgamemanager.configuration.security.authentication.boundary.shared.dynamodb.DynamoDbUserTable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;

/**
 * CreateUserConfiguration
 * Beans required for the CreateUser use case
 */
@Configuration
class CreateUserConfiguration {

    @Bean
    SaveUser saveUser(
        DynamoDbUserTable dynamoDbUserTable,
        DynamoDbEnhancedClient enhancedClient,
        PasswordEncoder passwordEncoder,
        UserMessagingService userMessagingService
    ) {
        return new SaveUserInDynamoDb(dynamoDbUserTable, enhancedClient, passwordEncoder, userMessagingService);
    }

    @Bean
    CreateUserService createUser(SaveUser saveUser) {
        return new CreateUserService(saveUser);
    }

}
