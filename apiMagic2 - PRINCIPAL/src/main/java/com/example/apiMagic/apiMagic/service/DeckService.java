package com.example.apiMagic.apiMagic.service;

import com.example.apiMagic.apiMagic.dto.*;
import com.example.apiMagic.apiMagic.model.Cards;
import com.example.apiMagic.apiMagic.model.Commander;
import com.example.apiMagic.apiMagic.model.Deck;
import com.example.apiMagic.apiMagic.repository.CardsRepository;
import com.example.apiMagic.apiMagic.repository.CommanderRepository;
import com.example.apiMagic.apiMagic.repository.DeckRepository;
import com.example.apiMagic.apiMagic.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DeckService {

    private final CardsRepository cardsRepository;
    private final UserRepository userRepository;
    private ConsomeApi consomeApi;
    private ConverteDados converteDados;
    private CardsRepository repository;
    private CommanderRepository commanderRepository;
    private DadosCardsCommander commander;
    private DadosCards cards;

    @Autowired
    private ProducerService producerService;
    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    public DeckService(ConsomeApi consomeApi, ConverteDados converteDados, CardsRepository repository, CommanderRepository commanderRepository, CardsRepository cardsRepository, UserRepository userRepository) {
        this.consomeApi = consomeApi;
        this.converteDados = converteDados;
        this.repository = repository;
        this.commanderRepository = commanderRepository;
        this.cardsRepository = cardsRepository;
        this.userRepository = userRepository;
    }

    public ApiResponse getAllCommanders() throws Exception {
        var allLegendaryJson = consomeApi.obterDadosComStream("https://api.magicthegathering.io/v1/cards?supertypes=legendary&types=creature");
        CardsResponseCommander allLegendaryResponse = converteDados.obterDadosCommander(allLegendaryJson, CardsResponseCommander.class);
        List<DadosCardsCommander> allLegendaryCardsList = allLegendaryResponse.getCards();

        Set<String> uniqueNames = new HashSet<>();
        for (DadosCardsCommander commander : allLegendaryCardsList) {
            uniqueNames.add(commander.name());
        }

        List<String> uniqueNamesList = new ArrayList<>(uniqueNames);

        return new ApiResponse(true, "Cartas lendárias para commander:", uniqueNamesList);
    }

    public ApiResponse fetchAndSaveCommanderByName(String name) {
        try {
            var json = consomeApi.obterDadosComStream("https://api.magicthegathering.io/v1/cards?supertypes=legendary&types=creature&name=" + name.replace(" ", "+"));
            CardsResponseCommander cardsResponseCommander = converteDados.obterDadosCommander(json, CardsResponseCommander.class);
            List<DadosCardsCommander> dadosCardsList = cardsResponseCommander.getCards();

            if (dadosCardsList != null && !dadosCardsList.isEmpty()) {
                List<Commander> cardsList = dadosCardsList.stream().map(this::convertToEntityCommander).limit(1).toList();
                commanderRepository.saveAll(cardsList);
                GeraJson.salvarEmJson(cardsList, "deck.json");
                return new ApiResponse(true, "Carta encontrada: ", dadosCardsList.stream().map(this::convertToEntityCommander).toList());
            } else {
                var allLegendaryJson = consomeApi.obterDadosComStream("https://api.magicthegathering.io/v1/cards?supertypes=legendary&types=creature");
                CardsResponseCommander allLegendaryResponse = converteDados.obterDadosCommander(allLegendaryJson, CardsResponseCommander.class);
                List<DadosCardsCommander> allLegendaryCardsList = allLegendaryResponse.getCards();

                if (allLegendaryCardsList != null && !allLegendaryCardsList.isEmpty()) {
                    return new ApiResponse(true, "Cartas lendárias para commander:", allLegendaryCardsList.stream().map(DadosCardsCommander::name).toList());
                } else {
                    return new ApiResponse(false, "Nenhuma carta lendária disponível para commander.", null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse(false, "Erro ao buscar ou salvar cards: " + e.getMessage(), null);
        }
    }

    private Cards convertToEntity(DadosCards dadosCards) {
        Cards cards = new Cards();
        cards.setName(dadosCards.name());
        cards.setNames(dadosCards.names());
        cards.setManaCost(dadosCards.manaCost());
        cards.setColors(dadosCards.colors());
        cards.setColorIdentity(dadosCards.colorIdentity());
        cards.setType(dadosCards.type());
        cards.setSupertypes(dadosCards.supertypes());
        cards.setTypes(dadosCards.types());
        cards.setSubtypes(dadosCards.subtypes());
        cards.setRarity(dadosCards.rarity());
        cards.setText(dadosCards.text());
        cards.setNumber(dadosCards.number());
        cards.setPower(dadosCards.power());
        cards.setToughness(dadosCards.toughness());
        cards.setImageUrl(dadosCards.imageUrl());
        return cards;
    }

    private Commander convertToEntityCommander(DadosCardsCommander dadosCardsCommander) {
        Commander commander = new Commander();
        commander.setName(dadosCardsCommander.name());
        commander.setNames(dadosCardsCommander.names());
        commander.setManaCost(dadosCardsCommander.manaCost());
        commander.setColors(dadosCardsCommander.colors());
        commander.setColorIdentity(dadosCardsCommander.colorIdentity());
        commander.setType(dadosCardsCommander.type());
        commander.setSupertypes(dadosCardsCommander.supertypes());
        commander.setTypes(dadosCardsCommander.types());
        commander.setSubtypes(dadosCardsCommander.subtypes());
        commander.setRarity(dadosCardsCommander.rarity());
        commander.setText(dadosCardsCommander.text());
        commander.setNumber(dadosCardsCommander.number());
        commander.setPower(dadosCardsCommander.power());
        commander.setToughness(dadosCardsCommander.toughness());
        commander.setImageUrl(dadosCardsCommander.imageUrl());
        return commander;
    }

    public ApiResponse getDeckByCommanderColor() throws Exception {
        var commanderColor = commanderRepository.findColorFromCommander();

        var json = consomeApi.obterDadosComStream("https://api.magicthegathering.io/v1/cards?colorIdentity=" + commanderColor);
        CardsResponse cardsResponse = converteDados.obterDados(json, CardsResponse.class);

        Set<DadosCards> uniqueCards = new HashSet<>();

        for (DadosCards card : cardsResponse.getCards()) {
            if (uniqueCards.size() >= 100) {
                break;
            }
            uniqueCards.add(card);
        }

        List<Cards> cardsList = uniqueCards.stream().map(this::convertToEntity).toList();

        Deck deck = new Deck();
        deck.setCommanderColor(commanderColor);
        deck.setCards(cardsList);

        Deck savedDeck = deckRepository.save(deck);

        GeraJson.salvarEmJson(cardsList, "deck.json");

        String message = "Novo deck criado com a cor do comandante: " + commanderColor + " e ID: " + savedDeck.getId();
        producerService.sendNotification(message);

        return new ApiResponse(true, "Deck Criado com ID: " + savedDeck.getId(), savedDeck);
    }


    public void addCardToDeck(Long deckId, String cardName) {
        String message = "Nova carta '" + cardName + "' adicionada ao baralho com ID " + deckId;
        producerService.sendNotification(message);
    }

    public Deck updateDeck(Long id, Deck updatedDeck) {
        Deck existingDeck = deckRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Deck not found"));

        existingDeck.setCommanderColor(updatedDeck.getCommanderColor());
        existingDeck.setCards(updatedDeck.getCards());

        return deckRepository.save(existingDeck);
    }


}
