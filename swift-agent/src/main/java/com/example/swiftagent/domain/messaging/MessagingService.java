package com.example.swiftagent.domain.messaging;

import com.example.swiftagent.core.SwiftApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessagingService {

    private final SwiftApiClient swiftApiClient;

    public void sendMessage(String messageContent) {
        log.info("Sending message via Swift Messaging API...");

        String msgType = determineMessageType(messageContent);
        log.info("Detected Message Type: {}", msgType);

        // Example logic: Construct JSON payload for Swift API
        // In reality, this would be a specific JSON structure required by Swift
        String payload = String.format("{\"message\": \"%s\", \"type\": \"%s\"}", messageContent, msgType);

        swiftApiClient.post("/messaging/v1/messages", payload)
                .subscribe(
                        response -> log.info("Message sent successfully. Response: {}", response),
                        error -> log.error("Failed to send message: {}", error.getMessage()));
    }

    private String determineMessageType(String content) {
        if (content == null)
            return "UNKNOWN";
        String trimmed = content.trim();
        if (trimmed.startsWith("{") && trimmed.contains(":")) {
            return "MT"; // Simplified FIN detection
        } else if (trimmed.startsWith("<")) {
            return "MX"; // XML/ISO 20022 detection
        }
        return "UNKNOWN";
    }

    public reactor.core.publisher.Flux<String> getDistributions() {
        return swiftApiClient.get("/alliancecloud/v2/distributions")
                .flatMapMany(response -> {
                    log.info("Retrieved distributions: {}", response);
                    return reactor.core.publisher.Flux.just(response);
                });
    }
}
