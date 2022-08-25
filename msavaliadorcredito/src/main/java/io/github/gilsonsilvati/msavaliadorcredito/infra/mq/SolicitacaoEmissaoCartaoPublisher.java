package io.github.gilsonsilvati.msavaliadorcredito.infra.mq;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.gilsonsilvati.msavaliadorcredito.domain.model.DadosSolicitacaoEmissaoCartao;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SolicitacaoEmissaoCartaoPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final Queue queue;

    public void solicitarCartao(DadosSolicitacaoEmissaoCartao dados) {
        var json = convertIntoJson(dados);

        rabbitTemplate.convertAndSend(queue.getName(), json);
    }

    @SneakyThrows
    private String convertIntoJson(DadosSolicitacaoEmissaoCartao dados) {
        var mapper = new ObjectMapper();
        var json = mapper.writeValueAsString(dados);

        return json;
    }
}
