package com.example.ondemand.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PricingStrategy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;
    private enum unitOfMeasure {KM,MILE};
    private double deliveryFeePerMile;
    private double deliveryFeePerKilometer;
    private double serviceFee;

    @Column(unique = true)
    private String uuid;
    @OneToMany
    private Collection<Estimation> estimations;
    @ManyToOne
    private User user;
}
