package cloud.jnolasco.rabbitmq.ingestor.external;

import cloud.jnolasco.rabbitmq.common.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "configuration-service")
public interface ConfigurationClient {

    @GetMapping("/api/v1/configurations/{jobId}")
    ResponseEntity<ApiResponse> getConfiguration(@PathVariable UUID jobId);
}
