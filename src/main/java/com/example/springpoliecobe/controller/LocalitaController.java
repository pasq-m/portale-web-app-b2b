package com.example.springpoliecobe.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import com.example.springpoliecobe.model.Categoria;
import com.example.springpoliecobe.model.Localita;
import com.example.springpoliecobe.model.Provincia;
import com.example.springpoliecobe.model.Regione;
import com.example.springpoliecobe.repository.LocalitaRepository;
import com.example.springpoliecobe.repository.ProvinciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.apache.commons.lang3.text.WordUtils.capitalizeFully;


@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:3000"}, maxAge = 3600)
@RestController
@RequestMapping("/api")

public class LocalitaController {

    @Autowired
    LocalitaRepository localitaRepository;

    @Autowired
    ProvinciaRepository provinciaRepository;

    @GetMapping("/localita")
    public ResponseEntity<List<Localita>> getAllLocalita(@RequestParam(required = false) String descrizione) {
        try {
            List<Localita> localita = new ArrayList<Localita>();

            if (descrizione == null)
                localitaRepository.findAll().forEach(localita::add);
            else
                localitaRepository.findByDescrizioneContaining(descrizione).forEach(localita::add);

            if (localita.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(localita, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/localita/{id}")
    public ResponseEntity<Localita> getLocalitaById(@PathVariable("id") int id) {
        Optional<Localita> localitaData = localitaRepository.findById(id);

        if (localitaData.isPresent()) {
            return new ResponseEntity<>(localitaData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /*@PostMapping("/add-localita")
    public ResponseEntity<Localita> createLocalita(@RequestBody Localita localita) {
        try {
            Localita _localita = localitaRepository
                    //.save(new Annuncio(annuncio.getTitolo(), annuncio.getDescrizione(), false));
                    .save(new Localita(localita.getDescrizione(), localita.getCap(), localita.getId_provincia()));
            return new ResponseEntity<>(_localita, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    } */

    @PostMapping("/add-localita/{idProvincia}")
    public ResponseEntity<Localita> createLocalita(@PathVariable(value = "idProvincia") int idProvincia, @RequestBody Localita localita) {

        if (localitaRepository.existsByDescrizioneIgnoreCase(localita.getDescrizione())) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Motivo-errore", "Località già presente");
            return new ResponseEntity<>(headers, HttpStatus.CONFLICT);    //Dovrà dare 409 anche su progetto FE ufficiale
        }

        //Funzionante ma molto meno pratico
        //List<Localita> localitaData = new ArrayList<Localita>();
        //localitaRepository.findAll().forEach(localitaData::add);

        /*for (Localita  localitaFor: localitaData) {
            if (localitaFor.getDescrizione().equals(localita.getDescrizione())) {
                HttpHeaders headers = new HttpHeaders();
                headers.add("Motivo-errore", "Località già presente");
                return new ResponseEntity<>(headers, HttpStatus.CONFLICT);   //Dovrà dare 409 anche su progetto FE ufficiale
            }
        }*/

        Optional<Provincia> provinciaData = provinciaRepository.findById(idProvincia);
        Provincia provincia = provinciaData.get();
        localita.setProvincia(provincia);

        Localita _localita = localitaRepository.save(new Localita(capitalizeFully(localita.getDescrizione()), localita.getCap().toUpperCase(), localita.getProvincia()));
        return new ResponseEntity<>(_localita, HttpStatus.CREATED);

    }


    @PutMapping("/update-localita/{id}/{idProvincia}")
    public ResponseEntity<Localita> updateLocalita(@PathVariable("id") int id, @PathVariable("idProvincia") int idProvincia, @RequestBody Localita localita) {
        Optional<Localita> localitaData = localitaRepository.findById(id);

        if (localitaData.isPresent()) {
                                    //Utilizzare equalsIgnoreCase() invece del classico equals
            if (localita.getDescrizione().equalsIgnoreCase(localitaData.get().getDescrizione())) {

                Optional<Provincia> provinciaData = provinciaRepository.findById(idProvincia);
                Provincia provincia = provinciaData.get();

                Localita _localita = localitaData.get();
                _localita.setDescrizione(capitalizeFully(localita.getDescrizione()));
                _localita.setCap(localita.getCap());
                _localita.setProvincia(provincia);

                return new ResponseEntity<>(localitaRepository.save(_localita), HttpStatus.OK);

            } else {
                //Questo metodo JPA è di default CASE INSENSITIVE, quindi a prescindere la stringa corrisponde.
                if (!localitaRepository.existsByDescrizioneContaining(localita.getDescrizione())) {

                    Optional<Provincia> provinciaData = provinciaRepository.findById(idProvincia);
                    Provincia provincia = provinciaData.get();

                    Localita _localita = localitaData.get();
                    _localita.setDescrizione(capitalizeFully(localita.getDescrizione()));
                    _localita.setCap(localita.getCap());
                    _localita.setProvincia(provincia);

                    return new ResponseEntity<>(localitaRepository.save(_localita), HttpStatus.OK);
                } else {
                    HttpHeaders headers = new HttpHeaders();
                    headers.add("Motivo-errore", "Località già presente");
                    return new ResponseEntity<>(headers, HttpStatus.PRECONDITION_FAILED);
                }
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
