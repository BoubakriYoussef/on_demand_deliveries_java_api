package com.example.ondemand.service;

import com.example.ondemand.entities.Evaluation;
import com.example.ondemand.entities.User;
import com.example.ondemand.request.RateDeliveryRequest;
import com.example.ondemand.request.updateEvaluation.UpdateEvaluationRequest;

import java.util.List;


public interface EvaluationService {


    List<Evaluation> getUserEvaluations(User authenticatedUser);


    List<Evaluation> findAllEvaluations();

    public void rateDelivery(Long deliveryId, RateDeliveryRequest rateRequest, User manager);

    public Evaluation updateEvaluation(Long id, UpdateEvaluationRequest updateEvaluationRequest);

    void deleteEvaluation(Long id);
}
