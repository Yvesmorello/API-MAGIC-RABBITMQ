package com.example.apiMagic.apiMagic.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {

    @RabbitListener(queues = "notificationQueue")
    public void receiveNotification(String message) {
        System.out.println("Notificação recebida: " + message);
    }
}
