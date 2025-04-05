# Spring Boot REST API Project

This project is a Spring Boot application that consumes a REST API.
The REST API server used for this project is available
at [spring-practice-restmvc](https://github.com/noobdevsam/spring-practice-restmvc).

## Prerequisites

- Java 21 or higher
- Maven 3.9.0 or higher
- IntelliJ IDEA or any other preferred IDE

## Run the REST API Server

```sh
git clone https://github.com/noobdevsam/spring-practice-restmvc.git

cd spring-practice-restmvc

mvn clean package -DskipTests

java -jar -Dspring.profiles.active=localdb target/spring-practice-restmvc-0.0.1-SNAPSHOT.jar
```
