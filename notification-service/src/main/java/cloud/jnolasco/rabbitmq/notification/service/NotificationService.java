package cloud.jnolasco.rabbitmq.notification.service;

import cloud.jnolasco.rabbitmq.common.event.DataIngestedEvent;
import cloud.jnolasco.rabbitmq.common.event.ReportGeneratedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class NotificationService {

    @RabbitListener(queues = {"${rabbitmq.queues.data-ingested}"})
    public void handleDataIngested(DataIngestedEvent event) {
        log.info("FileUpload event received => {}", event);

        sendNotification(event);
    }

    private void sendNotification(DataIngestedEvent event) {
        log.info("Sending DEMO notification for jobId: %s".formatted(event));
    }

    @RabbitListener(queues = {"${rabbitmq.queues.report-generated}"})
    public void handleReportGenerated(ReportGeneratedEvent event) {
        log.info("ReportGeneratedEvent event received => {}", event);
    }
}
