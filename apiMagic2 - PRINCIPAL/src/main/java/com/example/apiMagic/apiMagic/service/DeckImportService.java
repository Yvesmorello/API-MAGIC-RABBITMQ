package com.example.apiMagic.apiMagic.service;

import com.example.apiMagic.apiMagic.repository.DeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.amqp.core.AmqpTemplate;

@Service
public class DeckImportService{

    @Autowired
    private AmqpTemplate amqpTemplate;

    private final String IMPORT_QUEUE = "deck_import_queue";

    @Autowired
    private DeckRepository deckRepository;


    public String startDeckImport(String deckDetails) {
        amqpTemplate.convertAndSend(IMPORT_QUEUE, deckDetails);
        return "Baralho enviado para a fila de importação: " + deckDetails;
    }

}










/*
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeckImportService {

    private final String DECK_IMPORT_QUEUE = "deck_import_queue";

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private DeckStatusService deckStatusService;

    public void startDeckImport(String deckDetails, String deckId) {
        deckStatusService.updateDeckStatus(deckId, "Iniciando importação");

        amqpTemplate.convertAndSend(DECK_IMPORT_QUEUE, deckDetails);

        System.out.println("Mensagem enviada para deck_import_queue: " + deckDetails);
    }
}*/