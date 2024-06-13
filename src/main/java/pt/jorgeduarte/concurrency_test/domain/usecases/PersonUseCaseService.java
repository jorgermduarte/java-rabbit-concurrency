package pt.jorgeduarte.concurrency_test.domain.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.jorgeduarte.concurrency_test.domain.entities.Person;
import pt.jorgeduarte.concurrency_test.infra.repositories.PersonRepository;

@Service
public class PersonUseCaseService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonUseCaseService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public void processCreateUpdatePerson(Person person) {
        try{
            if (person.getId() != null) {
                Person existingPerson = personRepository.findById(person.getId()).orElse(null);
                if (existingPerson != null) {
                    existingPerson.setName(person.getName());
                    existingPerson.setAge(person.getAge());
                    personRepository.save(existingPerson);
                } else {
                    // Handle the case where the person does not exist
                    System.err.println("Person not found with ID (method1): " + person.getId());
                }
            }else{
                personRepository.save(person);
            }
        }catch (Exception ex){
            System.err.println("Error processing message: " + ex.getMessage());
        }
    }
    @Transactional
    public void processCreateUpdatePerson2(Person person, int sleepTime) {
        try{
            if (person.getId() != null) {
                Person existingPerson = personRepository.findById(person.getId()).orElse(null);
                if (existingPerson != null) {
                    existingPerson.setName(person.getName());
                    existingPerson.setAge(person.getAge());
                    Thread.sleep(sleepTime);
                    personRepository.save(existingPerson);
                } else {
                    // Handle the case where the person does not exist
                    System.err.println("Person not found with ID (method1): " + person.getId());
                }
            }else{
                personRepository.save(person);
            }
        }catch (Exception ex){
            System.err.println("Error processing message: " + ex.getMessage());
        }
    }
}
