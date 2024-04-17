package com.example.ondemand.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor

public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ){


        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
            ){
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PutMapping("/user/{userId}/changePassword")
    public ResponseEntity<AuthenticationResponse> changePassword(
            @RequestBody ChangePasswordRequest request,
            @PathVariable Long userId
            ){
        return ResponseEntity.ok(service.updateUserPassword(request,userId));
    }

    @PutMapping("/user/{userId}/updateUser")
    public ResponseEntity<AuthenticationResponse> updateUser(@PathVariable Long userId, @RequestBody UpdateUserRequest updateUserRequest){
            return ResponseEntity.ok(service.updateUser(updateUserRequest,userId));
    }
}
