package com.example.springpoliecobe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

//@Getter
//@Setter
@Entity
@Table(name = "aziende")
public class Azienda {

    public Azienda() {

    }

    public Azienda(int id, FormaGiuridica formaGiuridica, Associazione associazione, Categoria categoria,
                   Categoria attivitaPrincipale, Categoria attivitaSecondaria, Localita localita) {
        this.id = id;
        this.formaGiuridica = formaGiuridica;
        this.associazione = associazione;
        this.categoria = categoria;
        this.attivitaPrincipale = attivitaPrincipale;
        this.attivitaSecondaria = attivitaSecondaria;
        this.localita = localita;
    }

    //Costruttore "quasi" completo per POST signup (aggiunta azienda singola) da pannello admin in AuthController
    //***** CONSTRUCTOR UTILIZZATO IN IMPORT EXCEL *****
    public Azienda(String email, String ragioneSociale, String indirizzo, String telefono, String telefono2,
                   String descrizioneTelefoni, String fax, String pec, String pIva, String codiceFiscale,
                   String legaleRappresentante, FormaGiuridica formaGiuridica, Associazione associazione,
                   Localita localita, Categoria categoria, Categoria attivitaPrincipale, Categoria attivitaSecondaria
                    ) {
        this.email = email;
        this.ragioneSociale = ragioneSociale;
        this.indirizzo = indirizzo;
        this.telefono = telefono;
        this.telefono2 = telefono2;
        this.descrizioneTelefoni = descrizioneTelefoni;
        this.fax = fax;
        this.pec = pec;
        this.pIva = pIva;
        this.codiceFiscale = codiceFiscale;
        this.legaleRappresentante = legaleRappresentante;
        this.formaGiuridica = formaGiuridica;
        this.associazione = associazione;
        this.localita = localita;
        this.categoria = categoria;
        this.attivitaPrincipale = attivitaPrincipale;
        this.attivitaSecondaria = attivitaSecondaria;
    }

    //***** CONSTRUCTOR UTILIZZATO PER SINGOLA AZIENDA, DENTRO AUTHCONTROLLER *****
    public Azienda(String ragioneSociale, String indirizzo, String telefono, String fax, String pec,
                   String pIva, String codiceFiscale
    ) {
        this.ragioneSociale = ragioneSociale;
        this.indirizzo = indirizzo;
        this.telefono = telefono;
        this.fax = fax;
        this.pec = pec;
        this.pIva = pIva;
        this.codiceFiscale = codiceFiscale;
    }

    public Azienda(int id, String ragioneSociale, String indirizzo, String email, String telefono) {
        this.id = id;
        this.ragioneSociale = ragioneSociale;
        this.indirizzo = indirizzo;
        this.email = email;
        this.telefono = telefono;
    }

    public Azienda(int id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "ragioneSociale")
    private String ragioneSociale;

    @Column(name = "indirizzo")
    private String indirizzo;

    @Column(name = "email")
    private String email;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "telefono2")
    private String telefono2;

    @Column(name = "descrizioneTelefoni")
    private String descrizioneTelefoni;

    @Column(name = "fax")
    private String fax;

    @Column(name = "pec")
    private String pec;

    @Column(name = "pIva")
    private String pIva;

    @Column(name = "codiceFiscale")
    private String codiceFiscale;

    @Column(name = "legaleRappresentante")
    private String legaleRappresentante;

    //Al momento la funzionalità "foto" è stata tolta dalle aziende
    //@Column(name = "foto")
    //private byte[] foto;

    //@Column(name="Price", columnDefinition="Decimal(10,2) default '100.00'")

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idFormaGiuridica", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private FormaGiuridica formaGiuridica;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idAssociazione", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Associazione associazione;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idLocalita", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Localita localita;

    //categoria, AttivitaPrincipale e attivitaSecondaria sono tutti collegati soltanto a Categoria
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCategoria", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idAttivitaPrincipale", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Categoria attivitaPrincipale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idAttivitaSecondaria", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Categoria attivitaSecondaria;

    @Column(name = "primoLogin")
    private Boolean primoLogin;

    @OneToMany(mappedBy = "azienda")
    private List<Annuncio> annunci;

    //Non owning side tra azienda e utente
    @OneToOne(mappedBy = "azienda")
    @JsonIgnore                         //Forse ha risolto l'infinite recursion su /azienda?
    private Utente utente;              //In questo modo da azienda va a pescare i dati dentro Utente
                                        //ed invia i dati ma viceversa di contro Utente automaticamente non
                                        //serializza ed invia i dati causando il loop di recursione (credo?).
    @OneToMany(mappedBy = "azienda")
    private List<InteresseUtenteAnnuncio> interesseUtenteAnnunci;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRagioneSociale() {
        return ragioneSociale;
    }

    public void setRagioneSociale(String ragioneSociale) {
        this.ragioneSociale = ragioneSociale;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
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

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    @JsonIgnore
    public List<InteresseUtenteAnnuncio> getInteresseUtenteAnnunci() {
        return interesseUtenteAnnunci;
    }

    public void setInteresseUtenteAnnunci(List<InteresseUtenteAnnuncio> interesseUtenteAnnunci) {
        this.interesseUtenteAnnunci = interesseUtenteAnnunci;
    }

    public String getTelefono2() {
        return telefono2;
    }
    public void setTelefono2(String telefono2) {
        this.telefono2 = telefono2;
    }
    public String getDescrizioneTelefoni() {
        return descrizioneTelefoni;
    }
    public void setDescrizioneTelefoni(String descrizioneTelefoni) {
        this.descrizioneTelefoni = descrizioneTelefoni;
    }
    public String getFax() {
        return fax;
    }
    public void setFax(String fax) {
        this.fax = fax;
    }
    public String getPec() {
        return pec;
    }
    public void setPec(String pec) {
        this.pec = pec;
    }
    public String getpIva() {
        return pIva;
    }
    public void setpIva(String pIva) {
        this.pIva = pIva;
    }
    public String getCodiceFiscale() {
        return codiceFiscale;
    }
    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }
    public String getLegaleRappresentante() {
        return legaleRappresentante;
    }
    public void setLegaleRappresentante(String legaleRappresentante) {
        this.legaleRappresentante = legaleRappresentante;
    }


    public String getFormaGiuridicaDescrizione(){
        return formaGiuridica.getDescrizione();
    }


    //@JsonIgnore
    public FormaGiuridica getFormaGiuridica() {
        return formaGiuridica;
    }
    //@JsonIgnore
    public void setFormaGiuridica(FormaGiuridica formaGiuridica) {
        this.formaGiuridica = formaGiuridica;
    }


    public String getAssociazioneDescrizione(){
        return associazione.getDescrizione();
    }


    //@JsonIgnore
    public Associazione getAssociazione() {
        return associazione;
    }
    //@JsonIgnore
    public void setAssociazione(Associazione associazione) {
        this.associazione = associazione;
    }


    public String getLocalitaDescrizione(){
        return localita.getDescrizione();
    }

    public int getLocalitaId() {
        return localita.getId();
    }

    public void setLocalitaId(int id) {
        this.localita.setId(id);
    }


    //@JsonIgnore
    public Localita getLocalita() {
        return localita;
    }
    //@JsonIgnore
    public void setLocalita(Localita localita) {
        this.localita = localita;
    }


    public String getCategoriaDescrizione(){
        return categoria.getDescrizione();
    }

    public int getCategoriaId() {
        return categoria.getId();
    }

    public void setCategoriaId(int id) {
        this.categoria.setId(id);
    }


    //@JsonIgnore
    public Categoria getCategoria() {
        return categoria;
    }
    //@JsonIgnore
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }


    public String getAttivitaPrincipaleDescrizione(){
        return attivitaPrincipale.getDescrizione();
    }


    //@JsonIgnore
    public Categoria getAttivitaPrincipale() {
        return attivitaPrincipale;
    }
    //@JsonIgnore
    public void setAttivitaPrincipale(Categoria attivitaPrincipale) {
        this.attivitaPrincipale = attivitaPrincipale;
    }


    public String getAttivitaSecondariaDescrizione(){
        return attivitaSecondaria.getDescrizione();
    }


    //@JsonIgnore
    public Categoria getAttivitaSecondaria() {
        return attivitaSecondaria;
    }
    //@JsonIgnore
    public void setAttivitaSecondaria(Categoria attivitaSecondaria) {
        this.attivitaSecondaria = attivitaSecondaria;
    }
    public Boolean getPrimoLogin() {
        return primoLogin;
    }
    public void setPrimoLogin(Boolean primoLogin) {
        this.primoLogin = primoLogin;
    }
    @JsonIgnore
    public List<Annuncio> getAnnunci() {
        return annunci;
    }
    @JsonIgnore
    public void setAnnunci(List<Annuncio> annunci) {
        this.annunci = annunci;
    }
}
