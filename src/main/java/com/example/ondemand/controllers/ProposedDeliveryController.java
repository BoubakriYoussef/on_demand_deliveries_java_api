package com.example.ondemand.controllers;


import com.example.ondemand.request.DeliveryRequest.DriverDecisionRequest;
import com.example.ondemand.service.ProposedDeliveryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/estimation")
public class ProposedDeliveryController {

    @Autowired
    private ProposedDeliveryService proposedDeliveryService;

    @PostMapping("/propose-delivery/{deliveryId}")
    public ResponseEntity<String> proposeDelivery(@PathVariable Long deliveryId) {
        try {
            proposedDeliveryService.proposeDeliveryToDriver(deliveryId);
            return ResponseEntity.ok("Delivery proposed successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to propose delivery.");
        }
    }

    @PostMapping("/driver-decision/{proposedDeliveryId}")
    public ResponseEntity<String> driverDecision(@PathVariable Long proposedDeliveryId, @RequestBody DriverDecisionRequest decisionRequest) {
        try {
            proposedDeliveryService.acceptOrRejectProposedDelivery(proposedDeliveryId, decisionRequest);
            return ResponseEntity.ok("Decision processed successfully.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Proposed delivery not found.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process decision.");
        }
    }
}