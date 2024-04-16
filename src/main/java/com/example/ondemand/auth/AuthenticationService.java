package com.example.ondemand.auth;


import com.example.ondemand.config.JwtService;
import com.example.ondemand.repositories.RoleRepository;
import com.example.ondemand.repositories.UserRepository;
import com.example.ondemand.entities.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

    // Method to create user, save it in db and generate token
    public AuthenticationResponse register(RegisterRequest request) {



        var user = User.builder()
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                                .email(request.getEmail())
                                        .password(passwordEncoder.encode(request.getPassword()))
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
}
