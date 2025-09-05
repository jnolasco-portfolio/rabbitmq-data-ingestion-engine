package cloud.jnolasco.rabbitmq.common.event;


import lombok.Builder;

import java.util.UUID;

@Builder
public record ReportGeneratedEvent(UUID id, UUID jobId, String originalName) {
}
