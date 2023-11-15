package com.example.springpoliecobe.repository;

import com.example.springpoliecobe.model.UnitaDiMisura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnitaDiMisuraRepository extends JpaRepository<UnitaDiMisura, Integer> {
    List<UnitaDiMisura> findByDescrizioneContaining(String descrizione);
}