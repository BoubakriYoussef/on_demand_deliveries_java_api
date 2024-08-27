package com.example.ondemand.entities;

import com.example.ondemand.enumClass.UnitOfMeasure;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private UnitOfMeasure unitOfMeasure;
    private double deliveryFeePerMile;
    private double deliveryFeePerKilometer;
    private double serviceFee;
    private double minimalFee;

    private double minimalDistance;
    private double tva;

    @Column(unique = true)
    private String uuid;
    @OneToMany
    private List<Estimation> estimations;
}
