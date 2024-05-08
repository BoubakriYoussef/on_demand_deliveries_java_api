package com.example.ondemand.request.DeliveryRequest;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class UpdateDeliveryPaymentRequest {
    private double paymentAmount;
    private LocalDateTime paymentTime;
    private double totalValue;
    private boolean withdrawDone;
    private double tipAmount;

    public boolean isPaymentAmount(){
        return paymentAmount != 0;
    }

    public boolean isPaymentTime(){
        return paymentTime != null;
    }

    public boolean isTotalValue(){
        return totalValue != 0;
    }

    public boolean isWithDrawDone(){
        return withdrawDone != false;
    }

    public boolean isTipAmount(){
        return tipAmount != 0;
    }


}
