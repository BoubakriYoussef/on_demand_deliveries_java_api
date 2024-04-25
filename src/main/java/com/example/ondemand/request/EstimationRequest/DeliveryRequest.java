package com.example.ondemand.request.EstimationRequest;

import com.example.ondemand.enumClass.PaymentMethod;
import com.example.ondemand.enumClass.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryRequest {

    private Status status;

    private PaymentMethod paymentMethod;

    private OrderRequest orderRequest;

    private PaymentRequest paymentRequest;


}
