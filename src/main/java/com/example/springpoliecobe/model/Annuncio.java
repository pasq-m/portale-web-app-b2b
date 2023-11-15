package com.example.springpoliecobe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//@Data
//@NoArgsConstructor
//@RequiredArgsConstructor
@Entity
@Table(name = "annunci")
public class Annuncio {

    public Annuncio() {

    }

    public Annuncio(String titolo, String descrizione, float quantita, byte[] foto, LocalDate dataDiScadenza,
                    Localita localita, Materiale materiale, UnitaDiMisura unitaDiMisura, Azienda azienda, Boolean chiuso) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.quantita = quantita;
        this.foto = foto;
        this.dataDiScadenza = dataDiScadenza;
        this.localita = localita;
        this.materiale = materiale;
        this.unitaDiMisura = unitaDiMisura;
        this.azienda = azienda;
        this.chiuso = chiuso;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titolo")
    private String titolo;

    @Column(name = "descrizione")
    private String descrizione;

    @Column(name = "quantita")
    private float quantita;

    @Lob                                        //Specifica che l'oggetto nel DB deve essere salvato come "Large object"
    //@JdbcTypeCode(Types.LONGVARBINARY)
    @Column(name = "foto", columnDefinition="longblob")
    private byte[] foto;

    @Column(name = "chiuso", columnDefinition = "boolean default false")
    private Boolean chiuso;                     //Ho dovuto usare la classe Boolean invece che il primitive type perché
                                                //così posso assegnargli il value null nella classe dove gira scheduled
    @Transient                                  //Denota un campo temporaneo che non necessita di colonna nella tabella
    private String fotoStringata;

    @Column(name = "dataDiScadenza")
    @DateTimeFormat(pattern = "yyyy-MM-dd")          //Converte la stringa che arriva dal FE nel formato giusto al volo
    private LocalDate dataDiScadenza;

    //private Azienda idAzienda;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idMateriale", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Materiale materiale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUnitaDiMisura", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UnitaDiMisura unitaDiMisura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idLocalita", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Localita localita;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idAzienda", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Azienda azienda;

    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "idUtenteEsterno", nullable = true)
    //@OnDelete(action = OnDeleteAction.CASCADE)
    //private UtenteEsterno utenteEsterno;

    //@ManyToMany(mappedBy = "annunci")
    //private List<UtenteEsterno> utentiEsterni;

    @OneToMany(mappedBy = "annuncio")
    private List<UtenteEsternoAnnuncio> utenteEsterno = new ArrayList<>();

    @OneToMany(mappedBy = "annuncio")
    private List<InteresseUtenteAnnuncio> interesseUtenteAnnunci;

    //Per prova codice per foto
    //private int count;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public float getQuantita() {
        return quantita;
    }

    public void setQuantita(float quantita) {
        this.quantita = quantita;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public Boolean getChiuso() {
        return chiuso;
    }

    public Boolean isChiuso() {
        return chiuso;
    }

    public void setChiuso(Boolean chiuso) {
        this.chiuso = chiuso;
    }

    public String getFotoStringata() {
        return fotoStringata;
    }

    public void setFotoStringata(String fotoStringata) {
        this.fotoStringata = fotoStringata;
    }

    public LocalDate getDataDiScadenza() {
        return dataDiScadenza;
    }

    public void setDataDiScadenza(LocalDate dataDiScadenza) {
        this.dataDiScadenza = dataDiScadenza;
    }

    public Materiale getMateriale() {
        return materiale;
    }

    public void setMateriale(Materiale materiale) {
        this.materiale = materiale;
    }

    public UnitaDiMisura getUnitaDiMisura() {
        return unitaDiMisura;
    }

    public void setUnitaDiMisura(UnitaDiMisura unitaDiMisura) {
        this.unitaDiMisura = unitaDiMisura;
    }

    public Localita getLocalita() {
        return localita;
    }

    public void setLocalita(Localita localita) {
        this.localita = localita;
    }

    public Azienda getAzienda() {
        return azienda;
    }

    public void setAzienda(Azienda azienda) {
        this.azienda = azienda;
    }

    //Prendiamo l'id azienda direttamente dalla tabella figlia collegata.
    public int getAziendaId() {
        return azienda.getId();
    }

    //Questo (nel set non dovrebbe servire?) @JsonIgnore previene l'infinite recursion sul GET annunci
    @JsonIgnore
    public List<UtenteEsternoAnnuncio> getUtentiEsterni() {
        return utenteEsterno;
    }

    //@JsonIgnore
    public void setUtentiEsterni(List<UtenteEsternoAnnuncio> utentiEsterni) {
        this.utenteEsterno = utentiEsterni;
    }

    @JsonIgnore
    public List<InteresseUtenteAnnuncio> getInteresseUtenteAnnunci() {
        return interesseUtenteAnnunci;
    }

    public void setInteresseUtenteAnnunci(List<InteresseUtenteAnnuncio> interesseUtenteAnnunci) {
        this.interesseUtenteAnnunci = interesseUtenteAnnunci;
    }

    /*public int getUtenteEsternoId() {
        return utenteEsterno.getId();
    }

    public UtenteEsterno getUtenteEsterno() {
        return utenteEsterno;
    }

    public void setUtenteEsterno(UtenteEsterno utenteEsterno) {
        this.utenteEsterno = utenteEsterno;
    }*/

    /*public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }*/

    //Add and remove methods <--- IN CASO DA CAPIRE SE USARLA QUA O SU UTENTE ESTERNO
    /*public void addTag(Tag tag) {
        PostTag postTag = new PostTag(this, tag);
        tags.add(postTag);
        tag.getPosts().add(postTag);
    }

    public void removeTag(Tag tag) {
        for (Iterator<PostTag> iterator = tags.iterator();
             iterator.hasNext(); ) {
            PostTag postTag = iterator.next();

            if (postTag.getPost().equals(this) &&
                    postTag.getTag().equals(tag)) {
                iterator.remove();
                postTag.getTag().getPosts().remove(postTag);
                postTag.setPost(null);
                postTag.setTag(null);
            }
        }
    }*/


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Annuncio)) return false;
        return id != -1 && id.equals(((Annuncio) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
