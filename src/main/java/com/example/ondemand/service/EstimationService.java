package com.example.ondemand.service;

import com.example.ondemand.entities.User;
import com.example.ondemand.request.EstimationRequest.*;
import com.example.ondemand.entities.Estimation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.apache.tomcat.util.json.ParseException;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EstimationService {

     Estimation createEstimation(NewEstimationRequest newEstimationRequest) throws IOException;

     void deleteEstimation(Long id);

     List<Estimation> getAllEstimationsByAuthenticatedUser(User user);

     Estimation updateEstimation(Long estimationId, NewEstimationRequest request) throws IOException;

     Estimation updateForAcceptOrRefuseEstimation(Long estimationId, ManagerDecisionRequest decisionRequest);


     Optional<Estimation> findEstimationById(Long id);

     public double estimateDeliveryFee(NewEstimationRequest newEstimationRequest) throws IOException, ParseException;

     public double calculateRoadDistance(NewEstimationRequest newEstimationRequest) throws IOException;


     public LocalDateTime estimateDeliveryTime(NewEstimationRequest newEstimationRequest) throws IOException;


     public LocalDateTime estimatePickUpTime(NewEstimationRequest newEstimationRequest) throws IOException;

}
