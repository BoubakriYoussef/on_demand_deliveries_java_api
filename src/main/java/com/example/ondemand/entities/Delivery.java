package com.example.ondemand.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private enum status {
        UNASSIGNED, ASSIGNED, RECUPERATED, DELIVERED
    }
    private Date orderTime;
    private Date deliveryTime;
    private enum paymentMethod {CASH,CREDIT_CARD};
    @Column(unique = true)
    private String uuid;
    @OneToMany
    private List<Estimation> estimations;
    @OneToOne
    private Orders orders;
    @OneToOne
    private Customer customer;
    @OneToOne
    private Address address;
    @ManyToOne
    private User user;
    @OneToMany
    private List<Tip> tips;
    @OneToMany
    private List<Payment> payments;
}
