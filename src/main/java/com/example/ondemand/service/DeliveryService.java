package com.example.ondemand.service;

import com.example.ondemand.entities.Delivery;
import com.example.ondemand.enumClass.Status;
import com.example.ondemand.request.DeliveryRequest.UpdateDeliveryPaymentRequest;
import com.example.ondemand.request.DeliveryRequest.DriverDecisionRequest;
import com.example.ondemand.request.DeliveryRequest.UpdateDeliveryStatusRequest;
import com.example.ondemand.request.rateRequest.RateUpdateRequest;

import java.util.List;

public interface DeliveryService {



    void updateRate (Long deliveryId, RateUpdateRequest updateRateRequest);

   // void assignDriverToDelivery(Long idDelivery);


    public Delivery updateDeliveryStatus(Long deliveryId, UpdateDeliveryStatusRequest updateDeliveryStatusRequest);

    public void updateDeliveryStatus(Long deliveryId, Status status);

    public void assignDriverToDelivery(Long deliveryId, Long userId);

    List<Delivery> findDeliveriesByUserId(Long id);

    public void updateDelivery(Long deliveryId, RateUpdateRequest updateRequest);
}
