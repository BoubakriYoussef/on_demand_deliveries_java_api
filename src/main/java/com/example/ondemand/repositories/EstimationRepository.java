package com.example.ondemand.repositories;

import com.example.ondemand.entities.Estimation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface EstimationRepository extends JpaRepository<Estimation,Long> {
}
