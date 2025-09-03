package cloud.jnolasco.rabbitmq.notification.service;

import cloud.jnolasco.rabbitmq.common.event.DataIngestedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class NotificationService {

    @RabbitListener(queues = {"${rabbitmq.queues.data-ingested}"})
    public void consume(DataIngestedEvent event) {
        log.info("FileUploadEvent received => {}", event);

        sendNotification(event);
    }

    private void sendNotification(DataIngestedEvent event) {
        log.info("Sending DEMO notification for jobId: %s".formatted(event));
    }

}
