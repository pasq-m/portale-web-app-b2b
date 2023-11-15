package com.example.springpoliecobe.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.springpoliecobe.model.UnitaDiMisura;
import com.example.springpoliecobe.repository.UnitaDiMisuraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:3000"}, maxAge = 3600)
@RestController
@RequestMapping("/api")

public class UnitaDiMisuraController {

    @Autowired
    UnitaDiMisuraRepository unitaDiMisuraRepository;

    @GetMapping("/unita-di-misura")
    public ResponseEntity<List<UnitaDiMisura>> getAllUnitaDiMisura(@RequestParam(required = false) String descrizione) {
        try {
            List<UnitaDiMisura> unitaDiMisura = new ArrayList<UnitaDiMisura>();

            if (descrizione == null)
                unitaDiMisuraRepository.findAll().forEach(unitaDiMisura::add);
            else
                unitaDiMisuraRepository.findByDescrizioneContaining(descrizione).forEach(unitaDiMisura::add);

            if (unitaDiMisura.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(unitaDiMisura, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/unita-di-misura/{id}")
    public ResponseEntity<UnitaDiMisura> getUnitaDiMisuraById(@PathVariable("id") int id) {
        Optional<UnitaDiMisura> unitaDiMisuraData = unitaDiMisuraRepository.findById(id);

        if (unitaDiMisuraData.isPresent()) {
            return new ResponseEntity<>(unitaDiMisuraData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/add-unita-di-misura")
    public ResponseEntity<UnitaDiMisura> createUnitaDiMisura(@RequestBody UnitaDiMisura unitaDiMisura) {
        try {
            UnitaDiMisura _unitaDiMisura = unitaDiMisuraRepository
                    .save(new UnitaDiMisura(unitaDiMisura.getDescrizione()));
            return new ResponseEntity<>(_unitaDiMisura, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update-unita-di-misura/{id}")
    public ResponseEntity<UnitaDiMisura> updateUnitaDiMisura(@PathVariable("id") int id, @RequestBody UnitaDiMisura descrizione) {
        Optional<UnitaDiMisura> unitaDiMisuraData = unitaDiMisuraRepository.findById(id);

        if (unitaDiMisuraData.isPresent()) {
            UnitaDiMisura _unitaDiMisura = unitaDiMisuraData.get();
            _unitaDiMisura.setDescrizione(descrizione.getDescrizione());
            return new ResponseEntity<>(unitaDiMisuraRepository.save(_unitaDiMisura), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}


