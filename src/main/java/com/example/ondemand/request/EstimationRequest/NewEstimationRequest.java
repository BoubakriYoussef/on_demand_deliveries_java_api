package com.example.ondemand.request.EstimationRequest;

import com.example.ondemand.enumClass.PaymentMethod;
import com.example.ondemand.enumClass.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class NewEstimationRequest {
    private double distance;
    private double estimatedFee;
    private Duration estimatedDeliveryTime;
    private Duration estimatedPickUpTime;
    private String pricingStrategyName;

    // Champs pour l'adresse du restaurant
    private String restaurantBuilding;
    private String restaurantStreet;
    private String restaurantFloor;
    private String restaurantAdditionalInfos;
    private String restaurantLandmark;
    private double restaurantLatitude;
    private double restaurantLongitude;

    // Champs pour l'adresse du client
    private String customerBuilding;
    private String customerStreet;
    private String customerFloor;
    private String customerAdditionalInfos;
    private String customerLandmark;
    private double customerLatitude;
    private double customerLongitude;

    // Champs pour le client
    private String customerName;
    private String customerEmail;
    private String customerPhoneNB;

    // Champs pour la commande
    private double orderAmount;
    private String orderDescr;
    private LocalDateTime orderTime;

    // Champs pour la livraison
    private Status deliveryStatus;
    private PaymentMethod deliveryPaymentMethod;

    // Champs pour le paiement
    private double paymentAmount;
    private LocalDateTime paymentTime;
    private double totalValue;
    private boolean isWithdrawDone;

    // Champ pour le pourboire
    private double tipAmount;

    // Champs pour le restaurant
    private String restaurantName;
    private String restaurantPhoneNumber;

    // Champs pour l'utilisateur
    private String userFullName;
    private String userEmail;
    private String userPhone;
}
