package com.example.ondemand.controllers;


import com.example.ondemand.authentication.authService.AuthenticationService;
import com.example.ondemand.entities.User;
import com.example.ondemand.request.EstimationRequest.*;
import com.example.ondemand.entities.Estimation;
import com.example.ondemand.service.EstimationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Estimation> createEstimation(@RequestBody NewEstimationRequest request) throws JsonProcessingException {
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
    public ResponseEntity<Estimation> updateEstimation(Long id, @RequestBody NewEstimationRequest request) throws JsonProcessingException {
        Estimation updatedEstimation = estimationService.updateEstimation(id, request);
        return new ResponseEntity<>(updatedEstimation, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public Optional<Estimation> getEstimationById(@PathVariable Long id){
        return estimationService.findEstimationById(id);
    }
}
