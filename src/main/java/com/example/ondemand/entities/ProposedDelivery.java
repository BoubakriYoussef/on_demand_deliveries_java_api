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
public class ProposedDelivery {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Delivery delivery;
    @ManyToOne
    private User user;
    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime expirationTime;

    @Enumerated(EnumType.STRING)
    private ProposedDeliveryStatus status;
}
