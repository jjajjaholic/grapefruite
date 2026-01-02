package com.example.swiftagent;

import com.example.swiftagent.domain.gpi.GpiService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SwiftAgentApplication {

	public static void main(String[] args) {
		SpringApplication.run(SwiftAgentApplication.class, args);
	}

	@Bean
	CommandLineRunner run(GpiService gpiService) {
		return args -> {
			System.out.println("Starting Swift Agent...");
			// This is just a placeholder to verify the bean is loaded
			// In a real scenario, we might trigger a periodic check or wait for API requests
			System.out.println("GPI Service loaded: " + gpiService);
		};
	}
}
