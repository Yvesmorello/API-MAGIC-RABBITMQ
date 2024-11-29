package com.example.apiMagic.apiMagic.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/*
@Service
public class DeckUpdateWorker {

    private final DeckStatusWebSocket deckStatusWebSocket;

    public DeckUpdateWorker(DeckStatusWebSocket deckStatusWebSocket) {
        this.deckStatusWebSocket = deckStatusWebSocket;
    }

    @RabbitListener(queues = "deck_updates_queue")
    public void handleDeckUpdate(String updateMessage) {
        // Envia a notificação via WebSocket para os clientes conectados
        deckStatusWebSocket.sendUpdateToClient(updateMessage);
    }
}*/

@Component
public class DeckUpdateWorker {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @RabbitListener(queues = "deck_updates_queue")
    public void handleDeckUpdate(String message) {
        System.out.println("Notificação de atualização do baralho: " + message);

        // Enviar a notificação via WebSocket para o cliente
        messagingTemplate.convertAndSend("/topic/deck-updates", message);
    }
}