package com.example.swiftagent.core;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.InputStream;
import java.security.KeyStore;

@Configuration
public class SwiftWebClientConfig {

    @Value("${swift.api.base-url}")
    private String baseUrl;

    @Value("${swift.client.ssl.key-store-path}")
    private Resource keyStoreResource;

    @Value("${swift.client.ssl.key-store-password}")
    private String keyStorePassword;

    @Bean
    public WebClient.Builder swiftWebClientBuilder() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(createHttpClient()));
    }

    private HttpClient createHttpClient() {
        try {
            // Load KeyStore
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            try (InputStream is = keyStoreResource.getInputStream()) {
                keyStore.load(is, keyStorePassword.toCharArray());
            }

            // Init KeyManagerFactory
            KeyManagerFactory keyManagerFactory = KeyManagerFactory
                    .getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, keyStorePassword.toCharArray());

            // Init TrustManagerFactory (using default truststore for public CAs, or custom
            // if needed)
            // For now specific Swift truststore might be needed if they use private CA.
            // Assuming system default truststore logic for now + our client cert.

            SslContext sslContext = SslContextBuilder.forClient()
                    .keyManager(keyManagerFactory)
                    .build();

            return HttpClient.create()
                    .secure(sslContextSpec -> sslContextSpec.sslContext(sslContext));

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize SSL Context for Swift Client", e);
        }
    }
}
