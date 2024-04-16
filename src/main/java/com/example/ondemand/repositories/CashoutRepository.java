package com.example.ondemand.repositories;

import com.example.ondemand.entities.Cashout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CashoutRepository extends JpaRepository<Cashout,Long> {
}
