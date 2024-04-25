package com.example.ondemand.dto.EstimationRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    private double paymentAmount;
    private LocalDateTime paymentTime;
    private TipRequest tipRequest;

    private double totalValue; //Montant payé plus frais suppl comme Tip
    private boolean isWithDrawDone;
}
