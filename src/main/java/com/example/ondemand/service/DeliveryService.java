package com.example.ondemand.service;

import com.example.ondemand.enumClass.Status;
import com.example.ondemand.request.DeliveryRequest.UpdateDeliveryPaymentRequest;
import com.example.ondemand.request.DeliveryRequest.DriverDecisionRequest;
import com.example.ondemand.request.DeliveryRequest.UpdateDeliveryStatusRequest;
import com.example.ondemand.request.rateRequest.RateUpdateRequest;

public interface DeliveryService {



    void updateRate (Long deliveryId, RateUpdateRequest updateRateRequest);

   // void assignDriverToDelivery(Long idDelivery);

    void acceptOrRefuseDelivery(DriverDecisionRequest decisionRequest);


    public void updateDeliveryStatus(Long deliveryId, UpdateDeliveryStatusRequest updateDeliveryStatusRequest);

    public void updateDeliveryStatus(Long deliveryId, Status status);

    public void assignDriverToDelivery(Long deliveryId, Long userId);
}
