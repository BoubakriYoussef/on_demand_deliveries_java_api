package com.example.ondemand.entities;

import com.example.ondemand.enumClass.EstimationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Estimation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double distance;
    private double estimatedFee;
    private LocalDateTime estimatedDeliveryTime;
    private LocalDateTime estimatedPickUpTime;

    @Enumerated(EnumType.STRING)
    private EstimationStatus estimationStatus;

    @Column(unique = true)
    private String uuid;
    @ManyToOne
    private Restaurant restaurant;
    @ManyToOne
    private User user;
    @ManyToOne
    private Delivery delivery;
    @ManyToOne
    private PricingStrategy pricingStrategy;
}
