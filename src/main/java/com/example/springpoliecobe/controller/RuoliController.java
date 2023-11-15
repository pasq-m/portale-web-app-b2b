package com.example.springpoliecobe.controller;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

import com.example.springpoliecobe.model.*;
import com.example.springpoliecobe.payload.request.LoginRequest;
import com.example.springpoliecobe.payload.request.SignupRequest;
import com.example.springpoliecobe.payload.response.JwtResponse;
import com.example.springpoliecobe.payload.response.MessageResponse;
import com.example.springpoliecobe.repository.*;
import com.example.springpoliecobe.security.jwt.JwtUtils;
import com.example.springpoliecobe.security.services.UserDetailsImpl;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:3000"}, maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class RuoliController {

    @Autowired
    RuoloRepository ruoloRepository;


    @GetMapping("/ruoli")
    public ResponseEntity<List<Ruolo>> getAllRuoli(@RequestParam(required = false) String descrizione) {
        try {
            List<Ruolo> ruoli = new ArrayList<Ruolo>();

            if (descrizione == null)
                ruoloRepository.findAll().forEach(ruoli::add);
            else
                ruoloRepository.findByDescrizioneContaining(descrizione).forEach(ruoli::add);

            if (ruoli.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(ruoli, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

