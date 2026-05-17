package com.ssd.orderservice.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssd.basedomain.dto.Order;
import com.ssd.basedomain.dto.OrderEvent;
import com.ssd.orderservice.kafka.OrderProducer;

@RestController
@RequestMapping("/api/v1")
public class OrderController {

    private final OrderProducer orderProducer;

    public OrderController(OrderProducer orderProducer) {
        this.orderProducer = orderProducer;
    }

    @PostMapping("/orders")
    public String placeOrder(@RequestBody Order order) {

        order.setOrderId(UUID.randomUUID().toString()); //generate order id based on UUID

        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setOrder(order);
        orderEvent.setStatus("ORDER_PENDING");
        orderEvent.setMessage("Order status is pending.");

        //create order event and send to kafka topic
        orderProducer.sendMessage(orderEvent);
        return "Order placed successfully ...";
    }
}
