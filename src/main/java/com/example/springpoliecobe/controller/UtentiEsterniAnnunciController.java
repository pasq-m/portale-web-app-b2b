package com.example.springpoliecobe.controller;

import com.example.springpoliecobe.model.Annuncio;
import com.example.springpoliecobe.model.UtenteEsterno;
import com.example.springpoliecobe.model.UtenteEsternoAnnuncio;
import com.example.springpoliecobe.repository.AnnuncioRepository;
import com.example.springpoliecobe.repository.UtenteEsternoAnnuncioRepository;
import com.example.springpoliecobe.repository.UtenteEsternoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:3000"}, maxAge = 3600)
@RestController
@RequestMapping("/api")
public class UtentiEsterniAnnunciController {

    @Autowired
    AnnuncioRepository annuncioRepository;

    @Autowired
    UtenteEsternoAnnuncioRepository utenteEsternoAnnuncioRepository;

    @Autowired
    UtenteEsternoRepository utenteEsternoRepository;


    @GetMapping("/interessi-verso-annunci")
    public ResponseEntity<List<UtenteEsternoAnnuncio>> getAllInteressiVersoAnnunci() {

        List<UtenteEsternoAnnuncio> utentiEsterniAnnunciData = utenteEsternoAnnuncioRepository.findAll();

        if (utentiEsterniAnnunciData.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } else {

            return new ResponseEntity<>(utentiEsterniAnnunciData, HttpStatus.OK);
        }
    }

    @PostMapping("/add-utente-esterno-annuncio/{idAnnuncio}")
    public ResponseEntity<UtenteEsternoAnnuncio> addUtenteEsternoAnnuncio(@PathVariable("idAnnuncio") Long idAnnuncio,
                                                                          @RequestBody UtenteEsterno utenteEsterno) {

        utenteEsternoRepository.save(new UtenteEsterno(utenteEsterno.getEmail(),
                                        utenteEsterno.getTelefono(), utenteEsterno.getRagioneSociale()));

        //È necessario prima salvare da solo l'utente esterno (sopra) e poi riandarlo a pescare tramite query metodo
        //della repo ed associarlo alla creazione del nuovo oggetto "UtenteEsternoAnnuncio" (sotto).

        UtenteEsterno lastUtenteEsterno = utenteEsternoRepository.findTopByOrderByIdDesc();

        List<Annuncio> _annunci = new ArrayList<>();

        Optional<Annuncio> annuncioData = annuncioRepository.findById(idAnnuncio);

        if (annuncioData.isPresent()) {

            return new ResponseEntity<>(utenteEsternoAnnuncioRepository.save(new UtenteEsternoAnnuncio(
                                                            lastUtenteEsterno, annuncioData.get(), LocalDate.now(),
                                                            false)), HttpStatus.CREATED);
        } else {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
