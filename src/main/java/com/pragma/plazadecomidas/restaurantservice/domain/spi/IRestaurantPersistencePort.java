package com.pragma.plazadecomidas.restaurantservice.domain.spi;

import com.pragma.plazadecomidas.restaurantservice.domain.model.Restaurant;

import java.util.Optional;

public interface IRestaurantPersistencePort {

    Restaurant save(Restaurant restaurant);

    Optional<Restaurant> findByName(String name);

    Optional<Restaurant> findByNit(String nit);

}
