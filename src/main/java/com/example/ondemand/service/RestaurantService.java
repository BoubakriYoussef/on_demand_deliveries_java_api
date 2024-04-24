package com.example.ondemand.service;

import com.example.ondemand.dto.AddRestaurantAddressRequest;
import com.example.ondemand.dto.UpdateRestaurantRequest;
import com.example.ondemand.entities.Restaurant;
import com.example.ondemand.entities.User;

import java.util.List;

public interface RestaurantService {

    Restaurant addRestaurant(AddRestaurantAddressRequest addRestaurantAddressRequest, User manager);

    List<Restaurant> getRestaurantsByUser(User user);

    void deleteRestaurant(Long restaurantId, User user);

    public void updateRestaurant(Long restaurantId, UpdateRestaurantRequest updateRequest, User user);

    public Restaurant getRestaurantById(Long restaurantId, User user);
}
