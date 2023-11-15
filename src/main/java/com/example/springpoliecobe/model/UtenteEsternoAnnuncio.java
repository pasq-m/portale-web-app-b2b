package com.example.springpoliecobe.model;

import com.example.springpoliecobe.embeddable.UtenteEsternoAnnuncioId;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Objects;


//Entità collegata alla classe del pacchetto Embeddable.
//Qua, tramite "@MapsId", mappiamo le altre due entità insieme per creare un composite primary key che viene gestito
//dentro la classe "UtenteEsternoAnnuncioId", contenuta nel pacchetto di cui sopra.
//Da notare come abbiamo specificato che il composite primary key che deve essere utilizzato è quello della classe
//"UtenteEsternoAnnuncioId", tramite l'annotation "@EmbeddedId".
//In questo modo evitiamo il classico @ManyToMany con la sua JoinTable e creiamo questa tabella dove possiamo
//aggiungere altre colonne extra, come "data" e "visionato".

@Entity
@Table(name="utentiEsterniAnnunci")
public class UtenteEsternoAnnuncio {

    public UtenteEsternoAnnuncio() {

    }

    public UtenteEsternoAnnuncio(UtenteEsterno utenteEsterno, Annuncio annuncio, LocalDate dataInteresse,
                                 Boolean visionato) {
        this.utenteEsterno = utenteEsterno;
        this.annuncio = annuncio;
        this.dataInteresse = dataInteresse;
        this.visionato = visionato;
        this.id = new UtenteEsternoAnnuncioId(utenteEsterno.getId(), annuncio.getId()); //Qua uniamo gli id in quello composite?

    }


    @EmbeddedId
    private UtenteEsternoAnnuncioId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("utenteEsternoId")              //"@MapsId" is used to mark the field as a part of the composite key
    private UtenteEsterno utenteEsterno;    //that we defined inside "UtenteEsternoAnnuncioId" embeddable class.

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("annuncioId")
    private Annuncio annuncio;

    @Column(name="dataInteresse")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataInteresse = LocalDate.now();

    @Column(name="visionato")
    private Boolean visionato;


    public UtenteEsternoAnnuncioId getId() {
        return id;
    }

    public void setId(UtenteEsternoAnnuncioId id) {
        this.id = id;
    }

    public UtenteEsterno getUtenteEsterno() {
        return utenteEsterno;
    }

    public void setUtenteEsterno(UtenteEsterno utenteEsterno) {
        this.utenteEsterno = utenteEsterno;
    }

    public Annuncio getAnnuncio() {
        return annuncio;
    }

    public void setAnnuncio(Annuncio annuncio) {
        this.annuncio = annuncio;
    }

    public LocalDate getDataInteresse() {
        return dataInteresse;
    }

    public void setDataInteresse(LocalDate dataInteresse) {
        this.dataInteresse = dataInteresse;
    }

    public Boolean getVisionato() {
        return visionato;
    }

    public void setVisionato(Boolean visionato) {
        this.visionato = visionato;
    }


    //Override dei metodi hashCode ed equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        UtenteEsternoAnnuncio that = (UtenteEsternoAnnuncio) o;
        return Objects.equals(utenteEsterno, that.utenteEsterno) &&
                Objects.equals(annuncio, that.annuncio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(utenteEsterno, annuncio);
    }

}
