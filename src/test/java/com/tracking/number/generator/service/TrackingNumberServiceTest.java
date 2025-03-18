package com.tracking.number.generator.service;

import com.tracking.number.generator.dto.TrackingNumberResponse;
import com.tracking.number.generator.model.TrackingNumber;
import com.tracking.number.generator.repository.TrackingNumberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrackingNumberServiceTest {

    @Mock
    private TrackingNumberRepository repository;

    @InjectMocks
    private TrackingNumberService service;

    private final String validOrigin = "US";
    private final String validDestination = "CA";
    private final String validCustomerId = "de619854-b59b-425e-9db4-943979e1bd49";
    private final Double validWeight = 1.234;
    private final String validOrderCreatedAt = "2023-03-18T12:00:00+00:00";
    private final String validCustomerName = "RedBox Logistics";
    private final String validCustomerSlug = "redbox-logistics";

    @BeforeEach
    void setup() {
        lenient().when(repository.save(any(TrackingNumber.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void generateTrackingNumber_Success() {
        TrackingNumberResponse response = service.generateTrackingNumber(
                validOrigin, validDestination, validCustomerId, validWeight, validOrderCreatedAt, validCustomerName, validCustomerSlug
        );

        assertNotNull(response);
        assertNotNull(response.getTrackingNumber());
        assertEquals(validOrigin, response.getOriginCountryId());
        assertEquals(validDestination, response.getDestinationCountryId());
        assertEquals(validCustomerId, response.getCustomerId().toString());
        assertEquals(validCustomerName, response.getCustomerName());
        assertEquals(validCustomerSlug, response.getCustomerSlug());
    }

    @Test
    void generateTrackingNumber_DuplicateTrackingNumber_ShouldThrowException() {
        when(repository.save(any(TrackingNumber.class))).thenThrow(DataIntegrityViolationException.class);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                service.generateTrackingNumber(validOrigin, validDestination, validCustomerId, validWeight, validOrderCreatedAt, validCustomerName, validCustomerSlug)
        );

        assertEquals("Duplicate tracking number detected. Please try again.", exception.getMessage());
    }

    @Test
    void generateTrackingNumber_InvalidOrigin_ShouldThrowException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                service.generateTrackingNumber("USA", validDestination, validCustomerId, validWeight, validOrderCreatedAt, validCustomerName, validCustomerSlug)
        );

        assertEquals("Invalid origin country ID. Must be in ISO 3166-1 alpha-2 format.", exception.getMessage());
    }

    @Test
    void generateTrackingNumber_InvalidDestination_ShouldThrowException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                service.generateTrackingNumber(validOrigin, "CAN", validCustomerId, validWeight, validOrderCreatedAt, validCustomerName, validCustomerSlug)
        );

        assertEquals("Invalid destination country ID. Must be in ISO 3166-1 alpha-2 format.", exception.getMessage());
    }

    @Test
    void generateTrackingNumber_InvalidCustomerId_ShouldThrowException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                service.generateTrackingNumber(validOrigin, validDestination, "123", validWeight, validOrderCreatedAt, validCustomerName, validCustomerSlug)
        );

        assertEquals("Invalid customer ID.", exception.getMessage());
    }

    @Test
    void generateTrackingNumber_InvalidWeight_ShouldThrowException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                service.generateTrackingNumber(validOrigin, validDestination, validCustomerId, -5.0, validOrderCreatedAt, validCustomerName, validCustomerSlug)
        );

        assertEquals("Invalid weight. Must be in kilograms, up to three decimal places.", exception.getMessage());
    }

    @Test
    void generateTrackingNumber_InvalidOrderCreatedAt_ShouldThrowException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                service.generateTrackingNumber(validOrigin, validDestination, validCustomerId, validWeight, "InvalidDate", validCustomerName, validCustomerSlug)
        );

        assertEquals("Invalid order_created_at format. Must be in RFC 3339 format.", exception.getMessage());
    }

    @Test
    void generateTrackingNumber_InvalidCustomerName_ShouldThrowException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                service.generateTrackingNumber(validOrigin, validDestination, validCustomerId, validWeight, validOrderCreatedAt, "", validCustomerSlug)
        );

        assertEquals("Invalid customer name.", exception.getMessage());
    }

    @Test
    void generateTrackingNumber_InvalidCustomerSlug_ShouldThrowException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                service.generateTrackingNumber(validOrigin, validDestination, validCustomerId, validWeight, validOrderCreatedAt, validCustomerName, "")
        );

        assertEquals("Invalid customer slug. Must be in slug-case/kebab-case.", exception.getMessage());
    }
}
