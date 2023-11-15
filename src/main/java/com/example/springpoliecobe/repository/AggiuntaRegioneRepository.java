package com.example.springpoliecobe.repository;

import com.example.springpoliecobe.model.AggiuntaRegione;
import com.example.springpoliecobe.model.Regione;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AggiuntaRegioneRepository extends JpaRepository<AggiuntaRegione, Integer> {
    List<AggiuntaRegione> findByDescrizioneContaining(String descrizione);
    boolean existsByDescrizioneIgnoreCase(String descrizione);
    boolean existsByCodiceIgnoreCase(String codice);
    boolean existsByDescrizioneContaining(String descrizione);
    boolean existsByCodiceContaining(String codice);
}