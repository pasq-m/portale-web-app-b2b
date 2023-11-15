package com.example.springpoliecobe.repository;

import com.example.springpoliecobe.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    List<Categoria> findByDescrizioneContaining(String descrizione);
    //Optional<Categoria> findByDescrizioneIgnoreCase(String descrizione);
    Categoria findByDescrizioneIgnoreCase(String descrizione);
    Categoria findTopByOrderByIdDesc();

    boolean existsByDescrizioneIgnoreCase(String descrizione);
}