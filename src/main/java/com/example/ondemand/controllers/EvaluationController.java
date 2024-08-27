package com.example.ondemand.controllers;


import com.example.ondemand.authentication.authService.AuthenticationService;
import com.example.ondemand.entities.Evaluation;
import com.example.ondemand.entities.User;
import com.example.ondemand.request.RateDeliveryRequest;
import com.example.ondemand.request.updateEvaluation.UpdateEvaluationRequest;
import com.example.ondemand.service.EvaluationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/evaluations")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class EvaluationController {

    @Autowired
    private EvaluationService evaluationService;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/rate-delivery/{deliveryId}")
    public ResponseEntity<String> rateDelivery(@PathVariable Long deliveryId, @RequestBody RateDeliveryRequest rateRequest) {
        try {
            User manager = authenticationService.getAuthenticatedUser(); // Récupérer le manager authentifié
            evaluationService.rateDelivery(deliveryId, rateRequest, manager);
            return ResponseEntity.ok("Delivery rated successfully.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to rate delivery.");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<List<Evaluation>> getUserEvaluations() {
        try {
            User authenticatedUser = authenticationService.getAuthenticatedUser();
            List<Evaluation> evaluations = evaluationService.getUserEvaluations(authenticatedUser);
            return ResponseEntity.ok(evaluations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Endpoint pour modifier une évaluation existante
    @PutMapping("/{id}")
    public ResponseEntity<Evaluation> updateEvaluation(@PathVariable("id") Long id, @RequestBody UpdateEvaluationRequest updateEvaluationRequest) {
        Evaluation updatedEvaluation = evaluationService.updateEvaluation(id, updateEvaluationRequest);
        return ResponseEntity.ok(updatedEvaluation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvaluation(@PathVariable("id") Long id) {
        evaluationService.deleteEvaluation(id);
        return ResponseEntity.noContent().build();
    }
}
