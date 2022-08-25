package io.github.gilsonsilvati.msclientes.application.resource;

import io.github.gilsonsilvati.msclientes.application.representation.ClienteRequest;
import io.github.gilsonsilvati.msclientes.domain.model.Cliente;
import io.github.gilsonsilvati.msclientes.domain.service.ClienteService;
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

@RestController
@RequestMapping("clientes")
@RequiredArgsConstructor
@Slf4j
public class ClienteResource {

    private final ClienteService service;

    @GetMapping
    public String status() {
        log.info("Obtendo o status do microservice de clientes");

        return "OK!";
    }

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody ClienteRequest request) {
        var cliente = service.salvar(request.toModel());

        var headerLocation = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .query("cpf={cpf}")
                .buildAndExpand(cliente.getCpf())
                .toUri();

        return ResponseEntity.created(headerLocation).build();
    }

    @GetMapping(params = "cpf")
    public ResponseEntity<Cliente> dadosCliente(@RequestParam("cpf") String cpf) {
        return service.buscarPorCPF(cpf)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
