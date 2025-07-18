package com.pragma.plazadecomidas.restaurantservice.infrastructure.out.jpa.repository;

import com.pragma.plazadecomidas.restaurantservice.infrastructure.out.jpa.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface IRestaurantRepository extends JpaRepository<RestaurantEntity, Long> {

    Optional<RestaurantEntity> findByName(String name);
    Optional<RestaurantEntity> findByNit(String nit);

}
