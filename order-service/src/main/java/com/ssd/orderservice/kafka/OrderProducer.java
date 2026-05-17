package com.ssd.orderservice.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import com.ssd.basedomain.dto.OrderEvent;


@Service
public class OrderProducer {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(OrderProducer.class);

    private NewTopic topic;

    private KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public OrderProducer(NewTopic topic, KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(OrderEvent event) {
        LOGGER.info(String.format("Order event => %s", event.toString()));
        //this.kafkaTemplate.send(topic.name(), event);

        //create message
        Message<OrderEvent> message = MessageBuilder
            .withPayload(event)
            .setHeader(KafkaHeaders.TOPIC, topic.name())
            .build();

        this.kafkaTemplate.send(message);
        
    }

}
