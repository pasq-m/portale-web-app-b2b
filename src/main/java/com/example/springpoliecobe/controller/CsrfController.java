package com.example.springpoliecobe.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:3000"}, maxAge = 3600)
@RestController
public class CsrfController {

    //Endpoint to access to get the first token (this endpoint must be accessible without restrictions)
    /*@GetMapping("/v1/csrf")
    public CsrfToken csrf(CsrfToken token) {
        return token;
    }*/
    @GetMapping("/v1/csrf")
    public void getCsrfToken(HttpServletRequest request) {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        csrfToken.getToken();
    }
}
