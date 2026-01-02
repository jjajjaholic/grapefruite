package com.example.swiftagent.domain.client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, String> {

    Optional<Client> findByIpAddress(String ipAddress);

    Optional<Client> findByApiKey(String apiKey);
}
