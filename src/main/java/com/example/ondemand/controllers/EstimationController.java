package com.example.ondemand.controllers;


import com.example.ondemand.authentication.authService.AuthenticationService;
import com.example.ondemand.entities.User;
import com.example.ondemand.request.EstimationRequest.*;
import com.example.ondemand.entities.Estimation;
import com.example.ondemand.service.EstimationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/estimation")
public class EstimationController {


    @Autowired
    EstimationService estimationService;

    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/add")
    public ResponseEntity<Estimation> createEstimation(@RequestBody NewEstimationRequest request) throws IOException {
        Estimation estimation = estimationService.createEstimation(request);
        return ResponseEntity.ok(estimation);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEstimation(@PathVariable Long id) {
        estimationService.deleteEstimation(id);
        return ResponseEntity.ok("Estimation deleted successfully");
    }

    @GetMapping("/me/estimations")
    public ResponseEntity<List<Estimation>> getAllEstimationsByAuthenticatedUser(Long id) {
        User authenticatedUser = authenticationService.getAuthenticatedUser();
        List<Estimation> estimations = estimationService.getAllEstimationsByAuthenticatedUser(authenticatedUser);
        return new ResponseEntity<>(estimations, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Estimation> updateEstimation(Long id, @RequestBody NewEstimationRequest request) throws IOException {
        Estimation updatedEstimation = estimationService.updateEstimation(id, request);
        return new ResponseEntity<>(updatedEstimation, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public Optional<Estimation> getEstimationById(@PathVariable Long id){
        return estimationService.findEstimationById(id);
    }


    // Endpoint for updating estimation status after manager decision
    @PutMapping("/{estimationId}/acceptOrRefuse")
    public ResponseEntity<Estimation> updateEstimationForAcceptOrRefuse(
            @PathVariable Long estimationId,
            @RequestBody ManagerDecisionRequest decisionRequest) {
        Estimation updatedEstimation = estimationService.updateForAcceptOrRefuseEstimation(estimationId, decisionRequest);
        return ResponseEntity.ok(updatedEstimation);
    }


    //For testing purpose
    @GetMapping("/calculate")
    public ResponseEntity<?> calculateRoadDistance(@RequestBody NewEstimationRequest newEstimationRequest) {
        try {
            double distance = estimationService.calculateRoadDistance(newEstimationRequest);
            return ResponseEntity.ok("Distance: " + distance + " km");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing JSON");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    //For testing purpose
    @GetMapping("/estimate-delivery-fee")
    public ResponseEntity<Double> estimateDeliveryFee(@RequestBody NewEstimationRequest newEstimationRequest) {
        try {
            double estimatedFee = estimationService.estimateDeliveryFee(newEstimationRequest);
            return ResponseEntity.ok(estimatedFee);
        } catch (JsonProcessingException e) {
            // Gérer l'erreur de façon appropriée, par exemple, en renvoyant une réponse HTTP avec un code d'erreur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


    //For testing purpose
    @PostMapping("/estimateDeliveryTime")
    public ResponseEntity<LocalDateTime> estimateDeliveryTime(@RequestBody NewEstimationRequest newEstimationRequest) {
        try {
            LocalDateTime estimatedDeliveryTime = estimationService.estimateDeliveryTime(newEstimationRequest);
            return new ResponseEntity<>(estimatedDeliveryTime, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //For testing purpose
    @PostMapping("/estimatePickUpTime")
    public ResponseEntity<LocalDateTime> estimatePickUpTime(@RequestBody NewEstimationRequest newEstimationRequest) {
        try {
            LocalDateTime estimatedDeliveryTime = estimationService.estimatePickUpTime(newEstimationRequest);
            return new ResponseEntity<>(estimatedDeliveryTime, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

