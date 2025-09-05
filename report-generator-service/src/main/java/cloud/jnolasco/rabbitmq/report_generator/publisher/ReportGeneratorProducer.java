package cloud.jnolasco.rabbitmq.report_generator.publisher;

import cloud.jnolasco.rabbitmq.common.event.ReportGeneratedEvent;
import cloud.jnolasco.rabbitmq.report_generator.config.RabbitMQProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReportGeneratorProducer {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQProperties rabbitMQProperties;

    public void sendMessage(ReportGeneratedEvent event)
    {
        log.info("ReportGeneratedEvent event sent: %s".formatted(event.toString()));
        rabbitTemplate.convertAndSend(rabbitMQProperties.exchanges().reportGenerated(),
                rabbitMQProperties.topics().reportGenerated(), event);
    }

}
