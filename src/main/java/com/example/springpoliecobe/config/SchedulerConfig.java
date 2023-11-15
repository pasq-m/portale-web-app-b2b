package com.example.springpoliecobe.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;


//Classe di configurazione creata appositamente per attivare le annotazioni "@Scheduled".
//Spring, grazie all'annotazione "@Configuration", la mette tra le classi di configurazione dell'app e tramite
//"@EnableScheduling" permette di attivare le annotazioni "@Scheduled" nel resto dell'app.
@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "scheduler.enabled", matchIfMissing = true)
public class SchedulerConfig {

}
