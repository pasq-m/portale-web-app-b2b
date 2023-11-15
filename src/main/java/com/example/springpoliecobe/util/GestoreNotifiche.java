package com.example.springpoliecobe.util;

import com.example.springpoliecobe.model.Annuncio;
import com.example.springpoliecobe.model.InteresseUtenteAnnuncio;
import com.example.springpoliecobe.model.Utente;
import com.example.springpoliecobe.repository.InteresseUtenteAnnuncioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GestoreNotifiche {

    @Autowired
    InteresseUtenteAnnuncioRepository interesseUtenteAnnuncioRepository;

    //Il parametro che arriva è l'id dell'annuncio che è stato appena chiuso
    public List<InteresseUtenteAnnuncio> inviaNotificheUtentiFeedback(Long idAnnuncio) {

        // - OPZIONALE - ArrayList che contiene gli ogg. interesseUtenteAnnuncio di un utente o di più utenti
        //verso l'annuncio che è stato appena chiuso.
        List<InteresseUtenteAnnuncio> interessiUtentiAnnunciData = new ArrayList<>();

        //Liste che contengono gli autori degli annunci appena chiusi ed i rispettivi utenti interessati ad essi.
        //List<Utente> autoriAnnunci = new ArrayList<>();
        //List<Utente> utentiInteressati = new ArrayList<>();

        //Prima cosa, check se era presente l'interesse all'interno dell'annuncio che è stato chiuso.
        //Se l'id dell'annuncio chiuso corrisponde ad un idAnnuncio dentro la tabella degli interessi
        //allora significa che c'erano già uno o più interessi verso quell'annuncio.
        interesseUtenteAnnuncioRepository.findAll().forEach(interesseUtenteAnnuncio -> {
            if (interesseUtenteAnnuncio.getAnnuncio().getId() == idAnnuncio) {

                //Volendo la lista possiamo rimuoverla ed utilizzare direttamente il codice basato sull'oggetto
                //passato ad ogni loop "interesseUtenteAnnuncio"
                interessiUtentiAnnunciData.add(interesseUtenteAnnuncio);

            }

            //NON VA BENE, COSI "SCOPPIAMO" ANNUNCIO CON AUTORE E DOPO COME RITROVIAMO I DATI NEL MOMENTO DELL'INVIO
            //DELLE NOTIFICHE?
            /*interessiUtentiAnnunciData.forEach(interesseUtenteAnnuncioDue -> {
                autoriAnnunci.add(interesseUtenteAnnuncioDue.getAnnuncio().getAzienda().getUtente());
                utentiInteressati.add(interesseUtenteAnnuncioDue.getAzienda().getUtente());
            });*/

        });

        return interessiUtentiAnnunciData;

        //}

        //Se è presente l'interesse, andiamo a prendere gli id dell'utente interessato e l'id dell'annuncio (da quello
        // poi otteniamo anche l'id dell'utente che l'ha pubblicato) e andiamo ad inviare la notifica
        // a quei due account tramite FE.

    }
}
