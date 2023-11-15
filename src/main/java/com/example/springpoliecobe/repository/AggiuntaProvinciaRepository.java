package com.example.springpoliecobe.repository;

import com.example.springpoliecobe.model.AggiuntaProvincia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AggiuntaProvinciaRepository extends JpaRepository<AggiuntaProvincia, Integer>{
    List<AggiuntaProvincia> findByCodiceContaining(String codice);
    boolean existsByCodiceIgnoreCase(String codice);
    boolean existsByCodiceContaining(String codice);
}