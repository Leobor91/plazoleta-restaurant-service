package com.pragma.plazadecomidas.restaurantservice.infrastructure.output.jpa.adapter;


import com.pragma.plazadecomidas.restaurantservice.domain.model.Restaurant;
import com.pragma.plazadecomidas.restaurantservice.domain.spi.IRestaurantPersistencePort;
import com.pragma.plazadecomidas.restaurantservice.infrastructure.output.jpa.mapper.IRestaurantEntityMapper;
import com.pragma.plazadecomidas.restaurantservice.infrastructure.output.jpa.repository.IRestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RestaurantPersistencePort implements IRestaurantPersistencePort {

    private final IRestaurantRepository restaurantRepository;
    private final IRestaurantEntityMapper restaurantEntityMapper;

    @Override
    public Restaurant save(Restaurant restaurant) {
        return restaurantEntityMapper.toRestaurant(
                restaurantRepository.save(
                        restaurantEntityMapper.toRestaurantEntity(restaurant)
                )
        );
    }

    @Override
    public Optional<Restaurant> findByName(String name) {
        return restaurantRepository.findByName(name)
                .map(restaurantEntityMapper::toRestaurant);
    }

    @Override
    public Optional<Restaurant> findByNit(String nit) {
        return restaurantRepository.findByNit(nit)
                .map(restaurantEntityMapper::toRestaurant);
    }
}
