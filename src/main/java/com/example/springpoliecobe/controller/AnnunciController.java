package com.example.springpoliecobe.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.example.springpoliecobe.model.*;
import com.example.springpoliecobe.repository.*;
import com.example.springpoliecobe.util.GestoreNotifiche;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:3000"}, maxAge = 3600,
                        exposedHeaders = {"Motivo-errore", "Notifica-check", "Segnale-per-admin"})
@RestController
@RequestMapping("/api")

public class AnnunciController {

    @Autowired
    AnnuncioRepository annuncioRepository;

    @Autowired
    AziendaRepository aziendaRepository;

    @Autowired
    LocalitaRepository localitaRepository;

    @Autowired
    MaterialeRepository materialeRepository;

    @Autowired
    UnitaDiMisuraRepository unitaDiMisuraRepository;

    //@Autowired
    //AnnuncioService annuncioService;    //Per prova codice foto

    @Autowired
    InteresseUtenteAnnuncioRepository interesseUtenteAnnuncioRepository;

    @Autowired
    GestoreNotifiche gestoreNotifiche;

    @GetMapping("/annunci")
    //@GetMapping(value = "/annunci", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<Annuncio>> getAllAnnunci(@RequestParam(required = false) String titolo) {
        try {
            List<Annuncio> annunci = new ArrayList<Annuncio>();

            if (titolo == null)
                annuncioRepository.findAll().forEach(annunci::add);
            else
                annuncioRepository.findByTitoloContaining(titolo).forEach(annunci::add);

            if (annunci.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            for (Annuncio annuncio : annunci) {

                if (annuncio.getFoto() != null) {
                    //byte[] decoded = Base64.getMimeDecoder().decode(annuncio.getFoto());

                    //Unico modo per inviare la stringa base64 corretta della foto è passarla come stringa normale
                    //così axios nel FE la può importare normalmente senza conflitti etc.

                    String fotoStringata = new String(annuncio.getFoto());
                    //Togliamo le prime e le ultime virgolette presenti nel db
                    String fotoStringataClear = fotoStringata.substring(1, fotoStringata.length() - 1);
                    annuncio.setFotoStringata(fotoStringataClear);

                }
            }

            //Se esiste la foto per un annuncio la risetta decompressa e pronta per essere mostrata
            /*annunci.forEach(annuncio -> {
                if (annuncio.getFoto() != null) {
                    annuncio.setFoto(ImageUtil.decompressImage(annuncio.getFoto()));
                }
            });*/

            return new ResponseEntity<>(annunci, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Per annuncio specifico
    @GetMapping("/annunci/{id}")
    public ResponseEntity<Annuncio> getAnnuncioById(@PathVariable("id") Long id) {
        Optional<Annuncio> annunciData = annuncioRepository.findById(id);

        if (annunciData.isPresent()) {
            return new ResponseEntity<>(annunciData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //DA CAPIRE SE DA IMPLEMENTARE O PRENDE TUTTO DI GET NORMALE E FILTRA IN FE SIMONE
    //UPDATE: per la performance sarebbe bene in futuro implementare i metodi mirati direttamente nel BE, così da
    //evitare di far caricare ogni volta tutti gli annunci in blocco a prescindere e portarli al FE.

    //Get annuncio in base ad una regione specifica dell'annuncio (per filtro home)


    //Get annuncio in base ad una provincia specifica dell'annuncio (per filtro home)


    //Get annuncio in base ad una località specifica dell'annuncio (per filtro home)


    //POST
    @RequestMapping(value = "/add-annuncio/{idLocalita}/{idMateriale}/{idUnitaDiMisura}/{idCurrentAzienda}", method = RequestMethod.POST,
                                consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @ResponseBody
    public ResponseEntity<Annuncio> createAnnuncio( @RequestPart("data") Annuncio annuncio,
                                                    //@RequestPart("foto") List<MultipartFile> foto,
                                                    @RequestPart("foto") MultipartFile foto,
                                                    //@RequestParam("idLocalita") int idLocalita,
                                                    @PathVariable("idLocalita") int idLocalita,
                                                    //@RequestParam("idMateriale") int idMateriale,
                                                    @PathVariable("idMateriale") int idMateriale,
                                                    //@RequestParam("idUnitaDiMisura") int idUnitaDiMisura,
                                                    @PathVariable("idUnitaDiMisura") int idUnitaDiMisura,
                                                    //@RequestParam("idCurrentAzienda") int idCurrentAzienda)//,
                                                    @PathVariable("idCurrentAzienda") int idCurrentAzienda)//,
                                                    //@RequestBody Annuncio annuncio)
                                                    {

        try {

            Optional<Localita> localitaData = localitaRepository.findById(idLocalita);
            Optional<Materiale> materialiData = materialeRepository.findById(idMateriale);
            Optional<UnitaDiMisura> unitaDiMisuraData = unitaDiMisuraRepository.findById(idUnitaDiMisura);
            Optional<Azienda> aziendeData = aziendaRepository.findById(idCurrentAzienda);

            annuncio.setFoto(foto.getBytes());  //Transform the file into byte array

            Localita localita = localitaData.get();
            annuncio.setLocalita(localita);

            Materiale materiale = materialiData.get();
            annuncio.setMateriale(materiale);

            UnitaDiMisura unitaDiMisura = unitaDiMisuraData.get();
            annuncio.setUnitaDiMisura(unitaDiMisura);

            Azienda azienda = aziendeData.get();
            annuncio.setAzienda(azienda);

            //Comprimiamo l'immagine prima di salvarla nel DB - DA RIAGGIUNGERE fotoCompressa SOTTO IN SAVE
            //E MODIFICARE POI CONSTRUCTOR DI NUOVO
            //byte[] fotoCompressa = ImageUtil.compressImage(annuncio.getFoto());

            Annuncio _annuncio = annuncioRepository
                    .save(new Annuncio(annuncio.getTitolo(), annuncio.getDescrizione(), annuncio.getQuantita(),
                            annuncio.getFoto(), annuncio.getDataDiScadenza(), annuncio.getLocalita(),
                            annuncio.getMateriale(), annuncio.getUnitaDiMisura(), annuncio.getAzienda(), false));
            return new ResponseEntity<>(_annuncio, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Il controller richiamato quando un utente registrato preme su "contatta" dentro ad un annuncio.
    //Serve a collegare utente interessato con annuncio (e di conseguenza anche con utente proprietario annuncio).
    @PostMapping("/annunci/contatta-premuto/{idAnnuncio}/{idAzienda}")
    public ResponseEntity<InteresseUtenteAnnuncio> contattaPremuto(@PathVariable("idAnnuncio") Long idAnnuncio,
                                                    @PathVariable("idAzienda") int idAzienda,
                                                    @RequestBody Annuncio annuncio) {
        //Prima cosa controllare che chi preme su contatta non sia lo stesso utente che ha creato l'annuncio
        //(a monte dovrebbe esserci un check per fare in modo che il tasto contatta non dovrebbe essere visibile
        //allo stesso autore dell'annuncio, ma per sicurezza lasciare anche questo check).
        Optional<Annuncio> _annuncio = annuncioRepository.findById(idAnnuncio);
        Optional<Azienda> _azienda = aziendaRepository.findById(idAzienda);

        if (_azienda.get().getUtente().getId() == _annuncio.get().getAziendaId()) {
            return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
        }

        InteresseUtenteAnnuncio interesseUtenteAnnuncio = new InteresseUtenteAnnuncio(_annuncio.get(), false,
                                                                                        _azienda.get());

        return new ResponseEntity<>(interesseUtenteAnnuncioRepository.save(interesseUtenteAnnuncio), HttpStatus.OK);

    }

    //PUT per CRON JOB da FE: il cron collegandosi a questo endpoint fa partire tutto il check per vedere se ci sono
    //annunci da chiudere (perché scaduti) ed in caso mandare di ritorno al FE i dati degli individui a cui
    //inviare le eventuali notifiche se erano presenti degli interessi
    //(le notifiche verranno mandate al proprietario dell'annuncio e all'utente interessato).
    @PutMapping("/annunci/check-chiusura-annuncio-ed-invio-notifiche")
    public ResponseEntity<List<InteresseUtenteAnnuncio>> checkChiusuraAnnuncioEdInvioNotifiche() {
        //In pratica il codice del metodo "azioneDaCompiere" situato nella classe "BackgroundCheckAnnuncioChiuso".
        //Va messo qua perché il check andrà fatto lato FE, quindi quella classe è inutile adesso.
        List<Annuncio> _annunciAttivi  = annuncioRepository.findByChiusoIsFalse();

        List<InteresseUtenteAnnuncio> listaDelleListe = new ArrayList<>();

        HttpHeaders headers = new HttpHeaders();

        if (_annunciAttivi.isEmpty()) {
            System.out.println("Nessun annuncio ATTIVO trovato");

            headers.add("Notifica-check", "Nessun annuncio ATTIVO trovato");
            return new ResponseEntity<>(headers, HttpStatus.OK);

        } else {
            _annunciAttivi.forEach(annuncio -> {
                //Controlliamo se tra gli annunci ancora attivi sono presenti annunci che sono scaduti
                if (annuncio.getDataDiScadenza().isBefore(LocalDate.now())) {
                    //Per ogni annuncio trovato scaduto andiamo a flaggarlo col "chiuso" = true
                    annuncio.setChiuso(true);
                    annuncioRepository.save(annuncio);

                    //Qua richiamiamo un metodo per inviare, se ci sono le condizioni, le notifiche agli utenti
                    //coinvolti.
                    listaDelleListe.addAll(gestoreNotifiche.inviaNotificheUtentiFeedback(annuncio.getId()));


                    System.out.println("Annuncio scaduto: " + annuncio.getTitolo());
                } else {
                    System.out.println("Annuncio non scaduto: " + annuncio.getTitolo());
                }
            });
        }
            if (!listaDelleListe.isEmpty()) {
                headers.add("Notifica-check", "Ci sono notifiche da inviare");

                //Prob. da mettere come errore (poteva benissimo essere messo come HttpStatus.OK, ad es.)
                //per beccarlo tramite "error.response.headers" (forse si può acchiappare anche tramite response normale
                //ma non ci giurerei)
                return new ResponseEntity<>(listaDelleListe, headers, HttpStatus.CONFLICT);
            }

            headers.add("Notifica-check", "*** NON *** ci sono notifiche da inviare - " +
                                                                            "Annunci attivi NON scaduti");
            return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @PutMapping("/annunci/{id}")
    public ResponseEntity<Annuncio> updateAnnuncio(@PathVariable("id") Long id, @RequestBody Annuncio annuncio) {
        Optional<Annuncio> annunciData = annuncioRepository.findById(id);

        if (annunciData.isPresent()) {
            Annuncio _annuncio = annunciData.get();
            _annuncio.setTitolo(annuncio.getTitolo());
            _annuncio.setDescrizione(annuncio.getDescrizione());
            _annuncio.setQuantita(annuncio.getQuantita());
            //_annuncio.setPublished(annuncio.isPublished());
            return new ResponseEntity<>(annuncioRepository.save(_annuncio), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Controller legato al pulsante "chiudi annuncio"
    @PutMapping("/annunci/chiudi-annuncio/{id}")
    public ResponseEntity<Annuncio> chiudiAnnuncio(@PathVariable("id") Long id, @RequestBody Annuncio annuncio) {
        Optional<Annuncio> annunciData = annuncioRepository.findById(id);

        if (annunciData.isPresent()) {
            Annuncio _annuncio = annunciData.get();
            _annuncio.setChiuso(true);

            return new ResponseEntity<>(annuncioRepository.save(_annuncio), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }



    // *** Commentati per sicurezza perché non utilizzati per ora ***

    /*
    @DeleteMapping("/annunci/{id}")
    public ResponseEntity<HttpStatus> deleteAnnuncio(@PathVariable("id") int id) {
        try {
            annuncioRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/annunci")
    public ResponseEntity<HttpStatus> deleteAllAnnunci() {
        try {
            annuncioRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    */
}
