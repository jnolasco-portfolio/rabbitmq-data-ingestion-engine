package cloud.jnolasco.rabbitmq.csv_ingestor_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;


@Validated
@ConfigurationProperties(prefix = "rabbitmq")
public record RabbitMQProperties(String host, int port, String username, String password,
                                 RabbitMQExchanges exchanges, RabbitMQQueues queues) {
    public record RabbitMQExchanges(String ingestion) {}
    public record RabbitMQQueues(String reportGenerator, String notification) {}
}
