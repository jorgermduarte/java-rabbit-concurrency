package pt.jorgeduarte.concurrency_test.presentation.controllers;

import jakarta.persistence.OptimisticLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pt.jorgeduarte.concurrency_test.domain.entities.Person;
import pt.jorgeduarte.concurrency_test.domain.usecases.PersonUseCaseService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.hibernate.internal.util.collections.CollectionHelper.listOf;

@RestController
public class PersonController {

    @Autowired
    private PersonUseCaseService personUseCaseService;

    @GetMapping(value = "/replicateProblem")
    public String replicateProblem() throws InterruptedException {

        // define thread list
        List<Thread> threads = listOf();

        for (int i = 0; i < 10; i++) {
            int finalI = i;
            Thread thread = new Thread(() -> {
                Person person1 = new Person();
                long personId = 1;
                person1.setId(personId);
                try {
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

        for (Thread thread : threads) {
            thread.join();
        }

        return "Problem Replication Started";
    }
}
