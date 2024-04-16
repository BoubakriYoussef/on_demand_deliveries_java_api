package com.example.ondemand.repositories;

import com.example.ondemand.entities.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface RateRepository extends JpaRepository<Rate,Long> {
}
