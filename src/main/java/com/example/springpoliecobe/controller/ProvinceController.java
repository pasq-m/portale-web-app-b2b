package com.example.springpoliecobe.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.springpoliecobe.model.Provincia;
import com.example.springpoliecobe.model.Regione;
import com.example.springpoliecobe.repository.ProvinciaRepository;
import com.example.springpoliecobe.repository.RegioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:3000"}, maxAge = 3600)
@RestController
@RequestMapping("/api")

public class ProvinceController {

    @Autowired
    ProvinciaRepository provinciaRepository;

    @Autowired
    RegioneRepository regioneRepository;

    @GetMapping("/province")
    public ResponseEntity<List<Provincia>> getAllProvince(@RequestParam(required = false) String codice) {
        try {
            List<Provincia> provincia = new ArrayList<Provincia>();

            if (codice == null)
                provinciaRepository.findAll().forEach(provincia::add);
            else
                provinciaRepository.findByCodiceContaining(codice).forEach(provincia::add);

            if (provincia.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(provincia, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/province/{id}")
    public ResponseEntity<Provincia> getProvinciaById(@PathVariable("id") int id) {
        Optional<Provincia> provinceData = provinciaRepository.findById(id);

        if (provinceData.isPresent()) {
            return new ResponseEntity<>(provinceData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/add-provincia/{idRegione}")
    public ResponseEntity<Provincia> createProvincia(@PathVariable("idRegione") int idRegione, @RequestBody Provincia provincia) {

        if (provinciaRepository.existsByCodiceIgnoreCase(provincia.getCodice())) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Motivo-errore", "Provincia già presente");
            return new ResponseEntity<>(headers, HttpStatus.CONFLICT);    //Dovrà dare 409 anche su progetto FE ufficiale
        }

        Optional<Regione> regioneData = regioneRepository.findById(idRegione);
        Regione regione = regioneData.get();

        provincia.setRegione(regione);
        Provincia _provincia = provinciaRepository.save(new Provincia(provincia.getCodice().toUpperCase(), provincia.getRegione()));
        return new ResponseEntity<>(_provincia, HttpStatus.CREATED);
    }

    @PutMapping("/update-provincia/{id}/{idRegione}")
    public ResponseEntity<Provincia> updateProvincia(@PathVariable("id") int id, @PathVariable("idRegione") int idRegione, @RequestBody Provincia provincia) {
        Optional<Provincia> provinceData = provinciaRepository.findById(id);

        //Check se è presente l'id della provincia che si vuole mod. da FE
        if (provinceData.isPresent()) {

            //ALL'INTERNO DI UNO SPECIFICO OGGETTO È POSSIBILE RIUTILIZZARE GLI STESSI DATI (CODICE IN QUESTO CASO)
            //Se il codice dell'oggetto che vogliamo modificare è uguale al codice inserito dall'utente in fase
            //di modifica allora passiamo allo step successivo senza dare errore (è possibile riscrivere lo stesso
            //codice se vogliamo ad esempio modificare solo la regione all'interno del solito oggetto).
            //Utilizzare equalsIgnoreCase() invece del classico equals
            if (provincia.getCodice().equalsIgnoreCase(provinceData.get().getCodice())) {

                //Andiamo a recuperare la regione passata dal FE
                Optional<Regione> regioneData = regioneRepository.findById(idRegione);
                Regione regione = regioneData.get();
                //provincia.setRegione(regione);

                Provincia _provincia = provinceData.get();   //Qua utilizziamo la provincia che abbiamo trovato sopra perché presente
                _provincia.setCodice(provincia.getCodice().toUpperCase()); //Settiamo la regione (passata dal FE) nella provincia che abbiamo passato dal FE
                _provincia.setRegione(regione);

                return new ResponseEntity<>(provinciaRepository.save(_provincia), HttpStatus.OK);

            } else {

                //Check se è già presente il codice della provincia che si vuole aggiungere in tutta la tabella
                //dopo che abbiamo controllato sopra che non sia lo stesso dell'oggetto da modificare.
                //Se non è presente andiamo avanti e lo aggiungiamo tramite modifica di quello attuale.
                if (!provinciaRepository.existsByCodiceContaining(provincia.getCodice())) {

                    //Andiamo a recuperare la regione passata dal FE
                    Optional<Regione> regioneData = regioneRepository.findById(idRegione);
                    Regione regione = regioneData.get();
                    //provincia.setRegione(regione);

                    Provincia _provincia = provinceData.get();   //Qua utilizziamo la provincia che abbiamo trovato sopra perché presente
                    _provincia.setCodice(provincia.getCodice().toUpperCase()); //Settiamo la regione (passata dal FE) nella provincia che abbiamo passato dal FE
                    _provincia.setRegione(regione);

                    return new ResponseEntity<>(provinciaRepository.save(_provincia), HttpStatus.OK);
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