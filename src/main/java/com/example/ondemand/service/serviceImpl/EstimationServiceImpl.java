package com.example.ondemand.service.serviceImpl;

import com.example.ondemand.authentication.authService.AuthenticationService;
import com.example.ondemand.enumClass.PaymentMethod;
import com.example.ondemand.enumClass.Status;
import com.example.ondemand.request.EstimationRequest.*;
import com.example.ondemand.entities.*;
import com.example.ondemand.repositories.*;
import com.example.ondemand.request.restaurantRequest.UpdateRestaurantRequest;
import com.example.ondemand.service.EstimationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


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
    private TipRepository tipRepository;


    //Créer Estimation

    @Override
    public Estimation createEstimation(NewEstimationRequest request) {
        // Get Authenticated user
        User authenticatedUser = authenticationService.getAuthenticatedUser();

        // Retrieve PricingStrategy from Database
        PricingStrategy pricingStrategy = pricingStrategyRepository.findByName(request.getPricingStrategyName());

        // Create restaurant address
        Address restaurantAddress = createAddress(request.getRestaurantBuilding(), request.getRestaurantStreet(),
                request.getRestaurantFloor(), request.getRestaurantAdditionalInfos(), request.getRestaurantLandmark(),
                request.getRestaurantLatitude(), request.getRestaurantLongitude());

        // Get or create restaurant by AuthenticatedUser
        Restaurant restaurant = getOrCreateRestaurant(authenticatedUser, request.getRestaurantName(), request.getRestaurantPhoneNumber(), restaurantAddress);

        // Create customer address
        Address customerAddress = createAddress(request.getCustomerBuilding(), request.getCustomerStreet(),
                request.getCustomerFloor(), request.getCustomerAdditionalInfos(), request.getCustomerLandmark(),
                request.getCustomerLatitude(), request.getCustomerLongitude());

        // Create new Customer
        Customer customer = createCustomer(request.getCustomerName(), request.getCustomerEmail(),
                request.getCustomerPhoneNB(), customerAddress);

        // Create new Order
        Orders order = createOrder(request.getOrderAmount(), request.getOrderDescr(),
                request.getOrderTime(), customer);

        // Create new Tip
        Tip tip = createTip(request.getTipAmount());

        // Create new Payment
        Payment payment = createPayment(request.getPaymentAmount(), request.getPaymentTime(),
                request.getTotalValue(), request.isWithdrawDone(), tip);

        // Create new Delivery
        Delivery delivery = createDelivery(request.getDeliveryStatus(), request.getDeliveryPaymentMethod(), order);

        // Create new Estimation
        Estimation estimation = createEstimation(request.getDistance(), request.getEstimatedFee(),
                request.getEstimatedDeliveryTime(), request.getEstimatedPickUpTime(), pricingStrategy,
                delivery, restaurant);

        return estimationRepository.save(estimation);
    }


    //Delete Estimation by id
    @Override
    public void deleteEstimation(Long id) {
        estimationRepository.deleteById(id);
    }


    //Get list of estimations
    @Override
    public List<Estimation> getAllEstimationsByAuthenticatedUser(User user) {
        User authenticatedUser = authenticationService.getAuthenticatedUser();
        return estimationRepository.findByRestaurantUser(authenticatedUser);
    }


    //update estimation
    @Override
    public Estimation updateEstimation(Long estimationId, UpdateEstimationRequest request) {
        // Retrieve the estimation from the database
        Estimation estimation = estimationRepository.findById(estimationId)
                .orElseThrow(() -> new EntityNotFoundException("Estimation not found with id: " + estimationId));

        // Update the estimation properties
        estimation.setDistance(request.getDistance());
        estimation.setEstimatedFee(request.getEstimatedFee());
        estimation.setEstimatedDeliveryTime(request.getEstimatedDeliveryTime());
        estimation.setEstimatedPickUpTime(request.getEstimatedPickUpTime());

        // Update related entities if needed
        Delivery delivery = estimation.getDelivery();
        delivery.setStatus(request.getDeliveryStatus());
        delivery.setPaymentMethod(request.getDeliveryPaymentMethod());

        Payment payment = delivery.getOrders().getDelivery().getPayment();
        payment.setPaymentAmount(request.getPaymentAmount());
        payment.setPaymentTime(request.getPaymentTime());
        payment.setTotalValue(request.getTotalValue());
        payment.setWithdrawDone(request.isWithdrawDone());

        Tip tip = payment.getTip();
        tip.setTipAmount(request.getTipAmount());

        Customer customer = delivery.getOrders().getCustomer();
        customer.setName(request.getCustomerName());
        customer.setEmail(request.getCustomerEmail());
        customer.setPhoneNB(request.getCustomerPhoneNB());

        Address customerAddress = customer.getAddress();
        customerAddress.setBuilding(request.getCustomerBuilding());
        customerAddress.setStreet(request.getCustomerStreet());
        customerAddress.setFloor(request.getCustomerFloor());
        customerAddress.setAdditionalInfos(request.getCustomerAdditionalInfos());
        customerAddress.setLandmark(request.getCustomerLandmark());
        customerAddress.setLatitude(request.getCustomerLatitude());
        customerAddress.setLongitude(request.getCustomerLongitude());

        Restaurant restaurant = estimation.getRestaurant();
        restaurant.setName(request.getRestaurantName());
        restaurant.setPhoneNumber(request.getRestaurantPhoneNumber());

        Address restaurantAddress = restaurant.getAddress();
        restaurantAddress.setBuilding(request.getRestaurantBuilding());
        restaurantAddress.setStreet(request.getRestaurantStreet());
        restaurantAddress.setFloor(request.getRestaurantFloor());
        restaurantAddress.setAdditionalInfos(request.getRestaurantAdditionalInfos());
        restaurantAddress.setLandmark(request.getRestaurantLandmark());
        restaurantAddress.setLatitude(request.getRestaurantLatitude());
        restaurantAddress.setLongitude(request.getRestaurantLongitude());

        // Update User information if applicable
        User user = restaurant.getUser();
        if (user != null) {
            user.setFirstName(request.getUserFullName());
            user.setEmail(request.getUserEmail());
            user.setPhone(request.getUserPhone());
        }

        // Save the updated estimation
        return estimationRepository.save(estimation);
    }

    @Override
    public Optional<Estimation> findEstimationById(Long id) {
        return estimationRepository.findById(id);
    }


    // Create Address object
    private Address createAddress(String building, String street, String floor, String additionalInfos,
                                  String landmark, double latitude, double longitude) {
        Address address = new Address();
        address.setBuilding(building);
        address.setStreet(street);
        address.setFloor(floor);
        address.setAdditionalInfos(additionalInfos);
        address.setLandmark(landmark);
        address.setLatitude(latitude);
        address.setLongitude(longitude);
        return addressRepository.save(address);
    }

    // Get or create restaurant by AuthenticatedUser
    private Restaurant getOrCreateRestaurant(User authenticatedUser, String name, String phoneNumber, Address address) {
        Restaurant restaurant = restaurantRepository.findByUser(authenticatedUser);
        if (restaurant == null) {
            restaurant = new Restaurant();
            restaurant.setName(name);
            restaurant.setPhoneNumber(phoneNumber);
            restaurant.setAddress(address);
            restaurant.setUser(authenticatedUser);
            return restaurantRepository.save(restaurant);
        }
        return restaurant;
    }

    // Create Customer object
    private Customer createCustomer(String name, String email, String phoneNB, Address address) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setEmail(email);
        customer.setPhoneNB(phoneNB);
        customer.setAddress(address);
        return customerRepository.save(customer);
    }

    // Create Order object
    private Orders createOrder(double orderAmount, String orderDescr, LocalDateTime orderTime, Customer customer) {
        Orders order = new Orders();
        order.setOrderAmount(orderAmount);
        order.setOrderDescription(orderDescr);
        order.setOrderTime(orderTime);
        order.setCustomer(customer);
        return orderRepository.save(order);
    }

    // Create Tip object
    private Tip createTip(double tipAmount) {
        Tip tip = new Tip();
        tip.setTipAmount(tipAmount);
        return tipRepository.save(tip);
    }

    // Create Payment object
    private Payment createPayment(double paymentAmount, LocalDateTime paymentTime, double totalValue,
                                  boolean isWithdrawDone, Tip tip) {
        Payment payment = new Payment();
        payment.setPaymentAmount(paymentAmount);
        payment.setPaymentTime(paymentTime);
        payment.setTotalValue(totalValue);
        payment.setWithdrawDone(isWithdrawDone);
        payment.setTip(tip);
        return paymentRepository.save(payment);
    }

    // Create Delivery object
    private Delivery createDelivery(Status status, PaymentMethod paymentMethod, Orders order) {
        Delivery delivery = new Delivery();
        delivery.setStatus(status);
        delivery.setPaymentMethod(paymentMethod);
        delivery.setOrders(order);
        return deliveryRepository.save(delivery);
    }

    // Create Estimation object
    private Estimation createEstimation(double distance, double estimatedFee, Duration estimatedDeliveryTime,
                                        Duration estimatedPickUpTime, PricingStrategy pricingStrategy,
                                        Delivery delivery, Restaurant restaurant) {
        Estimation estimation = new Estimation();
        estimation.setDistance(distance);
        estimation.setEstimatedFee(estimatedFee);
        estimation.setEstimatedDeliveryTime(estimatedDeliveryTime);
        estimation.setEstimatedPickUpTime(estimatedPickUpTime);
        estimation.setPricingStrategy(pricingStrategy);
        estimation.setDelivery(delivery);
        estimation.setRestaurant(restaurant);
        return estimation;
    }



























}
