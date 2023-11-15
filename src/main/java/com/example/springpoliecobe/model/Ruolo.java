package com.example.springpoliecobe.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ruoli")
public class Ruolo {

    public Ruolo() {

    }

    /*public Ruolo(int id, ERuolo descrizione) {
        this.id = id;
        this.descrizione = descrizione;
    }*/

    public Ruolo(ERuolo descrizione) {
        this.descrizione = descrizione;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "descrizione")
    private ERuolo descrizione;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ERuolo getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(ERuolo descrizione) {
        this.descrizione = descrizione;
    }

}
