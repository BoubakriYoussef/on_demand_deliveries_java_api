package com.example.ondemand.service;

import com.example.ondemand.entities.User;
import com.example.ondemand.request.EstimationRequest.*;
import com.example.ondemand.entities.Estimation;
import com.example.ondemand.request.restaurantRequest.UpdateRestaurantRequest;

import java.util.List;
import java.util.Optional;

public interface EstimationService {

     Estimation createEstimation(NewEstimationRequest newEstimationReques);

     void deleteEstimation(Long id);

     List<Estimation> getAllEstimationsByAuthenticatedUser(User user);

     Estimation updateEstimation(Long estimationId, UpdateEstimationRequest request);


     Optional<Estimation> findEstimationById(Long id);



}
