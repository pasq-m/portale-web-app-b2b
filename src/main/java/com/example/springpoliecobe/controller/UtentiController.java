package com.example.springpoliecobe.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.springpoliecobe.model.Azienda;
import com.example.springpoliecobe.model.FormaGiuridica;
import com.example.springpoliecobe.model.Utente;
import com.example.springpoliecobe.repository.FormaGiuridicaRepository;
import com.example.springpoliecobe.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:3000"}, maxAge = 3600)
@RestController
@RequestMapping("/api")

public class UtentiController {

    @Autowired
    UtenteRepository utenteRepository;

    @GetMapping("/utenti")
    public ResponseEntity<List<Utente>> getAllUtenti(@RequestParam(required = false) String username) {
        try {
            List<Utente> utente = new ArrayList<Utente>();

            if (username == null)
                utenteRepository.findAll().forEach(utente::add);
            else
                utenteRepository.findByUsernameContaining(username).forEach(utente::add);

            if (utente.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(utente, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/utenti/{id}")
    public ResponseEntity<Utente> getUtenteById(@PathVariable("id") int id) {
        Optional<Utente> utenteData = utenteRepository.findById(id);

        if (utenteData.isPresent()) {
            return new ResponseEntity<>(utenteData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

