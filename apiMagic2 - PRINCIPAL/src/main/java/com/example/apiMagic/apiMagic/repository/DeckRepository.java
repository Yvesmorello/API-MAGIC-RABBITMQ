package com.example.apiMagic.apiMagic.repository;


import com.example.apiMagic.apiMagic.model.Deck;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeckRepository extends JpaRepository<Deck, Long> {
}
