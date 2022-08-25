package io.github.gilsonsilvati.mscartoes.domain.service;

import io.github.gilsonsilvati.mscartoes.domain.model.ClienteCartao;
import io.github.gilsonsilvati.mscartoes.infra.repository.ClienteCartaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteCartaoService {

    private final ClienteCartaoRepository repository;

    public List<ClienteCartao> cartoesPorCpf(String cpf) {
        return repository.findByCpf(cpf);
    }
}
