package com.example.ondemand.service.serviceImpl;

import com.example.ondemand.authentication.authService.AuthenticationService;
import com.example.ondemand.entities.Delivery;
import com.example.ondemand.entities.ProposedDelivery;
import com.example.ondemand.entities.ProposedDeliveryStatus;
import com.example.ondemand.entities.User;
import com.example.ondemand.enumClass.Decision;
import com.example.ondemand.enumClass.Status;
import com.example.ondemand.exceptions.DeliveryNotFoundException;
import com.example.ondemand.exceptions.NoAvailableDriversException;
import com.example.ondemand.repositories.DeliveryRepository;
import com.example.ondemand.repositories.ProposedDeliveryRepository;
import com.example.ondemand.repositories.UserRepository;
import com.example.ondemand.request.DeliveryRequest.DriverDecisionRequest;
import com.example.ondemand.service.DeliveryService;
import com.example.ondemand.service.ProposedDeliveryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;


@Service
public class ProposedDeliveryServiceImpl implements ProposedDeliveryService {


    @Autowired
    private DeliveryRepository deliveryRepository; // Inject DeliveryRepository

    @Autowired
    private UserRepository userRepository; // Inject UserRepository

    @Autowired
    private ProposedDeliveryRepository proposedDeliveryRepository; // Inject ProposedDeliveryRepository

    @Autowired
    private DeliveryService deliveryService;

    @Autowired
    private AuthenticationService authenticationService;

    @Override
    public void proposeDeliveryToDriver(Long deliveryId) {
        // Retrieve the delivery by its ID
        Optional<Delivery> optionalDelivery = deliveryRepository.findById(deliveryId);

        if (optionalDelivery.isPresent()) {
            Delivery delivery = optionalDelivery.get();

            // Retrieve all available drivers
            List<User> availableDrivers = userRepository.findAllAvailableDrivers();

            if (availableDrivers.isEmpty()) {
                throw new NoAvailableDriversException("No available drivers to propose the delivery.");
            }

            // Filter out drivers who have already been proposed this delivery
            List<User> driversNotProposed = availableDrivers.stream()
                    .filter(driver -> proposedDeliveryRepository.findByUserAndDelivery(driver, delivery).isEmpty())
                    .collect(Collectors.toList());

            if (driversNotProposed.isEmpty()) {
                throw new NoAvailableDriversException("No new drivers available to propose the delivery.");
            }

            // Select a random driver
            Random random = new Random();
            User randomDriver = driversNotProposed.get(random.nextInt(driversNotProposed.size()));

            // Set the expiration time for the proposal (1 minute from now)
            LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(1);

            // Create ProposedDelivery entity
            ProposedDelivery proposedDelivery = new ProposedDelivery();
            proposedDelivery.setUser(randomDriver);
            proposedDelivery.setDelivery(delivery);
            proposedDelivery.setExpirationTime(expirationTime);
            proposedDelivery.setCreatedAt(LocalDateTime.now());
            proposedDelivery.setStatus(ProposedDeliveryStatus.PENDING);

            // Save the proposed delivery
            proposedDeliveryRepository.save(proposedDelivery);

            // Other actions may include notifying the driver or logging the proposal
        } else {
            throw new DeliveryNotFoundException("Delivery not found with ID: " + deliveryId);
        }
    }

    @Override
    public void acceptOrRejectProposedDelivery(Long proposedDeliveryId, DriverDecisionRequest driverDecisionRequest) {

        User authenticatedUser = authenticationService.getAuthenticatedUser();

        ProposedDelivery proposedDelivery = proposedDeliveryRepository.findById(proposedDeliveryId)
                .orElseThrow(() -> new EntityNotFoundException("Proposed delivery not found with id: " + proposedDeliveryId));

        if (proposedDelivery.getExpirationTime().isBefore(LocalDateTime.now())) {
            System.out.println("La livraison proposée est expirée.");
            proposedDelivery.setStatus(ProposedDeliveryStatus.IGNORED);
            proposedDeliveryRepository.save(proposedDelivery);
            return;
        }

        if (driverDecisionRequest.getDecision() == Decision.ACCEPTED) {
            deliveryService.assignDriverToDelivery(proposedDelivery.getDelivery().getId(), authenticatedUser.getId());
            deliveryService.updateDeliveryStatus(proposedDelivery.getDelivery().getId(), Status.ASSIGNED);
            proposedDelivery.setStatus(ProposedDeliveryStatus.ACCEPTED);
        } else {
            proposedDelivery.setStatus(ProposedDeliveryStatus.REFUSED);
        }
        proposedDeliveryRepository.save(proposedDelivery);
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void checkAndMarkExpiredProposals() {
        LocalDateTime now = LocalDateTime.now();
        List<ProposedDelivery> expiredProposals = proposedDeliveryRepository.findByStatusAndExpirationTimeBefore(ProposedDeliveryStatus.PENDING, now);

        for (ProposedDelivery proposal : expiredProposals) {
            proposal.setStatus(ProposedDeliveryStatus.IGNORED);
            proposedDeliveryRepository.save(proposal);
        }
    }

    @Override
    public List<ProposedDelivery> getProposedDeliveriesForUser(User user){
        return proposedDeliveryRepository.findByUser(user);
    }
}