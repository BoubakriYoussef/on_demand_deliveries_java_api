package com.example.ondemand.service;

import com.example.ondemand.request.DeliveryRequest.DriverDecisionRequest;

public interface ProposedDeliveryService {

    public void proposeDeliveryToDriver(Long deliveryId);

    public void acceptOrRejectProposedDelivery(Long proposedDeliveryId, DriverDecisionRequest driverDecisionRequest);
}
