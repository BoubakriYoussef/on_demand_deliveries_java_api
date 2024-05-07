package com.example.ondemand.controllers;


import com.example.ondemand.request.DeliveryRequest.DeliveryUpdateRequest;
import com.example.ondemand.request.rateRequest.RateUpdateRequest;
import com.example.ondemand.service.DeliveryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/deliveries")
public class DeliveryController {

    @Autowired
    DeliveryService deliveryService;

    @PutMapping("/{deliveryId}")
    public ResponseEntity<String> updateDeliveryDetails(@PathVariable Long deliveryId, @RequestBody DeliveryUpdateRequest updateRequest) {
        try {
            deliveryService.updateDeliveryDetails(deliveryId, updateRequest);
            return ResponseEntity.ok("Delivery details updated successfully.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{deliveryId}/rate")
    public ResponseEntity<String> updateRate(@PathVariable Long deliveryId, @RequestBody RateUpdateRequest rateUpdateRequest) {
        try {
            deliveryService.updateRate(deliveryId, rateUpdateRequest);
            return ResponseEntity.ok("Rate updated successfully.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
