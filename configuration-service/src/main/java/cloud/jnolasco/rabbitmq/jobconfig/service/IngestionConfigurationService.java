package cloud.jnolasco.rabbitmq.jobconfig.service;

import cloud.jnolasco.rabbitmq.common.dto.IngestionConfigurationResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

public interface IngestionConfigurationService {
    IngestionConfigurationResponse getConfiguration(UUID jobId);
}
