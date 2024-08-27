package com.example.ondemand.entities;



import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="Utilisateur")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @Column(name = "email", nullable = false, unique = true)
    private String email;


    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    private String firstName;
    private String lastName;
    private String phone;

    @Column(name = "is_available")
    @Enumerated(EnumType.STRING)
    private IsAvailable isAvailable;

    @Column(unique = true)
    private String uuid;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Availability> availabilities;
    @ManyToOne
    private Role role;
    @OneToMany
    private List<Restaurant> restaurants;
    @OneToMany
    private List<Delivery> deliveries;

    @OneToMany
    private List<Cashout> cashouts;

    @OneToMany
    private List<Rate> rates;



    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role != null) {
            return Collections.singletonList(role);
        } else {
            return Collections.emptyList();
        }
    }
}
