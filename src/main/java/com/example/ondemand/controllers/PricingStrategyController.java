package com.example.ondemand.controllers;

import com.example.ondemand.request.PriceStrategyRequest.NewPriceStrategyRequest;
import com.example.ondemand.entities.PricingStrategy;
import com.example.ondemand.repositories.UserRepository;
import com.example.ondemand.service.PricingStrategyService;
import com.example.ondemand.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pricing-strategy")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class PricingStrategyController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;


    @Autowired
    private PricingStrategyService pricingStrategyService;



    @PostMapping("/add")
    public ResponseEntity<PricingStrategy> addPricingStrategy(@RequestBody NewPriceStrategyRequest request)
    {
        PricingStrategy pricingStrategy = pricingStrategyService.addPricingStrategy(request);
        return ResponseEntity.ok(pricingStrategy);
    }


    @GetMapping("/all")
    public List<PricingStrategy> getAllPricingStrategies() {
        return pricingStrategyService.getAllPricingStrategies();
    }


    @GetMapping("/{id}")
    public Optional<PricingStrategy> getPricingStrategyById(@PathVariable Long id) {
        Optional<PricingStrategy> pricingStrategy = pricingStrategyService.getPricingStrategyById(id);
        return pricingStrategy;
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePricingStrategy(@PathVariable Long id) {
        pricingStrategyService.deletePricingStrategy(id);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<PricingStrategy> updatePricingStrategy(@PathVariable Long id, @RequestBody NewPriceStrategyRequest request) {
        PricingStrategy pricingStrategy = pricingStrategyService.updatePricingStrategy(id, request);
        return ResponseEntity.ok(pricingStrategy);
    }
}

