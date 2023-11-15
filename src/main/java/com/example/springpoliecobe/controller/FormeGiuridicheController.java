package com.example.springpoliecobe.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.springpoliecobe.model.Associazione;
import com.example.springpoliecobe.model.FormaGiuridica;
import com.example.springpoliecobe.repository.FormaGiuridicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:3000"}, maxAge = 3600)
@RestController
@RequestMapping("/api")

public class FormeGiuridicheController {

    @Autowired
    FormaGiuridicaRepository formaGiuridicaRepository;

    @GetMapping("/forme-giuridiche")
    public ResponseEntity<List<FormaGiuridica>> getAllFormeGiuridiche(@RequestParam(required = false) String descrizione) {
        try {
            List<FormaGiuridica> formaGiuridica = new ArrayList<FormaGiuridica>();

            if (descrizione == null)
                formaGiuridicaRepository.findAll().forEach(formaGiuridica::add);
            else
                formaGiuridicaRepository.findByDescrizioneContaining(descrizione).forEach(formaGiuridica::add);

            if (formaGiuridica.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(formaGiuridica, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/forme-giuridiche/{id}")
    public ResponseEntity<FormaGiuridica> getFormaGiuridicaById(@PathVariable("id") int id) {
        Optional<FormaGiuridica> formeGiuridicheData = formaGiuridicaRepository.findById(id);

        if (formeGiuridicheData.isPresent()) {
            return new ResponseEntity<>(formeGiuridicheData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/add-forma-giuridica")
    public ResponseEntity<FormaGiuridica> createFormaGiuridica(@RequestBody FormaGiuridica formaGiuridica) {
        try {
            FormaGiuridica _formaGiuridica = formaGiuridicaRepository
                    //.save(new Annuncio(annuncio.getTitolo(), annuncio.getDescrizione(), false));
                    .save(new FormaGiuridica(formaGiuridica.getDescrizione()));
            return new ResponseEntity<>(_formaGiuridica, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update-forma-giuridica/{id}")
    public ResponseEntity<FormaGiuridica> updateFormaGiuridica(@PathVariable("id") int id, @RequestBody FormaGiuridica descrizione) {
        Optional<FormaGiuridica> formeGiuridicheData = formaGiuridicaRepository.findById(id);

        if (formeGiuridicheData.isPresent()) {
            FormaGiuridica _categoria = formeGiuridicheData.get();
            _categoria.setDescrizione(descrizione.getDescrizione());
            return new ResponseEntity<>(formaGiuridicaRepository.save(_categoria), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
