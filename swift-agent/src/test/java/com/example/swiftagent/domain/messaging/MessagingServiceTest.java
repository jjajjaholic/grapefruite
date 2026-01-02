package com.example.swiftagent.domain.messaging;

import com.example.swiftagent.core.SwiftApiClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessagingServiceTest {

    @Mock
    private SwiftApiClient swiftApiClient;

    @InjectMocks
    private MessagingService messagingService;

    @Test
    void testGetDistributions() {
        // Mock response from Swift API (Real structure based on Alliance Cloud v2)
        String mockResponse = """
                {
                  "distributions": [
                    {
                      "id": 44984189498,
                      "service": "fin",
                      "type": "transmissionReport",
                      "copy": false,
                      "message_cloud_reference": "68f78910-6484-4c20-b73f-8424a20b8e23",
                      "links": [
                        { "href": "https://api.swiftnet.sipn.swift.com/alliancecloud/v2/distributions/44984189498", "rel": "self", "type": "GET" }
                      ]
                    }
                  ],
                  "links": [
                    { "href": "https://api.swiftnet.sipn.swift.com/alliancecloud/v2/distributions?limit=50,offset=100", "rel": "self", "type": "GET" }
                  ]
                }
                """;
        when(swiftApiClient.get(anyString())).thenReturn(Mono.just(mockResponse));

        // Use StepVerifier to test Reactive stream
        StepVerifier.create(messagingService.getDistributions())
                .expectNext(mockResponse)
                .verifyComplete();
    }
}
