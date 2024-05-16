package com.example.ondemand.entities;

import com.example.ondemand.request.EstimationRequest.OrderType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double orderAmount;

    private String orderDescription;

    private LocalDateTime orderTime;

    private LocalDateTime requestedDeliveryTime;

    private OrderType orderType;


    @Column(unique = true)
    private String uuid;

    @OneToOne
    private Delivery delivery;

    @OneToOne
    private Customer customer;
}
