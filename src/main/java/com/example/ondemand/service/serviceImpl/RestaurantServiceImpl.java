package com.example.ondemand.service.serviceImpl;

import com.example.ondemand.authentication.authService.AuthenticationService;
import com.example.ondemand.entities.*;
import com.example.ondemand.enumClass.PaymentMethod;
import com.example.ondemand.enumClass.Status;
import com.example.ondemand.repositories.*;
import com.example.ondemand.request.EstimationRequest.NewEstimationRequest;
import com.example.ondemand.request.restaurantRequest.AddRestaurantAddressRequest;
import com.example.ondemand.request.restaurantRequest.UpdateRestaurantRequest;
import com.example.ondemand.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    RestaurantRepository restaurantRepository;



    @Override
    public Restaurant addRestaurant(AddRestaurantAddressRequest addRestaurantAddressRequest, User manager){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        User currentUser = userRepository.findByEmail(username)
                .orElseThrow();

        if(currentUser==null){
            throw new UsernameNotFoundException("User not found in the database");
        }

        Restaurant restaurant = new Restaurant();
        restaurant.setName(addRestaurantAddressRequest.getRestaurantName());
        restaurant.setPhoneNumber(addRestaurantAddressRequest.getPhoneNumber());

        Address address = new Address();
        address.setBuilding(addRestaurantAddressRequest.getBuilding());
        address.setStreet(addRestaurantAddressRequest.getStreet());
        address.setFloor(addRestaurantAddressRequest.getFloor());
        address.setAdditionalInfos(addRestaurantAddressRequest.getAdditionalInfos());
        address.setLandmark(addRestaurantAddressRequest.getLandmark());
        address.setLatitude(addRestaurantAddressRequest.getLatitude());
        address.setLongitude(addRestaurantAddressRequest.getLongitude());
        addressRepository.save(address);
        restaurant.setAddress(address);
        restaurant.setUser(currentUser);
        return restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant getRestaurantsByUser(User user) {
        return restaurantRepository.findByUser(user);
    }


    @Override
    public void deleteRestaurant(Long restaurantId, User user) {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(restaurantId);
        if (restaurantOptional.isPresent()) {
            Restaurant restaurant = restaurantOptional.get();
            // Vérifie si l'utilisateur est propriétaire du restaurant
            if (restaurant.getUser().equals(user)) {
                restaurantRepository.delete(restaurant);
            } else {
                throw new IllegalArgumentException("Vous n'êtes pas autorisé à supprimer ce restaurant.");
            }
        } else {
            throw new IllegalArgumentException("Restaurant non trouvé avec l'ID : " + restaurantId);
        }
    }

    @Override
    public void updateRestaurant(Long restaurantId, UpdateRestaurantRequest updateRequest, User user) {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(restaurantId);
        if (restaurantOptional.isPresent()) {
            Restaurant restaurant = restaurantOptional.get();
            // Vérifie si l'utilisateur est propriétaire du restaurant
            if (restaurant.getUser().equals(user)) {
                // Mettre à jour les informations du restaurant
                restaurant.setName(updateRequest.getRestaurantName());
                restaurant.setPhoneNumber(updateRequest.getPhoneNumber());
                // Mettre à jour d'autres champs si nécessaire
                // Enregistrement des modifications
                restaurantRepository.save(restaurant);
            } else {
                throw new IllegalArgumentException("Vous n'êtes pas autorisé à mettre à jour ce restaurant.");
            }
        } else {
            throw new IllegalArgumentException("Restaurant non trouvé avec l'ID : " + restaurantId);
        }
    }

    @Override
    public Restaurant getRestaurantById(Long restaurantId, User user) {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(restaurantId);
        if (restaurantOptional.isPresent()) {
            Restaurant restaurant = restaurantOptional.get();
            // Vérifier si l'utilisateur est propriétaire du restaurant
            if (restaurant.getUser().equals(user)) {
                return restaurant;
            } else {
                throw new IllegalArgumentException("Vous n'êtes pas autorisé à accéder à ce restaurant.");
            }
        } else {
            throw new IllegalArgumentException("Restaurant non trouvé avec l'ID : " + restaurantId);
        }
    }


}
