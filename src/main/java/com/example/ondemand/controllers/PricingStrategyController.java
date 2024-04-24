package com.example.ondemand.controllers;

import com.example.ondemand.dto.NewPriceStrategyRequest;
import com.example.ondemand.entities.PricingStrategy;
import com.example.ondemand.entities.User;
import com.example.ondemand.repositories.UserRepository;
import com.example.ondemand.service.PricingStrategyService;
import com.example.ondemand.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pricing-strategy")
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
    public ResponseEntity<List<PricingStrategy>> getAllPricingStrategies() {
        List<PricingStrategy> pricingStrategies = pricingStrategyService.getAllPricingStrategies();
        return ResponseEntity.ok(pricingStrategies);
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

