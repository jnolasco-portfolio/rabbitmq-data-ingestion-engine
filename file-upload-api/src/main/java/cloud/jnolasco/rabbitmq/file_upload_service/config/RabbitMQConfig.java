package cloud.jnolasco.rabbitmq.file_upload_service.config;

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
    public Queue ingestorQueue() {
        return new Queue(rabbitMQProperties.queues().ingestor());
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue(rabbitMQProperties.queues().notification());
    }

    // Spring beans for Exchange
    @Bean
    public Exchange fileUploadExchange() {
        return new FanoutExchange(rabbitMQProperties.exchanges().fileUpload());
    }

    // Spring beans for Bindings
    @Bean
    public Binding ingestorBinding(Queue ingestorQueue, FanoutExchange fileUploadExchange) {
        return BindingBuilder.bind(ingestorQueue)
                .to(fileUploadExchange);
    }

    @Bean
    public Binding notificationBinding(Queue notificationQueue, FanoutExchange fileUploadExchange)
    {
        return BindingBuilder.bind(notificationQueue)
                .to(fileUploadExchange);
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
