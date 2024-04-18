package com.example.ondemand.auth;


import com.example.ondemand.config.JwtService;
import com.example.ondemand.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.ondemand.entities.User;

import java.lang.reflect.Type;


@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;


    // Method to create user, save it in db and generate token
    public AuthenticationResponse register(RegisterRequest request) {

        var user = User.builder()
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .phone(request.getPhone())
                        .build();

        userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }


    //Authentication Manager has "authenticate" Method that will do the job and throw an Exception
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
             var jwtToken = jwtService.generateToken(user);
             return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }



    // Update User Pwd
    public AuthenticationResponse updateUserPassword(ChangePasswordRequest changePasswordRequest, Long userId){
        User user = userRepository.findById(userId)
                        .orElseThrow(()->new RuntimeException("User not found :"+userId));

        String newPassword = changePasswordRequest.getNewPassword();

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    // Update User Infos
    public AuthenticationResponse updateUser(UpdateUserRequest updateUserRequest, Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(()->new RuntimeException("User not found :"+userId));

        if(updateUserRequest.isFirstNamePresent()) {
            user.setFirstName(updateUserRequest.getFirstName());
        }

        if(updateUserRequest.isLastNamePresent()) {
            user.setLastName(updateUserRequest.getLastName());
        }

        if(updateUserRequest.isEmailPresent()) {
            user.setEmail(updateUserRequest.getEmail());
        }

        if(updateUserRequest.isPhonePresent()) {
            user.setPhone(updateUserRequest.getPhone());
        }

        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    // Get authenticated User from SecurityContextHolder

    public User getAuthenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        return userRepository.findByEmail(userEmail)
                .orElseThrow(()-> new RuntimeException("User not found"));

    }
}
