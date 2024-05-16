package com.example.ondemand.service.serviceImpl;


import com.example.ondemand.enumClass.EstimationStatus;
import com.example.ondemand.enumClass.UnitOfMeasure;
import com.example.ondemand.authentication.authService.AuthenticationService;
import com.example.ondemand.enumClass.PaymentMethod;
import com.example.ondemand.enumClass.Status;
import com.example.ondemand.request.EstimationRequest.*;
import com.example.ondemand.entities.*;
import com.example.ondemand.repositories.*;
import com.example.ondemand.service.EstimationService;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
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


    @Override
    public double calculateRoadDistance(NewEstimationRequest newEstimationRequest) throws IOException {
        double lat1 = Math.toRadians(newEstimationRequest.getCustomerLatitude());
        double lon1 = Math.toRadians(newEstimationRequest.getCustomerLongitude());
        double lat2 = Math.toRadians(newEstimationRequest.getRestaurantLatitude());
        double lon2 = Math.toRadians(newEstimationRequest.getRestaurantLongitude());

        double EARTH_RADIUS = 6371;


        // Calcul des différences de latitude et de longitude
        double deltaLat = lat2 - lat1;
        double deltaLon = lon2 - lon1;

        // Formule de Haversine
        double a = Math.pow(Math.sin(deltaLat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(deltaLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Calcul de la distance en utilisant le rayon de la Terre
        double distance = EARTH_RADIUS * c;
        return distance;

    }


    // Estimer le prix de livraison de la commande

        @Override
        public double estimateDeliveryFee(NewEstimationRequest newEstimationRequest) throws IOException {

            //Calculate distance using calculateRoadDistance method
            double distance = calculateRoadDistance(newEstimationRequest);

            //Recuperate pricing strategy with her fields
            PricingStrategy pricingStrategy = pricingStrategyRepository.findByName(newEstimationRequest.getPricingStrategyName());

            UnitOfMeasure unitOfMeasure = pricingStrategy.getUnitOfMeasure();

            double deliveryFeePerMile = pricingStrategy.getDeliveryFeePerMile();

            double deliveryFeePerKilometer = pricingStrategy.getDeliveryFeePerKilometer();

            double serviceFee = pricingStrategy.getServiceFee();

            double tva = pricingStrategy.getTva();

            double minimalFee = pricingStrategy.getMinimalFee();

            double minimalDistance = pricingStrategy.getMinimalDistance();

            double estimatedFee = 0;

            if (unitOfMeasure == UnitOfMeasure.KM) {
                double k = (distance - minimalDistance) * deliveryFeePerKilometer;
                estimatedFee = k + minimalFee + tva + serviceFee;
            } else if (unitOfMeasure == UnitOfMeasure.MILE) {
                double m = (distance - minimalDistance) * deliveryFeePerMile;
                estimatedFee = m + minimalFee + tva + serviceFee;
            }

            return estimatedFee;

        }

        //Estimer le temps de livraison si l'ORDER est instantané
        @Override
        public LocalDateTime estimateDeliveryTime (NewEstimationRequest request) throws IOException {
            // Define variables
            double preparationTime = 1;
            double distance = calculateRoadDistance(request);
            double driverAverageSpeed = 40;
            Duration duration = Duration.ZERO; // Initialize duration using Duration class
            LocalDateTime orderTime = request.getOrderTime();

            // Calculate transportation time in minutes
            int transportationTimeInMinutes = (int) Math.ceil(distance / driverAverageSpeed * 60);

            // Apply calculation for INSTANT orders
            if (request.getOrderType().equals(OrderType.INSTANT)) {
                duration = duration.ofMinutes(transportationTimeInMinutes)
                        .plusMinutes((int) (preparationTime * 60))
                        .plusMinutes(8); // Add preparation time and delay margin
            }

            // Calculate estimated delivery time
            LocalDateTime estimatedDeliveryTime = orderTime.plus(duration);

            // Return estimated delivery time and duration (without DeliveryTime class)
            return estimatedDeliveryTime;
        }


        //Estimer le temps de récupération si l'ORDER est plannifié
        @Override
        public LocalDateTime estimatePickUpTime (NewEstimationRequest newEstimationRequest) throws
                IOException {
            LocalDateTime orderTime = newEstimationRequest.getOrderTime();
            OrderType orderType = newEstimationRequest.getOrderType();
            double distance = calculateRoadDistance(newEstimationRequest);
            double averageSpeed = 40.0;
            LocalDateTime requestedDeliveryTime = newEstimationRequest.getRequestedDeliveryTime();
            LocalDateTime estimatedPickUpTime = null;

            if (orderType == OrderType.PLANNED) {
                Duration drivingTime = Duration.ofMinutes((long) (distance / averageSpeed * 60));
                estimatedPickUpTime = requestedDeliveryTime.minus(drivingTime);
                return estimatedPickUpTime;
            }

            return estimatedPickUpTime;
        }


        //Créer Estimation
        @Override
        public Estimation createEstimation (NewEstimationRequest request) throws IOException {
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
            Tip tip = createTip();

            // Create new Payment
            Payment payment = createPayment(tip);

            //Create Rate
            Rate rate = createRate();

            // Create new Delivery
            Delivery delivery = createDelivery(request.getDeliveryStatus(), request.getDeliveryPaymentMethod(), order, rate);


            // Create new Estimation
            Estimation estimation = createEstimation(pricingStrategy, delivery, restaurant, request);
            return estimationRepository.save(estimation);
        }


        //Delete Estimation by id
        @Override
        public void deleteEstimation (Long id){
            estimationRepository.deleteById(id);
        }


        //Get list of estimations
        @Override
        public List<Estimation> getAllEstimationsByAuthenticatedUser (User user){
            User authenticatedUser = authenticationService.getAuthenticatedUser();
            return estimationRepository.findByRestaurantUser(authenticatedUser);
        }


        //update estimation
        @Override
        public Estimation updateEstimation (Long estimationId, NewEstimationRequest request) throws
                IOException {
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
        public Optional<Estimation> findEstimationById (Long id){
            return estimationRepository.findById(id);
        }


        //Update Estimation status based on Manager Accepting or Refusing the Estimated Fee
        @Override
        public Estimation updateForAcceptOrRefuseEstimation (Long estimationId, ManagerDecisionRequest decisionRequest){
            // Récupérer l'estimation depuis la base de données
            Estimation estimation = estimationRepository.findById(estimationId)
                    .orElseThrow(() -> new EntityNotFoundException("Estimation not found with id: " + estimationId));

            // Mettre à jour l'estimation en fonction de la décision du manager
            if (decisionRequest.isManagerDecision()) {
                // Si le manager a accepté l'estimation, mettez à jour le statut en conséquence
                estimation.setEstimationStatus(EstimationStatus.ACCEPTED);
                // D'autres actions peuvent être effectuées ici, comme l'assignation d'un conducteur, etc.
            } else {
                // Si le manager a refusé l'estimation, mettez à jour le statut en conséquence
                estimation.setEstimationStatus(EstimationStatus.REFUSED);
                // D'autres actions peuvent être effectuées ici, comme un nettoyage supplémentaire, etc.
            }

            // Sauvegarder et retourner l'estimation mise à jour
            return estimationRepository.save(estimation);
        }


        // Create Address object
        private Address createAddress (String building, String street, String floor, String additionalInfos,
                String landmark,double latitude, double longitude){
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
        private Restaurant getOrCreateRestaurant (User authenticatedUser, String name, String phoneNumber, Address
        address){

            Optional<Restaurant> optionalRestaurant = restaurantRepository.findByUserAndName(authenticatedUser, name);

            if (optionalRestaurant.isPresent()) {
                // Si le restaurant existe déjà, retournez-le
                return optionalRestaurant.get();
            } else {
                // Sinon, créez un nouveau restaurant
                Restaurant restaurant = new Restaurant();
                restaurant.setName(name);
                restaurant.setPhoneNumber(phoneNumber);
                restaurant.setAddress(address);
                restaurant.setUser(authenticatedUser);
                restaurant.setUuid(UUID.randomUUID().toString());
                return restaurantRepository.save(restaurant);
            }
        }

        // Create Customer object
        private Customer createCustomer (String name, String email, String phoneNB, Address address){
            Customer customer = new Customer();
            customer.setName(name);
            customer.setEmail(email);
            customer.setPhoneNB(phoneNB);
            customer.setAddress(address);
            return customerRepository.save(customer);
        }

        // Create Order object
        private Orders createOrder ( double orderAmount, String orderDescr, LocalDateTime orderTime, LocalDateTime
        requestedDeliveryTime, Customer customer){
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
        private Tip createTip () {
            Tip tip = new Tip();
            tip.setTipAmount(0.0);
            // You can set other fields of Tip here if needed
            tip.setUuid(UUID.randomUUID().toString());
            return tipRepository.save(tip);
        }

        // Create Payment object
        private Payment createPayment (Tip tip){
            Payment payment = new Payment();
            payment.setPaymentAmount(0.0);
            payment.setPaymentTime(null);
            payment.setTotalValue(0.0);
            payment.setWithdrawDone(false);
            payment.setTip(tip);

            payment.setUuid(UUID.randomUUID().toString());

            // Save payment only if it contains valid data
            return paymentRepository.save(payment);
        }


        // Create Delivery object
        private Delivery createDelivery (Status status, PaymentMethod paymentMethod, Orders order, Rate rate){
            Delivery delivery = new Delivery();
            delivery.setStatus(status);
            delivery.setPaymentMethod(paymentMethod);
            delivery.setOrders(order);
            delivery.setRate(rate);


            delivery.setUuid(UUID.randomUUID().toString());

            return deliveryRepository.save(delivery);
        }


        // Create Estimation object
        public Estimation createEstimation (PricingStrategy pricingStrategy, Delivery delivery, Restaurant
        restaurant, NewEstimationRequest newEstimationRequest) throws IOException {

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

        public Rate createRate () {
            Rate rate = new Rate();
            rate.setRating(0.0);
            rate.setCommentary("");
            rate.setEvaluatedAt(null);
            rate.setUpdatedAt(null);
            rate.setUuid(UUID.randomUUID().toString());
            return rateRepository.save(rate);
        }
    }
