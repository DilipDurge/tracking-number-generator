package com.tracking.number.generator.controller;

import com.tracking.number.generator.dto.TrackingNumberResponse;
import com.tracking.number.generator.service.TrackingNumberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tracking")
@RequiredArgsConstructor
public class TrackingNumberController {

    private final TrackingNumberService service;

    @GetMapping("/next-tracking-number")
    public TrackingNumberResponse getTrackingNumber(
            @RequestParam String origin_country_id,
            @RequestParam String destination_country_id,
            @RequestParam String customer_id,
            @RequestParam Double weight,
            @RequestParam String order_created_at,
            @RequestParam String customer_name,
            @RequestParam String customer_slug){
        System.out.println("Enter getTrackingNumber");
        return service.generateTrackingNumber(origin_country_id, destination_country_id, customer_id, weight, order_created_at, customer_name, customer_slug);
    }
}

