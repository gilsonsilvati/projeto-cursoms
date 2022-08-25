package io.github.gilsonsilvati.msclientes.application.representation;

import io.github.gilsonsilvati.msclientes.domain.model.Cliente;
import lombok.Data;

@Data
public class ClienteRequest {

    private String cpf;
    private String nome;
    private Integer idade;

    public Cliente toModel() {
        return new Cliente(cpf, nome, idade);
    }
}
