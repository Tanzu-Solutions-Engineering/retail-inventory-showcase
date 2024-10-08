package com.vmare.retail.inventory.consumer;


import com.vmware.retail.inventory.domain.StoreProductInventory;
import com.vmware.retail.inventory.service.transaction.TransactionDataService;
import lombok.extern.slf4j.Slf4j;
import nyla.solutions.core.patterns.integration.Publisher;
import org.springframework.amqp.rabbit.connection.ConnectionNameStrategy;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@ComponentScan(basePackageClasses = TransactionDataService.class)
public class RabbitConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${spring.rabbitmq.username:guest}")
    private String username = "guest";

    @Value("${spring.rabbitmq.password:guest}")
    private String password   = "guest";

    @Value("${spring.rabbitmq.host:127.0.0.1}")
    private String hostname = "localhost";

    @Bean
    ConnectionNameStrategy connectionNameStrategy(){
        return (connectionFactory) -> applicationName;
    }

    @Bean
    public MessageConverter converter()
    {
        return new Jackson2JsonMessageConverter();
    }



    @Bean
    Publisher<StoreProductInventory> storeProductInventoryPublisher(RabbitTemplate rabbitTemplate)
    {

        Publisher<StoreProductInventory> publisher =
                storeInventory -> {
                    log.info("SENDING updates for StoreProductInventory {}",storeInventory);
                    rabbitTemplate.convertAndSend("retail.storeProductInventory", storeInventory.getProductId(),storeInventory);
                };

        return publisher;
    }

}