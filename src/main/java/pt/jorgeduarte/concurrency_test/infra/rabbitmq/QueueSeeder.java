package pt.jorgeduarte.concurrency_test.infra.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;
import pt.jorgeduarte.concurrency_test.domain.entities.Person;

import java.util.Random;

@Component
public class QueueSeeder implements CommandLineRunner {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void sendPersonMessage(String queueName, Person person) throws JsonProcessingException {
        String personJson = objectMapper.writeValueAsString(person);
        rabbitTemplate.convertAndSend(queueName, personJson);
    }

    @Override
    public void run(String... args) throws Exception {
        for (long i = 1; i < 501; i++) {
            Person person = new Person();
            person.setName("Person " + i);
            person.setAge(new Random().nextInt(100));
            sendPersonMessage("dataQueue2", person);
        }
    }
}