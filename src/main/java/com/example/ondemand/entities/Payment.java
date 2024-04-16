package com.example.ondemand.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double paymentTime;
    private double orderValue;
    private double tipValue;
    private double totalValue;
    private boolean isWithdrawDone;

    @Column(unique = true)
    private String uuid;

    @ManyToOne
    private Delivery delivery;

    @ManyToOne
    private Cashout cashout;
}
