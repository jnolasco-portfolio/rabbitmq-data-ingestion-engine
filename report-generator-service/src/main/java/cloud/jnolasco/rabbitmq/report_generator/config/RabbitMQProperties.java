package cloud.jnolasco.rabbitmq.report_generator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;


@Validated
@ConfigurationProperties(prefix = "rabbitmq")
public record RabbitMQProperties(String host, int port, String username, String password,
                                 RabbitMQExchanges exchanges,
                                 RabbitMQQueues queues,
                                 RabbitMQTopics topics) {
    public record RabbitMQExchanges(String dataIngested, String reportGenerated) {}
    public record RabbitMQQueues(String dataIngested) {}
    public record RabbitMQTopics(String reportGenerated) {}
}
