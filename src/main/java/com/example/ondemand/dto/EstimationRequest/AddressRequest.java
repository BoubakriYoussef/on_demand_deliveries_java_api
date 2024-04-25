package com.example.ondemand.dto.EstimationRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequest {
    private String building;
    private String street;
    private String floor;
    private String additionalInfos;
    private String landmark;
    private double latitude;
    private double longitude;

}
