package cloud.jnolasco.rabbitmq.report_generator.service;

import cloud.jnolasco.rabbitmq.common.event.DataIngestedEvent;
import cloud.jnolasco.rabbitmq.common.event.ReportGeneratedEvent;
import cloud.jnolasco.rabbitmq.report_generator.publisher.ReportGeneratorProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class ReportGeneratorService {

    private final ReportGeneratorProducer reportGeneratorProducer;

    @RabbitListener(queues = {"${rabbitmq.queues.data-ingested}"})
    public void consume(DataIngestedEvent event) {
        log.info("DataIngested event received => {}", event);

        generateReport(event);
    }

    private void generateReport(DataIngestedEvent event) {
        log.info("Simulating DEMO Report generation for %s".formatted(event));

        ReportGeneratedEvent reportGeneratedEvent = ReportGeneratedEvent.builder()
                .id(UUID.randomUUID())
                .jobId(event.jobId())
                .originalName(event.originalName())
                .build();

        reportGeneratorProducer.sendMessage(reportGeneratedEvent);
    }

}
