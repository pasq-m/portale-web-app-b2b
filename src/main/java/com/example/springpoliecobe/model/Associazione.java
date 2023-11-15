package com.example.springpoliecobe.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

//@Data
@Entity
@Table(name = "associazioni")
public class Associazione {

    public Associazione() {
    }

    public Associazione(String descrizione) {
        this.descrizione = descrizione;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "descrizione")
    private String descrizione;

    @OneToMany(mappedBy = "associazione", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Azienda> aziende;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
}
