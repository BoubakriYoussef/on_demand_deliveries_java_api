package com.example.ondemand.controllers;


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

   /* @PutMapping("/{deliveryId}/payment")
    public ResponseEntity<String> updatePayment(@PathVariable Long deliveryId, @RequestBody UpdateDeliveryPaymentRequest updateRequest) {
        try {
            deliveryService.updateDeliveryDetails(deliveryId, updateRequest);
            return ResponseEntity.ok("Delivery details updated successfully.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }*/

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

    /*@PutMapping("/{deliveryId}/assign-driver")
    public ResponseEntity<String> assignDriverToDelivery(@PathVariable Long deliveryId) {
        try {
            deliveryService.assignDriverToDelivery(deliveryId);
            return ResponseEntity.ok("Driver assigned successfully to delivery.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to assign driver to delivery: " + e.getMessage());
        }
    }*/




}
