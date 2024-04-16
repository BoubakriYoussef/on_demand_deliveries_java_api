package com.example.ondemand.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private double estimatedDeliveryTime;
    private double estimatedPickUpTime;

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
