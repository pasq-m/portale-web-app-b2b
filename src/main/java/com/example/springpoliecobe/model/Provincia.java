package com.example.springpoliecobe.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

//@Data
@Entity
@Table(name = "province")
public class Provincia {

    public Provincia() {
    }

    public Provincia(String codice, Regione regione) {
        this.codice = codice;
        this.regione = regione;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "codice")
    private String codice;

    //@JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="idRegione", nullable=false)
    //@Fetch(FetchMode.SELECT)
    @OnDelete(action = OnDeleteAction.CASCADE)
    //@JsonIgnore
    private Regione regione;            //La variabile specificata con "mappedBy" in Regione.
                                        //Queste variabili delle JoinColumn sono "passaggi" con cui andare a prendere
                                        //dati delle tabelle madri collegate, le One di ManyToOne
                                        //(ad es. con "getId_regione()" prendiamo l'id della regione
                                        //collegata a questo oggetto direttamente nella tabella madre).
    @OneToMany(mappedBy = "provincia")
    private List<Localita> localita;

    @OneToMany (mappedBy = "provincia")
    private List<AggiuntaLocalita> aggiunteLocalita;


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

    //getter method to retrieve the idRegione - prende l'Id dalla tabella id_regione
    public Integer getId_regione(){
        return regione.getId();
    }

    //getter Method to get the regione's description
    /*public String getRegioneDescrizione(){
        return regione.getDescrizione();
    }*/

    //getter Method to get the regione's codice - prende il codice dalla tabella id_regione
    //ed utilizziamo regioneCodice come variabile nel FE.
    //Richiamo nel FE con regioneCodice e automaticamente suppongo faccia girare questo get.
    public String getRegioneCodice(){
        return regione.getCodice();
    }

    public String getRegioneDescrizione(){
        return regione.getDescrizione();
    }

    @JsonIgnore                     //Serve ad evitare che vada in loop durante la generazione dei Json
    public Regione getRegione() {
        return regione;
    }

    @JsonIgnore
    public void setRegione(Regione regione) {
        this.regione = regione;
    }

    public List<Localita> getLocalita() {
        return localita;
    }

    public void setLocalita(List<Localita> localita) {
        this.localita = localita;
    }

    public List<AggiuntaLocalita> getAggiunteLocalita() {
        return aggiunteLocalita;
    }

    public void setAggiunteLocalita(List<AggiuntaLocalita> aggiunteLocalita) {
        this.aggiunteLocalita = aggiunteLocalita;
    }
}

