package com.ssd.stockservice.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.ssd.basedomain.dto.OrderEvent;

@Service
public class  OrderConsumer {

    private static final Logger logger = LoggerFactory.getLogger(OrderConsumer.class);

    @KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(OrderEvent message) {
        logger.info("Order event received in stock service: {}", message.toString());
        // Here you would add logic to process the order and update stock levels
    }

}
