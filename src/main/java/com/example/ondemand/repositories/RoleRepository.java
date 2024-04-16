package com.example.ondemand.repositories;

import com.example.ondemand.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface RoleRepository extends JpaRepository<Role, Long> {


    @Override
    Optional<Role> findById(Long aLong);

    Role findByRoleName(String roleName);
}
