package com.example.ondemand.repositories;

import com.example.ondemand.entities.Address;
import com.example.ondemand.entities.Evaluation;
import com.example.ondemand.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface EvaluationRepository extends JpaRepository<Evaluation,Long> {

    Evaluation findByUser(User user);

    List<Evaluation> findAllByUser(User user);
}
