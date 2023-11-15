package com.example.springpoliecobe.model;

import jakarta.persistence.*;

@Entity
@Table(name = "interessiUtentiAnnunci")
public class InteresseUtenteAnnuncio {

    public InteresseUtenteAnnuncio() {

    }

    public InteresseUtenteAnnuncio(Annuncio annuncio, boolean feedbackDato, Azienda azienda) {
        this.annuncio = annuncio;
        this.feedbackDato = feedbackDato;
        this.azienda = azienda;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //"idAnnuncio" serve anche per andare a riprendere l'id dell'utente che ha pubblicato l'annuncio.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idAnnuncio")
    private Annuncio annuncio;

    @Column(name = "feedbackDato")
    private boolean feedbackDato;

    //"idAzienda" intesa come l'utente che ha mostrato l'interesse.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idAzienda")
    private Azienda azienda;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Annuncio getAnnuncio() {
        return annuncio;
    }

    public void setAnnuncio(Annuncio annuncio) {
        this.annuncio = annuncio;
    }

    public boolean isFeedbackDato() {
        return feedbackDato;
    }

    public void setFeedbackDato(boolean feedbackDato) {
        this.feedbackDato = feedbackDato;
    }

    public Azienda getAzienda() {
        return azienda;
    }

    public void setAzienda(Azienda azienda) {
        this.azienda = azienda;
    }
}
