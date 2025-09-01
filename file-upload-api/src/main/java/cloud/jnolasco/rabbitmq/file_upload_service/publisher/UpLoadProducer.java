package cloud.jnolasco.rabbitmq.file_upload_service.publisher;

import cloud.jnolasco.rabbitmq.file_upload_service.config.RabbitMQProperties;
import cloud.jnolasco.rabbitmq.file_upload_service.dto.FileUploadEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UpLoadProducer {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQProperties rabbitMQProperties;

    public void sendMessage(FileUploadEvent event)
    {
        log.info("FileUpload event sent: %s".formatted(event.toString()));
        rabbitTemplate.convertAndSend(rabbitMQProperties.exchanges().fileUpload(), "", event);
    }

}
