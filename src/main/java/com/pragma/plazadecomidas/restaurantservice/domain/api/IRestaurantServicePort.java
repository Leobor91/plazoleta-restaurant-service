package com.pragma.plazadecomidas.restaurantservice.domain.api;

import com.pragma.plazadecomidas.restaurantservice.domain.model.Restaurant;

public interface IRestaurantServicePort {

    Restaurant saveRestaurant(Restaurant restaurant);
}
