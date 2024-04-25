package com.example.ondemand.service;

import com.example.ondemand.dto.NewPriceStrategyRequest;
import com.example.ondemand.entities.PricingStrategy;
import com.example.ondemand.entities.User;

import java.util.List;
import java.util.Optional;

public interface PricingStrategyService {

    PricingStrategy addPricingStrategy(NewPriceStrategyRequest request);
    List<PricingStrategy> getAllPricingStrategies();
    void deletePricingStrategy(Long id);
    PricingStrategy updatePricingStrategy(Long id, NewPriceStrategyRequest request);

    Optional<PricingStrategy> getPricingStrategyById(Long id);
}
