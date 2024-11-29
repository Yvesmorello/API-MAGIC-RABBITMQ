package com.example.apiMagic.apiMagic.controller;

import com.example.apiMagic.apiMagic.dto.*;
import com.example.apiMagic.apiMagic.model.Deck;
import com.example.apiMagic.apiMagic.service.DeckImportService;
import com.example.apiMagic.apiMagic.service.DeckService;
import com.example.apiMagic.apiMagic.service.ProducerService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/deck")
public class DeckController {

    @Autowired
    private DeckService deckService;
    @Autowired
    private DeckImportService deckImportService;
    @Autowired
    private ProducerService producerService;

    @Autowired
    //private DeckStatusWebSocket deckStatusService;

    @GetMapping("/commanders")
    public ResponseEntity<ApiResponse> commander() throws Exception {
        ApiResponse response = deckService.getAllCommanders();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/commander/{name}")
    public ResponseEntity<ApiResponse> commanderName(@PathVariable String name) {
        ApiResponse response = deckService.fetchAndSaveCommanderByName(name);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else if (response.getData() != null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/cards")
    public ResponseEntity<ApiResponse> cards() throws Exception {
        ApiResponse response = deckService.getDeckByCommanderColor();

        if(response.isSuccess()) {
            return ResponseEntity.ok((response));
        } else if (response.getData() != null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Deck> updateDeck(@PathVariable Long id, @RequestBody Deck updatedDeck) {
        try {
            Deck deck = deckService.updateDeck(id, updatedDeck);
            return ResponseEntity.ok(deck);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/import")
    public String importDeck(@RequestBody String deckDetails) {
        deckImportService.startDeckImport(deckDetails);
        return "Importação de baralho iniciada!";
    }

/*
    @GetMapping("/status/{deckId}")
    public String getDeckStatus(@PathVariable String deckId) {
        return deckStatusService.getDeckStatus(deckId);
    }*/

}


