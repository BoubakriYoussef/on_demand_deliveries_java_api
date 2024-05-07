package com.example.ondemand.service;

import com.example.ondemand.request.PriceStrategyRequest.NewPriceStrategyRequest;
import com.example.ondemand.entities.PricingStrategy;

import java.util.List;
import java.util.Optional;

public interface PricingStrategyService {

    PricingStrategy addPricingStrategy(NewPriceStrategyRequest request);
    List<PricingStrategy> getAllPricingStrategies();
    void deletePricingStrategy(Long id);
    PricingStrategy updatePricingStrategy(Long id, NewPriceStrategyRequest request);

    Optional<PricingStrategy> getPricingStrategyById(Long id);
}
