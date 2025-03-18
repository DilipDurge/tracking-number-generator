package com.tracking.number.generator.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tracking_numbers", uniqueConstraints = @UniqueConstraint(columnNames = "trackingNumber"))
public class TrackingNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String trackingNumber;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;
}

