package io.github.gilsonsilvati.mscartoes.application.representation;

import io.github.gilsonsilvati.mscartoes.domain.model.Bandeira;
import io.github.gilsonsilvati.mscartoes.domain.model.Cartao;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartaoRequest {

    private String nome;
    private Bandeira bandeira;
    private BigDecimal renda;
    private BigDecimal limite;

    public Cartao toModel() {
        return new Cartao(nome, bandeira, renda, limite);
    }
}
