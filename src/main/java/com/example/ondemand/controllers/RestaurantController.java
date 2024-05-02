package com.example.ondemand.controllers;


import com.example.ondemand.request.restaurantRequest.AddRestaurantAddressRequest;
import com.example.ondemand.request.restaurantRequest.UpdateRestaurantRequest;
import com.example.ondemand.entities.Restaurant;
import com.example.ondemand.entities.User;
import com.example.ondemand.repositories.RestaurantRepository;
import com.example.ondemand.repositories.UserRepository;
import com.example.ondemand.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurant")
public class RestaurantController {

    @Autowired
    RestaurantService restaurantService;
    @Autowired
    UserRepository userRepository;

    @Autowired
    RestaurantRepository restaurantRepository;


    // Ajouter restaurant
    @PostMapping("/addRestaurant")
    public ResponseEntity<?> addRestaurant(@RequestBody AddRestaurantAddressRequest addRestaurantAddressRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        User currentUser = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found in the database"));

        Restaurant restaurant = restaurantService.addRestaurant(addRestaurantAddressRequest, currentUser);
        return ResponseEntity.ok().body("Restaurant added successfully with id: " + restaurant.getId());
    }


    //Afficher tout les restaurants
    @GetMapping("/allRestaurant")
    public ResponseEntity<?> getAllRestaurants(){
        return ResponseEntity.ok(restaurantRepository.findAll());
    }


    @GetMapping("/current")
    public ResponseEntity<?> getRestaurantsByCurrentUser() {
        try {
            Restaurant restaurant = restaurantService.getRestaurantsByCurrentUser();
            return ResponseEntity.ok().body(restaurant);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get restaurants: " + e.getMessage());
        }
    }


    // Supprimer restaurant par Id avec un user associé
    @DeleteMapping("/{restaurantId}")
    public  void deleteUser(@PathVariable Long restaurantId, User user){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        User currentUser = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found in the database"));

        restaurantService.deleteRestaurant(restaurantId, currentUser);
    }


    // Mettre à jour restaurant associé à un user
    @PutMapping("/{restaurantId}")
    public ResponseEntity<String> updateRestaurant(@PathVariable Long restaurantId,
                                                   @RequestBody UpdateRestaurantRequest updateRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User currentUser = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found in the database"));
        restaurantService.updateRestaurant(restaurantId, updateRequest, currentUser);
        return ResponseEntity.ok("Restaurant mis à jour avec succès.");
    }


    @GetMapping("/{restaurantId}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Long restaurantId) {
        // Récupérer l'utilisateur connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        // Récupérer l'utilisateur à partir du repository
        User currentUser = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found in the database"));

        // Appeler le service pour récupérer le restaurant
        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId, currentUser);
        return ResponseEntity.ok(restaurant);
    }


}
