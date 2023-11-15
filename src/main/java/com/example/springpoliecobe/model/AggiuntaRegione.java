package com.example.springpoliecobe.model;

import jakarta.persistence.*;

@Entity
@Table(name = "aggiunteRegioni")
public class AggiuntaRegione {

    public AggiuntaRegione() {

    }

    public AggiuntaRegione(String descrizione, String codice) {
        this.descrizione = descrizione;
        this.codice = codice;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "descrizione")
    private String descrizione;

    @Column(name = "codice")
    private String codice;

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

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }
}
