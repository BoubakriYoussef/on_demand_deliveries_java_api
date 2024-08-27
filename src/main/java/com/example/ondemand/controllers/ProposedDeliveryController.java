package com.example.ondemand.controllers;


import com.example.ondemand.authentication.authService.AuthenticationService;
import com.example.ondemand.entities.ProposedDelivery;
import com.example.ondemand.entities.User;
import com.example.ondemand.request.DeliveryRequest.DriverDecisionRequest;
import com.example.ondemand.service.ProposedDeliveryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estimation")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class ProposedDeliveryController {

    @Autowired
    private ProposedDeliveryService proposedDeliveryService;

    @Autowired
    private AuthenticationService authenticationService;

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

    @GetMapping("/proposedDeliveries/me")
    public ResponseEntity<List<ProposedDelivery>> getProposedDeliveriesByAuthenticatedUser(){
        try {
            User authenticatedUser = authenticationService.getAuthenticatedUser();
            List<ProposedDelivery> proposedDeliveries = proposedDeliveryService.getProposedDeliveriesForUser(authenticatedUser);
            return ResponseEntity.ok(proposedDeliveries);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}