package io.github.gilsonsilvati.mscartoes.infra.mq;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.gilsonsilvati.mscartoes.domain.model.ClienteCartao;
import io.github.gilsonsilvati.mscartoes.domain.model.DadosSolicitacaoEmissaoCartao;
import io.github.gilsonsilvati.mscartoes.infra.repository.CartaoRepository;
import io.github.gilsonsilvati.mscartoes.infra.repository.ClienteCartaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmissaoCartaoSubscriber {

    private final CartaoRepository cartaoRepository;
    private final ClienteCartaoRepository clienteCartaoRepository;

    @RabbitListener(queues = { "${mq.queues.emissao-cartoes}" })
    public void receberSolicitacaoEmissao(@Payload String payload) {
        var mapper = new ObjectMapper();

        try {
            var dados = mapper.readValue(payload, DadosSolicitacaoEmissaoCartao.class);

            var cartao = cartaoRepository.findById(dados.getIdCartao()).orElseThrow();

            var clienteCartao = new ClienteCartao();
            clienteCartao.setCartao(cartao);
            clienteCartao.setCpf(dados.getCpf());
            clienteCartao.setLimite(dados.getLimiteLiberado());

            clienteCartaoRepository.save(clienteCartao);
        } catch (Exception e) {
            log.error("Erro ao receber solicitacao de emissao de cartao: {}", e.getMessage());
        }

    }
}
