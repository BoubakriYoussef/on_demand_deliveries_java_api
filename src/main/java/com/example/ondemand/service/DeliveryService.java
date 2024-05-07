package com.example.ondemand.service;

import com.example.ondemand.entities.User;
import com.example.ondemand.request.DeliveryRequest.DeliveryUpdateRequest;
import com.example.ondemand.request.rateRequest.RateUpdateRequest;

import java.util.List;

public interface DeliveryService {

    void updateDeliveryDetails(Long deliveryId, DeliveryUpdateRequest updateRequest);

    void updateRate (Long deliveryId, RateUpdateRequest updateRateRequest);

    void assignDriverToDelivery(Long idDelivery);


}
