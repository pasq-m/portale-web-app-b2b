package com.example.springpoliecobe.repository;

import com.example.springpoliecobe.model.InteresseUtenteAnnuncio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InteresseUtenteAnnuncioRepository extends JpaRepository<InteresseUtenteAnnuncio, Integer> {

}