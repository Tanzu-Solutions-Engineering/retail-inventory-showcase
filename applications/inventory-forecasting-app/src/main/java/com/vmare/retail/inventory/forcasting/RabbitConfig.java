package com.vmare.retail.inventory.forcasting;


import com.rabbitmq.stream.Environment;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionNameStrategy;
import org.springframework.amqp.rabbit.connection.ThreadChannelConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import static nyla.solutions.core.util.Organizer.toMap;

@Configuration
public class RabbitConfig {

    @Value("${spring.application.name}")
    private String applicationName;


    @Value("${spring.rabbitmq.username:guest}")
    private String username = "guest";

    @Value("${spring.rabbitmq.password:guest}")
    private String password   = "guest";

    @Value("${spring.rabbitmq.host:127.0.0.1}")
    private String hostname = "localhost";
    private String customerOrderForInventory = "manufacturing.customer.orders.inventory-order-app";
    private String customerOrderNewForInventory = "manufacturing.customer.orders.new.inventory-order-app";
    private String orderUpdateExchange= "manufacturing.orderUpdate";

    @Bean
    ConnectionNameStrategy connectionNameStrategy(){
        return (connectionFactory) -> applicationName;
    }

    @Bean("UpdateOrder")
    RabbitTemplate newOrderTemplate(MessageConverter messageConverter) throws URISyntaxException, NoSuchAlgorithmException, KeyManagementException {
        com.rabbitmq.client.ConnectionFactory rabbitFactory = new com.rabbitmq.client.ConnectionFactory();
        rabbitFactory.setClientProperties(toMap("connection_name",applicationName));
        rabbitFactory.setHost(hostname);
        rabbitFactory.setUsername(username);
        rabbitFactory.setPassword(password);
        var template = new RabbitTemplate(new ThreadChannelConnectionFactory(rabbitFactory));
        template.setExchange(orderUpdateExchange);
        template.setUsePublisherConnection(true);
        template.setMessageConverter(messageConverter);
        return template;
    }

    @Bean
    TopicExchange orderUpdateExchange()
    {
        return new TopicExchange(orderUpdateExchange);
    }

    @Bean
    public MessageConverter converter()
    {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    Environment rabbitStreamEnvironment() {

        var env = Environment.builder()
                .host(hostname)
                .username(username)
                .password(password)
                .clientProperty("id",applicationName)
                .build();

        env.streamCreator().stream(customerOrderNewForInventory).create();
        env.streamCreator().stream(customerOrderForInventory).create();

        return env;
    }

}