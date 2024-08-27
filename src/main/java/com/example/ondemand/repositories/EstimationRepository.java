package com.example.ondemand.repositories;

import com.example.ondemand.entities.Estimation;
import com.example.ondemand.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface EstimationRepository extends JpaRepository<Estimation,Long> {
    @Override
    void deleteById(Long aLong);

    List<Estimation> findByRestaurantUser(User user);

    @Override
    Optional<Estimation> findById(Long aLong);

    List<Estimation> findAll();

    void deleteEstimationById(Long idEstimation);
}
