package com.livraria.service;

import com.livraria.model.Livro;
import com.livraria.repository.LivroRepository;
import com.livraria.service.impl.LivroServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LivroServiceTest {

    @InjectMocks
    private LivroServiceImpl livroService;

    @Mock
    private LivroRepository livroRepository;

    private Livro livro;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        livro = new Livro();
        livro.setId(1L);
        livro.setTitulo("Test Book");
        livro.setAutor("Test Author");
        livro.setTexto("Test Description");
    }

    @Test
    void testFindAll() {
        when(livroRepository.findAll()).thenReturn(Arrays.asList(livro));

        List<Livro> livros = livroService.findAll();

        assertNotNull(livros);
        assertEquals(1, livros.size());
        assertEquals("Test Book", livros.get(0).getTitulo());
    }

    @Test
    void testFindById() {
        when(livroRepository.findById(1L)).thenReturn(Optional.of(livro));

        Optional<Livro> foundLivro = livroService.findById(1L);

        assertTrue(foundLivro.isPresent());
        assertEquals("Test Book", foundLivro.get().getTitulo());
    }

    @Test
    void testSave() {
        when(livroRepository.save(any(Livro.class))).thenReturn(livro);

        Livro savedLivro = livroService.save(livro);

        assertNotNull(savedLivro);
        assertEquals("Test Book", savedLivro.getTitulo());
    }

    @Test
    void testDelete() {
        doNothing().when(livroRepository).deleteById(1L);

        assertDoesNotThrow(() -> livroService.delete(1L));

        verify(livroRepository, times(1)).deleteById(1L);
    }
}