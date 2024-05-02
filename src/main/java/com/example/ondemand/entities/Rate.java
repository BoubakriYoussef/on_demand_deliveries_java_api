package com.example.ondemand.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Rate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double rating;
    private String commentary;
    private LocalDateTime evaluatedAt;
    private LocalDateTime updatedAt;

    @Column(unique = true)
    private String uuid;
    @ManyToOne
    private User user;

    @OneToOne
    private Delivery delivery;
}
