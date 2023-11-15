package com.example.springpoliecobe.repository;

import com.example.springpoliecobe.model.Associazione;
import com.example.springpoliecobe.model.Categoria;
import com.example.springpoliecobe.model.Localita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssociazioneRepository extends JpaRepository<Associazione, Integer> {
    List<Associazione> findByDescrizioneContaining(String descrizione);
    //Optional<Associazione> findByDescrizioneIgnoreCase(String descrizione);
    Associazione findByDescrizioneIgnoreCase(String descrizione);
    Associazione findByDescrizione(String descrizione);
    Associazione findTopByOrderByIdDesc();

    boolean existsByDescrizione(String descrizione);
}