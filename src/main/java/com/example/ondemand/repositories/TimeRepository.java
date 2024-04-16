package com.example.ondemand.repositories;

import com.example.ondemand.entities.Time;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface TimeRepository extends JpaRepository<Time, Repository> {
}
