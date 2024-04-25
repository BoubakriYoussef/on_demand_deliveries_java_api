package com.example.ondemand.request.EstimationRequest;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private double orderAmount;
    private String orderDescr;
    private LocalDateTime orderTime;

    private CustomerRequest customerRequest;

}
