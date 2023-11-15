package com.example.springpoliecobe.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.springpoliecobe.model.AggiuntaRegione;
import com.example.springpoliecobe.model.Provincia;
import com.example.springpoliecobe.model.Regione;
import com.example.springpoliecobe.repository.AggiuntaRegioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.apache.commons.lang3.text.WordUtils.capitalizeFully;

//La sintassi dentro le graffe di exposedHeaders DOVREBBE essere corretta ma in caso di problemi controllare
@CrossOrigin(origins = "*", maxAge = 3600, exposedHeaders = {"Motivo-errore", "Segnale-per-admin"})
@RestController
@RequestMapping("/api")

public class AggiunteRegioniController {

    @Autowired
    AggiuntaRegioneRepository aggiuntaRegioneRepository;

    @GetMapping("/aggiunte-regioni")
    public ResponseEntity<List<AggiuntaRegione>> getAllAggiunteRegioni(@RequestParam(required = false) String descrizione) {
        try {
            List<AggiuntaRegione> aggiunteRegioni = new ArrayList<AggiuntaRegione>();

            if (descrizione == null)
                aggiuntaRegioneRepository.findAll().forEach(aggiunteRegioni::add);

            else
                aggiuntaRegioneRepository.findByDescrizioneContaining(descrizione).forEach(aggiunteRegioni::add);

            if (aggiunteRegioni.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(aggiunteRegioni, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/aggiunte-regioni/{id}")
    public ResponseEntity<AggiuntaRegione> getAggiuntaRegioneById(@PathVariable("id") int id) {
        Optional<AggiuntaRegione> regioniData = aggiuntaRegioneRepository.findById(id);

        if (regioniData.isPresent()) {
            return new ResponseEntity<>(regioniData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //GET per ottenere le province da una regione selezionata dal FE
    /*@GetMapping("/regioni/province/{id}")
    public ResponseEntity<List<Provincia>> getProvinceByRegioneId(@PathVariable("id") int id) {
        Optional<AggiuntaRegione> regioneData = aggiuntaRegioneRepository.findById(id);

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
    }*/

    @PostMapping("/add-aggiunta-regione")
    public ResponseEntity<AggiuntaRegione> createAggiuntaRegione(@RequestBody AggiuntaRegione regione) {

        if (aggiuntaRegioneRepository.existsByDescrizioneIgnoreCase(regione.getDescrizione()) ||
                aggiuntaRegioneRepository.existsByCodiceIgnoreCase(regione.getCodice())) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Motivo-errore", "Regione e/o codice regione già presenti");
            return new ResponseEntity<>(headers, HttpStatus.CONFLICT);    //Dovrà dare 409 anche su progetto FE ufficiale
        }

        if (aggiuntaRegioneRepository.findAll().isEmpty()) {
            //Invio un codice di STATUS apposito al FE che fa un check sull'error del response e di
            //conseguenza attiva il processo di notifica all'admin
            AggiuntaRegione _regione = aggiuntaRegioneRepository
                    .save(new AggiuntaRegione(capitalizeFully(regione.getDescrizione()), regione.getCodice().toUpperCase()));

            HttpHeaders headers = new HttpHeaders();
            headers.add("Segnale-per-admin", "Via libera notifica richiesta aggiunta REGIONE per admin");

            return new ResponseEntity<>(_regione, headers, HttpStatus.CREATED);    //Qui serve custom headers per check su FE
        }

        AggiuntaRegione _regione = aggiuntaRegioneRepository
                    .save(new AggiuntaRegione(capitalizeFully(regione.getDescrizione()), regione.getCodice().toUpperCase()));

        return new ResponseEntity<>(_regione, HttpStatus.CREATED);
    }

    @PutMapping("/update-aggiunta-regione/{id}")
    public ResponseEntity<AggiuntaRegione> updateAggiuntaRegione(@PathVariable("id") int id, @RequestBody AggiuntaRegione regione) {
        Optional<AggiuntaRegione> RegioniData = aggiuntaRegioneRepository.findById(id);

        if (RegioniData.isPresent()) {
            //Utilizzare equalsIgnoreCase() invece del classico equals
            if (regione.getDescrizione().equalsIgnoreCase(RegioniData.get().getDescrizione())) {

                AggiuntaRegione _regione = RegioniData.get();
                _regione.setDescrizione(capitalizeFully(regione.getDescrizione()));

                //Check se il codice era quello già presente nello stesso campo
                if (_regione.getCodice().equalsIgnoreCase(regione.getCodice())) {

                    _regione.setCodice(regione.getCodice().toUpperCase());
                    return new ResponseEntity<>(aggiuntaRegioneRepository.save(_regione), HttpStatus.OK);

                    //Se siamo su questo blocco significa che stiamo aggiungendo un altro codice; controlliamo che già
                    //non esista nella tabella
                } else if (!aggiuntaRegioneRepository.existsByCodiceContaining(regione.getCodice())) {

                    _regione.setCodice(regione.getCodice().toUpperCase());
                    return new ResponseEntity<>(aggiuntaRegioneRepository.save(_regione), HttpStatus.OK);

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
                if (!aggiuntaRegioneRepository.existsByDescrizioneContaining(regione.getDescrizione()) &&
                        !aggiuntaRegioneRepository.existsByCodiceContaining(regione.getCodice())) {

                    AggiuntaRegione _regione = RegioniData.get();
                    _regione.setDescrizione(capitalizeFully(regione.getDescrizione()));
                    _regione.setCodice(regione.getCodice().toUpperCase());

                    return new ResponseEntity<>(aggiuntaRegioneRepository.save(_regione), HttpStatus.OK);
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

    //Qua ci andrà il DELETE
}



