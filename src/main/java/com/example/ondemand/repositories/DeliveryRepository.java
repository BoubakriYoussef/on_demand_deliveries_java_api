package com.example.ondemand.repositories;

import com.example.ondemand.entities.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface DeliveryRepository extends JpaRepository<Delivery,Long> {
}
