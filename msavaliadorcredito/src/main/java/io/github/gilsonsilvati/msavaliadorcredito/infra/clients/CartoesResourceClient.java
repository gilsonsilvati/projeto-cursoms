package io.github.gilsonsilvati.msavaliadorcredito.infra.clients;

import io.github.gilsonsilvati.msavaliadorcredito.domain.model.Cartao;
import io.github.gilsonsilvati.msavaliadorcredito.domain.model.CartaoCliente;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "mscartoes", path = "/cartoes")
public interface CartoesResourceClient {

    @GetMapping
    ResponseEntity<List<CartaoCliente>> buscarPorCliente(@RequestParam String cpf);

    @GetMapping
    ResponseEntity<List<Cartao>> buscarComRendaAteh(@RequestParam Long renda);
}
