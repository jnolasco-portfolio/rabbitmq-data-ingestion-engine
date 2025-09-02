package cloud.jnolasco.rabbitmq.common.event;


import lombok.Builder;

import java.util.UUID;

@Builder
public record DataIngestedEvent(UUID id, UUID jobId, String originalName) {
}
