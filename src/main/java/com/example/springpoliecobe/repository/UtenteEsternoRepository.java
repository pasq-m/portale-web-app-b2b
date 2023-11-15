package com.example.springpoliecobe.repository;

import com.example.springpoliecobe.model.UtenteEsterno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UtenteEsternoRepository extends JpaRepository<UtenteEsterno, Long> {
    List<UtenteEsterno> findByEmailContaining(String email);
    //List<UtenteEsterno> findAllByAnnunci();     //Non so se la query è corretta anche sintatticamente

    UtenteEsterno findTopByOrderByIdDesc();
}