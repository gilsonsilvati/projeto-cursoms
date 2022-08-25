package io.github.gilsonsilvati.msavaliadorcredito.domain.service;

import feign.FeignException.FeignClientException;
import io.github.gilsonsilvati.msavaliadorcredito.application.exception.DadosClienteNotFoundException;
import io.github.gilsonsilvati.msavaliadorcredito.application.exception.ErroComunicacaoException;
import io.github.gilsonsilvati.msavaliadorcredito.application.exception.ErroSolicitacaoCartaoException;
import io.github.gilsonsilvati.msavaliadorcredito.domain.model.Cartao;
import io.github.gilsonsilvati.msavaliadorcredito.domain.model.CartaoAprovado;
import io.github.gilsonsilvati.msavaliadorcredito.domain.model.CartaoCliente;
import io.github.gilsonsilvati.msavaliadorcredito.domain.model.DadosAvaliacao;
import io.github.gilsonsilvati.msavaliadorcredito.domain.model.DadosCliente;
import io.github.gilsonsilvati.msavaliadorcredito.domain.model.DadosSolicitacaoEmissaoCartao;
import io.github.gilsonsilvati.msavaliadorcredito.domain.model.ProtocoloSolicitacaoCartao;
import io.github.gilsonsilvati.msavaliadorcredito.domain.model.RetornoAvaliacaoCliente;
import io.github.gilsonsilvati.msavaliadorcredito.domain.model.SituacaoCliente;
import io.github.gilsonsilvati.msavaliadorcredito.infra.clients.CartoesResourceClient;
import io.github.gilsonsilvati.msavaliadorcredito.infra.clients.ClienteResourceClient;
import io.github.gilsonsilvati.msavaliadorcredito.infra.mq.SolicitacaoEmissaoCartaoPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    private final ClienteResourceClient clienteResourceClient;
    private final CartoesResourceClient cartoesResourceClient;
    private final SolicitacaoEmissaoCartaoPublisher emissaoCartaoPublisher;

    public SituacaoCliente obterSituacaoCliente(String cpf) throws DadosClienteNotFoundException, ErroComunicacaoException {

        try {
            ResponseEntity<DadosCliente> dadosClienteResponse = getDadosClienteResponse(cpf);
            ResponseEntity<List<CartaoCliente>> cartoesClienteResponse = cartoesResourceClient.buscarPorCliente(cpf);

            return SituacaoCliente.builder()
                    .cliente(dadosClienteResponse.getBody())
                    .cartoes(cartoesClienteResponse.getBody())
                    .build();
        } catch (FeignClientException e) {
            buildException(e);
        }

        return null;
    }

    public RetornoAvaliacaoCliente realizarAvaliacao(DadosAvaliacao dadosAvaliacao) throws DadosClienteNotFoundException, ErroComunicacaoException {

        try {
            var dadosClienteResponse = getDadosClienteResponse(dadosAvaliacao.getCpf());
            var cartoesResponse = cartoesResourceClient.buscarComRendaAteh(dadosAvaliacao.getRenda());

            List<Cartao> cartoes = cartoesResponse.getBody();
            List<CartaoAprovado> cartoesAprovados = cartoes.stream()
                    .map(cartao -> {
                        var limiteBasico = cartao.getLimiteBasico();
                        var idade = BigDecimal.valueOf(dadosClienteResponse.getBody().getIdade());
                        var fator = idade.divide(BigDecimal.TEN);
                        var limiteAprovado = fator.multiply(limiteBasico);

                        var cartaoAprovado = CartaoAprovado.builder()
                                .cartao(cartao.getNome())
                                .bandeira(cartao.getBandeira())
                                .limiteAprovado(limiteAprovado)
                                .build();

                        return cartaoAprovado;
                    })
                    .collect(Collectors.toList());

            return RetornoAvaliacaoCliente.builder()
                    .cartoes(cartoesAprovados)
                    .build();
        } catch (FeignClientException e) {
            buildException(e);
        }

        return null;
    }

    public ProtocoloSolicitacaoCartao solicitarEmissaoCartao(DadosSolicitacaoEmissaoCartao dados) {

        try {
            emissaoCartaoPublisher.solicitarCartao(dados);

            var protocolo = UUID.randomUUID().toString();

            return new ProtocoloSolicitacaoCartao(protocolo);
        } catch (Exception e) {
            throw new ErroSolicitacaoCartaoException(e.getMessage());
        }

    }

    private ResponseEntity<DadosCliente> getDadosClienteResponse(String cpf) {
        return clienteResourceClient.dadosCliente(cpf);
    }

    private static void buildException(FeignClientException e) throws DadosClienteNotFoundException, ErroComunicacaoException {
        var status = e.status();

        if (HttpStatus.NOT_FOUND.value() == status) {
            throw new DadosClienteNotFoundException();
        }

        throw new ErroComunicacaoException(e.getMessage(), status);
    }
}
