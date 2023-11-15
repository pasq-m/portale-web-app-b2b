package com.example.springpoliecobe.repository;

import com.example.springpoliecobe.model.Annuncio;
import com.example.springpoliecobe.model.UtenteEsternoAnnuncio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AnnuncioRepository extends JpaRepository<Annuncio, Long> {
    //List<Annuncio> findByPublished(boolean published);
    List<Annuncio> findByTitoloContaining(String titolo);

    //Optional<UtenteEsternoAnnuncio> findById(Long id);

    //Metteremo nello scheduling (nel package scheduledtasks) l'ora attuale come parametro da passare qua sotto
    //List<Annuncio> findByDataDiScadenzaBefore(LocalDateTime dataScadenza);
    List<Annuncio> findByDataDiScadenzaBefore(LocalDate dataScadenza);
    List<Annuncio> findByChiusoIsFalse();

    //Cerca tutti gli annunci collegati a un determinato id utenteEsterno e li piazza in una lista Annuncio
    //Optional<List<Annuncio>> findByUtenteEsternoIdContaining(int utenteEsternoId);
}
