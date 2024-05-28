package com.example.ondemand.request.DeliveryRequest;


import com.example.ondemand.enumClass.Decision;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DriverDecisionRequest {
    private Decision decision;
}
