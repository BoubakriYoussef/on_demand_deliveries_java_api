package com.example.ondemand.service.serviceImpl;

import com.example.ondemand.auth.AuthenticationResponse;
import com.example.ondemand.auth.AuthenticationService;
import com.example.ondemand.auth.UpdateUserRequest;
import com.example.ondemand.config.JwtService;
import com.example.ondemand.repositories.UserRepository;
import com.example.ondemand.entities.User;
import com.example.ondemand.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private JwtService jwtService;

    // Find User By Id
    @Override
    public Optional<User> getUserById(Long id) {
        Optional<User> userById = userRepository.findById(id);
        return userById;
    }

    //Find list of users
    @Override
    public List<User> getAllUsers() {
        List<User> listUsers = userRepository.findAll();
        return listUsers;
    }


    // Update User Credentials
    @Override
    public void deleteUser(Long id) {
         userRepository.deleteById(id);
    }


    @Override
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

    @Override
    public List<User> getUserByRole(String roleName) {
            return userRepository.findByRole_Name(roleName);
    }
    public ResponseEntity<Optional<User>> findByUsername(String username) {
        return ResponseEntity.ok(userRepository.findByEmail(username));
    }
}
