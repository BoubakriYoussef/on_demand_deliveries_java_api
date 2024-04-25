package com.example.ondemand.controllers;


import com.example.ondemand.request.availabilityRequest.NewAvailabilityRequest;
import com.example.ondemand.entities.Availability;
import com.example.ondemand.entities.User;
import com.example.ondemand.repositories.UserRepository;
import com.example.ondemand.service.AvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/availability")
public class AvailabilityController {

    @Autowired
    AvailabilityService availabilityService;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/add")
    public ResponseEntity<Availability> addAvailability(@RequestBody NewAvailabilityRequest request) {
        Availability availability = availabilityService.addAvailability(request);
        return ResponseEntity.ok(availability);
    }

    @DeleteMapping("/{availabilityId}")
    public ResponseEntity<?> deleteAvailability(@PathVariable Long availabilityId) {
        availabilityService.deleteAvailability(availabilityId);
        return ResponseEntity.ok("Availability deleted successfully");
    }


    @PutMapping("/{id}")
    public ResponseEntity<Availability> updateAvailability(@PathVariable Long id, @RequestBody NewAvailabilityRequest request) {
        Availability updatedAvailability = availabilityService.updateAvailability(id, request);
        return ResponseEntity.ok(updatedAvailability);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Availability>> getAvailabilitiesByUser(@PathVariable Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        List<Availability> userAvailabilities = availabilityService.getAvailabilitiesByUser(user);
        return ResponseEntity.ok(userAvailabilities);
    }
}
