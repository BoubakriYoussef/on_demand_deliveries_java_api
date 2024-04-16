package com.example.ondemand.repositories;

import com.example.ondemand.entities.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface RestaurantRepository extends JpaRepository<Restaurant,Long> {
}
