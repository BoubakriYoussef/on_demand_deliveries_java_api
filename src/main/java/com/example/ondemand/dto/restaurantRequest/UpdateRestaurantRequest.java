package com.example.ondemand.dto.restaurantRequest;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRestaurantRequest {
        private String restaurantName;
        private String phoneNumber;
        private String building;
        private String street;
        private String floor;
        private String additionalInfos;
        private String landmark;
        private double latitude;
        private double longitude;

         public boolean isRestaurantNamePresent(){
                return restaurantName != null;
         }

    public boolean isPhoneNumberPresent(){
        return phoneNumber != null;
    }

    public boolean isBuildingPresent(){
        return building != null;
    }

    public boolean isStreetPresent(){
        return street != null;
    }

    public boolean isFloorPresent(){
        return floor != null;
    }

    public boolean isAdditionalInfosPresent(){
        return additionalInfos != null;
    }

    public boolean isLandmarkPresent(){
        return landmark != null;
    }

    public boolean isLatitudePresent(){
        return latitude != 0;
    }

    public boolean isLongitudePresent(){
        return longitude != 0;
    }
}

