package com.example.ondemand.dto.EstimationRequest;

import com.example.ondemand.EnumClass.PaymentMethod;
import com.example.ondemand.EnumClass.Status;
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

    private UserRequest userRequest;
}
