package com.example.springpoliecobe.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.lang.Long;

//Questa classe rappresenta una sorta di entità che svolge la funzione di composite primary key
//(l'unione delle due entità UtenteEsterno e Annuncio).
//@Embeddable è un'annotazione che rende i campi di questa classe "embeddable", cioè incorporabili all'interno
//di altre entità (nella entità "UtentiEsterniAnnuncio" che rappresenta una tabella che unisce le due entità,
//suppongo sostituendo la joinTable dle manyToMany).
//In questo modo noi possiamo organizzare una struttura che ci permette di avere un'alternativa ad un semplice
//"@ManyToMany", aggiungendo colonne extra (in questo caso "data" e "visionato").

@Embeddable
public class UtenteEsternoAnnuncioId implements Serializable {

    UtenteEsternoAnnuncioId() {

    }

    public UtenteEsternoAnnuncioId(Long utenteEsternoId, Long annuncioId) {
        this.utenteEsternoId = utenteEsternoId;
        this.annuncioId = annuncioId;
    }


    @Column(name="utenteEsternoId")
    private Long utenteEsternoId;

    @Column(name="annuncioId")
    private Long annuncioId;


    public Long getUtenteEsternoId() {
        return utenteEsternoId;
    }

    public void setUtenteEsternoId(Long utenteEsternoId) {
        this.utenteEsternoId = utenteEsternoId;
    }

    public Long getAnnuncioId() {
        return annuncioId;
    }

    public void setAnnuncioId(Long annuncioId) {
        this.annuncioId = annuncioId;
    }

    //Override dei metodi hashCode ed equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        UtenteEsternoAnnuncioId that = (UtenteEsternoAnnuncioId) o;
        return Objects.equals(utenteEsternoId, that.utenteEsternoId) &&
                Objects.equals(annuncioId, that.annuncioId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(utenteEsternoId, annuncioId);
    }
}
