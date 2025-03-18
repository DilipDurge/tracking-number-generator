package com.tracking.number.generator.service;

import com.tracking.number.generator.dto.TrackingNumberResponse;
import com.tracking.number.generator.model.TrackingNumber;
import com.tracking.number.generator.repository.TrackingNumberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TrackingNumberService {

    private final TrackingNumberRepository repository;

    @Transactional
    public TrackingNumberResponse generateTrackingNumber(String origin, String destination, String customer_id, Double weight, String order_created_at, String customer_name, String customer_slug) {
        try {
            validateInputs(origin, destination, customer_id, weight, order_created_at, customer_name, customer_slug);

            String uniqueId = generateUniqueId(origin, destination, customer_id, order_created_at);
            UUID customerUUID = UUID.fromString(customer_id);

            TrackingNumber trackingNumber = TrackingNumber.builder()
                    .trackingNumber(uniqueId)
                    .createdAt(Instant.now())
                    .customerId(customerUUID)
                    .build();
            repository.save(trackingNumber);

            return TrackingNumberResponse.builder()
                    .trackingNumber(uniqueId)
                    .createdAt(Instant.now())
                    .customerId(UUID.fromString(customer_id))
                    .originCountryId(origin)
                    .destinationCountryId(destination)
                    .weight(weight)
                    .orderCreatedAt(order_created_at)
                    .customerName(customer_name)
                    .customerSlug(customer_slug)
                    .build();
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Duplicate tracking number detected. Please try again.");
        }
    }

    private void validateInputs(String origin, String destination, String customer_id, Double weight, String order_created_at, String customer_name, String customer_slug) {
        if (origin == null || origin.length() != 2) {
            throw new IllegalArgumentException("Invalid origin country ID. Must be in ISO 3166-1 alpha-2 format.");
        }
        if (destination == null || destination.length() != 2) {
            throw new IllegalArgumentException("Invalid destination country ID. Must be in ISO 3166-1 alpha-2 format.");
        }
        if (customer_id == null || customer_id.length() < 4) {
            throw new IllegalArgumentException("Invalid customer ID.");
        }
        try {
            UUID.fromString(customer_id);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid customer ID format. Must be a valid UUID.");
        }
        if (weight == null || weight <= 0) {
            throw new IllegalArgumentException("Invalid weight. Must be in kilograms, up to three decimal places.");
        }
        if (order_created_at == null || order_created_at.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid order creation date. Must be in RFC 3339 format.");
        }
        if (customer_name == null || customer_name.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid customer name.");
        }
        if (customer_slug == null || customer_slug.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid customer slug. Must be in slug-case/kebab-case.");
        }
    }

    private String generateUniqueId(String origin, String destination, String customerId, String order_created_at) {
        long epochSecond;
        try {
            Instant instant = ZonedDateTime.parse(order_created_at.trim().replaceAll("\\s", "+"), DateTimeFormatter.ISO_ZONED_DATE_TIME).toInstant();
            epochSecond = instant.getEpochSecond();
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid order_created_at format. Must be in RFC 3339 format.", ex);
        }

        return (origin + destination + customerId.substring(0, 6) + epochSecond % 1000000).toUpperCase().substring(0, 16);
    }
}
