package com.example.ondemand.authentication.authService;


import com.example.ondemand.authentication.authRequest.AuthenticationRequest;
import com.example.ondemand.authentication.authRequest.AuthenticationResponse;
import com.example.ondemand.authentication.authRequest.ChangePasswordRequest;
import com.example.ondemand.authentication.authRequest.RegisterRequest;
import com.example.ondemand.securityConfiguration.JwtService;
import com.example.ondemand.entities.Role;
import com.example.ondemand.repositories.RoleRepository;
import com.example.ondemand.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.ondemand.entities.User;


@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final RoleRepository roleRepository;


    // Method to create user, save it in db and generate token
    public AuthenticationResponse register(RegisterRequest request, String roleName) {

        Role role = roleRepository.findByName(roleName);

        if(role == null) {
            throw  new IllegalArgumentException("Role not found with name :"+ roleName);
        }

        var user = User.builder()
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .phone(request.getPhone())
                        .isAvailable(request.getIsAvailable())
                        .role(role)
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

        if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("The current Password is incorrect");
        }

        String newPassword = changePasswordRequest.getNewPassword();

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    // Update User Infos


    // Get authenticated User from SecurityContextHolder
    public User getAuthenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        return userRepository.findByEmail(userEmail)
                .orElseThrow(()-> new RuntimeException("User not found"));
    }
}
