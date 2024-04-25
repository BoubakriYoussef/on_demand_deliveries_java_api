package com.example.ondemand.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double paymentAmount;
    private LocalDateTime paymentTime;

    private double totalValue;
    private boolean isWithdrawDone;

    @Column(unique = true)
    private String uuid;

    @ManyToOne
    private Delivery delivery;

    @ManyToOne
    private Cashout cashout;

    @OneToOne
    private Tip tip;
}
