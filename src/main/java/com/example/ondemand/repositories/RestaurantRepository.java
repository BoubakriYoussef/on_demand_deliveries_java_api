package com.example.ondemand.repositories;

import com.example.ondemand.entities.Restaurant;
import com.example.ondemand.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface RestaurantRepository extends JpaRepository<Restaurant,Long> {


    List<Restaurant> findAll();

    List<Restaurant> findByUser(User user);

    void deleteById(Long restaurantId);
}
