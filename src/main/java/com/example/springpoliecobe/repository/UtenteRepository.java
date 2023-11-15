package com.example.springpoliecobe.repository;

import com.example.springpoliecobe.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Integer> {

    //Utente findByUsername(String username);

    //Boolean existsByEmail(String email);        //JpaRepository provides a way to use dynamic methods correctly defining
                                                //the name of these method using prefixes like "existsBy" or
                                                //"findBy" followed by the name of the field of the entity given (in
                                                //this case "Utente").

                                                //This way, JpaRepository can check the data inside the DB.

    //Optional<Utente> findByUsernameOrEmail(String username, String email);


    //Serve direttamente l'id azienda da prendere tramite utente quindi findByIdAzienda o simile
    Optional<Utente> findByIdAzienda(int id);
    Optional<Utente> findByUsername(String username);
    List<Utente> findByUsernameContaining(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Utente findTopByOrderByIdDesc();    //Trova l'ultimo elemento aggiunto nella tabella del DB

    //@Query(value="select * from ruoli_utenti a where a.utente_id = :utenteId", nativeQuery=true)
    //Utente findById(@Param("utenteId")int utenteId);

}