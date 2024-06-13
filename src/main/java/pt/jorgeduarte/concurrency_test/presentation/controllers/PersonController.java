package pt.jorgeduarte.concurrency_test.presentation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pt.jorgeduarte.concurrency_test.domain.entities.Person;
import pt.jorgeduarte.concurrency_test.domain.usecases.PersonUseCaseService;

import java.util.List;

import static org.hibernate.internal.util.collections.CollectionHelper.listOf;

@RestController
public class PersonController {

    @Autowired
    private PersonUseCaseService personUseCaseService;

    // create a get method example
    @GetMapping(value = "/replicateProblem")
    public String replicateProblem() throws InterruptedException {
        Person person1 = new Person();
        long personId = 1;
        person1.setId(personId);
        person1.setName("Concurrent Update 1");
        person1.setAge(25);


        // define thread list
        List<Thread> threads = listOf();

        // genrate more 10 threads
        for (int i = 0; i < 1000; i++) {
            int finalI = i;
            Thread thread = new Thread(() -> {
                try {
                    // Introduzindo um leve atraso para forçar a condição de corrida
                    //define random person2 age
                    person1.setAge((int) (Math.random() * 100));
                    //define random person2 name concating with thread number
                    person1.setName("Concurrent Update - Thread " + Thread.currentThread().getId());
                    personUseCaseService.processCreateUpdatePerson2(person1, finalI);
                } catch (Exception e) {
                    System.err.println("Thread Error: " + e.getMessage());
                    e.printStackTrace();
                }
            });
            threads.add(thread);
        }

        threads.forEach(Thread::start);

        // wait for all threads to finish
        for (Thread thread : threads) {
            thread.join();
        }


        return "Problem Replication Started";
    }


}
