package com.example.ondemand.controllers;


import com.example.ondemand.dto.EstimationRequest.*;
import com.example.ondemand.entities.Estimation;
import com.example.ondemand.service.EstimationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/estimation")
public class EstimationController {


    @Autowired
    EstimationService estimationService;

    @PostMapping("/addEstimation")
    public ResponseEntity<Estimation> createEstimation(@RequestBody NewEstimationRequest request,
                                                       @RequestBody AddressRequest addressRequest,
                                                       @RequestBody  CustomerRequest customerRequest,
                                                       @RequestBody  OrderRequest orderRequest,
                                                       @RequestBody  DeliveryRequest deliveryRequest,
                                                       @RequestBody  PaymentRequest paymentRequest,
                                                       @RequestBody TipRequest tipRequest,
                                                       String pricingStrategyName) {
        Estimation estimation = estimationService.createEstimation(request,addressRequest,customerRequest,orderRequest,deliveryRequest,paymentRequest,tipRequest,pricingStrategyName);
        return ResponseEntity.ok(estimation);
    }


}
