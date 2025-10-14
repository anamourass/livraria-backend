package com.livraria.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Categoria {
    DRAMA("Drama"),
    FICCAO("Ficção"),
    TERROR("Terror"),
    ROMANCE("Romance"),
    BIOGRAFIA("Biografia");

    private final String valor;

    Categoria(String valor) {
        this.valor = valor;
    }

    @JsonValue
    public String getValor() {
        return valor;
    }

    @JsonCreator
    public static Categoria fromValor(String valor) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.valor.equalsIgnoreCase(valor)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Categoria inválida: " + valor);
    }
}