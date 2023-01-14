package com.example.demo.RabbitMQ;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.*;

@Configuration
public class RabbitConf {


    @Bean
    public ConnectionFactory connectionFactory() {
      CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory("localhost");
      cachingConnectionFactory.setUsername("user");
      cachingConnectionFactory.setPassword("password");
      cachingConnectionFactory.setVirtualHost("cpp");
      return cachingConnectionFactory;
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
      return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
      return new RabbitTemplate(connectionFactory());
    }

    @Bean
    public Queue myQueue() {
      return new Queue("queue");
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange("NotifyNewMessage", true, false);
    }

    @Bean
    Binding binding(Queue queue, DirectExchange exchange) {
      return BindingBuilder.bind(queue).to(exchange).with("testRK");
    }
}
