package com.example.springpoliecobe.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.springpoliecobe.model.Categoria;
import com.example.springpoliecobe.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:3000"}, maxAge = 3600)
@RestController
@RequestMapping("/api")

public class CategorieController {

    @Autowired
    CategoriaRepository categoriaRepository;

    @GetMapping("/categorie")
    public ResponseEntity<List<Categoria>> getAllCategorie(@RequestParam(required = false) String descrizione) {
        try {
            List<Categoria> categoria = new ArrayList<Categoria>();

            if (descrizione == null)
                categoriaRepository.findAll().forEach(categoria::add);
            else
                categoriaRepository.findByDescrizioneContaining(descrizione).forEach(categoria::add);

            if (categoria.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(categoria, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/categorie/{id}")
    public ResponseEntity<Categoria> getCategoriaById(@PathVariable("id") int id) {
        Optional<Categoria> categorieData = categoriaRepository.findById(id);

        if (categorieData.isPresent()) {
            return new ResponseEntity<>(categorieData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/add-categoria")
    public ResponseEntity<Categoria> createCategoria(@RequestBody Categoria categoria) {
        try {
            Categoria _categoria = categoriaRepository
                    //.save(new Annuncio(annuncio.getTitolo(), annuncio.getDescrizione(), false));
                    .save(new Categoria(categoria.getDescrizione()));
            return new ResponseEntity<>(_categoria, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update-categoria/{id}")
    public ResponseEntity<Categoria> updateCategoria(@PathVariable("id") int id, @RequestBody Categoria categoria) {
        Optional<Categoria> categorieData = categoriaRepository.findById(id);

        if (categorieData.isPresent()) {
            Categoria _categoria = categorieData.get();
            _categoria.setDescrizione(categoria.getDescrizione());
            return new ResponseEntity<>(categoriaRepository.save(_categoria), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // *** Commentati per sicurezza perché non utilizzati per ora ***

    /*
    @DeleteMapping("/categorie/{id}")
    public ResponseEntity<HttpStatus> deleteCategoria(@PathVariable("id") int id) {
        try {
            categoriaRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/categorie")
    public ResponseEntity<HttpStatus> deleteAllCategorie() {
        try {
            categoriaRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    */

}
