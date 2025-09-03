package cloud.jnolasco.rabbitmq.ingestor.service;

import cloud.jnolasco.rabbitmq.common.dto.ApiResponse;
import cloud.jnolasco.rabbitmq.common.event.FileUploadEvent;
import cloud.jnolasco.rabbitmq.ingestor.external.ConfigurationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class FileUploadConsumerService {

    private final ConfigurationClient configurationClient;
    private final DynamicCsvIngestor dynamicCsvIngestor;
    private final boolean truncateOnLoad;

    public FileUploadConsumerService(ConfigurationClient configurationClient,
                                     DynamicCsvIngestor dynamicCsvIngestor,
                                     @Value("${app.ingestion.truncate-on-load:false}") boolean truncateOnLoad) {
        this.configurationClient = configurationClient;
        this.dynamicCsvIngestor = dynamicCsvIngestor;
        this.truncateOnLoad = truncateOnLoad;
    }

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
            dynamicCsvIngestor.ingest(event, (Map<String, Object>) apiResponse.data(), this.truncateOnLoad);

        } catch (Exception e) {
            log.error("An unexpected error occurred while processing event for jobId: {}", event.jobId(), e);
        }
    }
}
