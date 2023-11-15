package com.example.springpoliecobe.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.springpoliecobe.model.AggiuntaLocalita;
import com.example.springpoliecobe.model.AggiuntaProvincia;
import com.example.springpoliecobe.model.Localita;
import com.example.springpoliecobe.model.Provincia;
import com.example.springpoliecobe.repository.AggiuntaLocalitaRepository;
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

public class AggiunteLocalitaController {

    @Autowired
    AggiuntaLocalitaRepository aggiuntaLocalitaRepository;

    @Autowired
    ProvinciaRepository provinciaRepository;

    @Autowired
    LocalitaRepository localitaRepository;

    @GetMapping("/aggiunta-localita")
    public ResponseEntity<List<AggiuntaLocalita>> getAllAggiuntaLocalita(@RequestParam(required = false) String descrizione) {
        try {
            List<AggiuntaLocalita> aggiuntaLocalita = new ArrayList<AggiuntaLocalita>();

            if (descrizione == null)
                aggiuntaLocalitaRepository.findAll().forEach(aggiuntaLocalita::add);
            else
                aggiuntaLocalitaRepository.findByDescrizioneContaining(descrizione).forEach(aggiuntaLocalita::add);

            if (aggiuntaLocalita.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(aggiuntaLocalita, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/aggiunta-localita/{id}")
    public ResponseEntity<AggiuntaLocalita> getAggiuntaLocalitaById(@PathVariable("id") int id) {
        Optional<AggiuntaLocalita> aggiuntaLocalitaData = aggiuntaLocalitaRepository.findById(id);

        if (aggiuntaLocalitaData.isPresent()) {
            return new ResponseEntity<>(aggiuntaLocalitaData.get(), HttpStatus.OK);
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

    @PostMapping("/add-aggiunta-localita/{idProvincia}")
    public ResponseEntity<AggiuntaLocalita> createAggiuntaLocalita(@PathVariable(value = "idProvincia") int idProvincia, @RequestBody Localita localita) {

        Optional<Provincia> provinciaData = provinciaRepository.findById(idProvincia);
        Provincia provincia = provinciaData.get();
        localita.setProvincia(provincia);

        if (aggiuntaLocalitaRepository.existsByDescrizioneIgnoreCase(localita.getDescrizione())) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Motivo-errore", "Località già presente");
            return new ResponseEntity<>(headers, HttpStatus.CONFLICT);    //Dovrà dare 409 anche su progetto FE ufficiale
        }

        if (aggiuntaLocalitaRepository.findAll().isEmpty()) {
            //Invio un codice di STATUS apposito al FE che fa un check sull'error del response e di
            //conseguenza attiva il processo di notifica all'admin
            AggiuntaLocalita _aggiuntaLocalita = aggiuntaLocalitaRepository
                    .save(new AggiuntaLocalita(capitalizeFully(localita.getDescrizione()), localita.getCap().toUpperCase(), localita.getProvincia()));

            HttpHeaders headers = new HttpHeaders();
            headers.add("Segnale-per-admin", "Via libera notifica richiesta aggiunta LOCALITA per admin");

            return new ResponseEntity<>(_aggiuntaLocalita, headers, HttpStatus.CREATED);    //Qui serve custom headers per check su FE
        }

        AggiuntaLocalita _aggiuntaLocalita = aggiuntaLocalitaRepository.save(new AggiuntaLocalita(capitalizeFully(localita.getDescrizione()), localita.getCap().toUpperCase(), localita.getProvincia()));
        return new ResponseEntity<>(_aggiuntaLocalita, HttpStatus.CREATED);

    }


    @PutMapping("/update-aggiunta-localita/{id}/{idProvincia}")
    public ResponseEntity<AggiuntaLocalita> updateAggiuntaLocalita(@PathVariable("id") int id, @PathVariable("idProvincia") int idProvincia, @RequestBody Localita localita) {
        Optional<AggiuntaLocalita> aggiuntaLocalitaData = aggiuntaLocalitaRepository.findById(id);

        if (aggiuntaLocalitaData.isPresent()) {
            //Utilizzare equalsIgnoreCase() invece del classico equals
            if (localita.getDescrizione().equalsIgnoreCase(aggiuntaLocalitaData.get().getDescrizione())) {

                Optional<Provincia> provinciaData = provinciaRepository.findById(idProvincia);
                Provincia provincia = provinciaData.get();

                AggiuntaLocalita _aggiuntaLocalita = aggiuntaLocalitaData.get();
                _aggiuntaLocalita.setDescrizione(capitalizeFully(localita.getDescrizione()));
                _aggiuntaLocalita.setCap(localita.getCap());
                _aggiuntaLocalita.setProvincia(provincia);

                return new ResponseEntity<>(aggiuntaLocalitaRepository.save(_aggiuntaLocalita), HttpStatus.OK);

            } else {
                //Questo metodo JPA è di default CASE INSENSITIVE, quindi a prescindere la stringa corrisponde.
                if (!aggiuntaLocalitaRepository.existsByDescrizioneContaining(localita.getDescrizione())) {

                    Optional<Provincia> provinciaData = provinciaRepository.findById(idProvincia);
                    Provincia provincia = provinciaData.get();

                    AggiuntaLocalita _aggiuntaLocalita = aggiuntaLocalitaData.get();
                    _aggiuntaLocalita.setDescrizione(capitalizeFully(localita.getDescrizione()));
                    _aggiuntaLocalita.setCap(localita.getCap());
                    _aggiuntaLocalita.setProvincia(provincia);

                    return new ResponseEntity<>(aggiuntaLocalitaRepository.save(_aggiuntaLocalita), HttpStatus.OK);
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

