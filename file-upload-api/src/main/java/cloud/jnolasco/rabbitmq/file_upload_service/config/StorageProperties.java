package cloud.jnolasco.rabbitmq.file_upload_service.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "storage")
public record StorageProperties(@NotBlank String path) {
}
