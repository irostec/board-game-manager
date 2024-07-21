package com.irostec.boardgamemanager.application.boundary.api.kafka.service;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameUser;
import com.irostec.boardgamemanager.application.boundary.api.jpa.service.BoardGameUserService;
import com.irostec.boardgamemanager.application.boundary.api.kafka.message.User;
import io.vavr.control.Try;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.irostec.boardgamemanager.application.boundary.api.kafka.KafkaParameters;

@Component
public class UserMessagingService {

    private final Logger logger = LogManager.getLogger(UserMessagingService.class);

    private final KafkaTemplate<String, User> userKafkaTemplate;
    private final String userTopicName;
    private final BoardGameUserService userService;

    public UserMessagingService(
        KafkaTemplate<String, User> userKafkaTemplate,
        KafkaParameters kafkaParameters,
        BoardGameUserService userService
    ) {

        this.userKafkaTemplate = userKafkaTemplate;
        this.userTopicName = kafkaParameters.userTopicName();
        this.userService = userService;

    }

    public void sendUserCreationMessage(String username) {

        User user = new User(username);

        logger.info(String.format("Notifying the creation of user: %s", user));

        userKafkaTemplate.send(userTopicName, user);

    }

    @KafkaListener(
        topics = "#{kafkaParameters.userTopicName}",
        groupId = "#{kafkaParameters.groupId}",
        containerFactory = "userKafkaListenerContainerFactory"
    )
    void receiveUserCreationMessage(User newUser) {

        logger.info(String.format("Received new user creation message: %s", newUser));

        Try<BoardGameUser> savedUserContainer = userService.saveUser(newUser.getUsername());

        savedUserContainer
            .onFailure(exception -> logger.error("", exception))
            .onSuccess(savedUser -> logger.info(String.format("Saved new user: %s", savedUser)))
            .get();

    }

}
