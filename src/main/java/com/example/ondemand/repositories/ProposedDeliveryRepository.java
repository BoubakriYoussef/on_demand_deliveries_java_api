package com.example.ondemand.repositories;

import com.example.ondemand.entities.Delivery;
import com.example.ondemand.entities.ProposedDelivery;
import com.example.ondemand.entities.ProposedDeliveryStatus;
import com.example.ondemand.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository

public interface ProposedDeliveryRepository extends JpaRepository<ProposedDelivery,Long> {


    List<ProposedDelivery> findByUserAndDelivery(User user, Delivery delivery);

    List<ProposedDelivery> findByStatusAndExpirationTimeBefore(ProposedDeliveryStatus status, LocalDateTime expirationTime);

}
