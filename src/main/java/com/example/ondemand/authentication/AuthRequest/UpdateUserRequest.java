package com.example.ondemand.authentication.AuthRequest;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;


    //Allow the client to specify only the fields that want to update
    public boolean isFirstNamePresent(){
        return firstName != null;
    }

    public boolean isLastNamePresent(){
        return lastName != null;
    }

    public boolean isEmailPresent(){
        return email != null;
    }

    public boolean isPhonePresent(){
        return phone != null;
    }
}
