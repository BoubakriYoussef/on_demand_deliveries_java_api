package com.example.ondemand.service;

import com.example.ondemand.request.restaurantRequest.AddRestaurantAddressRequest;
import com.example.ondemand.request.restaurantRequest.UpdateRestaurantRequest;
import com.example.ondemand.entities.Restaurant;
import com.example.ondemand.entities.User;

import java.util.List;

public interface RestaurantService {

    Restaurant addRestaurant(AddRestaurantAddressRequest addRestaurantAddressRequest, User manager);

    Restaurant getRestaurantsByCurrentUser();

    void deleteRestaurant(Long restaurantId, User user);

    public void updateRestaurant(Long restaurantId, UpdateRestaurantRequest updateRequest, User user);

    public Restaurant getRestaurantById(Long restaurantId, User user);
}
