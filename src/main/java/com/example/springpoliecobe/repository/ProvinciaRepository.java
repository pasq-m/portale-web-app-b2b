package com.example.springpoliecobe.repository;

import com.example.springpoliecobe.model.Provincia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProvinciaRepository extends JpaRepository<Provincia, Integer> {
    List<Provincia> findByCodiceContaining(String codice);
    Provincia findByCodiceIgnoreCase(String codice);
    boolean existsByCodiceContaining(String codice);
    boolean existsByCodiceIgnoreCase(String codice);

    Provincia findTopByOrderByIdDesc();

    //List<Provincia> findByTutorialId(Integer postId);
}