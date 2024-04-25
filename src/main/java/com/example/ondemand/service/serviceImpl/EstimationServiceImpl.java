package com.example.ondemand.service.serviceImpl;

import com.example.ondemand.authentication.AuthService.AuthenticationService;
import com.example.ondemand.dto.EstimationRequest.*;
import com.example.ondemand.entities.*;
import com.example.ondemand.repositories.*;
import com.example.ondemand.service.EstimationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class EstimationServiceImpl implements EstimationService {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private PricingStrategyRepository pricingStrategyRepository;

    @Autowired
    private EstimationRepository estimationRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    TipRepository tipRepository;

    @Override
    public Estimation createEstimation(NewEstimationRequest request,
                                       AddressRequest addressRequest,
                                       CustomerRequest customerRequest,
                                       OrderRequest orderRequest,
                                       DeliveryRequest deliveryRequest,
                                       PaymentRequest paymentRequest,
                                       TipRequest tipRequest,
                                       String pricingStrategyName
                                       ) {
        //Get Authenticated user
        User authenticatedUser = authenticationService.getAuthenticatedUser();

        //Recuperate PricingStrategy from Database
        PricingStrategy pricingStrategy = pricingStrategyRepository.findByName(pricingStrategyName);

        //Recuperer restaurants par Authenticated User
        Restaurant restaurant = getRestaurantByAuthenticatedUser(authenticatedUser);

        //Créer nouvelle adresse
        Address address = new Address();
        address.setBuilding(addressRequest.getBuilding());
        address.setFloor(addressRequest.getFloor());
        address.setAdditionalInfos(addressRequest.getAdditionalInfos());
        address.setLandmark(addressRequest.getLandmark());
        address.setLatitude(addressRequest.getLatitude());
        address.setLongitude(addressRequest.getLongitude());
        addressRepository.save(address);

        //Créer nouveau Customer
        Customer customer = new Customer();
        customer.setName(customerRequest.getName());
        customer.setEmail(customerRequest.getEmail());
        customer.setPhoneNB(customerRequest.getPhoneNB());
        customer.setAddress(address);
        customerRepository.save(customer);

        //Créer nouveau Order
        Orders order = new Orders();
        order.setOrderAmount(orderRequest.getOrderAmount());
        order.setOrderDescription(orderRequest.getOrderDescr());
        order.setOrderTime(orderRequest.getOrderTime());
        order.setCustomer(customer);
        orderRepository.save(order);

        //Créer nouveau Tip
        Tip tip = new Tip();
        tip.setTipAmount(tipRequest.getTipAmount());
        tipRepository.save(tip);

        //Créer nouveau Paiement
        Payment payment = new Payment();
        payment.setPaymentAmount(payment.getPaymentAmount());
        payment.setPaymentTime(paymentRequest.getPaymentTime());
        payment.setTotalValue(payment.getTotalValue());
        payment.setWithdrawDone(payment.isWithdrawDone());
        payment.setTip(tip);
        paymentRepository.save(payment);

        //Créer Nouvelle Delivery
        Delivery delivery = new Delivery();
        delivery.setStatus(deliveryRequest.getStatus());
        delivery.setPaymentMethod(deliveryRequest.getPaymentMethod());
        delivery.setOrders(order);
        deliveryRepository.save(delivery);

        //Créer nouvelle estimation
        Estimation estimation  = new Estimation();
        estimation.setDistance(request.getDistance());
        estimation.setEstimatedFee(request.getEstimatedFee());
        estimation.setEstimatedDeliveryTime(request.getEstimatedDeliveryTime());
        estimation.setEstimatedPickUpTime(request.getEstimatedPickUpTime());
        estimation.setPricingStrategy(pricingStrategy);
        estimation.setDelivery(delivery);
        estimation.setRestaurant(restaurant);
        return estimationRepository.save(estimation);
    }

    // Méthode pour récupérer les restaurants associés à l'utilisateur authentifié
    public Restaurant getRestaurantByAuthenticatedUser(User authenticatedUser) {
        return restaurantRepository.findByUser(authenticatedUser);
    }
}
