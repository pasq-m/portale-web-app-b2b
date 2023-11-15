package com.example.springpoliecobe.repository;

import com.example.springpoliecobe.model.Azienda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AziendaRepository extends JpaRepository<Azienda, Integer> {
    List<Azienda> findByRagioneSocialeContaining(String ragioneSociale);
}