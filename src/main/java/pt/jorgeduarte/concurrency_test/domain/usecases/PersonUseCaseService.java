package pt.jorgeduarte.concurrency_test.domain.usecases;

import jakarta.persistence.OptimisticLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.jorgeduarte.concurrency_test.domain.entities.Person;
import pt.jorgeduarte.concurrency_test.infra.repositories.PersonRepository;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class PersonUseCaseService {

    private final PersonRepository personRepository;

    private final AtomicInteger totalRequests = new AtomicInteger(0);

    @Autowired
    public PersonUseCaseService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Transactional
    @Retryable(
            retryFor = {OptimisticLockException.class, ObjectOptimisticLockingFailureException.class },
            maxAttempts = 10
    )
    public void processCreateUpdatePerson2(Person person, int sleepTime)  throws InterruptedException {
        if (person.getId() != null) {
            Person existingPerson = personRepository.findById(person.getId()).orElseThrow(() -> new RuntimeException("Person not found"));
            existingPerson.setName(person.getName());
            existingPerson.setAge(person.getAge());
            // This is used to trigger different save times
            // and reproduce the "was updated or deleted by another transaction"
            Thread.sleep(sleepTime);
            int current = totalRequests.addAndGet(1);
            System.out.println("Saving person["+person.getName()+"]["+person.getAge()+"] times: " + current);
            personRepository.save(existingPerson);
        } else {
            personRepository.save(person);
        }
    }
}
