package com.example.ondemand.service.serviceImpl;

import com.example.ondemand.entities.*;
import com.example.ondemand.enumClass.Status;
import com.example.ondemand.repositories.AvailabilityRepository;
import com.example.ondemand.repositories.DeliveryRepository;
import com.example.ondemand.repositories.RateRepository;
import com.example.ondemand.repositories.UserRepository;
import com.example.ondemand.request.DeliveryRequest.DeliveryUpdateRequest;
import com.example.ondemand.request.DeliveryRequest.DriverDecisionRequest;
import com.example.ondemand.request.rateRequest.RateUpdateRequest;
import com.example.ondemand.service.AvailabilityService;
import com.example.ondemand.service.DeliveryService;
import com.example.ondemand.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

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

    //Mettre à jour les détails du Paiement et de la livraison durant le processus de livraison
    @Override
    public void updateDeliveryDetails(Long deliveryId, DeliveryUpdateRequest updateRequest) {
        // Récupérer la livraison depuis la base de données
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new EntityNotFoundException("Delivery not found with id: " + deliveryId));

        // Mettre à jour les détails du paiement si spécifié dans la demande
        if (updateRequest != null) {
            Payment payment = delivery.getPayment();
            if (payment != null) {
                // Mettre à jour les détails du paiement
                payment.setPaymentAmount(updateRequest.getPaymentAmount());
                payment.setPaymentTime(updateRequest.getPaymentTime());
                payment.setTotalValue(updateRequest.getTotalValue());
                payment.setWithdrawDone(updateRequest.isWithdrawDone());

                // Mettre à jour les détails du pourboire
                Tip tip = payment.getTip();
                if (tip != null) {
                    tip.setTipAmount(updateRequest.getTipAmount());
                } else {
                    tip = new Tip();
                    tip.setTipAmount(updateRequest.getTipAmount());
                    payment.setTip(tip);
                }
            } else {
                // Créer un nouveau paiement si aucun paiement n'est associé à la livraison
                Tip tip = new Tip();
                tip.setTipAmount(updateRequest.getTipAmount());

                payment = new Payment();
                payment.setPaymentAmount(updateRequest.getPaymentAmount());
                payment.setPaymentTime(updateRequest.getPaymentTime());
                payment.setTotalValue(updateRequest.getTotalValue());
                payment.setWithdrawDone(updateRequest.isWithdrawDone());
                payment.setTip(tip);

                delivery.setPayment(payment);
            }
        }

        // Sauvegarder la livraison mise à jour
        deliveryRepository.save(delivery);
    }


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


        //Assigner un utilisateur (Driver) à Delivery
        @Transactional
        @Override
        public void assignDriverToDelivery(Long idDelivery) {
            // Récupérer la livraison à partir de l'identifiant
            Delivery delivery = deliveryRepository.findById(idDelivery)
                    .orElseThrow(() -> new RuntimeException("Delivery not found with ID: " + idDelivery));

            // Vérifier si la livraison est déjà assignée
            if (delivery.getStatus() != Status.UNASSIGNED) {
                throw new RuntimeException("Delivery is already assigned.");
            }

            // Récupérer la liste des conducteurs disponibles
            List<User> availableDrivers = userService.findAvailableDrivers();

            // Exclure les conducteurs qui ont déjà refusé la livraison
            List<User> nonRejectedDrivers = availableDrivers.stream()
                    .filter(driver -> !hasDriverRejectedDelivery(driver, idDelivery))
                    .collect(Collectors.toList());



            // Trouver le premier conducteur disponible pour cette livraison
            User availableDriver = availableDrivers.stream()
                    .filter(driver -> isDriverAvailableForDelivery(delivery, driver))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No available driver found for this delivery."));

            // Assigner la livraison au conducteur trouvé
            delivery.setUser(availableDriver);
            delivery.setStatus(Status.ASSIGNED);

            // Mettre à jour la livraison dans la base de données
            deliveryRepository.save(delivery);

            // Envoyer une notification au conducteur pour lui informer de la nouvelle livraison assignée
            // (Vous pouvez implémenter cette fonctionnalité selon vos besoins)

        }

        private boolean hasDriverRejectedDelivery(User driver, Long deliveryId) {
            // Récupérer la livraison à partir de l'identifiant
            Delivery delivery = deliveryRepository.findById(deliveryId)
                    .orElseThrow(() -> new RuntimeException("Delivery not found with ID: " + deliveryId));

            // Vérifier si le conducteur a déjà rejeté cette livraison
            return delivery.getRejections().stream()
                    .anyMatch(rejection -> rejection.getUser().equals(driver));
        }



    //1- Find Available Driver
    private boolean isDriverAvailableForDelivery(Delivery delivery, User user) {
        // Récupérer l'ensemble des disponibilités du conducteur
        List<Availability> availabilities = user.getAvailabilities();

        // Récupérer le jour de la livraison
        LocalDate deliveryDate = delivery.getEstimation().getEstimatedDeliveryTime().toLocalDate();
        DayOfWeek deliveryDayOfWeek = deliveryDate.getDayOfWeek();

        // Vérifier la disponibilité du conducteur pour ce jour
        for (Availability availability : availabilities) {
            for (Day day : availability.getDays()) {
                if (day.getDayName().equals(deliveryDayOfWeek.toString())) {
                    // Vérifier les plages horaires de disponibilité pour ce jour
                    for (Time time : day.getTimes()) {
                        LocalTime startTime = time.getStart();
                        LocalTime endTime = time.getEnd();
                        LocalTime deliveryTime = delivery.getEstimation().getEstimatedDeliveryTime().toLocalTime();
                        if (deliveryTime.isAfter(startTime) && deliveryTime.isBefore(endTime)) {
                            return true; // Le conducteur est disponible à cette heure pour la livraison
                        }
                    }
                }
            }
        }
        return false; // Le conducteur n'est pas disponible pour la livraison à cette heure
    }


    private void acceptOrRefuseDelivery(DriverDecisionRequest decisionRequest){
        Long deliveryId = decisionRequest.getDeliveryId();
        boolean accept = decisionRequest.isAccept();

        // Récupérer la livraison à partir de l'identifiant
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new EntityNotFoundException("Delivery not found with id: " + deliveryId));

        if (accept) {
            // Le conducteur accepte la livraison
            // Mettre à jour le statut de la livraison et effectuer d'autres opérations nécessaires
            delivery.setStatus(Status.ASSIGNED);
        } else {
            // Le conducteur refuse la livraison
            // Gérer le cas où la livraison est refusée
        }
    }
}
