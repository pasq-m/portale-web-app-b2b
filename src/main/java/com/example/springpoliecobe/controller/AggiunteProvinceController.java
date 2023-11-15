package com.example.springpoliecobe.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.springpoliecobe.model.AggiuntaProvincia;
import com.example.springpoliecobe.model.AggiuntaRegione;
import com.example.springpoliecobe.model.Provincia;
import com.example.springpoliecobe.model.Regione;
import com.example.springpoliecobe.repository.AggiuntaProvinciaRepository;
import com.example.springpoliecobe.repository.ProvinciaRepository;
import com.example.springpoliecobe.repository.RegioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.apache.commons.lang3.text.WordUtils.capitalizeFully;


@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:3000"}, maxAge = 3600)
@RestController
@RequestMapping("/api")

public class AggiunteProvinceController {

    @Autowired
    AggiuntaProvinciaRepository aggiuntaProvinciaRepository;

    @Autowired
    RegioneRepository regioneRepository;

    @Autowired
    ProvinciaRepository provinciaRepository;

    @GetMapping("/aggiunte-province")
    public ResponseEntity<List<AggiuntaProvincia>> getAllAggiunteProvince(@RequestParam(required = false) String codice) {
        try {
            List<AggiuntaProvincia> aggiuntaProvincia = new ArrayList<AggiuntaProvincia>();

            if (codice == null)
                aggiuntaProvinciaRepository.findAll().forEach(aggiuntaProvincia::add);
            else
                aggiuntaProvinciaRepository.findByCodiceContaining(codice).forEach(aggiuntaProvincia::add);

            if (aggiuntaProvincia.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(aggiuntaProvincia, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/aggiunte-province/{id}")
    public ResponseEntity<AggiuntaProvincia> getAggiuntaProvinciaById(@PathVariable("id") int id) {
        Optional<AggiuntaProvincia> aggiunteProvinceData = aggiuntaProvinciaRepository.findById(id);

        if (aggiunteProvinceData.isPresent()) {
            return new ResponseEntity<>(aggiunteProvinceData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/add-aggiunta-provincia/{idRegione}")
    public ResponseEntity<AggiuntaProvincia> createProvincia(@PathVariable("idRegione") int idRegione, @RequestBody Provincia provincia) {

        Optional<Regione> regioneData = regioneRepository.findById(idRegione);
        Regione regione = regioneData.get();
        provincia.setRegione(regione);

        if (aggiuntaProvinciaRepository.existsByCodiceIgnoreCase(provincia.getCodice())) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Motivo-errore", "Provincia già presente");
            return new ResponseEntity<>(headers, HttpStatus.CONFLICT);    //Dovrà dare 409 anche su progetto FE ufficiale
        }

        if (aggiuntaProvinciaRepository.findAll().isEmpty()) {
            //Invio un codice di STATUS apposito al FE che fa un check sull'error del response e di
            //conseguenza attiva il processo di notifica all'admin
            AggiuntaProvincia _provincia = aggiuntaProvinciaRepository
                    .save(new AggiuntaProvincia(provincia.getCodice().toUpperCase(), provincia.getRegione()));

            HttpHeaders headers = new HttpHeaders();
            headers.add("Segnale-per-admin", "Via libera notifica richiesta aggiunta PROVINCIA per admin");

            return new ResponseEntity<>(_provincia, headers, HttpStatus.CREATED);    //Qui serve custom headers per check su FE
        }

        AggiuntaProvincia _provincia = aggiuntaProvinciaRepository.save(new AggiuntaProvincia(provincia.getCodice().toUpperCase(), provincia.getRegione()));
        return new ResponseEntity<>(_provincia, HttpStatus.CREATED);
    }

    @PutMapping("/update-aggiunta-provincia/{id}/{idRegione}")
    public ResponseEntity<AggiuntaProvincia> updateProvincia(@PathVariable("id") int id, @PathVariable("idRegione") int idRegione, @RequestBody Provincia provincia) {
        Optional<AggiuntaProvincia> aggiunteProvinceData = aggiuntaProvinciaRepository.findById(id);

        //Check se è presente l'id della provincia che si vuole mod. da FE
        if (aggiunteProvinceData.isPresent()) {

            //ALL'INTERNO DI UNO SPECIFICO OGGETTO È POSSIBILE RIUTILIZZARE GLI STESSI DATI (CODICE IN QUESTO CASO)
            //Se il codice dell'oggetto che vogliamo modificare è uguale al codice inserito dall'utente in fase
            //di modifica allora passiamo allo step successivo senza dare errore (è possibile riscrivere lo stesso
            //codice se vogliamo ad esempio modificare solo la regione all'interno del solito oggetto).
            //Utilizzare equalsIgnoreCase() invece del classico equals
            if (provincia.getCodice().equalsIgnoreCase(aggiunteProvinceData.get().getCodice())) {

                //Andiamo a recuperare la regione passata dal FE
                Optional<Regione> regioneData = regioneRepository.findById(idRegione);
                Regione regione = regioneData.get();
                //provincia.setRegione(regione);

                AggiuntaProvincia _aggiuntaProvincia = aggiunteProvinceData.get();   //Qua utilizziamo la provincia che abbiamo trovato sopra perché presente
                _aggiuntaProvincia.setCodice(provincia.getCodice().toUpperCase()); //Settiamo la regione (passata dal FE) nella provincia che abbiamo passato dal FE
                _aggiuntaProvincia.setRegione(regione);

                return new ResponseEntity<>(aggiuntaProvinciaRepository.save(_aggiuntaProvincia), HttpStatus.OK);

            } else {

                //Check se è già presente il codice della provincia che si vuole aggiungere in tutta la tabella
                //dopo che abbiamo controllato sopra che non sia lo stesso dell'oggetto da modificare.
                //Se non è presente andiamo avanti e lo aggiungiamo tramite modifica di quello attuale.
                if (!aggiuntaProvinciaRepository.existsByCodiceContaining(provincia.getCodice())) {

                    //Andiamo a recuperare la regione passata dal FE
                    Optional<Regione> regioneData = regioneRepository.findById(idRegione);
                    Regione regione = regioneData.get();
                    //provincia.setRegione(regione);

                    AggiuntaProvincia _aggiuntaProvincia = aggiunteProvinceData.get();   //Qua utilizziamo la provincia che abbiamo trovato sopra perché presente
                    _aggiuntaProvincia.setCodice(provincia.getCodice().toUpperCase()); //Settiamo la regione (passata dal FE) nella provincia che abbiamo passato dal FE
                    _aggiuntaProvincia.setRegione(regione);

                    return new ResponseEntity<>(aggiuntaProvinciaRepository.save(_aggiuntaProvincia), HttpStatus.OK);
                } else {
                    HttpHeaders headers = new HttpHeaders();
                    headers.add("Motivo-errore", "Provincia già presente");
                    return new ResponseEntity<>(headers, HttpStatus.PRECONDITION_FAILED);
                }
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
