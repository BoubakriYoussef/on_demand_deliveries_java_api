package com.example.ondemand.dto.EstimationRequest;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantRequest {

    private String name;
    private String phoneNumber;
    private AddressRequest addressRequest;

    private UserRequest userRequest;
}
