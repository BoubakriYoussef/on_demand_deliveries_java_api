package com.example.ondemand.service;

import com.example.ondemand.entities.User;
import com.example.ondemand.request.EstimationRequest.*;
import com.example.ondemand.entities.Estimation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.apache.tomcat.util.json.ParseException;
import org.hibernate.query.Order;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EstimationService {

     Estimation createEstimation(NewEstimationRequest newEstimationRequest) throws IOException, ParseException;

     void deleteEstimation(Long id);

     List<Estimation> getAllEstimationsByAuthenticatedUser(User user);

     Estimation updateEstimation(Long estimationId, NewEstimationRequest request) throws IOException, ParseException;

     Estimation updateForAcceptOrRefuseEstimation(Long estimationId, ManagerDecisionRequest decisionRequest);


     Optional<Estimation> findEstimationById(Long id);

     public double estimateDeliveryFee(double customerLatitude,
                                       double customerLongitude,
                                       double restaurantLatitude,
                                       double restaurantLongitude) throws IOException, ParseException;

     public double calculateRoadDistance(double customerLatitude, double customerLongitude, double restaurantLatitude, double restaurantLongitude) throws IOException;


     public LocalDateTime estimateDeliveryTime(double customerLatitude,
                                               double customerLongitude,
                                               double restaurantLatitude,
                                               double restaurantLongitude,
                                               LocalDateTime orderTime,
                                               OrderType orderType,
                                               LocalDateTime requestedDeliveryTime) throws IOException;



     public LocalDateTime estimatePickUpTime (LocalDateTime requestedDeliveryTime,
                                              double customerLatitude,
                                              double customerLongitude,
                                              double restaurantLatitude,
                                              double restaurantLongitude,
                                              OrderType orderType) throws IOException ;

}
