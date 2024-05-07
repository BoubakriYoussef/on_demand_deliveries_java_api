package com.example.ondemand.service;

import com.example.ondemand.entities.User;
import com.example.ondemand.request.EstimationRequest.*;
import com.example.ondemand.entities.Estimation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EstimationService {

     Estimation createEstimation(NewEstimationRequest newEstimationRequest) throws JsonProcessingException, JsonMappingException ;

     void deleteEstimation(Long id);

     List<Estimation> getAllEstimationsByAuthenticatedUser(User user);

     Estimation updateEstimation(Long estimationId, NewEstimationRequest request) throws JsonProcessingException;

     Estimation updateForAcceptOrRefuseEstimation(Long estimationId, ManagerDecisionRequest decisionRequest);


     Optional<Estimation> findEstimationById(Long id);

     public double estimateDeliveryFee(NewEstimationRequest newEstimationRequest) throws JsonProcessingException ;

     public double calculateRoadDistance(NewEstimationRequest newEstimationRequest)throws JsonProcessingException, JsonMappingException;


     public LocalDateTime estimateDeliveryTime(NewEstimationRequest newEstimationRequest) throws JsonProcessingException;


     public LocalDateTime estimatePickUpTime(NewEstimationRequest newEstimationRequest) throws JsonProcessingException;

}
