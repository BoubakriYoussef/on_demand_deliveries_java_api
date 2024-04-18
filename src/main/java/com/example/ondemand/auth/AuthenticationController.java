package com.example.ondemand.auth;

import com.example.ondemand.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor

public class AuthenticationController {

    private final AuthenticationService authenticationService;



    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ){


        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
            ){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        System.out.print("name :" + name) ;
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PutMapping("/user/{userId}/changePassword")
    public ResponseEntity<AuthenticationResponse> changePassword(
            @RequestBody ChangePasswordRequest request,
            @PathVariable Long userId
            ){
        return ResponseEntity.ok(authenticationService.updateUserPassword(request,userId));
    }

    @PutMapping("/user/{userId}/updateUser")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody UpdateUserRequest updateUserRequest){
        User authenticatedUser = authenticationService.getAuthenticatedUser();

        if(!authenticatedUser.getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only update your own information.");
        }

        authenticationService.updateUser(updateUserRequest,userId);

        return ResponseEntity.ok("You updated your own informations ");
    }
}
