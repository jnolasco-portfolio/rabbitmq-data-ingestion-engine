package cloud.jnolasco.rabbitmq.common.dto;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record IngestionConfigurationResponse(
        UUID jobId,
        String targetTable,
        Map<String, String> mappingJson,
        Instant createdAt,
        Instant updatedAt,
        boolean isActive
) {}