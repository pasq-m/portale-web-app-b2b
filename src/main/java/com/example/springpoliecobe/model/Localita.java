package com.example.springpoliecobe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

//@Data
@Entity
@Table(name = "localita")
public class Localita {

    public Localita() {
    }

    public Localita(String descrizione, String cap, Provincia provincia) {
        this.descrizione = descrizione;
        this.cap = cap;
        this.provincia = provincia;
    }

    /*public Provincia(String codice, int idRegione) {
        this.codice = codice;
        this.idRegione = idRegione;
    }*/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "descrizione")
    private String descrizione;

    @Column(name = "cap")
    private String cap;

    //@Column(name = "idRegione")
    //private int idRegione;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="idProvincia", nullable=false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Provincia provincia;

    @OneToMany(mappedBy = "localita")
    private List<Annuncio> annunci;

    //@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @OneToMany(mappedBy = "localita", cascade = CascadeType.ALL, orphanRemoval = true)
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

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    //getter method to retrieve the idProvincia
    public Integer getId_provincia(){
        return provincia.getId();
    }

    //getter Method to get the provincia's codice
    public String getProvinciaCodice(){
        return provincia.getCodice();
    }

    @JsonIgnore
    public Provincia getProvincia() {
        return provincia;
    }

    @JsonIgnore
    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }
}
