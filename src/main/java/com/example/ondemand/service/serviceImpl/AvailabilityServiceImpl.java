package com.example.ondemand.service.serviceImpl;

import com.example.ondemand.entities.Availability;
import com.example.ondemand.repositories.AvailabilityRepository;
import com.example.ondemand.service.AvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;

public class AvailabilityServiceImpl implements AvailabilityService {

    @Autowired
    private AvailabilityRepository availabilityRepository;


    @Override
    public Availability addAvailability() {
        return null;
    }
}
