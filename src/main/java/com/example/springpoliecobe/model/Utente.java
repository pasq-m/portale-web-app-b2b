package com.example.springpoliecobe.model;

import jakarta.persistence.*;

import java.util.Collection;

@Entity
@Table(name = "utenti")
public class Utente {

    public Utente() {

    }

    public Utente(Azienda azienda) {
        this.azienda = azienda;
    }

    public Utente(int id, String email, String username, String password) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public Utente(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    //Constructor per la creazione e aggiunta di una nuova azienda all'utente appena creato
    public Utente(int id, String username, String email, String password, Azienda azienda, Collection<Ruolo> ruoli) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.azienda = azienda;
        this.ruoli = ruoli;

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //private Long id;
    private int id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    //@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "ruoli_utenti",
            joinColumns = @JoinColumn(name = "utente_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "ruolo_id", referencedColumnName = "id")
    )
    //private Set<Ruolo> ruoli;
    private Collection<Ruolo> ruoli;    //La collection deve comprendere due dati, utente id e ruolo id e questi
    //private Set<Ruolo> ruoli = new HashSet<>();     //vengono forniti dalla query in AuthController
                                        //"ruoloRepository.findByDescrizione("default")", che restituisce un oggetto
                                        //di tipo Ruolo con due valori (è un array in pratica) cioè id e descrizione
                                        //del row selezionato in tabella (in questo caso selezionato byDescrizione("default")).
                                        //Tramite "Arrays.AsList(ruolo)" restituiamo una collezione che viene settata
                                        //sul SetRuoli dell'utente (che appunto accetta collezioni).
                                        //Ora, nella tabella "ruoli_utenti" avremo una coppia di id che rappresentano
                                        //il ruolo e l'utente assegnato a tale ruolo.

    //Versione template
    //private Set<Ruolo> ruoli = new HashSet<>();

    //Owning side tra utente e azienda
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "idAzienda", referencedColumnName = "id")
    private Azienda azienda;

    //In questo modo riesco ad andare a prendere l'id dentro azienda tramite utente,
    //(vedere sotto getIdAzienda()) ed evitare il doppio mapping sulla colonna "idAzienda".
    //Per fare questo ho dovuto specificare questo "@Column...etc".
    //In questo modo non da errore perché trova il nome della colonna corretto ("idAzienda") ed evitiamo
    //il multiple mapping.
    @Column(insertable=false, updatable=false)
    private int idAzienda;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<Ruolo> getRuoli() {
        return ruoli;
    }

    public void setRuoli(Collection<Ruolo> ruoli) {
        this.ruoli = ruoli;
    }
    //public void setRuoli(Set<Ruolo> ruoli) {
        //this.ruoli = ruoli;
    //}

    public Azienda getAzienda() {
        return azienda;
    }

    public void setAzienda(Azienda azienda) {
        this.azienda = azienda;
    }

    public int getIdAzienda() {
        idAzienda = azienda.getId();
        return idAzienda;
    }

    /*public void setAziendaId(int id) {
        aziendaId = this.azienda.setId(id)
        this.azienda.setId(id);
    }*/
}
