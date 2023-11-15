package com.example.springpoliecobe.repository;

import com.example.springpoliecobe.model.Materiale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialeRepository extends JpaRepository<Materiale, Integer> {
    List<Materiale> findByDescrizioneContaining(String descrizione);
}