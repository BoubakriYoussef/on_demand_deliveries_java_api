package com.example.ondemand.request.DeliveryRequest;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class DeliveryUpdateRequest {
    private double paymentAmount;
    private LocalDateTime paymentTime;
    private double totalValue;
    private boolean withdrawDone;
    private double tipAmount;
}
