package io.github.gilsonsilvati.msavaliadorcredito.application.controller;

import io.github.gilsonsilvati.msavaliadorcredito.application.exception.DadosClienteNotFoundException;
import io.github.gilsonsilvati.msavaliadorcredito.application.exception.ErroComunicacaoException;
import io.github.gilsonsilvati.msavaliadorcredito.application.exception.ErroSolicitacaoCartaoException;
import io.github.gilsonsilvati.msavaliadorcredito.domain.model.DadosAvaliacao;
import io.github.gilsonsilvati.msavaliadorcredito.domain.model.DadosSolicitacaoEmissaoCartao;
import io.github.gilsonsilvati.msavaliadorcredito.domain.service.AvaliadorCreditoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("avaliacoes-credito")
@RequiredArgsConstructor
@Slf4j
public class AvaliadorCreditoController {

    private final AvaliadorCreditoService avaliadorCreditoService;

    @GetMapping
    public String status() {
        log.info("Obtendo o status do microservice avaliador credito");

        return "OK!";
    }

    @GetMapping(value = "situacao-cliente", params = "cpf")
    public ResponseEntity<?> consultarSituacaoCliente(@RequestParam String cpf) {
        try {
            return ResponseEntity.ok(avaliadorCreditoService.obterSituacaoCliente(cpf));
        } catch (DadosClienteNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ErroComunicacaoException e) {
            return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> realizarAvaliacao(@RequestBody DadosAvaliacao dadosAvaliacao) {
        try {
            return ResponseEntity.ok(avaliadorCreditoService.realizarAvaliacao(dadosAvaliacao));
        } catch (DadosClienteNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ErroComunicacaoException e) {
            return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).body(e.getMessage());
        }
    }

    @PostMapping("solicitacoes-cartao")
    public ResponseEntity<?> solicitarCartao(@RequestBody DadosSolicitacaoEmissaoCartao dados) {
        try {
            var protocoloSolicitacaoCartao = avaliadorCreditoService.solicitarEmissaoCartao(dados);

            return ResponseEntity.ok(protocoloSolicitacaoCartao);
        } catch (ErroSolicitacaoCartaoException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
