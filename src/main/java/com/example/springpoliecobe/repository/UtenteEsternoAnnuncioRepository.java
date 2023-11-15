package com.example.springpoliecobe.repository;

import com.example.springpoliecobe.embeddable.UtenteEsternoAnnuncioId;
import com.example.springpoliecobe.model.UtenteEsternoAnnuncio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtenteEsternoAnnuncioRepository extends JpaRepository<UtenteEsternoAnnuncio, UtenteEsternoAnnuncioId> {

}