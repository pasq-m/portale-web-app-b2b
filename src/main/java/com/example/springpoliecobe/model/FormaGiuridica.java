package com.example.springpoliecobe.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

//@Data
@Entity
@Table(name = "formeGiuridiche")
public class FormaGiuridica {

    public FormaGiuridica() {
    }

    public FormaGiuridica(String descrizione) {
        this.descrizione = descrizione;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "descrizione")
    private String descrizione;

    //@OneToMany(mappedBy = "formaGiuridica" ,cascade = CascadeType.ALL, orphanRemoval = true)
    @OneToMany(mappedBy = "formaGiuridica", cascade = CascadeType.ALL, orphanRemoval = true)
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
