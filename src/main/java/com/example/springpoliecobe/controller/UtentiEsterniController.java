package com.example.springpoliecobe.controller;

import com.example.springpoliecobe.model.*;
import com.example.springpoliecobe.repository.AnnuncioRepository;
import com.example.springpoliecobe.repository.UtenteEsternoRepository;
import com.example.springpoliecobe.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:3000"}, maxAge = 3600)
@RestController
@RequestMapping("/api")
public class UtentiEsterniController {

    @Autowired
    UtenteEsternoRepository utenteEsternoRepository;

    @Autowired
    UtenteRepository utenteRepository;

    @Autowired
    AnnuncioRepository annuncioRepository;

    //Controller che invia una notifica ad altro codice appena vengono inviati i dati di contatto
    //nel FE da parte dell'utente esterno.

    //In seguito, solo l'admin potrà visualizzare queste richieste in una apposita sezione, NON l'autore dell'annuncio.


    @GetMapping("/utenti-esterni")
    public ResponseEntity<?> getAllUtentiEsterni(@RequestParam(required = false) String email) {
        try {
            List<UtenteEsterno> utentiEsterni = new ArrayList<>();

            if (email == null)
                utenteEsternoRepository.findAll().forEach(utentiEsterni::add);
            else
                utenteEsternoRepository.findByEmailContaining(email).forEach(utentiEsterni::add);

            if (utentiEsterni.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(utentiEsterni, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //***** IMPORTANTE: I METODI PER SCRIVERE SU DB RIGUARDO AGLI INTERESSI DEGLI UTENTI ESTERNI VERSO GLI ANNUNCI *****
    //***** SONO ORA GESTITI DENTRO IL CONTROLLER "UtentiEsterniAnnunciController" *****

    //@PostMapping("/add-utente-esterno/{idAnnuncio}/{idAutoreAnnuncio}")
    @PostMapping("/add-utente-esterno/{idAnnuncio}")
    public ResponseEntity<UtenteEsterno> addUtenteEsterno(@PathVariable("idAnnuncio") Long idAnnuncio,
                                                          //@PathVariable("idAutoreAnnuncio") int idAutoreAnnuncio,
                                                          @RequestBody UtenteEsterno utenteEsterno){
        List<Annuncio> _annunci = new ArrayList<>();

        Optional<Annuncio> annuncioData = annuncioRepository.findById(idAnnuncio);
        //Optional<UtenteEsternoAnnuncio> annuncioData = annuncioRepository.findById(idAnnuncio);

        if (annuncioData.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } else {
            _annunci.add(annuncioData.get());
        }

        //Optional<Utente> utenteData = utenteRepository.findById(idAutoreAnnuncio);
        /*return new ResponseEntity<> (utenteEsternoRepository.save(new UtenteEsterno(utenteEsterno.getEmail(),
                                    utenteEsterno.getTelefono(), utenteEsterno.getRagioneSociale(), _annunci)), HttpStatus.CREATED);*/

        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
