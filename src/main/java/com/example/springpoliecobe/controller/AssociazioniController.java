package com.example.springpoliecobe.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.springpoliecobe.model.Associazione;
import com.example.springpoliecobe.model.Categoria;
import com.example.springpoliecobe.repository.AssociazioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:3000"}, maxAge = 3600)
@RestController
@RequestMapping("/api")

public class AssociazioniController {

    @Autowired
    AssociazioneRepository associazioneRepository;

    @GetMapping("/associazioni")
    public ResponseEntity<List<Associazione>> getAllAssociazioni(@RequestParam(required = false) String descrizione) {
        try {
            List<Associazione> associazione = new ArrayList<Associazione>();

            if (descrizione == null)
                associazioneRepository.findAll().forEach(associazione::add);
            else
                associazioneRepository.findByDescrizioneContaining(descrizione).forEach(associazione::add);

            if (associazione.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(associazione, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/associazioni/{id}")
    public ResponseEntity<Associazione> getAssociazioneById(@PathVariable("id") int id) {
        Optional<Associazione> associazioniData = associazioneRepository.findById(id);

        if (associazioniData.isPresent()) {
            return new ResponseEntity<>(associazioniData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/add-associazione")
    public ResponseEntity<Associazione> createAssociazione(@RequestBody Associazione associazione) {
        try {
            Associazione _associazione = associazioneRepository
                    //.save(new Annuncio(annuncio.getTitolo(), annuncio.getDescrizione(), false));
                    .save(new Associazione(associazione.getDescrizione()));
            return new ResponseEntity<>(_associazione, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update-associazione/{id}")
    public ResponseEntity<Associazione> updateAssociazione(@PathVariable("id") int id, @RequestBody Associazione descrizione) {
        Optional<Associazione> associazioniData = associazioneRepository.findById(id);

        if (associazioniData.isPresent()) {
            Associazione _associazione = associazioniData.get();
            _associazione.setDescrizione(descrizione.getDescrizione());
            return new ResponseEntity<>(associazioneRepository.save(_associazione), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // *** Commentati per sicurezza perché non utilizzati per ora ***

    /*
    @DeleteMapping("/associazioni/{id}")
    public ResponseEntity<HttpStatus> deleteAssociazione(@PathVariable("id") int id) {
        try {
            associazioneRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/associazioni")
    public ResponseEntity<HttpStatus> deleteAllAssociazioni() {
        try {
            associazioneRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    */
}
