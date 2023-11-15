/*package com.example.springpoliecobe.controller;

import com.example.springpoliecobe.dto.UtenteDto;
import jakarta.validation.Valid;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrazioneController {          //Tutti gli endpoint collegati ai metodi che manipolano la registrazione.

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    UtenteService utenteService;

    // Registrazione utente - creazione nuovo utente con ruolo di default (no admin)

    @PostMapping("/saveData")
    //@ResponseBody                       //ResponseBody indica che l'oggetto restituito sarà formattato come JSON
    //public String registerUserAccount(@Valid final UtenteDto accountDto, final HttpServletRequest request) {
    public String registerUserAccount(@Valid final UtenteDto accountDto) {
        LOGGER.debug("Registering user account with information: {}", accountDto);  //@Valid controlla che i par. siano
                                                                                    //validi, ad es. non nulli (credo).
        utenteService.registerNewUserAccount(accountDto);       //Qua facciamo girare il metodo per creare l'utente.
        return "redirect:/add-user";
    }


}*/
