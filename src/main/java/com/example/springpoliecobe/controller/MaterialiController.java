package com.example.springpoliecobe.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.springpoliecobe.model.Materiale;
import com.example.springpoliecobe.repository.MaterialeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:3000"}, maxAge = 3600)
@RestController
@RequestMapping("/api")

public class MaterialiController {

    @Autowired
    MaterialeRepository materialeRepository;

    @GetMapping("/materiali")
    public ResponseEntity<List<Materiale>> getAllMateriali(@RequestParam(required = false) String descrizione) {
        try {
            List<Materiale> materiale = new ArrayList<Materiale>();

            if (descrizione == null)
                materialeRepository.findAll().forEach(materiale::add);
            else
                materialeRepository.findByDescrizioneContaining(descrizione).forEach(materiale::add);

            if (materiale.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(materiale, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/materiali/{id}")
    public ResponseEntity<Materiale> getMaterialeById(@PathVariable("id") int id) {
        Optional<Materiale> materialiData = materialeRepository.findById(id);

        if (materialiData.isPresent()) {
            return new ResponseEntity<>(materialiData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/add-materiale")
    public ResponseEntity<Materiale> createMateriale(@RequestBody Materiale materiale) {
        try {
            Materiale _materiale = materialeRepository
                    //.save(new Annuncio(annuncio.getTitolo(), annuncio.getDescrizione(), false));
                    .save(new Materiale(materiale.getDescrizione()));
            return new ResponseEntity<>(_materiale, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update-materiale/{id}")
    public ResponseEntity<Materiale> updateMateriale(@PathVariable("id") int id, @RequestBody Materiale descrizione) {
        Optional<Materiale> MaterialiData = materialeRepository.findById(id);

        if (MaterialiData.isPresent()) {
            Materiale _materiale = MaterialiData.get();
            _materiale.setDescrizione(descrizione.getDescrizione());
            return new ResponseEntity<>(materialeRepository.save(_materiale), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

