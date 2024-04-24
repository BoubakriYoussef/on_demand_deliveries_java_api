package com.example.ondemand.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryDTO {
    private LocalDateTime orderTime;
    private LocalDateTime deliveryTime;
    private String paymentMethod;
    private CustomerDTO customer;

}
