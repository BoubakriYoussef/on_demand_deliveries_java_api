package com.example.ondemand.authentication.AuthController;

import com.example.ondemand.authentication.AuthRequest.AuthenticationRequest;
import com.example.ondemand.authentication.AuthRequest.AuthenticationResponse;
import com.example.ondemand.authentication.AuthRequest.ChangePasswordRequest;
import com.example.ondemand.authentication.AuthRequest.RegisterRequest;
import com.example.ondemand.authentication.AuthService.AuthenticationService;
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
        String roleName = request.getRoleName();
        return ResponseEntity.ok(authenticationService.register(request, roleName));
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
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            @PathVariable Long userId,
            @RequestBody String currentPassword
            ){
        User authenticatedUser = authenticationService.getAuthenticatedUser();

        if(!authenticatedUser.getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only update your own information");
        }
       AuthenticationResponse response = authenticationService.updateUserPassword(request,userId,currentPassword);

        return ResponseEntity.ok(response);
    }

}
