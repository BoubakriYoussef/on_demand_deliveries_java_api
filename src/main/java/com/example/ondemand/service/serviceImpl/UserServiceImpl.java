package com.example.ondemand.service.serviceImpl;

import com.example.ondemand.repositories.UserRepository;
import com.example.ondemand.entities.Role;
import com.example.ondemand.entities.User;
import com.example.ondemand.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;



    //Sign Up & Add user
    @Override
    public User addUser(User user, Role role) {

        if (userRepository.existsByEmail(user.getEmail())) {
                throw new RuntimeException("Email already exists");
            }

            User savedUser = userRepository.save(user);
            return (savedUser);
        }


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
    public User updateUser(Long userId, User updatedUser) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("User not found with id :"+userId));
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setPhone(updatedUser.getPhone());
        return null;
    }

    @Override
    public void deleteUser(Long id) {
         userRepository.deleteById(id);
    }

    @Override
    public void updateUserPassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new RuntimeException("User not found :"+userId));

        //Need to add PasswordEncoder
        user.setPassword(newPassword);
        userRepository.save(user);
    }
}
