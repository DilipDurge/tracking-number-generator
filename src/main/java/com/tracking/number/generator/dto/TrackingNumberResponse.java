package com.tracking.number.generator.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class TrackingNumberResponse {
    private String trackingNumber;
    private Instant createdAt;
    private UUID customerId;
    private Double weight;
    private String originCountryId;
    private String destinationCountryId;
    private String orderCreatedAt;
    private String customerName;
    private String customerSlug;
}

