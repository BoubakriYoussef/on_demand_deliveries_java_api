package com.example.ondemand.repositories;

import com.example.ondemand.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;


@RepositoryRestResource
public interface UserRepository extends JpaRepository<User,Long> {

    boolean existsByEmail(String email);


    Optional<User> findByEmail(String email);

    @Override
    List<User> findAll();
}
