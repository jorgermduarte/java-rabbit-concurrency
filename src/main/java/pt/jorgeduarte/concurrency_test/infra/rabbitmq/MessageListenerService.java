package pt.jorgeduarte.concurrency_test.infra.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import pt.jorgeduarte.concurrency_test.domain.entities.Person;
import pt.jorgeduarte.concurrency_test.domain.usecases.PersonUseCaseService;

@Service
public class MessageListenerService {

    private final ObjectMapper objectMapper;
    private final PersonUseCaseService personUseCaseService;

    @Autowired
    public MessageListenerService(ObjectMapper objectMapper, PersonUseCaseService personUseCaseService) {
        this.objectMapper = objectMapper;
        this.personUseCaseService = personUseCaseService;
    }

    @RabbitListener(queues = "dataQueue2")
    public void receiveMessageFromQueue2(String message) {
        try {
            Person person = objectMapper.readValue(message, Person.class);
            personUseCaseService.processCreateUpdatePerson2(person,0);
        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
        }
    }
}

