package cloud.jnolasco.rabbitmq.ingestor.external;

import cloud.jnolasco.rabbitmq.common.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@FeignClient(name = "configuration-service")
@RequestMapping("/api/v1/configurations")
public interface ConfigurationClient {

    @GetMapping("/{jobId}")
    ResponseEntity<ApiResponse> getConfiguration(@PathVariable UUID jobId);
}
