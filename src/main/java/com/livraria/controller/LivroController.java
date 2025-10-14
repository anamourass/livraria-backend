package com.livraria.controller;

import com.livraria.model.Livro;
import com.livraria.service.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/livros")
public class LivroController {

    @Autowired
    private LivroService livroService;

    @GetMapping
    public List<Livro> getAllLivros() {
        return livroService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Livro> getLivroById(@PathVariable Long id) {
        Optional<Livro> livro = livroService.findById(id);
        return livro.isPresent() ? ResponseEntity.ok(livro.get()) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public Livro createLivro(@RequestBody Livro livro) {
        return livroService.save(livro);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Livro> updateLivro(@PathVariable Long id, @RequestBody Livro livroDetails) {
        Livro updatedLivro = livroService.update(id, livroDetails);
        return updatedLivro != null ? ResponseEntity.ok(updatedLivro) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLivro(@PathVariable Long id) {
        Optional<Livro> livro = livroService.findById(id);
        if (livro.isPresent()) {
            livroService.delete(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}