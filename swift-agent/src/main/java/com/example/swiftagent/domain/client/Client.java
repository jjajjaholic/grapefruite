package com.example.swiftagent.domain.client;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "TB_SWIFT_CLIENT")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    @Id
    @Column(name = "CLIENT_ID", length = 50)
    private String clientId;

    @Column(name = "BIC", length = 11, nullable = false)
    private String bic;

    @Column(name = "IP_ADDRESS", length = 50)
    private String ipAddress;

    @Column(name = "REGISTERED_DATE")
    private LocalDateTime registeredDate;

    @Column(name = "API_KEY", length = 100)
    private String apiKey;

    @Column(name = "SERVICE_TYPE", length = 50)
    private String serviceType;
}
