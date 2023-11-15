package com.example.springpoliecobe.payload.request;


import java.util.List;
import java.util.Set;

import com.example.springpoliecobe.model.*;
import jakarta.validation.constraints.*;

public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private Set<String> ruolo;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    //Aggiungo anche i campi di Azienda per poter gestire tutto tramite singola classe wrapper
    //(ovvero questa "SignupRequest") nella richiesta JSON tramite @RequestBody

    private String ragioneSociale;

    private String indirizzo;

    private String telefono;

    private String telefono2;

    private String descrizioneTelefoni;

    private String fax;

    private String pec;

    private String pIva;

    private String codiceFiscale;

    private String legaleRappresentante;

    private FormaGiuridica formaGiuridica;

    private Associazione associazione;

    private Localita localita;

    private Categoria categoria;

    private Categoria attivitaPrincipale;

    private Categoria attivitaSecondaria;

    private Boolean primoLogin;

    private List<Annuncio> annunci;



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRuolo() {
        return this.ruolo;
    }

    public void setRuolo(Set<String> ruolo) {
        this.ruolo = ruolo;
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
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

    public FormaGiuridica getFormaGiuridica() {
        return formaGiuridica;
    }

    public void setFormaGiuridica(FormaGiuridica formaGiuridica) {
        this.formaGiuridica = formaGiuridica;
    }

    public Associazione getAssociazione() {
        return associazione;
    }

    public void setAssociazione(Associazione associazione) {
        this.associazione = associazione;
    }

    public Localita getLocalita() {
        return localita;
    }

    public void setLocalita(Localita localita) {
        this.localita = localita;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Categoria getAttivitaPrincipale() {
        return attivitaPrincipale;
    }

    public void setAttivitaPrincipale(Categoria attivitaPrincipale) {
        this.attivitaPrincipale = attivitaPrincipale;
    }

    public Categoria getAttivitaSecondaria() {
        return attivitaSecondaria;
    }

    public void setAttivitaSecondaria(Categoria attivitaSecondaria) {
        this.attivitaSecondaria = attivitaSecondaria;
    }

    public Boolean getPrimoLogin() {
        return primoLogin;
    }

    public void setPrimoLogin(Boolean primoLogin) {
        this.primoLogin = primoLogin;
    }

    public List<Annuncio> getAnnunci() {
        return annunci;
    }

    public void setAnnunci(List<Annuncio> annunci) {
        this.annunci = annunci;
    }
}