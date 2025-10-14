package com.livraria.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Tamanho {
    PEQUENO("Pequeno"),
    MEDIO("Médio"),
    GRANDE("Grande");

    private final String valor;

    Tamanho(String valor) {
        this.valor = valor;
    }

    @JsonValue
    public String getValor() {
        return valor;
    }

    @JsonCreator
    public static Tamanho fromValor(String valor) {
        for (Tamanho tamanho : Tamanho.values()) {
            if (tamanho.valor.equalsIgnoreCase(valor)) {
                return tamanho;
            }
        }
        throw new IllegalArgumentException("Tamanho inválido: " + valor);
    }
}