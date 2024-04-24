package com.example.ondemand.repositories;

import com.example.ondemand.entities.Day;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface DayRepository extends JpaRepository<Day,Long> {


    Optional<Day> findByDayName(String dayName);
}
