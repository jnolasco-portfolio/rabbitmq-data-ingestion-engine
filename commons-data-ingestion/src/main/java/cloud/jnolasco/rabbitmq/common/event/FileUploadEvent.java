package cloud.jnolasco.rabbitmq.common.event;


import lombok.Builder;

import java.util.UUID;

@Builder
public record FileUploadEvent(UUID id, UUID jobId, String originalName, String contentType, String filePath) {
}
