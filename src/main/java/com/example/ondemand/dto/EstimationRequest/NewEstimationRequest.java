package com.example.ondemand.dto.EstimationRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class NewEstimationRequest {
    private double distance;
    private double estimatedFee;
    private Duration estimatedDeliveryTime;
    private Duration estimatedPickUpTime;

    private RestaurantRequest restaurantRequest;

    private DeliveryRequest deliveryRequest;

    private String pricingStrategyName;
}