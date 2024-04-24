package com.example.ondemand.repositories;

import com.example.ondemand.entities.Availability;
import com.example.ondemand.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface AvailabilityRepository extends JpaRepository<Availability,Long> {


    void deleteById(Long id);

    List<Availability> findByUser(User user);
}
