package pt.jorgeduarte.concurrency_test.infra.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue dataQueue1() {
        return new Queue("dataQueue1", true);
    }

    @Bean
    public Queue dataQueue2() {
        return new Queue("dataQueue2", true);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange("exchange");
    }

    @Bean
    public Binding binding1(TopicExchange exchange, Queue dataQueue1) {
        return BindingBuilder.bind(dataQueue1).to(exchange).with("routing.key1");
    }

    @Bean
    public Binding binding2(TopicExchange exchange, Queue dataQueue2) {
        return BindingBuilder.bind(dataQueue2).to(exchange).with("routing.key2");
    }
}