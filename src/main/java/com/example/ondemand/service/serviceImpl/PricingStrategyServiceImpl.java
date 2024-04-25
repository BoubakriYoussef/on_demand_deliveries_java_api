package com.example.ondemand.service.serviceImpl;

import com.example.ondemand.dto.NewPriceStrategyRequest;
import com.example.ondemand.entities.PricingStrategy;
import com.example.ondemand.repositories.PricingStrategyRepository;
import com.example.ondemand.service.PricingStrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PricingStrategyServiceImpl implements PricingStrategyService {

    @Autowired
    private PricingStrategyRepository pricingStrategyRepository;

    public PricingStrategy addPricingStrategy(NewPriceStrategyRequest request) {
        // Assurez-vous que l'utilisateur est présent avant de l'extraire

            PricingStrategy pricingStrategy = new PricingStrategy();
            pricingStrategy.setName(request.getName());
            pricingStrategy.setUnitOfMeasure(request.getUnitOfMeasure());
            pricingStrategy.setDeliveryFeePerMile(request.getDeliveryFeePerMile());
            pricingStrategy.setDeliveryFeePerKilometer(request.getDeliveryFeePerKilometer());
            pricingStrategy.setServiceFee(request.getServiceFee());

            return pricingStrategyRepository.save(pricingStrategy);

    }

    @Override
    public List<PricingStrategy> getAllPricingStrategies() {
        return pricingStrategyRepository.findAll();
    }

    @Override
    public void deletePricingStrategy(Long id) {
        pricingStrategyRepository.deleteById(id);
    }

    @Override
    public PricingStrategy updatePricingStrategy(Long id, NewPriceStrategyRequest request) {
        PricingStrategy pricingStrategy = pricingStrategyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pricing strategy not found with id: " + id));
        pricingStrategy.setName(request.getName());
        pricingStrategy.setUnitOfMeasure(request.getUnitOfMeasure());
        pricingStrategy.setDeliveryFeePerMile(request.getDeliveryFeePerMile());
        pricingStrategy.setDeliveryFeePerKilometer(request.getDeliveryFeePerKilometer());
        pricingStrategy.setServiceFee(request.getServiceFee());
        return pricingStrategyRepository.save(pricingStrategy);
    }

    @Override
    public Optional<PricingStrategy> getPricingStrategyById(Long id) {
        return pricingStrategyRepository.findPricingStrategiesById(id);
    }
}
