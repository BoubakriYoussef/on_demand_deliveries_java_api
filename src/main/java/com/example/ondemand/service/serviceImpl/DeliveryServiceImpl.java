package com.example.ondemand.service.serviceImpl;

import com.example.ondemand.authentication.authService.AuthenticationService;
import com.example.ondemand.entities.*;
import com.example.ondemand.enumClass.Status;
import com.example.ondemand.exceptions.DeliveryNotFoundException;
import com.example.ondemand.exceptions.UnauthorizedException;
import com.example.ondemand.repositories.DeliveryRepository;
import com.example.ondemand.repositories.RateRepository;
import com.example.ondemand.repositories.UserRepository;
import com.example.ondemand.request.DeliveryRequest.UpdateDeliveryStatusRequest;
import com.example.ondemand.request.rateRequest.RateUpdateRequest;
import com.example.ondemand.service.AvailabilityService;
import com.example.ondemand.service.DeliveryService;

import com.example.ondemand.service.UserService;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;


import java.util.List;
import java.util.Optional;

@Service
public class DeliveryServiceImpl implements DeliveryService {

    @Autowired
    DeliveryRepository deliveryRepository;

    @Autowired
    RateRepository rateRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AvailabilityService availabilityService;


    @Autowired
    UserService userService;

    @Autowired
    AuthenticationService authenticationService;

    //Evaluer une livraison à condition que la commande soit livrée
        public void updateRate(Long deliveryId, RateUpdateRequest rateUpdateRequest) {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String authenticationUsername = authentication.getName();
            User authenticatedUser = userRepository.findByEmail(authenticationUsername)
                    .orElseThrow(()-> new UsernameNotFoundException("User not found"));

            Delivery delivery = deliveryRepository.findById(deliveryId)
                    .orElseThrow(()-> new DeliveryNotFoundException("Delivery not found"));


            User manager = delivery.getEstimation().getRestaurant().getUser();

            if(!manager.getId().equals(authenticatedUser.getId())) {
                throw new RuntimeException("You aren't allowed to update this delivery rate");
            }

            if(delivery.getStatus().equals(Status.DELIVERED)){


                delivery.setRating(rateUpdateRequest.getRating());
                delivery.setCommentary(rateUpdateRequest.getCommentary());


                deliveryRepository.save(delivery);
            } else {
                new RuntimeException("The delivery isn't delivered yet");
            }
        }



    @Override
    public Delivery updateDeliveryStatus(Long deliveryId, UpdateDeliveryStatusRequest updateDeliveryStatusRequest) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();
            Optional<User> currentUserOpt = userRepository.findByEmail(currentUsername);

            if(currentUserOpt.isEmpty()){
                throw new RuntimeException("User not found");
            }

            User currentUser = currentUserOpt.get();
            Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(() -> new RuntimeException("Delivery not found"));

            if(!delivery.getUser().getId().equals(currentUser.getId())) {
                throw new RuntimeException("User not authorized to update Delivery Status");
            }

            delivery.setStatus(updateDeliveryStatusRequest.getStatus());
            return deliveryRepository.save(delivery);
    }

    @Transactional
    public void assignDriverToDelivery(Long deliveryId, Long userId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new EntityNotFoundException("Delivery not found with id: " + deliveryId));
        User driver = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        delivery.setUser(driver);
        deliveryRepository.save(delivery);
    }

    @Override
    public List<Delivery> findDeliveriesByUserId(Long id) {
        return deliveryRepository.findByUserId(id);
    }

    @Transactional
    public void updateDeliveryStatus(Long deliveryId, Status status) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new EntityNotFoundException("Delivery not found with id: " + deliveryId));
        delivery.setStatus(status);
        deliveryRepository.save(delivery);
    }

    @Override
    @Transactional
    public void updateDelivery(Long deliveryId, RateUpdateRequest updateRequest) {


        // Récupérer la delivery par son ID
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new DeliveryNotFoundException("Delivery not found with id: " + deliveryId));



        // Mettre à jour les champs de la delivery avec les valeurs du RateUpdateRequest
        if (updateRequest.getRating() != 0) {
            delivery.setRating(updateRequest.getRating());
        }
        if (updateRequest.getCommentary() != null) {
            delivery.setCommentary(updateRequest.getCommentary());
        }

        // Sauvegarder la delivery mise à jour dans la base de données
        deliveryRepository.save(delivery);
    }
}
