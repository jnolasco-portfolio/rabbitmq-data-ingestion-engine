package cloud.jnolasco.rabbitmq.file_upload_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;


@Validated
@ConfigurationProperties(prefix = "rabbitmq")
public record RabbitMQProperties(String host, int port, String username, String password,
                                 RabbitMQExchanges exchanges) {
    public record RabbitMQExchanges(String fileUpload) {}
}
