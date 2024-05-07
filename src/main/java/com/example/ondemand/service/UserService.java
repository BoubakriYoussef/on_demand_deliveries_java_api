package com.example.ondemand.service;

import com.example.ondemand.authentication.authRequest.AuthenticationResponse;
import com.example.ondemand.authentication.authRequest.UpdateUserRequest;
import com.example.ondemand.entities.Restaurant;
import com.example.ondemand.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> getUserById(Long id);

    List<User> getAllUsers();

    void deleteUser(Long id);

    AuthenticationResponse updateUser(UpdateUserRequest request, Long userId);

    List<User> getUserByRole(String roleName);

    List<User> findAvailableDrivers();
}
