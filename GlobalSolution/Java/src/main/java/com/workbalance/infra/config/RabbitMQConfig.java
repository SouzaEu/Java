package com.workbalance.infra.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ configuration for asynchronous messaging
 * Implements messaging requirement with async queues
 */
@Configuration
@EnableRabbit
public class RabbitMQConfig {

    public static final String RECOMMENDATIONS_QUEUE = "workbalance.recommendations.queue";
    public static final String RECOMMENDATIONS_EXCHANGE = "workbalance.recommendations.exchange";
    public static final String RECOMMENDATIONS_ROUTING_KEY = "workbalance.recommendations.routing";

    public static final String NOTIFICATIONS_QUEUE = "workbalance.notifications.queue";
    public static final String NOTIFICATIONS_EXCHANGE = "workbalance.notifications.exchange";
    public static final String NOTIFICATIONS_ROUTING_KEY = "workbalance.notifications.routing";

    @Bean
    public Queue recommendationsQueue() {
        return QueueBuilder.durable(RECOMMENDATIONS_QUEUE)
                .withArgument("x-message-ttl", 3600000) // 1 hour TTL
                .build();
    }

    @Bean
    public Queue notificationsQueue() {
        return QueueBuilder.durable(NOTIFICATIONS_QUEUE)
                .withArgument("x-message-ttl", 3600000)
                .build();
    }

    @Bean
    public DirectExchange recommendationsExchange() {
        return new DirectExchange(RECOMMENDATIONS_EXCHANGE);
    }

    @Bean
    public DirectExchange notificationsExchange() {
        return new DirectExchange(NOTIFICATIONS_EXCHANGE);
    }

    @Bean
    public Binding recommendationsBinding(Queue recommendationsQueue, DirectExchange recommendationsExchange) {
        return BindingBuilder.bind(recommendationsQueue)
                .to(recommendationsExchange)
                .with(RECOMMENDATIONS_ROUTING_KEY);
    }

    @Bean
    public Binding notificationsBinding(Queue notificationsQueue, DirectExchange notificationsExchange) {
        return BindingBuilder.bind(notificationsQueue)
                .to(notificationsExchange)
                .with(NOTIFICATIONS_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
