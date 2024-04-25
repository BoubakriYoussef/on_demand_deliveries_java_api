package com.example.ondemand.service;

import com.example.ondemand.dto.EstimationRequest.*;
import com.example.ondemand.entities.Estimation;

public interface EstimationService {

     Estimation createEstimation(NewEstimationRequest newEstimationRequest,AddressRequest addressRequest,
                                 CustomerRequest customerRequest,
                                 OrderRequest orderRequest,
                                 DeliveryRequest deliveryRequest,
                                 PaymentRequest paymentRequest,
                                 TipRequest tipRequest, String pricingStrategyName);
}
