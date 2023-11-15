/*package com.example.springpoliecobe.config;

import com.example.springpoliecobe.model.Ruolo;
import com.example.springpoliecobe.model.Utente;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class UtentePrincipale implements UserDetails {      //Classe che dovrebbe rappresentare l'utente attualmente
                                                            //loggato.
                                                            //Viene creato passandogli l'attuale utente come parametro
                                                            //in "CustomUserDetailsService".
    private Utente utente;
    private String password;

    public UtentePrincipale(Utente utente) {
        this.utente = utente;
    }   //Qua avviene il collegamento con l'utente.

    /*@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("USER"));
    }*/

    /*@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {        //Qua dovrebbe prendere il ruolo assegnato
        Collection<Ruolo> ruoli = utente.getRuoli();                        //all'utente presente nel DB ed aggiungerlo
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();       //ad "authorities".

        for (Ruolo ruolo : ruoli) {
            authorities.add(new SimpleGrantedAuthority(ruolo.getDescrizione()));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return utente.getPassword();
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override                           //Va cancellato qua l'override?
    public String getUsername() {
        return utente.getUsername();
    }

    public String getEmail() {
        return utente.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}*/
