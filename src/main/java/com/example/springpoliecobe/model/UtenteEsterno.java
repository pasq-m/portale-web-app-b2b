package com.example.springpoliecobe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NaturalIdCache;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "utentiEsterni")
@NaturalIdCache
@Cache(
        usage = CacheConcurrencyStrategy.READ_WRITE
)
public class UtenteEsterno {

    public UtenteEsterno() {

    }

    public UtenteEsterno(String email, String telefono, String ragioneSociale) {
        this.email = email;
        this.telefono = telefono;
        this.ragioneSociale = ragioneSociale;
    }

    public UtenteEsterno(Long id, String email, String telefono, String ragioneSociale) {
        this.id = id;
        this.email = email;
        this.telefono = telefono;
        this.ragioneSociale = ragioneSociale;
    }

    /*public UtenteEsterno(String email, String telefono, String ragioneSociale, List<UtenteEsternoAnnuncio> annunci) {
        this.email = email;
        this.telefono = telefono;
        //this.fax = fax;
        this.ragioneSociale = ragioneSociale;
        this.annunci = annunci;
    }*/

    /*public UtenteEsterno(String email, String telefono, String ragioneSociale, List<Annuncio> annunciAnnunci) {
        this.email = email;
        this.telefono = telefono;
        this.ragioneSociale = ragioneSociale;
        //this.annunciAnnunci = annunciAnnunci;
        this.annuncio = annunciAnnunci;
    }*/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "telefono")
    private String telefono;

    //@Column(name = "fax")
    //private String fax;

    @Column(name="ragioneSociale")
    private String ragioneSociale;

    @OneToMany(mappedBy = "utenteEsterno",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<UtenteEsternoAnnuncio> annuncio = new ArrayList<>();

    /*@ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "utentiEsterni_annunci",
            joinColumns = @JoinColumn(name = "idUtenteEsterno"),    //Essendo JoinColumns di default prende in automatico
            inverseJoinColumns = @JoinColumn(name = "idAnnuncio"))  //l'id dell'entità dove è richiamata(in questo caso
    private List<Annuncio> annunci;*/                                 //UtenteEsterno)?
                                                                    //E l'inverseJoinColumns prende l'id dell'entità che
                                                                    //viene annotata col JoinTable (cioè quello che gli
                                                                    //settiamo noi, in questo caso "annunci")?

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /*public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }*/

    public String getRagioneSociale() {
        return ragioneSociale;
    }

    public void setRagioneSociale(String ragioneSociale) {
        this.ragioneSociale = ragioneSociale;
    }

    //Mettendo @JsonIgnore sul corrispettivo in Annunci evitiamo l'infinite recursion che si genera, poiché Annunci
    //fa il get su utentiEsterni e una volta fatto il get su questa classe viene di conseguenza triggerato il get
    //su ogni campo che va a richiamare anche questo getAnnunci(), andando a creare un loop infinito.
    @JsonIgnore
    public List<UtenteEsternoAnnuncio> getAnnunci() {
        return annuncio;
    }

    public void setAnnunci(List<UtenteEsternoAnnuncio> annunci) {
        this.annuncio = annunci;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UtenteEsterno utenteEsterno = (UtenteEsterno) o;
        return Objects.equals(email, utenteEsterno.email) &&
                Objects.equals(telefono, utenteEsterno.telefono) &&
                Objects.equals(ragioneSociale, utenteEsterno.ragioneSociale);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, telefono, ragioneSociale);
    }

}
