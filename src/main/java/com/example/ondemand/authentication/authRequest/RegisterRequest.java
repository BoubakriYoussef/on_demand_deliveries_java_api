package com.example.ondemand.authentication.authRequest;


import com.example.ondemand.entities.IsAvailable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;

    private IsAvailable isAvailable;

    private String roleName;
}
