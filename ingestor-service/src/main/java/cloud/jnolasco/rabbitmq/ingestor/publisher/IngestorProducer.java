package cloud.jnolasco.rabbitmq.ingestor.publisher;

import cloud.jnolasco.rabbitmq.common.event.DataIngestedEvent;
import cloud.jnolasco.rabbitmq.common.event.FileUploadEvent;
import cloud.jnolasco.rabbitmq.ingestor.config.RabbitMQProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class IngestorProducer {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQProperties rabbitMQProperties;

    public void sendMessage(DataIngestedEvent event)
    {
        log.info("DataIngested event sent: %s".formatted(event.toString()));
        rabbitTemplate.convertAndSend(rabbitMQProperties.exchanges().dataIngested(), "", event);
    }

}
