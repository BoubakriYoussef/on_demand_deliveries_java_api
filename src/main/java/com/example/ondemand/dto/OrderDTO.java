package com.example.ondemand.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private BigDecimal orderAmount;
    private String orderDescr;
    private LocalDateTime orderTime;
    private boolean isPrepared;

}
