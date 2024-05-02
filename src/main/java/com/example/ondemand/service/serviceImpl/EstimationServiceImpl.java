package com.example.ondemand.service.serviceImpl;


import com.example.ondemand.enumClass.UnitOfMeasure;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.ondemand.authentication.authService.AuthenticationService;
import com.example.ondemand.enumClass.PaymentMethod;
import com.example.ondemand.enumClass.Status;
import com.example.ondemand.request.EstimationRequest.*;
import com.example.ondemand.entities.*;
import com.example.ondemand.repositories.*;
import com.example.ondemand.service.EstimationService;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.query.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


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


    @Autowired
    private RateRepository rateRepository;

    private static final String key = "5b3ce3597851110001cf6248eb36aaad5afb41909b627fc97e724c15";


    // Calcul de la distance entre Customer & Restaurant en utilisant lat et long de la requête NewEstimationRequest
    @Override
    public double calculateRoadDistance(NewEstimationRequest newEstimationRequest) throws JsonProcessingException, JsonMappingException {

        // Construction de l'URL de la requête
        String url = "https://api.openrouteservice.org/v2/directions/driving-car?api_key=" + key
                + "&start=" + newEstimationRequest.getCustomerLongitude() + "," + newEstimationRequest.getCustomerLatitude()
                + "&end=" + newEstimationRequest.getRestaurantLongitude() + "," + newEstimationRequest.getRestaurantLatitude();


        // Envoi de la requête HTTP get
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);

        // Parse de la reponse JSON pour extraire la distance
        JsonNode root = new ObjectMapper().readTree(response);
        double distance = root.at("/features/0/properties/segments/0/distance").asDouble();

        return distance/1000.0;
    }

    // Estimer le prix de livraison de la commande

    @Override
    public double estimateDeliveryFee(NewEstimationRequest newEstimationRequest) throws JsonProcessingException {

        //Calculate distance using calculateRoadDistance method
        double distance = calculateRoadDistance(newEstimationRequest);

        //Recuperate pricing strategy with her fields
        PricingStrategy pricingStrategy = pricingStrategyRepository.findByName(newEstimationRequest.getPricingStrategyName());

        UnitOfMeasure unitOfMeasure = pricingStrategy.getUnitOfMeasure();

        double deliveryFeePerMile  = pricingStrategy.getDeliveryFeePerMile();

        double deliveryFeePerKilometer = pricingStrategy.getDeliveryFeePerKilometer();

        double serviceFee = pricingStrategy.getServiceFee();

        double tva = pricingStrategy.getTva();

        double minimalFee = pricingStrategy.getMinimalFee();


        double minimalDistance = pricingStrategy.getMinimalDistance() ;

        double estimatedFee= 0;

        if(unitOfMeasure == unitOfMeasure.KM){
            double k = (distance - minimalDistance) * deliveryFeePerKilometer;
            estimatedFee = k + minimalFee + tva + serviceFee;
        }
        else if(unitOfMeasure == unitOfMeasure.MILE) {
            double m = (distance - minimalDistance) * deliveryFeePerMile;
            estimatedFee = m + minimalFee + tva + serviceFee;
        }

        return estimatedFee;

    }


    //Estimer le temps de livraison si l'ORDER est instantané

    @Override
    public LocalDateTime estimateDeliveryTime(NewEstimationRequest newEstimationRequest) throws JsonProcessingException {

        //Order Time
        LocalDateTime orderTime = newEstimationRequest.getOrderTime();

        //ORDER TYPE
        OrderType orderType = newEstimationRequest.getOrderType();

        //PREPARATION
        double preparationTime = 0.25;

        //DISTANCE
        double distance = calculateRoadDistance(newEstimationRequest);


        //Vitesse moyenne du livreur sur la moto
        double driverAverageSpeed = 40.0;

        double duration = 0;

        //INSTANT ORDER
        //0.08 heures pour les retards imprévus
        if (orderType == OrderType.INSTANT) {
            double drivingTime = distance / driverAverageSpeed;
            duration = drivingTime + preparationTime + 0.08;
        }


        //Conversion de duration en long
        LocalDateTime estimatedDeliveryTime = orderTime.plusHours((long) duration);

        return estimatedDeliveryTime;
    }



    //Estimer le temps de récupération si l'ORDER est plannifié
    @Override
    public LocalDateTime estimatePickUpTime(NewEstimationRequest newEstimationRequest) throws JsonProcessingException {
        //Order Time
        LocalDateTime orderTime = newEstimationRequest.getOrderTime();

        //Order Type
        OrderType orderType = newEstimationRequest.getOrderType();

        double distance = calculateRoadDistance(newEstimationRequest);

        LocalDateTime requestedDeliveryTime = newEstimationRequest.getRequestedDeliveryTime();

        double drivingTime = 0.0;

        double averageSpeed = 40.0;

        LocalDateTime estimatedPickUpTime = null;

        if(orderType == OrderType.PLANNED){
            drivingTime = distance / averageSpeed ;
            estimatedPickUpTime = requestedDeliveryTime.minusHours((long) (drivingTime * 60));
            return estimatedPickUpTime;
        }
        return estimatedPickUpTime;

    }


    //Créer Estimation

    @Override
    public Estimation createEstimation(NewEstimationRequest request) throws JsonProcessingException {
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
                request.getOrderTime(), request.getRequestedDeliveryTime(), customer);

        // Create new Tip
        Tip tip = createTip(request.getTipAmount());

        // Create new Payment
        Payment payment = createPayment(request.getPaymentAmount(), request.getPaymentTime(),
                request.getTotalValue(), request.isWithdrawDone(), tip);

        //Create User
        User user = new User();

        //Create Rate
        Rate rate = createRate(request.getRating(), request.getCommentary(), request.getEvaluatedAt(), request.getUpdatedAt());

        // Create new Delivery
        Delivery delivery = createDelivery(request.getDeliveryStatus(), request.getDeliveryPaymentMethod(), order, user, rate);



        // Create new Estimation
        Estimation estimation = createEstimation(pricingStrategy,delivery,restaurant,request);

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
    public Estimation updateEstimation(Long estimationId, NewEstimationRequest request) throws JsonProcessingException {
        // Retrieve the estimation from the database
        Estimation estimation = estimationRepository.findById(estimationId)
                .orElseThrow(() -> new EntityNotFoundException("Estimation not found with id: " + estimationId));


        double distance = calculateRoadDistance(request);

        double estimatedFee = estimateDeliveryFee(request);

        LocalDateTime estimatedDeliveryTime = estimateDeliveryTime(request);

        LocalDateTime estimatedPickUpTime = estimatePickUpTime(request);


        // Update the estimation properties
        estimation.setDistance(distance);
        estimation.setEstimatedFee(estimatedFee);
        estimation.setEstimatedDeliveryTime(estimatedDeliveryTime);
        estimation.setEstimatedPickUpTime(estimatedPickUpTime);
        estimation.setUuid(UUID.randomUUID().toString());

        // Update related entities if needed
        Delivery delivery = estimation.getDelivery();
        delivery.setStatus(request.getDeliveryStatus());
        delivery.setPaymentMethod(request.getDeliveryPaymentMethod());
        delivery.setUser(null);

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
        address.setUuid(UUID.randomUUID().toString());
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
            restaurant.setUuid(UUID.randomUUID().toString());
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
    private Orders createOrder(double orderAmount, String orderDescr, LocalDateTime orderTime, LocalDateTime requestedDeliveryTime,Customer customer) {
        Orders order = new Orders();
        order.setOrderAmount(orderAmount);
        order.setOrderDescription(orderDescr);
        order.setOrderTime(orderTime);
        order.setRequestedDeliveryTime(requestedDeliveryTime);
        order.setCustomer(customer);
        order.setUuid(UUID.randomUUID().toString());
        return orderRepository.save(order);
    }


    // Create Tip object
    private Tip createTip(double tipAmount) {
        Tip tip = new Tip();
        tip.setTipAmount(tipAmount);
        // You can set other fields of Tip here if needed
        tip.setUuid(UUID.randomUUID().toString());
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

        // Check if the tip is null or if all fields of the tip are empty
        if (tip == null || tip.getTipAmount() == 0 /* add other fields checks */) {
            // If tip is null or all fields are empty, set payment's tip to null
            payment.setTip(null);
        } else {
            // If tip is not null or has some valid data, associate it with the payment
            payment.setTip(tip);
        }

        payment.setUuid(UUID.randomUUID().toString());

        // Save payment only if it contains valid data
        return paymentRepository.save(payment);
    }



    // Create Delivery object
    private Delivery createDelivery(Status status, PaymentMethod paymentMethod, Orders order, User user, Rate rate) {
        Delivery delivery = new Delivery();
        delivery.setStatus(status);
        delivery.setPaymentMethod(paymentMethod);
        delivery.setOrders(order);
        delivery.setRate(rate);

        // Check if the user is null or not
        if (user != null) {
            delivery.setUser(user);
        } else {
            // Create a new user with empty fields or null values
            User emptyUser = new User();
            // Set default values or leave fields empty as per your application's requirements
            emptyUser.setFirstName("");
            emptyUser.setEmail("");
            emptyUser.setPhone("");

            // Associate the empty user with the delivery
            delivery.setUser(emptyUser);
        }

        delivery.setUuid(UUID.randomUUID().toString());

        return deliveryRepository.save(delivery);
    }


    // Create Estimation object
    public Estimation createEstimation(PricingStrategy pricingStrategy, Delivery delivery, Restaurant restaurant, NewEstimationRequest newEstimationRequest) throws JsonProcessingException, JsonMappingException {

        double distance = calculateRoadDistance(newEstimationRequest);

        double estimatedFee = estimateDeliveryFee(newEstimationRequest);

        LocalDateTime estimatedDeliveryTime = estimateDeliveryTime(newEstimationRequest);

        LocalDateTime estimatedPickUpTime = estimatePickUpTime(newEstimationRequest);

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

    public Rate createRate(double rating, String commentary, LocalDateTime evaluatedAt, LocalDateTime updatedAt){
        Rate rate = new Rate();
        rate.setRating(rating);
        rate.setCommentary(commentary);
        rate.setEvaluatedAt(evaluatedAt);
        rate.setUpdatedAt(updatedAt);
        rate.setUuid(UUID.randomUUID().toString());
        return rateRepository.save(rate);
    }
}
