/*package com.example.springpoliecobe.controller;

import com.example.springpoliecobe.config.UtentePrincipale;
import com.example.springpoliecobe.model.Utente;
import com.example.springpoliecobe.repository.UtenteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;*/

/*@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@Controller
public class SecurityController {

    @Autowired
    UtenteService utenteService;

    @Autowired
    UtenteRepository utenteRepository;

    @GetMapping("/utente-corrente-backup")
    //@ResponseBody
    public String currentUserName(ModelMap model, Principal principal) {        //Restituisce il "nome" (username nel nostro caso)
        //String prova = utenteService.getPrincipal(principal);                 //dell'utente corrente (loggato).
        Utente auth = (Utente)SecurityContextHolder.getContext().getAuthentication();
        String prova = auth.getEmail();
        model.addAttribute("name",prova);
        //return principal.getName();
        return "utente-corrente-backup";
    }

    @GetMapping("/utente-corrente-comm")                      //userDetails è di tipo
    public String viewUserAccountForm(
            @AuthenticationPrincipal UtentePrincipale userDetails,
            Model model) {                                          //L'annotazione permette di passare l'utente corrente
                                                                    //all'interno del controller per poterne estrapolare
                                                                    //i dati all'interno delle viste.
        model.addAttribute("user", userDetails);
        //model.addAttribute("pageTitle", "Account Details");

        return "utente-corrente-comm";
    }

    //Versione per React con ResponseEntity
    @GetMapping("/utente-corrente")
    //public ResponseEntity<List<UtentePrincipale>> getUtenteCorrenteReact(@RequestParam(required = false)
    public ResponseEntity<List<Utente>> getUtenteCorrenteReact(
            @AuthenticationPrincipal UtentePrincipale userDetails, Model model) {
        try {

            List<Utente> utentePrincipale = new ArrayList<Utente>();


            /*if (userDetails == null) {                                                                    //tutti gli altri dati e li aggiunge
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);                                     //alla lista.
            }*/

            //String ruoli = userDetails.getAuthorities();
            //String email = userDetails.getEmail();
            //String username = userDetails.getUsername();

            /*utentePrincipale.add(utenteRepository.findByUsername(userDetails.getUsername()));   //Basta uno perchè prende
            //utentePrincipale.add(utenteRepository.findByEmail(userDetails.getEmail()));       //già tutti i dati così.


            //return new ResponseEntity<>(azienda, HttpStatus.OK);        //Devo convertire i dati in JSON prima di inviarli.
            return new ResponseEntity<>(utentePrincipale, HttpStatus.OK);        //Devo convertire i dati in JSON prima di inviarli.
        } catch (Exception e) {                                         //a React con ResponseEntity<T>;
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}*/
