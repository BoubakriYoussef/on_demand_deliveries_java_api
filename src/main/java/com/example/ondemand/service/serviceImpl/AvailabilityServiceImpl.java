package com.example.ondemand.service.serviceImpl;

import com.example.ondemand.authentication.AuthService.AuthenticationService;
import com.example.ondemand.dto.availabilityRequest.NewAvailabilityRequest;
import com.example.ondemand.dto.availabilityRequest.NewTimeRequest;
import com.example.ondemand.entities.Availability;
import com.example.ondemand.entities.Day;
import com.example.ondemand.entities.Time;
import com.example.ondemand.entities.User;
import com.example.ondemand.repositories.AvailabilityRepository;
import com.example.ondemand.repositories.DayRepository;
import com.example.ondemand.repositories.TimeRepository;
import com.example.ondemand.service.AvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class AvailabilityServiceImpl implements AvailabilityService {

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    DayRepository dayRepository;

    @Autowired
    TimeRepository timeRepository;

    @Autowired
    AvailabilityRepository availabilityRepository;

    @Override
    public Availability addAvailability(NewAvailabilityRequest request) {
        // Récupérer l'utilisateur authentifié
        User currentUser = authenticationService.getAuthenticatedUser();

        // Créer une nouvelle disponibilité
        Availability availability = new Availability();
        availability.setUser(currentUser);

        // Récupérer les jours à partir des noms de jour
        List<Day> days = new ArrayList<>();
        for (String dayName : request.getDayNames()) {
            Day day = dayRepository.findByDayName(dayName)
                    .orElseThrow(() -> new RuntimeException("Day not found: " + dayName));
            days.add(day);
        }
        availability.setDays(days);

        // Ajouter les temps pour chaque jour
        for (Day day : days) {
            List<Time> timesForDay = new ArrayList<>();
            for (NewTimeRequest timeRequest : request.getTimes()) {
                Time time = new Time();
                time.setStart(timeRequest.getStart());
                time.setEnd(timeRequest.getEnd());
                timeRepository.save(time);
                timesForDay.add(time);
            }
            day.setTimes(timesForDay);
            dayRepository.save(day);
        }
        return availabilityRepository.save(availability);
    }



        @Override
        public void deleteAvailability(Long id) {
            // Récupérer l'utilisateur authentifié
            User currentUser = authenticationService.getAuthenticatedUser();

            // Récupérer l'availability à supprimer
            Availability availability = availabilityRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Availability not found with ID: " + id));

            // Vérifier si l'utilisateur authentifié est propriétaire de l'availability
            if (!availability.getUser().equals(currentUser)) {
                throw new IllegalArgumentException("You are not authorized to delete this availability.");
            }

            // Supprimer l'availability
            availabilityRepository.delete(availability);
        }

    @Override
    public Availability updateAvailability(Long availabilityId, NewAvailabilityRequest request) {
        // Récupérer l'utilisateur authentifié
        User currentUser = authenticationService.getAuthenticatedUser();

        // Récupérer l'availability à mettre à jour
        Availability availability = availabilityRepository.findById(availabilityId)
                .orElseThrow(() -> new RuntimeException("Availability not found with ID: " + availabilityId));

        // Vérifier si l'utilisateur authentifié est propriétaire de l'availability
        if (!availability.getUser().equals(currentUser)) {
            throw new IllegalArgumentException("You are not authorized to update this availability.");
        }

        // Mettre à jour les détails de l'availability avec les nouvelles données de la demande
        // Mettre à jour les jours de disponibilité
        List<Day> days = new ArrayList<>();
        for (String dayName : request.getDayNames()) {
            Day day = dayRepository.findByDayName(dayName)
                    .orElseThrow(() -> new RuntimeException("Day not found: " + dayName));
            days.add(day);
        }
        availability.setDays(days);

        // Mettre à jour les temps de disponibilité pour chaque jour
        for (Day day : days) {
            List<Time> timesForDay = new ArrayList<>();
            for (NewTimeRequest timeRequest : request.getTimes()) {
                Time time = new Time();
                time.setStart(timeRequest.getStart());
                time.setEnd(timeRequest.getEnd());
                timeRepository.save(time);
                timesForDay.add(time);
            }
            day.setTimes(timesForDay);
            dayRepository.save(day);
        }

        return availabilityRepository.save(availability);
    }

    @Override
    public List<Availability> getAvailabilitiesByUser(User user) {
        return availabilityRepository.findByUser(user);
    }


}

