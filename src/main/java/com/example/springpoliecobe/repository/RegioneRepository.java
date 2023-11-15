package com.example.springpoliecobe.repository;

import com.example.springpoliecobe.model.Regione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegioneRepository extends JpaRepository<Regione, Integer> {
    List<Regione> findByDescrizioneContaining(String descrizione);

    //FindBy() restituisce tutto l'oggetto trovato ed è sotto interfaccia Optional
    Regione findByDescrizioneIgnoreCase(String descrizione);

    //existsBy() invece restituisce un boolean e non, naturalmente, l'oggetto trovato
    boolean existsByDescrizioneIgnoreCase(String descrizione);
    boolean existsByCodiceIgnoreCase(String codice);
    boolean existsByDescrizioneContaining(String descrizione);
    boolean existsByCodiceContaining(String codice);
}