/*package com.example.springpoliecobe.service;

import com.example.springpoliecobe.config.UtentePrincipale;
import com.example.springpoliecobe.repository.UtenteRepository;
import com.example.springpoliecobe.model.Utente;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;*/

/*@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {   //Per ottenere i dati dell'utente loggato.

    private UtenteRepository UtenteRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {

        Utente utente = UtenteRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not exists by Username or Email"));

        Set<GrantedAuthority> authorities = utente.getRuoli().stream()
                .map((ruolo) -> new SimpleGrantedAuthority(ruolo.getDescrizione()))
                .collect(Collectors.toSet());

        /*return new org.springframework.security.core.userdetails.User(
                usernameOrEmail,
                utente.getPassword(),
                authorities
        );*/

        //Assegna l'utente loggato come quello principale, cioè che sta navigando con autorizzazioni (ruoli e etc)
        /*return new UtentePrincipale(utente);
    }
}*/