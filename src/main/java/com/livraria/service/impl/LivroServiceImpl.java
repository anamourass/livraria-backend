package com.livraria.service.impl;

import com.livraria.model.Livro;
import com.livraria.repository.LivroRepository;
import com.livraria.service.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LivroServiceImpl implements LivroService {

    private final LivroRepository livroRepository;

    @Autowired
    public LivroServiceImpl(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    @Override
    public List<Livro> findAll() {
        return livroRepository.findAll();
    }

    @Override
    public Optional<Livro> findById(Long id) {
        return livroRepository.findById(id);
    }

    @Override
    public Livro save(Livro livro) {
        return livroRepository.save(livro);
    }

    @Override
    public Livro update(Long id, Livro livroDetails) {
        return livroRepository.findById(id)
                .map(livro -> {
                    livro.setTitulo(livroDetails.getTitulo());
                    livro.setAutor(livroDetails.getAutor());
                    livro.setTexto(livroDetails.getTexto());
                    livro.setCategoria(livroDetails.getCategoria());
                    livro.setTamanho(livroDetails.getTamanho());
                    return livroRepository.save(livro);
                })
                .orElse(null);
    }

    @Override
    public void delete(Long id) {
        livroRepository.deleteById(id);
    }
}