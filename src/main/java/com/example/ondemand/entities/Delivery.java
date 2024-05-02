package com.example.ondemand.entities;

import com.example.ondemand.enumClass.PaymentMethod;
import com.example.ondemand.enumClass.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Status status;

    private PaymentMethod paymentMethod;
    @Column(unique = true)
    private String uuid;
    @OneToOne
    private Estimation estimation;
    @OneToOne
    private Orders orders;

    @ManyToOne
    private User user;

    @OneToOne
    private Payment payment;

    @OneToOne(mappedBy = "delivery")
    private Rate rate;
}
