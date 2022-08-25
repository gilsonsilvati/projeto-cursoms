package io.github.gilsonsilvati.mscartoes.application.resource;

import io.github.gilsonsilvati.mscartoes.application.representation.CartaoRequest;
import io.github.gilsonsilvati.mscartoes.application.representation.ClienteCartaoResponse;
import io.github.gilsonsilvati.mscartoes.domain.model.Cartao;
import io.github.gilsonsilvati.mscartoes.domain.service.CartaoService;
import io.github.gilsonsilvati.mscartoes.domain.service.ClienteCartaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("cartoes")
@RequiredArgsConstructor
@Slf4j
public class CartoesResource {

    private final CartaoService cartaoService;
    private final ClienteCartaoService clienteCartaoService;

    @GetMapping
    public String status() {
        log.info("Obtendo o status do microservice de cartoes");

        return "OK!";
    }

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody CartaoRequest request) {
        var cartao = cartaoService.salvar(request.toModel());

        var headerLocation = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(cartao.getId())
                .toUri();

        return ResponseEntity.created(headerLocation).build();
    }

    @GetMapping(params = "renda")
    public ResponseEntity<List<Cartao>> buscarComRendaAteh(@RequestParam Long renda) {
        var cartoes = cartaoService.buscarComRendaMenorIgual(renda);

        return ResponseEntity.ok(cartoes);
    }

    @GetMapping(params = "cpf")
    public ResponseEntity<List<ClienteCartaoResponse>> buscarPorCliente(@RequestParam String cpf) {
        var clienteCartoes = clienteCartaoService.cartoesPorCpf(cpf);

        var result = clienteCartoes.stream()
                .map(ClienteCartaoResponse::fromModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }
}
