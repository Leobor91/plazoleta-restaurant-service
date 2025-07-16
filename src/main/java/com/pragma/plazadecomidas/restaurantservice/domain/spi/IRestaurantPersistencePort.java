package com.pragma.plazadecomidas.restaurantservice.domain.spi;

import com.pragma.plazadecomidas.restaurantservice.domain.model.Restaurant;

public interface IRestaurantPersistencePort {

    Restaurant save(Restaurant restaurant);
}
