package com.example.springpoliecobe.repository;

import com.example.springpoliecobe.model.FormaGiuridica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormaGiuridicaRepository extends JpaRepository<FormaGiuridica, Integer> {
    List<FormaGiuridica> findByDescrizioneContaining(String descrizione);
    Optional<FormaGiuridica> findByDescrizioneIgnoreCase(String descrizione);
    FormaGiuridica findTopByOrderByIdDesc();

    boolean existsByDescrizioneIgnoreCase(String descrizione);
}