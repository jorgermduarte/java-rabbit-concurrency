# Project README

## Introduction

After encountering an error where different queues/messages were processing and changing the same row/table simultaneously or with open transactions, it was suggested by Pavel that this could be the cause. Based on this report and the errors on Sentry, I created an isolated scenario to test the problem.

## Solution Base

The solution includes the following components:

- **Docker Compose**:
    - RabbitMQ
    - Rabbit queue seeder, creating 500 persons in an H2 database.
- **Controller Endpoint**:
    - To replicate the problem, accessible at `("/replicateProblem")`.

## Executing the Application

Follow these steps to execute the project:

1. **Start Docker Compose**:
    - Run the following command to build and start the containers in detached mode:
      ```sh
      docker-compose up --build -d
      ```
2. **Execute the Application**:
    - Start your Spring Boot application.

3. **Replicate the Problem**:
    - Access the endpoint to replicate the problem:
      ```
      http://localhost:8080/replicateProblem
      ```

4. **View Row Changes**:
    - Check the table in the H2 database. The specific row should have `version: 10`, indicating 10 saves/updates ()

## Explanation of the Problem

### What is Happening?

Different threads or pods are updating the same rows simultaneously. In a scenario where different queues and messages end up updating a specific row in a table in a multi-pod/thread environment, this can cause an error similar to:

