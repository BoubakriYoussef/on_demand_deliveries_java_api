package com.example.ondemand.service;

import com.example.ondemand.entities.ProposedDelivery;
import com.example.ondemand.entities.User;
import com.example.ondemand.request.DeliveryRequest.DriverDecisionRequest;

import java.util.List;

public interface ProposedDeliveryService {

    public void proposeDeliveryToDriver(Long deliveryId);

    public void acceptOrRejectProposedDelivery(Long proposedDeliveryId, DriverDecisionRequest driverDecisionRequest);

    public List<ProposedDelivery> getProposedDeliveriesForUser(User user);
}
