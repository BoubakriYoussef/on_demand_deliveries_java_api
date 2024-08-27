package com.example.ondemand.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;


@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;
    @Column(unique = true)
    private String uuid;

    @ManyToMany
    private List<Permission> permissions;

    @OneToMany
    private List<User> users;

    @Override
    public String getAuthority() {
        return name;
    }
}
