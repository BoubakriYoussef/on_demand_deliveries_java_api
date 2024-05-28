package com.example.ondemand.controllers;


import com.example.ondemand.entities.Delivery;
import com.example.ondemand.entities.User;
import com.example.ondemand.exceptions.UnauthorizedException;
import com.example.ondemand.repositories.DeliveryRepository;
import com.example.ondemand.request.DeliveryRequest.UpdateDeliveryStatusRequest;
import com.example.ondemand.request.rateRequest.RateUpdateRequest;
import com.example.ondemand.service.DeliveryService;
import com.example.ondemand.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.SecondaryTable;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {

    @Autowired
    DeliveryService deliveryService;

    @Autowired
    DeliveryRepository deliveryRepository;

    @Autowired
    UserService userService;

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
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }


    @GetMapping("/listDeliveries")
    public ResponseEntity<List<Delivery>> getDeliveriesByAuthenticatedUser(){
        Authentication authentifcation = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) authentifcation.getPrincipal()).getUsername();
        User user = userService.findByUsername(username);
        List<Delivery> deliveries = deliveryService.findDeliveriesByUserId(user.getId());
        return ResponseEntity.ok(deliveries);
    }


    @PutMapping("/{deliveryId}/updateStatus")
    public ResponseEntity<Delivery> updateDeliveryStatus(@PathVariable Long deliveryId, @RequestBody UpdateDeliveryStatusRequest updateDeliveryStatusRequest){
        Delivery updatedDelivery = deliveryService.updateDeliveryStatus(deliveryId, updateDeliveryStatusRequest);
        return ResponseEntity.ok(updatedDelivery);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateDelivery(@PathVariable Long id, @RequestBody RateUpdateRequest updateRequest) {
        deliveryService.updateDelivery(id, updateRequest);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> handleUnauthorizedException(UnauthorizedException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }
}
