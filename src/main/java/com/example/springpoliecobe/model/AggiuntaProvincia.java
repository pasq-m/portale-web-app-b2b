package com.example.springpoliecobe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "aggiunte_province")
public class AggiuntaProvincia {

    public AggiuntaProvincia() {

    }

    public AggiuntaProvincia(String codice, Regione regione) {
        this.codice = codice;
        this.regione = regione;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "codice")
    private String codice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_regione")
    private Regione regione;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    @JsonIgnore
    public Regione getRegione() {
        return regione;
    }

    public void setRegione(Regione regione) {
        this.regione = regione;
    }
}
