package com.example.ondemand.service;

import com.example.ondemand.dto.restaurantRequest.AddRestaurantAddressRequest;
import com.example.ondemand.dto.restaurantRequest.UpdateRestaurantRequest;
import com.example.ondemand.entities.Restaurant;
import com.example.ondemand.entities.User;

public interface RestaurantService {

    Restaurant addRestaurant(AddRestaurantAddressRequest addRestaurantAddressRequest, User manager);

    Restaurant getRestaurantsByUser(User user);

    void deleteRestaurant(Long restaurantId, User user);

    public void updateRestaurant(Long restaurantId, UpdateRestaurantRequest updateRequest, User user);

    public Restaurant getRestaurantById(Long restaurantId, User user);
}
