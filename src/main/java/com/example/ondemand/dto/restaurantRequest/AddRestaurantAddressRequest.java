package com.example.ondemand.dto.restaurantRequest;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddRestaurantAddressRequest {

    private String restaurantName;
    private String phoneNumber;
    private String building;
    private String street;
    private String floor;
    private String additionalInfos;
    private String landmark;
    private double latitude;
    private double longitude;

}
