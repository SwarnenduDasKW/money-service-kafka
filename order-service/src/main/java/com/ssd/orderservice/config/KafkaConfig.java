package com.ssd.orderservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    @Value("${spring.kafka.topic.name}")
    private String topicName;

    //Bean for creating a new topic if it doesn't exist
    @Bean
    public NewTopic createTopic() {
        return TopicBuilder.name(topicName)
            .partitions(3)
            .replicas(1)
            .build();


    }

}
