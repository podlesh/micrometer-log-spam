package org.example.bugdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

/**
 *
 */
@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    @Autowired
    private WebClient webClient;

    @Override
    public void run(String... args) {
        final ResponseEntity<Void> response = webClient.mutate()
                .baseUrl("http://www.github.com")
                .build()
                .get()
                .retrieve()
                .toBodilessEntity()
                .block(Duration.ofSeconds(5));
        System.out.printf("response: %s%n", response);
        System.exit(0);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(DemoApplication.class, args);
    }
}
