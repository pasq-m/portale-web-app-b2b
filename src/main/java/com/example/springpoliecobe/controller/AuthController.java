package com.example.springpoliecobe.controller;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

import com.example.springpoliecobe.model.*;
import com.example.springpoliecobe.payload.request.LoginRequest;
import com.example.springpoliecobe.payload.request.SignupRequest;
import com.example.springpoliecobe.payload.response.JwtResponse;
import com.example.springpoliecobe.payload.response.MessageResponse;
import com.example.springpoliecobe.repository.*;
import com.example.springpoliecobe.security.jwt.JwtUtils;
import com.example.springpoliecobe.security.services.UserDetailsImpl;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:3000"}, maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;        //Lo prende direttamente da Spring Security con @Autowired

    @Autowired
    UtenteRepository utenteRepository;

    @Autowired
    RuoloRepository ruoloRepository;

    @Autowired
    AziendaRepository aziendaRepository;

    @Autowired
    AssociazioneRepository associazioneRepository;

    @Autowired
    FormaGiuridicaRepository formaGiuridicaRepository;

    @Autowired
    CategoriaRepository categoriaRepository;

    @Autowired
    LocalitaRepository localitaRepository;

    @Autowired
    ProvinciaRepository provinciaRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;


    @GetMapping("/utenti")
    public ResponseEntity<List<Utente>> getAllUtenti(@RequestParam(required = false) String username) {
        try {
            List<Utente> utente = new ArrayList<Utente>();

            if (username == null)
                utenteRepository.findAll().forEach(utente::add);
            else
                utenteRepository.findByUsernameContaining(username).forEach(utente::add);

            if (utente.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(utente, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/utenti/{id}")
    public ResponseEntity<Utente> getUtenteById(@PathVariable("id") int id) {
        Optional<Utente> utenteData = utenteRepository.findById(id);

        if (utenteData.isPresent()) {
            return new ResponseEntity<>(utenteData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Tramite id specifico utente trovo la relativa azienda
    @GetMapping("/utenti/id-azienda/{id}")
    public ResponseEntity<Azienda> getIdAziendaById(@PathVariable("id") int id) {
        Optional<Utente> utenteData = utenteRepository.findById(id);

        if (utenteData.isPresent()) {
            Utente utente = utenteData.get();
            Azienda azienda = utente.getAzienda();
            return new ResponseEntity<Azienda>(azienda, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Tramite id azienda trovo relativo utente
    @GetMapping("/utenti/azienda-id/{idAzienda}")
    public ResponseEntity<Utente> getUtenteByIdAzienda(@PathVariable("idAzienda") int id) {
        Optional<Utente> utenteData = utenteRepository.findByIdAzienda(id);

        if (utenteData.isPresent()) {
            Utente utente = utenteData.get();
            return new ResponseEntity<Utente>(utente, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/signin")                         //LoginRequest that is a DTO kept inside the "/payload/request" folder
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {  //loginRequest è un oggetto
                                                                                                //che possiede user e pass
                                                                                            //inviati dal form in FE React
        Authentication authentication = authenticationManager.authenticate(   //Qua fa un'autenticazione ma già sul DB?
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);                 //Ci creiamo un token con i dati

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();  //Qua imposta l'utente autentic.
                                                                                        //impostandone i dati su un ogg.
                                                                //dell'interfaccia UserDetails di modo che possano essere
                                                                //utilizzati a livello di security (autenticazione etc.).
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    //Controller utilizzato per creare utente più dati default azienda SOLO via pagina signup
    @PostMapping("/signup")     //@RequestBody "converte" l'oggetto JSON in arrivo dal FE in un oggetto Java, i quali campi
                                //sono già definiti all'interno della classe SignupRequest (lo stesso vale per LoginRequest).
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (utenteRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Errore: Username già registrato!"));
        }

        if (utenteRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Errore: Email già registrata!"));
        }

        // Create new user's account
        Utente utente = new Utente(signUpRequest.getUsername(), signUpRequest.getEmail(),
                            encoder.encode(signUpRequest.getPassword()));

        //Sempre vuoto almeno che non viene settato esplicitamente durante la procedura di signup?
        Set<String> strRoles = signUpRequest.getRuolo();
        Set<Ruolo> ruoli = new HashSet<>();

        //Di default setta sempre il ROLE_USER
        //É possibile cambiare il default a mano qua sul codice per creare un account ADMIN
        if (strRoles == null) {
            Ruolo userRole = ruoloRepository.findByDescrizione(ERuolo.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            ruoli.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Ruolo adminRole = ruoloRepository.findByDescrizione(ERuolo.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        ruoli.add(adminRole);

                        break;
                    case "mod":
                        Ruolo modRole = ruoloRepository.findByDescrizione(ERuolo.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        ruoli.add(modRole);

                        break;
                    default:
                        Ruolo userRole = ruoloRepository.findByDescrizione(ERuolo.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        ruoli.add(userRole);
                }
            });
        }

        utente.setRuoli(ruoli);
        utenteRepository.save(utente);  //Di default dovrebbe attendere che il save nel db sia finito prima di andare
                                        //avanti con la prossima linea di codice.

        Utente lastUtente = utenteRepository.findTopByOrderByIdDesc();  //Restituisce l'ultimo oggetto utente aggiunto;
                                                                        //quello aggiunto con il save() sopra.

        //Troviamo una provincia di default (l'ultima già aggiunta) da passare al costruttore specifico di Localita
        //Provincia lastProvincia = provinciaRepository.findTopByOrderByIdDesc();

        //Queste istanze sono temporanee e servono solo per generare un nuovo Utente senza eccezioni.
        //Vanno cancellate dal DB a fine registerUser metodo. <-- CANCELLANO TUTTO QUINDI INUTILE CON CASCADE
        /*formaGiuridicaRepository.save(new FormaGiuridica());
        associazioneRepository.save(new Associazione());
        categoriaRepository.save(new Categoria());
        localitaRepository.save(new Localita(lastProvincia));*/

        /*FormaGiuridica formaGiuridicaFindTop = formaGiuridicaRepository.findTopByOrderByIdDesc();
        Associazione associazioneFindTop = associazioneRepository.findTopByOrderByIdDesc();
        Categoria categoriaFindTop = categoriaRepository.findTopByOrderByIdDesc();
        Localita localitaFindTop = localitaRepository.findTopByOrderByIdDesc();*/

        //Andiamo a settare dei valori di default (solo quelli delle tabelle figlie) da impostare nel relativo profilo
        //azienda appena viene creato un nuovo utente
        Optional<FormaGiuridica> formaGiuridicaDefault = formaGiuridicaRepository.findById(1);
        Optional<Localita> localitaDefault = localitaRepository.findById(1);
        Optional<Categoria> categoriaDefault = categoriaRepository.findById(1);
        Optional<Associazione> associazioneDefault = associazioneRepository.findById(1);

        Azienda nuovaAziendaPerId = new Azienda(lastUtente.getId(), formaGiuridicaDefault.get(), associazioneDefault.get(),
                                        categoriaDefault.get(), categoriaDefault.get(), categoriaDefault.get(),
                                        localitaDefault.get());

        //aziendaRepository.save(nuovaAziendaPerId);

        //Qua aggiorna l'utente già presente invece di crearne uno nuovo.
        //Avendo aggiunto su Utente "CascadeType.PERSIST" su @OneToOne per la @JoinColumn idAzienda
        //dovrebbe salvare sia Utente che nuova Azienda; altrimenti dava errore di persistenza
        Utente utenteSetIdAziendaUpdate = new Utente(lastUtente.getId(), lastUtente.getUsername(), lastUtente.getEmail(),
                                                lastUtente.getPassword(), nuovaAziendaPerId, lastUtente.getRuoli());

        utenteRepository.save(utenteSetIdAziendaUpdate);    //Se è già presente l'id invece di un post fa un update
                                                            //del row presente nella tabella.

        //Cancelliamo dal DB i nuovi row "temporanei" delle tabelle non-nullable
        /*formaGiuridicaRepository.delete(formaGiuridicaFindTop);
        associazioneRepository.delete(associazioneFindTop);
        categoriaRepository.delete(categoriaFindTop);
        localitaRepository.delete(localitaFindTop);*/

        return ResponseEntity.ok(new MessageResponse("Utente registrato correttamente!"));
    }

    //Controller utilizzato per creare utente più relativi dati azienda tramite pannello admin "Aggiungi nuova azienda"
    //***** Utilizzato al posto di quello in AziendeController *****
    //***** COMMENTATO PER CAMPI RIMOSSI/NASCOSTI *****
    //@PostMapping("/aggiungi-azienda-singola/{idLocalita}/{idCategoria}/{idAttivitaPrincipale}/{idAttivitaSecondaria}" +
    //                                            "/{idAssociazione}/{idFormaGiuridica}/{idRuolo}")
    @PostMapping("/aggiungi-azienda-singola/{idRuolo}")
    public ResponseEntity<?> adminAddNewUser(//@PathVariable("id") int id,
                                             /*@PathVariable("idLocalita") int idLocalita,
                                             @PathVariable("idCategoria") int idCategoria,
                                             @PathVariable("idAttivitaPrincipale") int idAttivitaPrincipale,
                                             @PathVariable("idAttivitaSecondaria") int idAttivitaSecondaria,
                                             @PathVariable("idAssociazione") int idAssociazione,
                                             @PathVariable("idFormaGiuridica") int idFormaGiuridica,*/
                                             @PathVariable("idRuolo") int idRuolo,
                                             //@Valid da errore in lunghezza username (non so come è impostato, forse default)
                                             //@Valid @RequestBody SignupRequest signUpRequest) {
                                             @RequestBody SignupRequest signUpRequest) {

        //NOTA: ho utilizzato "signupRequest" come classe "wrapper" per inserire nel body del JSON due classi distinte:
        //Azienda e Utente, cosicché da poterli gestire dentro al controller.

        //Anche se le classi sono differenti (SignupRequest vs Utente) basta che ottenga il tipo di dato identico
        //e lo associ per poi gestirlo all'interno della classe "reale" (in questo caso "Utente")
        if (utenteRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Errore: Username già registrato!"));
        }

        if (utenteRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Errore: Email già registrata!"));
        }

        // Create new user's account
        Utente utente = new Utente(signUpRequest.getUsername(), signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Collection<Ruolo> userRole = ruoloRepository.findById(idRuolo);

        utente.setRuoli(userRole);
        utenteRepository.save(utente);  //Aggiungiamo anche il ruolo e l'oggetto utente può essere salvato

        Utente lastUtente = utenteRepository.findTopByOrderByIdDesc();  //Restituisce l'ultimo oggetto utente aggiunto;
        //quello aggiunto con il save() sopra.


        //Qua inseriamo i dati dell'azienda appena arrivati dal FE

        //Optional<FormaGiuridica> formaGiuridicaDefault = formaGiuridicaRepository.findById(1);

        //***** COMMENTATO PER CAMPI RIMOSSI/NASCOSTI *****
        /*Optional<FormaGiuridica> formaGiuridica = formaGiuridicaRepository.findById(idFormaGiuridica);

        Optional<Localita> localita = localitaRepository.findById(idLocalita);

        Optional<Categoria> categoria = categoriaRepository.findById(idCategoria);
        Optional<Categoria> attivitaPrincipale = categoriaRepository.findById(idAttivitaPrincipale);
        Optional<Categoria> attivitaSecondaria = categoriaRepository.findById(idAttivitaSecondaria);

        Optional<Associazione> associazione = associazioneRepository.findById(idAssociazione);*/


        //Colleghiamo l'utente appena creato ad una nuova azienda associando lo stesso id (vedi "lastUtente.getId()")

        //***** COMMENTATO PER CAMPI RIMOSSI/NASCOSTI *****
        /*Azienda nuovaAziendaPerId = new Azienda(signUpRequest.getEmail(), signUpRequest.getRagioneSociale(),
                signUpRequest.getIndirizzo(), signUpRequest.getTelefono(), signUpRequest.getTelefono2(),
                signUpRequest.getDescrizioneTelefoni(), signUpRequest.getFax(), signUpRequest.getPec(),
                signUpRequest.getpIva(), signUpRequest.getCodiceFiscale(), signUpRequest.getLegaleRappresentante(),
                formaGiuridica.get(), associazione.get(), localita.get(), categoria.get(), attivitaPrincipale.get(),
                attivitaSecondaria.get()
                );*/

        Azienda nuovaAziendaPerId = new Azienda(signUpRequest.getRagioneSociale(), signUpRequest.getIndirizzo(),
                                        signUpRequest.getTelefono(), signUpRequest.getFax(), signUpRequest.getPec(),
                                        signUpRequest.getpIva(), signUpRequest.getCodiceFiscale()
        );

        //Qua aggiorna l'utente già presente invece di crearne uno nuovo.
        //Avendo aggiunto su Utente "CascadeType.PERSIST" su @OneToOne per la @JoinColumn idAzienda
        //dovrebbe salvare sia Utente che nuova Azienda; altrimenti dava errore di persistenza
        Utente utenteSetIdAziendaUpdate = new Utente(lastUtente.getId(), lastUtente.getUsername(), lastUtente.getEmail(),
                lastUtente.getPassword(), nuovaAziendaPerId, lastUtente.getRuoli());

        utenteRepository.save(utenteSetIdAziendaUpdate);    //Se è già presente l'id invece di un post fa un update
        //del row presente nella tabella.

        //Cancelliamo dal DB i nuovi row "temporanei" delle tabelle non-nullable
        /*formaGiuridicaRepository.delete(formaGiuridicaFindTop);
        associazioneRepository.delete(associazioneFindTop);
        categoriaRepository.delete(categoriaFindTop);
        localitaRepository.delete(localitaFindTop);*/

        return ResponseEntity.ok(new MessageResponse("Utente registrato correttamente!"));
    }

}
