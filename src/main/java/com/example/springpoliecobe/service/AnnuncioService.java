package com.example.springpoliecobe.service;

import java.io.IOException;
import java.util.List;

import com.example.springpoliecobe.model.Annuncio;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AnnuncioService {

    public Annuncio getJson(String annuncio, List<MultipartFile> foto) {

        Annuncio userJson = new Annuncio();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            userJson = objectMapper.readValue(annuncio, Annuncio.class);
        } catch (IOException err) {
            System.out.printf("Error", err.toString());
        }

        int fileCount = foto.size();
        //userJson.setCount(fileCount);

        return userJson;

    }

}
