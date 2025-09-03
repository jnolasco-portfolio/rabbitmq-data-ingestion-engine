package cloud.jnolasco.rabbitmq.ingestor.service;

import cloud.jnolasco.rabbitmq.common.dto.ApiResponse;
import cloud.jnolasco.rabbitmq.common.event.DataIngestedEvent;
import cloud.jnolasco.rabbitmq.common.event.FileUploadEvent;
import cloud.jnolasco.rabbitmq.ingestor.config.IngestionProperties;
import cloud.jnolasco.rabbitmq.ingestor.external.ConfigurationClient;
import cloud.jnolasco.rabbitmq.ingestor.publisher.IngestorProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@EnableConfigurationProperties(IngestionProperties.class)
@RequiredArgsConstructor
@Service
@Slf4j
public class FileUploadService {

    private final ConfigurationClient configurationClient;
    private final DynamicCsvIngestor dynamicCsvIngestor;
    private final IngestionProperties ingestionProperties;
    private final IngestorProducer ingestorProducer;

    @RabbitListener(queues = {"${rabbitmq.queues.file-upload}"})
    public void consume(FileUploadEvent event) {
        log.info("FileUploadEvent received => {}", event);

        try {
            ResponseEntity<ApiResponse> response = configurationClient.getConfiguration(event.jobId());
            ApiResponse<?> apiResponse = response.getBody();

            if (apiResponse == null || apiResponse.data() == null) {
                log.error("Failed to retrieve valid configuration for jobId: {}. Response was empty or invalid.", event.jobId());
                return;
            }

            log.info("Configuration received: {}. Delegating to DynamicCsvIngestor.", apiResponse.data());
            dynamicCsvIngestor.ingest(event, (Map<String, Object>) apiResponse.data(), ingestionProperties.truncateOnLoad());
            DataIngestedEvent dataIngestedEvent = DataIngestedEvent.builder()
                    .id(event.id())
                    .jobId(event.jobId())
                    .originalName(event.originalName())
                    .build();
            ingestorProducer.sendMessage(dataIngestedEvent);
        } catch (Exception e) {
            log.error("An unexpected error occurred while processing event for jobId: {}", event.jobId(), e);
        }
    }

}
