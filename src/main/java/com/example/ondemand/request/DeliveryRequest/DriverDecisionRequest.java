package com.example.ondemand.request.DeliveryRequest;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class DriverDecisionRequest {
    private boolean accept;
}
