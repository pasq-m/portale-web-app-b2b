package com.example.springpoliecobe.repository;

import com.example.springpoliecobe.model.Localita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocalitaRepository extends JpaRepository<Localita, Integer> {
    List<Localita> findByDescrizioneContaining(String descrizione);
    Optional<Localita> findByDescrizioneIgnoreCase(String descrizione);
    boolean existsByDescrizioneIgnoreCase(String descrizione);
    boolean existsByDescrizioneContaining(String descrizione);

    Localita findTopByOrderByIdDesc();
}