package com.example.springpoliecobe.repository;

import com.example.springpoliecobe.model.AggiuntaLocalita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AggiuntaLocalitaRepository extends JpaRepository<AggiuntaLocalita, Integer> {
    List<AggiuntaLocalita> findByDescrizioneContaining(String descrizione);
    boolean existsByDescrizioneIgnoreCase(String descrizione);
    boolean existsByDescrizioneContaining(String descrizione);
}