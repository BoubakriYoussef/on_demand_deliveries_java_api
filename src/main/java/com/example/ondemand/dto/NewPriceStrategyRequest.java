package com.example.ondemand.dto;


import com.example.ondemand.EnumClass.UnitOfMeasure;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewPriceStrategyRequest {
    private String name;

    @JsonProperty("unitOfMeasure")
    private UnitOfMeasure unitOfMeasure;
    private double deliveryFeePerMile;
    private double deliveryFeePerKilometer;
    private double serviceFee;

    public boolean isNamePresent(){
        return name != null;
    }

    public boolean isUnitOfMeasurePresent(){
        return unitOfMeasure != null;
    }

    public boolean isDeliveryFeePerMilePresent(){
        return deliveryFeePerMile != 0;
    }

    public boolean isDeliveryFeePerKilometerPresent(){
        return deliveryFeePerKilometer != 0;
    }

    public boolean isServiceFeePresent(){
        return serviceFee != 0;
    }

    public UnitOfMeasure getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }
}
