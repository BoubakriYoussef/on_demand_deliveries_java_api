package com.example.ondemand.service.serviceImpl;

import com.example.ondemand.repositories.UserRepository;
import com.example.ondemand.entities.User;
import com.example.ondemand.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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


}
