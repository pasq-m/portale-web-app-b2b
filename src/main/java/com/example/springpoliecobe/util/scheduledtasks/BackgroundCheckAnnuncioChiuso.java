package com.example.springpoliecobe.util.scheduledtasks;

import com.example.springpoliecobe.model.Annuncio;
import com.example.springpoliecobe.repository.AnnuncioRepository;
import com.example.springpoliecobe.util.GestoreNotifiche;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

// ***** CLASSE AL MOMENTO NON UTILIZZATA *****
//Perché aveva senso se il cron veniva utilizzato nel BE, ma in realtà deve essere utilizzato in FE per poter inviare
//l'input al BE e far partire tutto il processo (e non viceversa).
//Il metodo "azioneDaCompiere" viene utilizzato direttamente nel controller AnnunciController
@Component
public class BackgroundCheckAnnuncioChiuso {

    @Autowired
    AnnuncioRepository annuncioRepository;

    @Autowired
    GestoreNotifiche gestoreNotifiche;

    //Appena parte l'app gira una volta (prima dell'inizializzazione dei beans)
    @PostConstruct
    public void onStartup() {
        azioneDaCompiere();
    }

    //5 minuti
    //@Scheduled(initialDelay = 30000 , fixedDelay = 30000)

    //Check una volta al giorno tramite cron

    //cron syntax: second, minute, hour, day of month, month, day(s) of week
    //In fase di sviluppo, per praticità, metteremo che il check lo fa durante le ore lavorative così
    //abbiamo il pc acceso - imposto alle 10 di mattina.
    @Scheduled(cron = "0 0 10 * * *")
    public void checkOgni24Ore() {
        azioneDaCompiere();
    }

    public void azioneDaCompiere() {

        //try {
            //Check nel repo se c'è un annuncio scaduto comparando la sua data di scadenza rispetto al momento
            //del check (".now()")
            //annuncioRepository.findByDataDiScadenzaBefore(LocalDate.now()).forEach(annuncio -> {
            //List<Annuncio> _annunciScaduti = annuncioRepository.findByDataDiScadenzaBefore(LocalDate.now());

            //Check per vedere se esistono annunci con colonna "chiuso" a false
            List<Annuncio> _annunciAttivi  = annuncioRepository.findByChiusoIsFalse();

            if (_annunciAttivi.isEmpty()) {
                System.out.println("Nessun annuncio ATTIVO trovato");

            } else {
                _annunciAttivi.forEach(annuncio -> {
                    //Controlliamo se tra gli annunci ancora attivi sono presenti annunci che sono scaduti
                    if (annuncio.getDataDiScadenza().isBefore(LocalDate.now())) {
                        //Per ogni annuncio trovato scaduto andiamo a flaggarlo col "chiuso" = true
                        annuncio.setChiuso(true);
                        annuncioRepository.save(annuncio);

                        //Qua richiamiamo un metodo per inviare, se ci sono le condizioni, le notifiche agli utenti
                        //coinvolti.
                        gestoreNotifiche.inviaNotificheUtentiFeedback(annuncio.getId());

                        System.out.println("Annuncio scaduto: " + annuncio.getTitolo());
                    } else {
                        System.out.println("Annuncio non scaduto: " + annuncio.getTitolo());
                    }
                });
            }

            //});

        //} catch (NullPointerException e) {

        //    System.out.println("Nessun annuncio scaduto trovato: " + e.getMessage());
        //}
    }



}
