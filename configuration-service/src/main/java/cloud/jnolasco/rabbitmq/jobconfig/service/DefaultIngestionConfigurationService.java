package cloud.jnolasco.rabbitmq.jobconfig.service;

import cloud.jnolasco.rabbitmq.common.dto.IngestionConfigurationResponse;
import cloud.jnolasco.rabbitmq.jobconfig.entity.IngestionConfiguration;
import cloud.jnolasco.rabbitmq.jobconfig.exception.IngestionConfigurationNotFoundException;
import cloud.jnolasco.rabbitmq.jobconfig.repository.IngestionConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class DefaultIngestionConfigurationService implements IngestionConfigurationService {

    private final IngestionConfigurationRepository ingestionConfigurationRepository;

    @Override
    public IngestionConfigurationResponse getConfiguration(UUID jobId) {
        IngestionConfiguration configuration = ingestionConfigurationRepository.findById(jobId)
                .orElseThrow(() -> new IngestionConfigurationNotFoundException("Not found configuration for jobId=%s".formatted(jobId)));

        return convertToDto(configuration);
    }

    private IngestionConfigurationResponse convertToDto(IngestionConfiguration configuration) {
        return new IngestionConfigurationResponse(configuration.getJobId(), configuration.getTargetTable(), configuration.getMappingJson(), configuration.getCreatedAt(), configuration.getUpdatedAt(), configuration.isActive());
    }
}
