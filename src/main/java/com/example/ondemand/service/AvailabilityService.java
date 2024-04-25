package com.example.ondemand.service;

import com.example.ondemand.request.availabilityRequest.NewAvailabilityRequest;
import com.example.ondemand.entities.Availability;
import com.example.ondemand.entities.User;

import java.util.List;

public interface AvailabilityService {

    public Availability addAvailability(NewAvailabilityRequest request);


    void deleteAvailability(Long id);

    Availability updateAvailability(Long availabilityId, NewAvailabilityRequest request);

    List<Availability> getAvailabilitiesByUser(User user);
}
