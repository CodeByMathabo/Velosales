package com.codebymathabo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication to tell Java to connect to the Database, and listen for API requests.
@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        // This command launches the full application ecosystem.
        SpringApplication.run(Main.class, args);
    }
}