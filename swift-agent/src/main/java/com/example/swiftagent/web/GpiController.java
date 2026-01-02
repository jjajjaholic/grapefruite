package com.example.swiftagent.web;

import com.example.swiftagent.domain.client.Client;
import com.example.swiftagent.domain.client.ClientRepository;
import com.example.swiftagent.domain.gpi.GpiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequestMapping("/api/gpi")
@RequiredArgsConstructor
@Slf4j
public class GpiController {

    private final GpiService gpiService;
    private final ClientRepository clientRepository;

    @GetMapping("/status/{uetr}")
    public Mono<ResponseEntity<String>> checkPaymentStatus(
            @PathVariable String uetr,
            @RequestHeader(value = "X-Client-ID", required = false) String clientId,
            ServerHttpRequest request) {

        String ipAddress = request.getRemoteAddress().getAddress().getHostAddress();
        log.info("Received request from IP: {}", ipAddress);

        // Validate Client (Note: JPA is blocking. In a real WebFlux app, use R2DBC or
        // wrap in Mono.fromCallable)
        // For this hybrid verification, we'll wrap it briefly or accept blocking for
        // now as ClientRepo is standard JPA.
        return Mono.fromCallable(() -> {
            Optional<Client> clientOpt;
            if (clientId != null) {
                clientOpt = clientRepository.findById(clientId);
            } else {
                clientOpt = clientRepository.findByIpAddress(ipAddress);
            }
            return clientOpt;
        }).flatMap(clientOpt -> {
            if (clientOpt.isEmpty()) {
                log.warn("Unauthorized access attempt from IP: {} ClientID: {}", ipAddress, clientId);
                return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized Client"));
            }

            Client client = clientOpt.get();
            log.info("Authorized Client: {} ({})", client.getClientId(), client.getBic());

            // Delegate to GPI Service
            try {
                gpiService.checkPaymentStatus(uetr);
                return Mono.just(ResponseEntity.ok("Request forwarded to Swift for UETR: " + uetr));
            } catch (Exception e) {
                return Mono
                        .just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing request"));
            }
        });
    }
}
