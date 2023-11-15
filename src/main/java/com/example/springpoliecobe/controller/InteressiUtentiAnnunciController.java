package com.example.springpoliecobe.controller;

import com.example.springpoliecobe.model.Annuncio;
import com.example.springpoliecobe.model.Azienda;
import com.example.springpoliecobe.model.InteresseUtenteAnnuncio;
import com.example.springpoliecobe.repository.AnnuncioRepository;
import com.example.springpoliecobe.repository.AziendaRepository;
import com.example.springpoliecobe.repository.InteresseUtenteAnnuncioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;


@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:3000"}, maxAge = 3600)
@Controller
@RequestMapping("/api")
public class InteressiUtentiAnnunciController {

    @Autowired
    InteresseUtenteAnnuncioRepository interesseUtenteAnnuncioRepository;

    @Autowired
    AnnuncioRepository annuncioRepository;

    @Autowired
    AziendaRepository aziendaRepository;


    //Triggera col tasto contatta in annuncio
    @PostMapping("/add-interesse-utente-annuncio/{idAnnuncio}/{idAzienda}")
    public ResponseEntity<InteresseUtenteAnnuncio> addInteresseUtenteAnnuncio(@PathVariable("idAnnuncio") Long idAnnuncio,
                                                                              @PathVariable("idAzienda") int idAzienda) {

        Optional<Annuncio> annuncioData = annuncioRepository.findById(idAnnuncio);
        Optional<Azienda> aziendaData = aziendaRepository.findById(idAzienda);

        if (annuncioData.isPresent() && aziendaData.isPresent()) {

            Annuncio _annuncio = annuncioData.get();
            Azienda _azienda = aziendaData.get();

            InteresseUtenteAnnuncio _interesseUtenteAnnuncio = new InteresseUtenteAnnuncio(_annuncio, false,
                                                                                                            _azienda);

            return new ResponseEntity<>(interesseUtenteAnnuncioRepository.save(_interesseUtenteAnnuncio),
                                                                                    HttpStatus.CREATED);

        } else {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
