package com.example.ondemand.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String building;
    private String street;
    private String floor;
    private String additionalInfos;
    private String landmark;
    private double latitude;
    private double longitude;

    @Column(unique = true)
    private String uuid;
    @OneToOne
    private Restaurant restaurant;
    @OneToOne
    private Customer customer;
    @OneToOne
    private Delivery delivery;
}
