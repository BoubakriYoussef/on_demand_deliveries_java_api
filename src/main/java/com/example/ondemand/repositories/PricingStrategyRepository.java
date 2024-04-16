package com.example.ondemand.repositories;

import com.example.ondemand.entities.PricingStrategy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface PricingStrategyRepository extends JpaRepository<PricingStrategy,Long> {
}
