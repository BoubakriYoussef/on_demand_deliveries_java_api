package com.example.ondemand.repositories;

import com.example.ondemand.entities.Tip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface TipRepository extends JpaRepository<Tip,Long> {
}
