package com.example.springpoliecobe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "aggiunte_localita")
public class AggiuntaLocalita {

    public AggiuntaLocalita() {

    }

    public AggiuntaLocalita(String descrizione, String cap, Provincia provincia) {
        this.descrizione = descrizione;
        this.cap = cap;
        this.provincia = provincia;
    }

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name ="descrizione")
    private String descrizione;

    @Column(name ="cap")
    private String cap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_provincia")
    private Provincia provincia;


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

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    @JsonIgnore
    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }
}
