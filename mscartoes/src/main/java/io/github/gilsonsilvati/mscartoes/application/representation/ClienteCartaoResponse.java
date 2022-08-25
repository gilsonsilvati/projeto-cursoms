package io.github.gilsonsilvati.mscartoes.application.representation;

import io.github.gilsonsilvati.mscartoes.domain.model.ClienteCartao;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ClienteCartaoResponse {

    private String nome;
    private String bandeira;
    private BigDecimal limiteLiberado;

    public static ClienteCartaoResponse fromModel(ClienteCartao clienteCartao) {
        return ClienteCartaoResponse.builder()
                .nome(clienteCartao.getCartao().getNome())
                .bandeira(clienteCartao.getCartao().getBandeira().toString())
                .limiteLiberado(clienteCartao.getLimite())
                .build();
    }
}
