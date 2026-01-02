package com.example.swiftagent.web;

import com.example.swiftagent.domain.client.Client;
import com.example.swiftagent.domain.client.ClientRepository;
import com.example.swiftagent.domain.messaging.MessageRequest;
import com.example.swiftagent.domain.messaging.MessagingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequestMapping("/api/messaging")
@RequiredArgsConstructor
@Slf4j
public class MessagingController {

    private final MessagingService messagingService;
    private final ClientRepository clientRepository;

    @PostMapping("/send")
    public Mono<ResponseEntity<String>> sendMessage(
            @RequestBody MessageRequest messageRequest,
            @RequestHeader(value = "X-Client-ID", required = false) String clientId,
            ServerHttpRequest request) {

        String ipAddress = request.getRemoteAddress().getAddress().getHostAddress();
        log.info("Received message request from IP: {}", ipAddress);

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
                log.warn("Unauthorized attempt to send message from IP: {}", ipAddress);
                return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized Client"));
            }

            Client client = clientOpt.get();
            log.info("Authorized Client: {} sending message", client.getClientId());

            try {
                messagingService.sendMessage(messageRequest.getContent());
                return Mono.just(ResponseEntity.accepted().body("Message accepted for processing"));
            } catch (Exception e) {
                log.error("Error sending message", e);
                return Mono
                        .just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing message"));
            }
        });
    }
}
