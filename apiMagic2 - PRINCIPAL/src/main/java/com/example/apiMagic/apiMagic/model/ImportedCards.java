package com.example.apiMagic.apiMagic.model;

import com.example.apiMagic.apiMagic.dto.DadosCards;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "imported_cards")
public class ImportedCards {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long _id;

    @ManyToOne
    @JoinColumn(name = "deck_imported_id")
    private DeckImported deckImported;

    private String name;
    private List<String> names = new ArrayList<>();
    @Column(length = 1000)
    private String manaCost;
    @Column(length = 1000)
    private List<String> colors = new ArrayList<>();
    @Column(length = 1000)
    private List<String> colorIdentity = new ArrayList<>();
    @Column(length = 1000)
    private String type;
    @Column(length = 1000)
    private List<String> supertypes = new ArrayList<>();
    @Column(length = 1000)
    private List<String> types = new ArrayList<>();
    @Column(length = 1000)
    private List<String> subtypes = new ArrayList<>();
    @Column(length = 1000)
    private String rarity;
    @Column(length = 1000)
    private String text;
    private String number;
    private String power;
    private String toughness;
    @Column(length = 1000)
    private String imageUrl;

    public ImportedCards(DadosCards dadosCards) {
        this.name = dadosCards.name();
        this.names = dadosCards.names();
        this.manaCost = dadosCards.manaCost();
        this.colors = dadosCards.colors();
        this.colorIdentity = dadosCards.colorIdentity();
        this.type = dadosCards.type();
        this.supertypes = dadosCards.supertypes();
        this.types = dadosCards.types();
        this.subtypes = dadosCards.subtypes();
        this.rarity = dadosCards.rarity();
        this.text = dadosCards.text();
        this.number = dadosCards.number();
        this.power = dadosCards.power();
        this.toughness = dadosCards.toughness();
        this.imageUrl = dadosCards.imageUrl();
    }

    public ImportedCards() {}

    public Long getId() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public List<String> getNames() {
        return names;
    }

    public String getManaCost() {
        return manaCost;
    }

    public List<String> getColors() {
        return colors;
    }

    public List<String> getColorIdentity() {
        return colorIdentity;
    }

    public String getType() {
        return type;
    }

    public List<String> getSupertypes() {
        return supertypes;
    }

    public List<String> getTypes() {
        return types;
    }

    public List<String> getSubtypes() {
        return subtypes;
    }

    public String getRarity() {
        return rarity;
    }

    public String getText() {
        return text;
    }

    public String getNumber() {
        return number;
    }

    public String getPower() {
        return power;
    }

    public String getToughness() {
        return toughness;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setId(Long id) {
        this._id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public void setManaCost(String manaCost) {
        this.manaCost = manaCost;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public void setColorIdentity(List<String> colorIdentity) {
        this.colorIdentity = colorIdentity;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSupertypes(List<String> supertypes) {
        this.supertypes = supertypes;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public void setSubtypes(List<String> subtypes) {
        this.subtypes = subtypes;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public void setToughness(String toughness) {
        this.toughness = toughness;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public DeckImported getDeckImported() {
        return deckImported;
    }

    public void setDeckImported(DeckImported deckImported) {
        this.deckImported = deckImported;
    }

}

