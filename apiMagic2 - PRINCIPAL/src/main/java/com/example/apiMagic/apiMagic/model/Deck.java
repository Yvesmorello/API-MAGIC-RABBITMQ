package com.example.apiMagic.apiMagic.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Deck{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String commanderColor;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Cards> cards;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommanderColor() {
        return commanderColor;
    }

    public void setCommanderColor(String commanderColor) {
        this.commanderColor = commanderColor;
    }

    public List<Cards> getCards() {
        return cards;
    }

    public void setCards(List<Cards> cards) {
        this.cards = cards;
    }
}

