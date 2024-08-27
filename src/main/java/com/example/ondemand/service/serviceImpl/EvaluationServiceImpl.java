package com.example.ondemand.service.serviceImpl;

import com.example.ondemand.authentication.authService.AuthenticationService;
import com.example.ondemand.entities.Delivery;
import com.example.ondemand.entities.Evaluation;
import com.example.ondemand.entities.User;
import com.example.ondemand.enumClass.Status;
import com.example.ondemand.repositories.DeliveryRepository;
import com.example.ondemand.repositories.EvaluationRepository;
import com.example.ondemand.repositories.UserRepository;
import com.example.ondemand.request.updateEvaluation.UpdateEvaluationRequest;
import com.example.ondemand.service.EvaluationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.ondemand.request.RateDeliveryRequest;

import java.util.List;

@Service
public class EvaluationServiceImpl implements EvaluationService {

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    EvaluationRepository evaluationRepository;

    @Autowired
    DeliveryRepository deliveryRepository;

    @Autowired
    UserRepository userRepository;
    @Override
    public Evaluation updateEvaluation(Long evaluationId, UpdateEvaluationRequest updateEvaluationRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticationUsername = authentication.getName();
        User authenticatedUser = userRepository.findByEmail(authenticationUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Evaluation evaluation = evaluationRepository.findById(evaluationId)
                .orElseThrow(() -> new RuntimeException("Evaluation not found with ID: " + evaluationId));

        if (!evaluation.getUser().getId().equals(authenticatedUser.getId())) {
            throw new RuntimeException("You are not authorized to update this evaluation.");
        }

        evaluation.setRate(updateEvaluationRequest.getRate());
        evaluation.setComment(updateEvaluationRequest.getComment());
        evaluation.setEvaluationTime(updateEvaluationRequest.getEvaluationTime());

        return evaluationRepository.save(evaluation);
    }

    @Override
    public void deleteEvaluation(Long id) {
        evaluationRepository.deleteById(id);
    }


    public void rateDelivery(Long deliveryId, RateDeliveryRequest rateRequest, User manager) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new EntityNotFoundException("Delivery not found with id: " + deliveryId));

        if (!delivery.getStatus().equals(Status.DELIVERED)) {
            throw new IllegalStateException("Cannot rate delivery that is not delivered.");
        }

        Evaluation evaluation = new Evaluation();
        evaluation.setRate(rateRequest.getRating());
        evaluation.setComment(rateRequest.getComment());
        evaluation.setEvaluationTime(rateRequest.getEvaluationTime());
        evaluation.setDelivery(delivery);
        evaluation.setUser(manager);

        evaluationRepository.save(evaluation);
    }


    @Override
    public List<Evaluation> getUserEvaluations(User authenticatedUser) {
        return evaluationRepository.findAllByUser(authenticatedUser);
    }

    @Override
    public List<Evaluation> findAllEvaluations() {
        User authenticatedUser = authenticationService.getAuthenticatedUser();
        List<Evaluation> evaluations = evaluationRepository.findAllByUser(authenticatedUser);
        return evaluations;
    }
}
