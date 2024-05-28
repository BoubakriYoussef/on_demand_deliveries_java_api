package com.example.ondemand.repositories;

import com.example.ondemand.entities.Delivery;
import com.example.ondemand.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface DeliveryRepository extends JpaRepository<Delivery,Long> {

    List<Delivery> findAllByUser(User user);

    List<Delivery> findByUserId(Long id);
}
