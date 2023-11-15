package com.example.springpoliecobe.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import com.example.springpoliecobe.model.Localita;
import com.example.springpoliecobe.model.Provincia;
import com.example.springpoliecobe.model.Regione;
import com.example.springpoliecobe.repository.RegioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.apache.commons.lang3.text.WordUtils.capitalizeFully;

//Per mostrare e gestire gli headers specifici al di fuori di quelli di default permessi da CORS, bisogna specificarli
//tramite "exposeHeaders"
@CrossOrigin(origins = "*", maxAge = 3600, exposedHeaders = {"Motivo-errore"})
@RestController
@RequestMapping("/api")

public class RegioniController {

    @Autowired
    RegioneRepository regioneRepository;

    @GetMapping("/regioni")
    public ResponseEntity<List<Regione>> getAllRegioni(@RequestParam(required = false) String descrizione) {
        try {
            List<Regione> regioni = new ArrayList<Regione>();

            if (descrizione == null)
                regioneRepository.findAll().forEach(regioni::add);
            else
                regioneRepository.findByDescrizioneContaining(descrizione).forEach(regioni::add);

            if (regioni.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(regioni, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/regioni/{id}")
    public ResponseEntity<Regione> getRegioneById(@PathVariable("id") int id) {
        Optional<Regione> regioniData = regioneRepository.findById(id);

        if (regioniData.isPresent()) {
            return new ResponseEntity<>(regioniData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //GET per ottenere le province da una regione selezionata dal FE
    @GetMapping("/regioni/province/{id}")
    public ResponseEntity<List<Provincia>> getProvinceByRegioneId(@PathVariable("id") int id) {
        Optional<Regione> regioneData = regioneRepository.findById(id);

        if (regioneData.isPresent()) {

            List<Provincia> provinceList = regioneData.get().getProvince();
            if (!provinceList.isEmpty()) {

                //return new ResponseEntity<Provincia>((Provincia) provinceList, HttpStatus.OK);
                return new ResponseEntity<List<Provincia>>(provinceList, HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/add-regione")
    public ResponseEntity<Regione> createRegione(@RequestBody Regione regione) {

        if (regioneRepository.existsByDescrizioneIgnoreCase(regione.getDescrizione()) ||
                regioneRepository.existsByCodiceIgnoreCase(regione.getCodice())) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Motivo-errore", "Regione e/o codice regione già presenti");
            return new ResponseEntity<>(headers, HttpStatus.CONFLICT);    //Dovrà dare 409 anche su progetto FE ufficiale
        }

        Regione _regione = regioneRepository
                .save(new Regione(capitalizeFully(regione.getDescrizione()), regione.getCodice().toUpperCase()));
        return new ResponseEntity<>(_regione, HttpStatus.CREATED);
    }

    @PutMapping("/update-regione/{id}")
    public ResponseEntity<Regione> updateRegione(@PathVariable("id") int id, @RequestBody Regione regione) {
        Optional<Regione> RegioniData = regioneRepository.findById(id);

        if (RegioniData.isPresent()) {
            //Utilizzare equalsIgnoreCase() invece del classico equals
            if (regione.getDescrizione().equalsIgnoreCase(RegioniData.get().getDescrizione())) {

                Regione _regione = RegioniData.get();
                _regione.setDescrizione(capitalizeFully(regione.getDescrizione()));

                //Check se il codice era quello già presente nello stesso campo
                if (_regione.getCodice().equalsIgnoreCase(regione.getCodice())) {

                    _regione.setCodice(regione.getCodice().toUpperCase());
                    return new ResponseEntity<>(regioneRepository.save(_regione), HttpStatus.OK);

                //Se siamo su questo blocco significa che stiamo aggiungendo un altro codice; controlliamo che già
                //non esista nella tabella
                } else if (!regioneRepository.existsByCodiceContaining(regione.getCodice())) {

                    _regione.setCodice(regione.getCodice().toUpperCase());
                    return new ResponseEntity<>(regioneRepository.save(_regione), HttpStatus.OK);

                } else {

                    HttpHeaders headers = new HttpHeaders();
                    headers.add("Motivo-errore", "Codice regione già presente");
                    return new ResponseEntity<>(headers, HttpStatus.PRECONDITION_FAILED);
                }

                //HttpHeaders headers = new HttpHeaders();
                //headers.add("Motivo-errore", "Codice regione già presente");
                //return new ResponseEntity<>(headers, HttpStatus.PRECONDITION_FAILED);

            } else {
                //Questo metodo JPA è di default CASE INSENSITIVE, quindi a prescindere la stringa corrisponde.
                if (!regioneRepository.existsByDescrizioneContaining(regione.getDescrizione()) &&
                        !regioneRepository.existsByCodiceContaining(regione.getCodice())) {

                    Regione _regione = RegioniData.get();
                    _regione.setDescrizione(capitalizeFully(regione.getDescrizione()));
                    _regione.setCodice(regione.getCodice().toUpperCase());

                    return new ResponseEntity<>(regioneRepository.save(_regione), HttpStatus.OK);
                } else {
                    HttpHeaders headers = new HttpHeaders();
                    headers.add("Motivo-errore", "Regione e/o codice regione già presenti");
                    return new ResponseEntity<>(headers, HttpStatus.PRECONDITION_FAILED);
                }
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}


