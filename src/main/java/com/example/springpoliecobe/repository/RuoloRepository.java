package com.example.springpoliecobe.repository;

import com.example.springpoliecobe.model.ERuolo;
import com.example.springpoliecobe.model.Ruolo;
import com.example.springpoliecobe.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface RuoloRepository extends JpaRepository<Ruolo, Integer> {
    Ruolo findByDescrizione(String descrizione);
    Optional<Ruolo> findByDescrizione(ERuolo descrizione);
    List<Ruolo> findByDescrizioneContaining(String descrizione);

    //@Query(value="select * from ruoli_utenti a where a.utente_id = :utenteId", nativeQuery=true)
    //Ruolo findByUtenteId(@Param("utenteId")int utenteId);

    //@Override
    Collection<Ruolo> findById(int id);
    //Ruolo findById(int id);
}