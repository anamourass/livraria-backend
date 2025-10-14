package com.livraria.service;

import com.livraria.model.Livro;
import java.util.List;
import java.util.Optional;

public interface LivroService {
    List<Livro> findAll();
    Optional<Livro> findById(Long id);
    Livro save(Livro livro);
    Livro update(Long id, Livro livro);
    void delete(Long id);
}