package com.example.springpoliecobe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

//@Data
@Entity
@Table(name = "regioni")
public class Regione {

    public Regione() {
    }

    public Regione(String descrizione, String codice) {
        this.descrizione = descrizione;
        this.codice = codice;
    }

    public Regione(String descrizione, String codice, List<Provincia> province, List<AggiuntaProvincia> aggiunteProvince) {
        this.descrizione = descrizione;
        this.codice = codice;
        this.province = province;
        this.aggiunteProvince = aggiunteProvince;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "descrizione")
    private String descrizione;

    @Column(name = "codice")
    private String codice;

    //@JsonManagedReference//           //Evita un loop infinito su JSON usato insieme a JsonBackReference su Provincia
    //@JsonIgnore
    @OneToMany(mappedBy = "regione")    //Con "mappedBy" specifichiamo che il "proprietario" (owner) della relazione è "province"
    private List<Provincia> province;   //Contiene la lista degli oggetti Provincia

    @OneToMany(mappedBy = "regione")
    private List<AggiuntaProvincia> aggiunteProvince;


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

    public List<Provincia> getProvince() {
        return province;
    }

    public void setProvince(List<Provincia> province) {
        this.province = province;
    }

    public List<AggiuntaProvincia> getAggiunteProvince() {
        return aggiunteProvince;
    }

    public void setAggiunteProvince(List<AggiuntaProvincia> aggiunteProvince) {
        this.aggiunteProvince = aggiunteProvince;
    }
}
