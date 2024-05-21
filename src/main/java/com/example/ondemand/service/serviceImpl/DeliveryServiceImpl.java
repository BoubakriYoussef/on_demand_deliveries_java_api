package com.example.ondemand.service.serviceImpl;

import com.example.ondemand.entities.*;
import com.example.ondemand.enumClass.Status;
import com.example.ondemand.repositories.DeliveryRepository;
import com.example.ondemand.repositories.RateRepository;
import com.example.ondemand.repositories.UserRepository;
import com.example.ondemand.request.DeliveryRequest.UpdateDeliveryPaymentRequest;
import com.example.ondemand.request.DeliveryRequest.DriverDecisionRequest;
import com.example.ondemand.request.DeliveryRequest.UpdateDeliveryStatusRequest;
import com.example.ondemand.request.rateRequest.RateUpdateRequest;
import com.example.ondemand.service.AvailabilityService;
import com.example.ondemand.service.DeliveryService;
import com.example.ondemand.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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


    //Evaluer une livraison à condition que la commande soit livrée
        public void updateRate(Long deliveryId, RateUpdateRequest rateUpdateRequest) {
            // Récupérer la livraison depuis la base de données
            Delivery delivery = deliveryRepository.findById(deliveryId)
                    .orElseThrow(() -> new EntityNotFoundException("Delivery not found with id: " + deliveryId));

            // Vérifier si le statut de livraison est "Delivered"
            if (delivery.getStatus() == Status.DELIVERED) {
                // Récupérer l'objet Rate associé à la livraison
                Rate rate = delivery.getRate();
                if (rate != null) {
                    // Mettre à jour la note
                    rate.setRating(rateUpdateRequest.getRating());
                    rate.setCommentary(rateUpdateRequest.getCommentary());
                    rate.setEvaluatedAt(LocalDateTime.now()); // Mettre à jour la date d'évaluation
                    // Sauvegarder la note mise à jour
                    rateRepository.save(rate);
                } else {
                    // Créer une nouvelle note si aucune n'est associée à la livraison
                    rate = new Rate();
                    rate.setRating(rateUpdateRequest.getRating());
                    rate.setCommentary(rateUpdateRequest.getCommentary());
                    rate.setEvaluatedAt(LocalDateTime.now()); // Mettre à jour la date d'évaluation
                    rate.setUpdatedAt(null); // Réinitialiser la date de mise à jour
                    // Associer la note à la livraison
                    delivery.setRate(rate);
                }
                // Sauvegarder la livraison mise à jour
                deliveryRepository.save(delivery);
            } else {
                // Si le statut de livraison n'est pas "Delivered", générer une exception
                throw new IllegalStateException("Rate can only be updated for delivered deliveries.");
            }
        }

    @Override
    public void acceptOrRefuseDelivery(DriverDecisionRequest decisionRequest) {
        return;
    }

    @Override
    public void updateDeliveryStatus(Long deliveryId, UpdateDeliveryStatusRequest updateDeliveryStatusRequest) {

    }

    public void assignDriverToDelivery(Long deliveryId, Long userId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new EntityNotFoundException("Delivery not found with id: " + deliveryId));
        User driver = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        delivery.setUser(driver);
        deliveryRepository.save(delivery);
    }


    public void updateDeliveryStatus(Long deliveryId, Status status) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new EntityNotFoundException("Delivery not found with id: " + deliveryId));
        delivery.setStatus(status);
        deliveryRepository.save(delivery);
    }
}
