package cloud.jnolasco.rabbitmq.notification.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(RabbitMQProperties.class)
public class RabbitMQConfig {

    private final RabbitMQProperties rabbitMQProperties;

    @Bean
    public Queue ingestorQueue() {
        return new Queue(rabbitMQProperties.queues().dataIngested());
    }

    @Bean
    public Exchange dataIngestedExchange() {
        return new FanoutExchange(rabbitMQProperties.exchanges().dataIngested());
    }

    @Bean
    public Binding fileUploadBinding(Queue ingestorQueue, FanoutExchange dataIngestedExchange) {
        return BindingBuilder.bind(ingestorQueue)
                .to(dataIngestedExchange);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        final var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitMQProperties.host(), rabbitMQProperties.port());
        connectionFactory.setUsername(rabbitMQProperties.username());
        connectionFactory.setPassword(rabbitMQProperties.password());
        return connectionFactory;
    }
}
