package com.example.ondemand.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Tip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double tipAmount;

    @Column(unique = true)
    private String uuid;

    @OneToOne
    private Delivery delivery;

    @ManyToOne
    private Cashout cashout;
}
