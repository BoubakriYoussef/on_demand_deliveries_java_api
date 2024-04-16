package com.example.ondemand.repositories;

import com.example.ondemand.entities.Day;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface DayRepository extends JpaRepository<Day,Long> {
}
