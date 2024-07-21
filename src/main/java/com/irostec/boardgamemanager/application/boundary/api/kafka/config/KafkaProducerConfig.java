package com.irostec.boardgamemanager.application.boundary.api.kafka.config;

import com.irostec.boardgamemanager.application.boundary.api.kafka.KafkaParameters;
import com.irostec.boardgamemanager.application.boundary.api.kafka.message.User;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;

@Configuration
@AllArgsConstructor
class KafkaProducerConfig {

    private final KafkaParameters kafkaParameters;

    @Bean
    public ProducerFactory<String, User> userProducerFactory() {

        Map<String, Object> configProps = Map.of(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaParameters.bootstrapServers(),
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class
        );

        return new DefaultKafkaProducerFactory<>(configProps);

    }

    @Bean
    public KafkaTemplate<String, User> kafkaTemplate() {
        return new KafkaTemplate<>(userProducerFactory());
    }

}
