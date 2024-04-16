package com.example.ondemand.service;

import com.example.ondemand.entities.Role;
import com.example.ondemand.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> getUserById(Long id);

    List<User> getAllUsers();

    User updateUser(Long userId, User user);

    void deleteUser(Long id);



}
