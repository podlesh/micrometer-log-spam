package org.example.bugdemo;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Hooks;

import java.time.Duration;
import java.util.List;

/**
 *
 */
@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(DemoApplication.class);

    @Autowired
    private WebClient.Builder webClientBuilder;
    @Autowired
    private ObservationRegistry observationRegistry;

    @Override
    public void run(String... args) {
        final Observation observation = Observation.createNotStarted("test", observationRegistry);
        observation.observe(() -> {
            for (WebClient webClient : List.of(
                    webClientBuilder.build(),
                    webClientBuilder.observationRegistry(ObservationRegistry.NOOP).build()
            )) {
                final ResponseEntity<Void> response = webClient.mutate()
                        .baseUrl("http://www.github.com")
                        .build()
                        .get()
                        .retrieve()
                        .toBodilessEntity()
                        .block(Duration.ofSeconds(5));

                logger.info(String.format("response: %s%n", response));
            }
        });
        logger.info(String.format("observation: %s%n", observation));
        System.exit(0);
    }

    public static void main(String[] args) {
        Hooks.enableAutomaticContextPropagation();
        SpringApplication.run(DemoApplication.class, args);
    }
}
