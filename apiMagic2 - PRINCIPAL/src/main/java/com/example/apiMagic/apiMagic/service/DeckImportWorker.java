package com.example.apiMagic.apiMagic.service;

import aj.org.objectweb.asm.TypeReference;
import com.example.apiMagic.apiMagic.dto.CardsResponse;
import com.example.apiMagic.apiMagic.dto.DadosCards;
import com.example.apiMagic.apiMagic.model.Cards;
import com.example.apiMagic.apiMagic.model.Deck;
import com.example.apiMagic.apiMagic.model.DeckImported;
import com.example.apiMagic.apiMagic.model.ImportedCards;
import com.example.apiMagic.apiMagic.repository.ImportedCardsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
/*
@Component
public class DeckImportWorker {

    @Autowired
    private ProducerService producerService;
    @Autowired
    private DeckRepository deckRepository;


    @RabbitListener(queues = "deck_import_queue")
    public void importDeck(String deckDetails) {
        System.out.println("Iniciando a importação do baralho: " + deckDetails);

        String updateMessage = "Importação do baralho concluída: " + deckDetails;
        producerService.sendDeckUpdate(updateMessage);  
    }
}
*/


@Component
public class DeckImportWorker {

    @Autowired
    private ProducerService producerService;

    @Autowired
    private ImportedCardsRepository importedCards;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ImportedCardsRepository importedCardsRepository;
    @Autowired
    private ConverteDados converteDados;


    @RabbitListener(queues = "deck_import_queue")
    public void importDeck(String deckDetails) {
        System.out.println("Iniciando a importação do baralho: " + deckDetails);

        try {
            CardsResponse cardsResponse = converteDados.obterDados(deckDetails, CardsResponse.class);

            Set<DadosCards> uniqueCards = new HashSet<>();

            for (DadosCards card : cardsResponse.getCards()) {
                if (uniqueCards.size() >= 100) {
                    break;
                }
                uniqueCards.add(card);
            }

            List<ImportedCards> cardsList = uniqueCards.stream().map(this::convertToEntity).toList();

            DeckImported deckImported = new DeckImported();
            deckImported.setCards(cardsList);

            DeckImported savedDeck = importedCardsRepository.save(deckImported);


            System.out.println("Baralho salvo com sucesso no banco: " + savedDeck);

            String updateMessage = "Importação do baralho concluída: " + savedDeck.getId();
            producerService.sendDeckUpdate(updateMessage);

        } catch (Exception e) {
            System.err.println("Erro ao importar o baralho: " + e.getMessage());
            e.printStackTrace();
        }

    }


    private ImportedCards convertToEntity(DadosCards dadosCards) {
        ImportedCards importedCards = new ImportedCards();
        importedCards.setName(dadosCards.name());
        importedCards.setNames(dadosCards.names());
        importedCards.setManaCost(dadosCards.manaCost());
        importedCards.setColors(dadosCards.colors());
        importedCards.setColorIdentity(dadosCards.colorIdentity());
        importedCards.setType(dadosCards.type());
        importedCards.setSupertypes(dadosCards.supertypes());
        importedCards.setTypes(dadosCards.types());
        importedCards.setSubtypes(dadosCards.subtypes());
        importedCards.setRarity(dadosCards.rarity());
        importedCards.setText(dadosCards.text());
        importedCards.setNumber(dadosCards.number());
        importedCards.setPower(dadosCards.power());
        importedCards.setToughness(dadosCards.toughness());
        importedCards.setImageUrl(dadosCards.imageUrl());
        return importedCards;
    }
}