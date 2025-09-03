package cloud.jnolasco.rabbitmq.jobconfig.controller;

import cloud.jnolasco.rabbitmq.common.dto.ApiResponse;
import cloud.jnolasco.rabbitmq.common.dto.IngestionConfigurationResponse;
import cloud.jnolasco.rabbitmq.jobconfig.service.IngestionConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/configurations")
public class ConfigurationController {

    private final IngestionConfigurationService configurationService;

    @GetMapping("/{jobId}")
    public ResponseEntity<ApiResponse> getConfiguration(@PathVariable UUID jobId) {
        IngestionConfigurationResponse configuration = configurationService.getConfiguration(jobId);
        return ResponseEntity.ok(ApiResponse.success(configuration));
    }
}
