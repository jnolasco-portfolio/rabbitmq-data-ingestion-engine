package cloud.jnolasco.rabbitmq.ingestor.service;

import cloud.jnolasco.rabbitmq.common.event.FileUploadEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FileUploadConsumerService {

    @RabbitListener(queues = {"${rabbitmq.queues.file-upload}"})
    public void consume(FileUploadEvent event) {
        log.info("FileUploadEvent received => %s".formatted(event));
    }
}
