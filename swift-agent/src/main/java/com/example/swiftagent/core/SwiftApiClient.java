package com.example.swiftagent.core;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class SwiftApiClient {

    private final WebClient.Builder startWebClientBuilder;

    /**
     * Performs a GET request to the specified path.
     * 
     * @param path Relative path (e.g. /gpi/v3/payments/...)
     * @return Raw JSON response as String (for simplicity in this phase)
     */
    public Mono<String> get(String path) {
        return startWebClientBuilder.build()
                .get()
                .uri(path)
                .header("Accept", "application/json")
                // Add common Swift headers here if needed, e.g. LAU, Date, etc.
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(e -> log.error("Error calling Swift API: {}", e.getMessage()));
    }

    public Mono<String> post(String path, String body) {
        return startWebClientBuilder.build()
                .post()
                .uri(path)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(e -> log.error("Error posting to Swift API: {}", e.getMessage()));
    }
}
