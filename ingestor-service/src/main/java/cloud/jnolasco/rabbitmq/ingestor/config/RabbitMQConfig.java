package cloud.jnolasco.rabbitmq.ingestor.config;

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
    // Spring beans for Queues
    @Bean
    public Queue reportGeneatorQueue() {
        return new Queue(rabbitMQProperties.queues().reportGenerator());
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue(rabbitMQProperties.queues().notification());
    }

    // Spring beans for Exchange
    @Bean
    public Exchange ingestionExchange() {
        return new FanoutExchange(rabbitMQProperties.exchanges().ingestion());
    }

    // Spring beans for Bindings
    @Bean
    public Binding reportGeneratorBinding(Queue reportGeneatorQueue, FanoutExchange ingestionExchange) {
        return BindingBuilder.bind(reportGeneatorQueue)
                .to(ingestionExchange);
    }

    @Bean
    public Binding notificationBinding(Queue notificationQueue, FanoutExchange ingestionExchange)
    {
        return BindingBuilder.bind(notificationQueue)
                .to(ingestionExchange);
    }

    // message converter
    @Bean
    public MessageConverter messageConverter()
    {
        return new Jackson2JsonMessageConverter();
    }

    // configure RabbitTemplate
    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory)
    {
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
