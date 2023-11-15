package com.example.springpoliecobe.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "attivitaPrincipale)")
public class AttivitaPrincipale {

    public AttivitaPrincipale() {
    }

    public AttivitaPrincipale(String descrizione) {
        this.descrizione = descrizione;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "descrizione")
    private String descrizione;

    //Non aggiungo "mappedBy" perchè voglio che resti unidirectional come relazione, poiché mi serve soltanto
    //accedere alle forme giuridiche dalla tabella aziende e non anche vice versa.
    @OneToMany
    private List<Azienda> aziende;

}

