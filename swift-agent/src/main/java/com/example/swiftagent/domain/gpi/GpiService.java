package com.example.swiftagent.domain.gpi;

import com.example.swiftagent.core.SwiftApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class GpiService {

    private final SwiftApiClient swiftApiClient;

    public void checkPaymentStatus(String uetr) {
        log.info("Checking payment status for UETR: {}", uetr);
        
        // Example endpoint pattern
        String path = "/gpi/v3/payments/" + uetr + "/status";
        
        swiftApiClient.get(path)
                .subscribe(
                        response -> log.info("GPI Response: {}", response),
                        error -> log.error("GPI Error: {}", error.getMessage())
                );
    }
}
