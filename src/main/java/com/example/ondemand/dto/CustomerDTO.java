package com.example.ondemand.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {
    private String name;
    private String customerEmail;
    private String customerPhoneNB;
    private AddressDTO address;

}