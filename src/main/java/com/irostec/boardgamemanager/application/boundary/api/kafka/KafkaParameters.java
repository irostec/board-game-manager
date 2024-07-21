package com.irostec.boardgamemanager.application.boundary.api.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public record KafkaParameters(
    @Value("${spring.kafka.bootstrap-servers}") String bootstrapServers,
    @Value("${spring.kafka.group-id}") String groupId,
    @Value("${spring.kafka.user-topic-name}") String userTopicName
) {}