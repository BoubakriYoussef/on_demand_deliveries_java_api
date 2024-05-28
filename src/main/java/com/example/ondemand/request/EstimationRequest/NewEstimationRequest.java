package com.example.ondemand.request.EstimationRequest;


import com.example.ondemand.enumClass.EstimationStatus;
import com.example.ondemand.enumClass.PaymentMethod;
import com.example.ondemand.enumClass.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor



public class NewEstimationRequest {
    private double distance;
    private double estimatedFee;
    private LocalDateTime estimatedDeliveryTime;
    private LocalDateTime estimatedPickUpTime;
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

    private LocalDateTime requestedDeliveryTime;

    private OrderType orderType;

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

    //Champs pour le Rate
    private double rating;
    private String commentary;
    private LocalDateTime evaluatedAt;
    private LocalDateTime updatedAt;


    // Champs pour l'utilisateur
    private String userFullName;
    private String userEmail;
    private String userPhone;

    private EstimationStatus estimationStatus = EstimationStatus.PENDING;

    // Presence check methods for all fields
    public boolean isDistancePresent() {
        return distance != 0;
    }

    public boolean isEstimationStatusPresent() {
        return estimationStatus != null;
    }

    public boolean isEstimatedFeePresent() {
        return estimatedFee != 0;
    }

    public boolean isEstimatedDeliveryTimePresent() {
        return estimatedDeliveryTime != null;
    }

    public boolean isEstimatedPickUpTimePresent() {
        return estimatedPickUpTime != null;
    }

    public boolean isPricingStrategyNamePresent() {
        return pricingStrategyName != null;
    }

    // Presence check methods for restaurant address fields
    public boolean isRestaurantBuildingPresent() {
        return restaurantBuilding != null;
    }

    public boolean isRestaurantStreetPresent() {
        return restaurantStreet != null;
    }

    public boolean isRestaurantFloorPresent() {
        return restaurantFloor != null;
    }

    public boolean isRestaurantAdditionalInfosPresent() {
        return restaurantAdditionalInfos != null;
    }

    public boolean isRestaurantLandmarkPresent() {
        return restaurantLandmark != null;
    }

    public boolean isRestaurantLatitudePresent() {
        return restaurantLatitude != 0; // Check for non-zero value
    }

    public boolean isRestaurantLongitudePresent() {
        return restaurantLongitude != 0; // Check for non-zero value
    }

    // Presence check methods for customer address fields
    public boolean isCustomerBuildingPresent() {
        return customerBuilding != null;
    }

    public boolean isCustomerStreetPresent() {
        return customerStreet != null;
    }

    public boolean isCustomerFloorPresent() {
        return customerFloor != null;
    }

    public boolean isCustomerAdditionalInfosPresent() {
        return customerAdditionalInfos != null;
    }

    public boolean isCustomerLandmarkPresent() {
        return customerLandmark != null;
    }

    public boolean isCustomerLatitudePresent() {
        return customerLatitude != 0; // Check for non-zero value
    }

    public boolean isCustomerLongitudePresent() {
        return customerLongitude != 0; // Check for non-zero value
    }

    // Presence check methods for customer fields
    public boolean isCustomerNamePresent() {
        return customerName != null;
    }

    public boolean isCustomerEmailPresent() {
        return customerEmail != null;
    }

    public boolean isCustomerPhoneNBPresent() {
        return customerPhoneNB != null;
    }

    // Presence check methods for order fields
    public boolean isOrderAmountPresent() {
        return orderAmount != 0; // Check for non-zero value
    }

    public boolean isOrderDescrPresent() {
        return orderDescr != null;
    }

    public boolean isOrderTimePresent() {
        return orderTime != null;
    }

    // Presence check methods for delivery fields
    public boolean isDeliveryStatusPresent() {
        return deliveryStatus != null;
    }

    public boolean isDeliveryPaymentMethodPresent() {
        return deliveryPaymentMethod != null;
    }

    // Presence check methods for payment fields
    public boolean isPaymentAmountPresent() {
        return paymentAmount != 0; // Check for non-zero value
    }

    public boolean isPaymentTimePresent() {
        return paymentTime != null;
    }

    public boolean isTotalValuePresent() {
        return totalValue != 0; // Check for non-zero value
    }

    public boolean isIsWithdrawDonePresent() {
        return true; // Boolean doesn't require null check
    }

    // Presence check method for tip amount
    public boolean isTipAmountPresent() {
        return tipAmount != 0; // Check for non-zero value
    }

    // Presence check methods for restaurant fields
    public boolean isRestaurantNamePresent() {
        return restaurantName != null;
    }

    public boolean isRestaurantPhoneNumberPresent() {
        return restaurantPhoneNumber != null;
    }

    // Presence check methods for user fields
    public boolean isUserFullNamePresent() {
        return userFullName != null;
    }

    public boolean isUserEmailPresent() {
        return userEmail != null;
    }

    public boolean isUserPhonePresent() {
        return userPhone != null;
    }

}