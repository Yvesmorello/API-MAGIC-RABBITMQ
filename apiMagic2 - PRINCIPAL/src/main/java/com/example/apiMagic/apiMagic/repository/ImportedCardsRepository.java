package com.example.apiMagic.apiMagic.repository;

import com.example.apiMagic.apiMagic.model.DeckImported;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImportedCardsRepository extends JpaRepository<DeckImported, Long> {
}
