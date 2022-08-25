package io.github.gilsonsilvati.msclientes.domain.service;

import io.github.gilsonsilvati.msclientes.domain.model.Cliente;
import io.github.gilsonsilvati.msclientes.infra.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository repository;

    @Transactional
    public Cliente salvar(Cliente cliente) {
        return repository.save(cliente);
    }

    public Optional<Cliente> buscarPorCPF(String cpf) {
        return repository.findByCpf(cpf);
    }
}
