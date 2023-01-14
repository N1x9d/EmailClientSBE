package com.example.demo.RabbitMQ;

public interface RabbitMQProducerService {

  void sendMessage(String message, String routingKey);
}
