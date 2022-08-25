package io.github.gilsonsilvati.msavaliadorcredito.domain.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RetornoAvaliacaoCliente {

    private List<CartaoAprovado> cartoes;
}
