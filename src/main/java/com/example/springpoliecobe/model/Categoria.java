package com.example.springpoliecobe.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

//@Data
//@Getter
//@Setter
@Entity
@Table(name = "categorie")
public class Categoria {


    public Categoria() {
    }

    @JsonCreator
    public Categoria(@JsonProperty("descrizione") String descrizione) {
        this.descrizione = descrizione;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "descrizione")
    private String descrizione;

    //@OneToMany(mappedBy = "categoria" , cascade = CascadeType.ALL, orphanRemoval = true)
    @OneToMany( mappedBy = "categoria" , cascade = CascadeType.ALL, orphanRemoval = true)
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
