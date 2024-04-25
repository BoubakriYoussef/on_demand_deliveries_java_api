package com.example.ondemand.controllers;

import com.example.ondemand.authentication.authService.AuthenticationService;
import com.example.ondemand.authentication.authRequest.UpdateUserRequest;
import com.example.ondemand.entities.User;
import com.example.ondemand.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationService authenticationService;

    // Recuperate Users
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> userList = userService.getAllUsers();
        return ResponseEntity.ok(userList);
    }

    // Recuperate user by id
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        Optional<User> userOptional = userService.getUserById(userId);
        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    //update user infos
    @PutMapping("/user/{userId}/updateUser")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody UpdateUserRequest updateUserRequest){
        User authenticatedUser = authenticationService.getAuthenticatedUser();

        if(!authenticatedUser.getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only update your own information.");
        }

        userService.updateUser(updateUserRequest,userId);

        return ResponseEntity.ok("You updated your own informations ");
    }


    // delete user
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/by-role/{roleName}")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable String roleName){
        List<User> users = userService.getUserByRole(roleName);
        return ResponseEntity.ok(users);
    }

}
