package com.example.apiMagic.apiMagic.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class DeckImported{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "deckImported")
    private List<ImportedCards> importedCards;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ImportedCards> getCards() {
        return importedCards;
    }
/*
    public void setCards(List<ImportedCards> ImportedCards) {
        this.importedCards = importedCards;
    }*/
public void setCards(List<ImportedCards> importedCards) {
    this.importedCards = importedCards;

    for (ImportedCards card : importedCards) {
        card.setDeckImported(this);
    }
}
}

