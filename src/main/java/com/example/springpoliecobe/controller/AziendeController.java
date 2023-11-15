package com.example.springpoliecobe.controller;

import com.example.springpoliecobe.model.*;
import com.example.springpoliecobe.repository.*;
//import com.example.springpoliecobe.wrapper.WrapperPerExcel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Stream;
import static org.apache.commons.lang3.text.WordUtils.capitalizeFully;

//@CrossOrigin(origins = "http://localhost:3000")     //Fondamentale per far girare react da 3000 a 8080 (insieme a proxy

//Fondamentale metterlo come "*" per farlo funzionare tramite adattamento del template utilizzato Security React-Spring
@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:3000"}, maxAge = 3600)
@Controller                                         //configurato su package.json)
//@RequestMapping("/views")
@RequestMapping("/api")
public class AziendeController {

    @Autowired
    AziendaRepository aziendaRepository;

    @Autowired
    LocalitaRepository localitaRepository;

    @Autowired
    CategoriaRepository categoriaRepository;

    @Autowired
    AssociazioneRepository associazioneRepository;

    @Autowired
    FormaGiuridicaRepository formaGiuridicaRepository;

    @Autowired
    UtenteRepository utenteRepository;

    @Autowired
    RuoloRepository ruoloRepository;

    @Autowired
    RegioneRepository regioneRepository;

    @Autowired
    ProvinciaRepository provinciaRepository;

    @Autowired
    PasswordEncoder encoder;


    //*** FUNZIONANTE - VERSIONE PER PROVE LOCALI THYMELEAF ***
    @GetMapping("/aziende-list")
    public String getAllAziende(Model model) {
        List<Azienda> aziendeList = aziendaRepository.findAll();
        model.addAttribute("aziendeList", aziendeList);
        return "prova";
    }

    @GetMapping("/aggiungi-azienda")
    public String addAziendaPage(Model model) {
        Azienda aziendaNuova = new Azienda();
        model.addAttribute("aziende", aziendaNuova);
        return "aggiungi-azienda";
    }

    @PostMapping("/add-azienda-thymeleaf")
    public String addAzienda(Azienda azienda) {     //L'oggetto Azienda gli viene fornito automaticamente (injection?)
        aziendaRepository.save(azienda);            //da quello inserito nel form?
        return "redirect:/views/aggiungi-azienda";
    }

    // (Funzionante anche questo) Metodo che si comporta come quello sopra ma strutturato diversamente
    /*@PostMapping("/add-azienda")
    public String addAzienda(Model model, @ModelAttribute("azienda") Azienda azienda) {
        aziendaRepository.save(azienda);
        return "redirect:/views/aggiungi-azienda";
    }*/



    //*** DA QUA IN POI VERSIONE PER REACT ***

    @GetMapping("/aziende")
    public ResponseEntity<List<Azienda>> getAllAziende(@RequestParam(required = false) String ragioneSociale) {
        try {
            List<Azienda> aziende = new ArrayList<Azienda>();

            if (ragioneSociale == null)
                aziendaRepository.findAll().forEach(aziende::add);      //Se non esiste l'oggetto, fa una query da db e crea un nuovo
            else                                                        //oggetto della lista con i suoi nuovi dati.
                aziendaRepository.findByRagioneSocialeContaining(ragioneSociale).forEach(aziende::add); //Se esiste già un oggetto, in base
                                                                                                        //alla ragione sociale va a prenderne
            if (aziende.isEmpty()) {                                                                    //tutti gli altri dati e li aggiunge
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);                                     //alla lista.
            }

            return new ResponseEntity<>(aziende, HttpStatus.OK);        //Qua restituisce la lista con un codice HTTP 200.
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/azienda-loggata")
    public ResponseEntity<Azienda> getAziendaLoggata(@RequestBody Azienda azienda) {

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("/azienda/{id}")
    public ResponseEntity<Azienda> getAziendaById(@PathVariable("id") int id) {
        Optional<Azienda> aziendeData = aziendaRepository.findById(id);

        if (aziendeData.isPresent()) {
            return new ResponseEntity<>(aziendeData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /*@PostMapping("/add-azienda")
    public ResponseEntity<Azienda> createAziende(@RequestBody Azienda azienda) {
        try {
            Azienda _azienda = aziendaRepository
                    .save(new Azienda(azienda.getId(), azienda.getRagioneSociale(), azienda.getIndirizzo(),
                            azienda.getEmail(), azienda.getTelefono()));
            return new ResponseEntity<>(_azienda, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/


    //Versione completa POST per Azienda - DA PROVARE POI DA FE - MODIFICARE SERVIZI FE ANCHE

    // ***** IMPORTANTE: questo POST non dovrebbe essere utilizzato, utilizziamo quello presente in AuthController *****
    // ***** con endpoint "/aggiungi-azienda-singola/" etc. etc. *****

    @PostMapping("/add-azienda/{id}/{idLocalita}/{idCategoria}/{idAttivitaPrincipale}/{idAttivitaSecondaria}/{idAssociazione}/{idFormaGiuridica}/{idRuolo}")
    public ResponseEntity<Azienda> createAzienda(@PathVariable("id") int id,
                                                 @PathVariable("idLocalita") int idLocalita,
                                                 @PathVariable("idCategoria") int idCategoria,
                                                 @PathVariable("idAttivitaPrincipale") int idAttivitaPrincipale,
                                                 @PathVariable("idAttivitaSecondaria") int idAttivitaSecondaria,
                                                 @PathVariable("idAssociazione") int idAssociazione,
                                                 @PathVariable("idFormaGiuridica") int idFormaGiuridica,
                                                 //@PathVariable("idRuolo") int idRuolo,
                                                 @RequestBody Azienda azienda) {


        Optional<Localita> localitaData = localitaRepository.findById(idLocalita);
        Localita localita = localitaData.get();

        //Con variabile chiamata per beccarlo "categoriaId" quando inviato da Axios dal FE
        //Optional<Categoria> categoriaData = categoriaRepository.findById(azienda.getCategoriaId());

        Optional<Categoria> categoriaData = categoriaRepository.findById(idCategoria);
        Categoria categoria = categoriaData.get();

        Optional<Categoria> attivitaPrincipaleData = categoriaRepository.findById(idAttivitaPrincipale);
        Categoria attivitaPrincipale = attivitaPrincipaleData.get();

        Optional<Categoria> attivitaSecondariaData = categoriaRepository.findById(idAttivitaSecondaria);
        Categoria attivitaSecondaria = attivitaSecondariaData.get();

        Optional<Associazione> associazioneData = associazioneRepository.findById(idAssociazione);
        Associazione associazione = associazioneData.get();

        Optional<FormaGiuridica> formaGiuridicaData = formaGiuridicaRepository.findById(idFormaGiuridica);
        FormaGiuridica formaGiuridica = formaGiuridicaData.get();


        Optional<Azienda> aziendeData = aziendaRepository.findById(id);


        if (aziendeData.isPresent()) {
            Azienda _azienda = aziendeData.get();
            _azienda.setRagioneSociale(azienda.getRagioneSociale());
            _azienda.setIndirizzo(azienda.getIndirizzo());
            _azienda.setTelefono(azienda.getTelefono());
            _azienda.setTelefono2(azienda.getTelefono2());
            _azienda.setDescrizioneTelefoni(azienda.getDescrizioneTelefoni());
            _azienda.setFax(azienda.getFax());
            _azienda.setEmail(azienda.getEmail());
            _azienda.setPec(azienda.getPec());
            _azienda.setCodiceFiscale(azienda.getCodiceFiscale());
            _azienda.setpIva(azienda.getpIva());
            _azienda.setLegaleRappresentante(azienda.getLegaleRappresentante());
            _azienda.setFormaGiuridica(formaGiuridica);
            _azienda.setAssociazione(associazione);
            _azienda.setLocalita(localita);
            //_azienda.setLocalitaId(azienda.getLocalitaId());
            _azienda.setCategoria(categoria);
            //_azienda.setCategoriaId(azienda.getCategoriaId());
            _azienda.setAttivitaPrincipale(attivitaPrincipale);
            _azienda.setAttivitaSecondaria(attivitaSecondaria);


            return new ResponseEntity<>(aziendaRepository.save(_azienda), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //Qua mettiamo un CONTROLLER POST per analizzare il JSON contenente i dati dell'Excel spediti dal FE
    //Ho fatto senza classe wrapper ma utilizzando il tipo String su @RequestBody dal momento che prendevo tutti i dati
    //da un JSON (a sto punti prob. è anche inutile come annotazione sul parametro)

    @PostMapping("/add-azienda-from-excel")
    public ResponseEntity<Azienda> createAziendaFromExcel(@RequestBody String listaAziendeExcel) throws JsonProcessingException {

        //Tree model di Jackson

        //Create an ObjectMapper instance
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = listaAziendeExcel;
        //create tree from JSON
        JsonNode rootNode = mapper.readTree(jsonString);

        JsonNode blocchiNode = rootNode.path("json");       //Qua siamo al livello dell'array interno che contiene
                                                            //tutti gli oggetti row dell'Excel.

        Iterator<JsonNode> fields = blocchiNode.elements();

        //Va a fare loop sul blocco interno (array) contenente tutti i singoli oggetti row
        while (fields.hasNext()) {
           JsonNode field = fields.next();


           //Lista variabili CHE SI RESETTA AUTOMATICAMENTE dopo che abbiamo mappato tutti i dati di un singolo row
           //e li abbiamo utilizzati per far girare il codice dei check e dei salvataggi su DB.
           //PERCHE' RESET: si resetta da solo quando il loop di elements finisce e torna al loop superiore (questo);
           //automaticamente le variabili vengono di nuovo reinizializzate a null e quindi si resettano.
           String idMappato = null, ragioneSocialeMappato = null, codFormaGiuridicaMappato = null,
                   associazioneMappato = null, indirizzoMappato = null, capMappato = null, localitaMappato = null,
                   provinciaMappato = null, regioneMappato = null, codRegioneMappato = null, telefono1Mappato = null,
                   telefono2Mappato = null, faxMappato = null, partitaIvaMappato = null, codiceFiscaleMappato = null,
                   emailMappato = null, pecMappato = null, legaleRappresentanteMappato = null,
                   codCategoriaMappato = null, descrTelefoniMappato = null, catAttivitaPrincipaleMappato = null,
                   catAttivitaSecondariaMappato = null;


            //Elements rappresenta i vari oggetti presenti dentro ad un singolo row, come ad esempio "id: 4590",
            //divisi già in key/value
            //fieldNames() estrapola le chiavi/valori dal row?
            Iterator<String> elements = field.fieldNames(); //Field rappresenta il singolo row invece
            while (elements.hasNext()) {
                String fieldName = elements.next(); //fieldName è la coppia key/value del loop attuale
                System.out.println("Key: " + fieldName + "\tValue:" + field.get(fieldName).asText());

                //Prima mappiamo tutte le variabili una ad una

                if (fieldName.equalsIgnoreCase("Id")) {
                    //Rimuoviamo gli spazi all'inizio e alla fine della stringa se presenti
                    idMappato = field.get(fieldName).asText().stripLeading().stripTrailing();
                }

                if (fieldName.equalsIgnoreCase("RagioneSociale")) {
                    ragioneSocialeMappato = capitalizeFully(field.get(fieldName).asText().stripLeading().stripTrailing());
                }

                if (fieldName.equalsIgnoreCase("CodFormaGiuridica")) {
                    codFormaGiuridicaMappato = field.get(fieldName).asText().stripLeading().stripTrailing();
                }

                if (fieldName.equalsIgnoreCase("Associazione")) {
                    //Controlla che il valore sia diverso da null prima
                    if (field.get(fieldName).asText() != null) {

                        associazioneMappato = field.get(fieldName).asText().stripLeading().stripTrailing();
                    }
                }

                if (fieldName.equalsIgnoreCase("Indirizzo")) {
                    indirizzoMappato = capitalizeFully(field.get(fieldName).asText().stripLeading().stripTrailing());
                }

                if (fieldName.equalsIgnoreCase("Cap")) {
                    capMappato = field.get(fieldName).asText().stripLeading().stripTrailing();
                }

                if (fieldName.equalsIgnoreCase("Localita")) {
                    localitaMappato = field.get(fieldName).asText().stripLeading().stripTrailing();
                }

                if (fieldName.equalsIgnoreCase("Provincia")) {
                    provinciaMappato = field.get(fieldName).asText().stripLeading().stripTrailing();
                }

                if (fieldName.equalsIgnoreCase("Regione")) {
                    regioneMappato = field.get(fieldName).asText().stripLeading().stripTrailing();
                }

                if (fieldName.equalsIgnoreCase("CodRegione")) {
                    codRegioneMappato = field.get(fieldName).asText().stripLeading().stripTrailing();
                }

                if (fieldName.equalsIgnoreCase("Telefono1")) {
                    telefono1Mappato = field.get(fieldName).asText().stripLeading().stripTrailing();
                }

                if (fieldName.equalsIgnoreCase("Telefono2")) {
                    if (field.get(fieldName).asText() != null) {

                        telefono2Mappato = field.get(fieldName).asText().stripLeading().stripTrailing();
                    }
                }

                if (fieldName.equalsIgnoreCase("Fax")) {
                    if (field.get(fieldName).asText() != null) {

                        faxMappato = field.get(fieldName).asText().stripLeading().stripTrailing();
                    }
                }

                if (fieldName.equalsIgnoreCase("PartitaIva")) {
                    partitaIvaMappato = field.get(fieldName).asText().stripLeading().stripTrailing();
                }

                boolean andatoDentroCodiceFiscale = false;

                if (fieldName.equalsIgnoreCase("CodiceFiscale")) {
                    //Non serve a nulla l'if per il check del null sotto, perché a quanto pare se nel file manca
                    //il valore sotto la colonna nel "field" durante il loop non appare nemmeno il nome della colonna
                    //(la key) e quindi non inizia proprio la comparazione sul primo if "se trovi la key con questo nome"
                    //Ce li lascio qua e negli altri campi potenzialmente nullable perché non si sa mai.
                    if (field.get(fieldName).asText() != null) {

                        andatoDentroCodiceFiscale = true;
                        codiceFiscaleMappato = field.get(fieldName).asText().stripLeading().stripTrailing();
                    }
                }

                if (fieldName.equalsIgnoreCase("Email")) {
                    if (field.get(fieldName).asText() != null) {

                        //Split per email multiple sullo stesso campo - prendiamo la prima
                        if (field.get(fieldName).asText().contains(";")) {
                            String emailSplitted = field.get(fieldName).asText().split(";")[0];
                            emailMappato = emailSplitted.stripLeading().stripTrailing();

                        } else {
                            emailMappato = field.get(fieldName).asText().stripLeading().stripTrailing();
                        }
                    }
                    //Check per capire se il precedente check verso il CodiceFiscale è stato saltato perché vuoto;
                    //in caso affermativo triggero un booleano che al prossimo giro (sul check "PEC") mi associa
                    //il la partita iva anche al codice fiscale (così pIVA sarà in entrambi i campi).
                    if (!andatoDentroCodiceFiscale) {

                        //Se il campo resta falso, ho la conferma che il loop ha saltato direttamente il campo sopra e quindi associo
                        //il codice fiscale alla Partita IVA (la pIva sarà in entrambi i campi)
                        codiceFiscaleMappato = partitaIvaMappato;
                    }

                }

                if (fieldName.equalsIgnoreCase("Pec")) {
                    if (field.get(fieldName).asText() != null) {

                        pecMappato = field.get(fieldName).asText().stripLeading().stripTrailing();
                    }
                    if (!andatoDentroCodiceFiscale) {

                        //Metto il check anche su PEC perché email potrebbe essere mancante come campo - se girano
                        //tutte e due al massimo sovrascrive la variabile con il solito dato alla seconda, non cambia
                        //nulla.
                        codiceFiscaleMappato = partitaIvaMappato;
                    }

                }

                if (fieldName.equalsIgnoreCase("LegaleRappresentante")) {
                    legaleRappresentanteMappato = capitalizeFully(field.get(fieldName).asText().stripLeading().stripTrailing());
                }

                if (fieldName.equalsIgnoreCase("CodCategoria")) {
                    codCategoriaMappato = field.get(fieldName).asText().stripLeading().stripTrailing();
                }

                if (fieldName.equalsIgnoreCase("DescrTelefoni")) {
                    descrTelefoniMappato = field.get(fieldName).asText().stripLeading().stripTrailing();
                }

                if (fieldName.equalsIgnoreCase("CatAttivitaPrincipale")) {
                    catAttivitaPrincipaleMappato = field.get(fieldName).asText().stripLeading().stripTrailing().toUpperCase();
                }

                if (fieldName.equalsIgnoreCase("CatAttivitaSecondaria")) {
                    if (field.get(fieldName).asText() != null) {

                        catAttivitaSecondariaMappato = field.get(fieldName).asText().stripLeading().stripTrailing().toUpperCase();
                    }
                }

            }

            //CODICE fuori dal loop interno elements


            //Dovremmo avere tutte (con qualcuna vuota) le variabili mappate a questo punto.
            //Primo: check campi anagrafica per capire se già esistono o se vanno creati.

            //Regione

            //Rimuoviamo eventuale lineetta dalla stringa
            if (regioneMappato.contains("-")) {
                regioneMappato = regioneMappato.replace("-", " ");
            }

            if (regioneRepository.existsByDescrizioneIgnoreCase(regioneMappato)) {
               System.out.println("Regione " + regioneMappato + " già presente nel DB");

            } else {
                if (regioneMappato != null) {
                    System.out.println("Regione " + regioneMappato + " aggiunta al DB");
                    Regione _regione = new Regione(capitalizeFully(regioneMappato), codRegioneMappato.toUpperCase());
                    regioneRepository.save(_regione);
                } else {
                    System.out.println("Il valore per regione è nullo - non viene scritto nel db");
                }
            }

            //Provincia - stessa cosa per località che aggiungeremo sotto (vedi sopra commento su regione)
            if (provinciaRepository.existsByCodiceIgnoreCase(provinciaMappato)) {
                System.out.println("Provincia " + provinciaMappato + " già presente nel DB");

            } else {
                if (provinciaMappato != null) {
                    //Creiamo la nuova provincia utilizzando anche il dato Regione passato sempre dallo stesso ogg.
                    //JSON dal FE (la regione sarà presente già nel DB per forza, perché in caso aggiunta sopra)
                    Regione regioneData = regioneRepository.findByDescrizioneIgnoreCase(regioneMappato);

                    System.out.println("Provincia " + provinciaMappato + " aggiunta al DB");
                    Provincia _provincia = new Provincia(provinciaMappato.toUpperCase(), regioneData);
                    provinciaRepository.save(_provincia);
                } else {
                    System.out.println("Il valore per provincia è nullo - non viene scritto nel db");
                }
            }

            //Località - come sopra
            if (localitaRepository.existsByDescrizioneIgnoreCase(localitaMappato)) {
                System.out.println("Località " + localitaMappato + " già presente nel DB");

            } else {
                if (localitaMappato != null) {
                    Provincia provinciaData = provinciaRepository.findByCodiceIgnoreCase(provinciaMappato);

                    System.out.println("Località " + localitaMappato + " aggiunta al DB");
                    Localita _localita = new Localita(capitalizeFully(localitaMappato), capMappato, provinciaData);
                    localitaRepository.save(_localita);
                } else {
                    System.out.println("Il valore per località è nullo - non viene scritto nel db");
                }
            }

            //Associazione
            if (associazioneRepository.existsByDescrizione(associazioneMappato)) {
                System.out.println("Associazione " + associazioneMappato + " già presente nel DB");

            } else {
                if (associazioneMappato != null) {
                    System.out.println("Associazione " + associazioneMappato + " aggiunta al DB");
                    Associazione _associazione = new Associazione(capitalizeFully(associazioneMappato));
                    associazioneRepository.save(_associazione);
                } else {
                    System.out.println("Il valore per associazione è nullo - non viene scritto nel db");
                }
            }

            //Forma giuridica
            if (formaGiuridicaRepository.existsByDescrizioneIgnoreCase(codFormaGiuridicaMappato)) {
                System.out.println("Forma giuridica " + codFormaGiuridicaMappato + " già presente nel DB");

            } else {
                if (codFormaGiuridicaMappato != null) {
                    System.out.println("Forma giuridica " + codFormaGiuridicaMappato + " aggiunta al DB");
                    FormaGiuridica _formaGiuridica = new FormaGiuridica(codFormaGiuridicaMappato.toUpperCase());
                    formaGiuridicaRepository.save(_formaGiuridica);
                } else {
                    System.out.println("Il valore per forma giuridica è nullo - non viene scritto nel db");
                }
            }

            //Categoria
            if (categoriaRepository.existsByDescrizioneIgnoreCase(codCategoriaMappato)) {
                System.out.println("Categoria " + codCategoriaMappato + " già presente nel DB");

            } else {
                if (codCategoriaMappato != null) {
                    System.out.println("Categoria " + codCategoriaMappato + " aggiunta al DB");
                    Categoria _categoria = new Categoria(codCategoriaMappato.toUpperCase());
                    categoriaRepository.save(_categoria);
                } else {
                    System.out.println("Il valore per categoria è nullo - non viene scritto nel db");
                }
            }


            //Creiamo l'utente

            String username = "";

            if (utenteRepository.existsByEmail(emailMappato)) {
                System.out.println("Utente " + emailMappato + " già presente nel DB");

                //UPDATE oggetto quindi? Magari è registrato ma mancano dei dati che qua vengono forniti

            } else {
                //NOTA: se non presente l'email userò la pec per fare la stessa cosa sull'username.
                if (emailMappato != null) {

                    //Metto come username l'email sostituendo la "@" con un "-" e come password l'id del row dell'Excel
                    //(quest'ultimo come richiesto dal cliente).
                    username = emailMappato.replace("@", ".");

                } else if (pecMappato != null) {

                    username = pecMappato.replace("@", ".");

                } else {

                    System.out.println("Deve essere fornita una email o una PEC per potersi registrare");
                }

                //Utilizziamo sempre l'email normale per registrare l'utente ma se manca utilizziamo la PEC
                Utente _utente;

                if (emailMappato != null) {

                    _utente = new Utente(username, emailMappato, encoder.encode(idMappato));

                } else {

                    _utente = new Utente(username, pecMappato, encoder.encode(idMappato));
                }


                //Imposto di default USER come ruolo scegliendolo nel db con il numero 1
                Collection<Ruolo> userRole = ruoloRepository.findById(1);
                _utente.setRuoli(userRole);

                utenteRepository.save(_utente);

            }

            //Creiamo l'azienda (dopo che abbiamo scritto nel DB gli eventuali oggetti di anagrafica mancanti).

            Utente lastUtente = utenteRepository.findTopByOrderByIdDesc();  //Restituisce l'ultimo oggetto utente aggiunto;
            //quello aggiunto con il save() sopra.

            //Qua inseriamo i dati dell'azienda appena arrivati dal FE

            Optional<FormaGiuridica> formaGiuridica = formaGiuridicaRepository.findByDescrizioneIgnoreCase(codFormaGiuridicaMappato);

            Optional<Localita> localita = localitaRepository.findByDescrizioneIgnoreCase(localitaMappato);

            Categoria categoria = categoriaRepository.findByDescrizioneIgnoreCase(codCategoriaMappato);
            Categoria attivitaPrincipale = categoriaRepository.findByDescrizioneIgnoreCase(catAttivitaPrincipaleMappato);

            Categoria attivitaSecondaria = null;

            if (catAttivitaSecondariaMappato != null) {
                attivitaSecondaria = categoriaRepository.findByDescrizioneIgnoreCase(catAttivitaSecondariaMappato);

            }

            Associazione associazione = null;

            if (associazioneMappato != null) {
                associazione = associazioneRepository.findByDescrizioneIgnoreCase(associazioneMappato);

            }

            //Colleghiamo l'utente appena creato ad una nuova azienda associando lo stesso id (vedi "lastUtente.getId()")

            //Se ci sono dei dati null non importa perché nell'entità Azienda solo l'id (e successivamente da aggiungerci
            //anche "primoLogin") sono NOT nullable.
            Azienda nuovaAziendaPerId = new Azienda(emailMappato, ragioneSocialeMappato, indirizzoMappato,
                    telefono1Mappato, telefono2Mappato, descrTelefoniMappato, faxMappato, pecMappato, partitaIvaMappato,
                    codiceFiscaleMappato, legaleRappresentanteMappato,
                    formaGiuridica.get(), associazione, localita.get(), categoria, attivitaPrincipale,
                    attivitaSecondaria
            );

            //NOTA: la nuova azienda che creiamo qua non utilizza mai il proprio repo per scrivere nel DB poiché viene
            //passata come parametro nel costruttore dell'utente: di conseguenza aggiornando il nuovo utente,
            //l'azienda viene legata tramite foreign key (come sempre), ma a quanto pare viene anche salvata
            //come oggetto completo nella sua tabella. Quindi salvare tramite repo un oggetto lo scrive ma scrive anche
            //direttamente, nelle loro relative tabelle, gli altri oggetti differenti collegati tramite foreign key.
            //VEDI SOTTO "CascadeType.ALL"

            //Qua aggiorna l'utente già presente invece di crearne uno nuovo.
            //Avendo aggiunto su Utente "CascadeType.ALL" (che contiene tutte le tipologie, tra cui PERSIST)
            //"Cascade Type PERSIST propagates the persist operation from a parent to a child entity."
            //Su @OneToOne per la @JoinColumn idAzienda dovrebbe salvare sia Utente che nuova Azienda;
            //altrimenti dava errore di persistenza
            Utente utenteSetIdAziendaUpdate = new Utente(lastUtente.getId(), lastUtente.getUsername(), lastUtente.getEmail(),
                    lastUtente.getPassword(), nuovaAziendaPerId, lastUtente.getRuoli());

            utenteRepository.save(utenteSetIdAziendaUpdate);    //Se è già presente l'id invece di un post fa un update
            //del row presente nella tabella.

        }

        //Che tipo di return di ResponseEntity ci mettiamo al posto di null?
        return new ResponseEntity<>(null, HttpStatus.CREATED);

    }

    @PutMapping("/update-azienda/{id}/{idLocalita}/{idCategoria}/{idAttivitaPrincipale}/{idAttivitaSecondaria}/{idAssociazione}/{idFormaGiuridica}/{idRuolo}")
    public ResponseEntity<Azienda> updateAzienda(@PathVariable("id") int id, @PathVariable("idLocalita") int idLocalita,
                                                 @PathVariable("idCategoria") int idCategoria,
                                                 @PathVariable("idAttivitaPrincipale") int idAttivitaPrincipale,
                                                 @PathVariable("idAttivitaSecondaria") int idAttivitaSecondaria,
                                                 @PathVariable("idAssociazione") int idAssociazione,
                                                 @PathVariable("idFormaGiuridica") int idFormaGiuridica,
                                                 @PathVariable("idRuolo") int idRuolo,
                                                 @RequestBody Azienda azienda)

                                    //Prova per passare anche ruolo.id che andrà fatto scrivere tramite utenteRepository(credo)
                                    //Va richiamata la join table da qualche parte insomma.
                                    //@PathVariable("idRuolo") int IdRuolo


    {

        Optional<Utente> utenteData = utenteRepository.findByIdAzienda(id);
        Utente utente = utenteData.get();

        Collection<Ruolo> ruoliUtenteAttuali = utente.getRuoli();

        //Definiamolo per inizializzarlo ma sotto verrà associato con il ruolo collegato all'utente selezionato.
        int ruoloAttuale = 0;

        //Si dà per scontato che ci sia un solo ruolo, altrimenti trattandosi di una collection ogni successivo loop
        //sovrascriverebbe la variabile "ruoloAttuale".
        for(Ruolo ruolo: ruoliUtenteAttuali) {
            ruoloAttuale = ruolo.getId();
        }

        //SE VIENE MODIFICATO IL RUOLO, AGGIORNALO (SOLO SE L'ID E' DIVERSO DA QUELLO ATTUALE)
        if (idRuolo != ruoloAttuale) {

            //Andiamo a cercare l'oggetto Ruolo in base all'idRuolo (che rappresenta il dato in modifica)
            //che viene inviato dal FE
            Collection<Ruolo> ruoloData = ruoloRepository.findById(idRuolo);

            //Impostiamo il nuovo ruolo passato da FE (e trovato tramite repository) sull'utente che corrisponde
            //all'azienda in modifica
            utente.setRuoli(ruoloData);

        }

        Optional<Azienda> aziendeData = aziendaRepository.findById(id);

        //Teoricamente funzionante per evitare i vari @PathVariable ma nella pratica non riesco a farlo girare
        //Optional<Localita> localitaData = localitaRepository.findById(azienda.getLocalitaId());
        Optional<Localita> localitaData = localitaRepository.findById(idLocalita);
        Localita localita = localitaData.get();

        //Con variabile chiamata per beccarlo "categoriaId" quando inviato dal Axios dal FE
        //Optional<Categoria> categoriaData = categoriaRepository.findById(azienda.getCategoriaId());

        Optional<Categoria> categoriaData = categoriaRepository.findById(idCategoria);
        Categoria categoria = categoriaData.get();

        Optional<Categoria> attivitaPrincipaleData = categoriaRepository.findById(idAttivitaPrincipale);
        Categoria attivitaPrincipale = attivitaPrincipaleData.get();

        Optional<Categoria> attivitaSecondariaData = categoriaRepository.findById(idAttivitaSecondaria);
        Categoria attivitaSecondaria = attivitaSecondariaData.get();

        Optional<Associazione> associazioneData = associazioneRepository.findById(idAssociazione);
        Associazione associazione = associazioneData.get();

        Optional<FormaGiuridica> formaGiuridicaData = formaGiuridicaRepository.findById(idFormaGiuridica);
        FormaGiuridica formaGiuridica = formaGiuridicaData.get();

        if (aziendeData.isPresent()) {
            Azienda _azienda = aziendeData.get();
            _azienda.setRagioneSociale(azienda.getRagioneSociale());
            _azienda.setIndirizzo(azienda.getIndirizzo());
            _azienda.setTelefono(azienda.getTelefono());
            _azienda.setTelefono2(azienda.getTelefono2());
            _azienda.setDescrizioneTelefoni(azienda.getDescrizioneTelefoni());
            _azienda.setFax(azienda.getFax());
            _azienda.setEmail(azienda.getEmail());
            _azienda.setPec(azienda.getPec());
            _azienda.setCodiceFiscale(azienda.getCodiceFiscale());
            _azienda.setpIva(azienda.getpIva());
            _azienda.setLegaleRappresentante(azienda.getLegaleRappresentante());
            _azienda.setFormaGiuridica(formaGiuridica);
            _azienda.setAssociazione(associazione);
            _azienda.setLocalita(localita);
            //_azienda.setLocalitaId(azienda.getLocalitaId());
            _azienda.setCategoria(categoria);
            //_azienda.setCategoriaId(azienda.getCategoriaId());
            _azienda.setAttivitaPrincipale(attivitaPrincipale);
            _azienda.setAttivitaSecondaria(attivitaSecondaria);
            //_azienda.setUtente();

            return new ResponseEntity<>(aziendaRepository.save(_azienda), HttpStatus.OK);   //Se già presente ID
        } else {                                                                            //allora aggiorna invece
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);                              //che salvare.
        }
    }

    // *** Commentati per sicurezza perché non utilizzati per ora ***

    /*
    @DeleteMapping("/aziende/{id}")
    public ResponseEntity<HttpStatus> deleteAzienda(@PathVariable("id") int id) {
        try {
            aziendaRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/aziende")
    public ResponseEntity<HttpStatus> deleteAllAziende() {
        try {
            aziendaRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    */
}
