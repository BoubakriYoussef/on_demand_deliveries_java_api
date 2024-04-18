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
public class Cashout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double amount;

    private enum typeUser{ADMIN,MANAGER,DRIVER};

    private enum cashoutStatus { PENDING, APPROVED, REJECTED, PROCESSING, COMPLETED};

    @Column(unique = true)
    private String uuid;

    private Date date;
    @OneToMany
    private List<Payment> payments;
    @OneToMany
    private List<Tip> tips;

    @ManyToOne
    private User user;
}
