package com.pragma.plazadecomidas.restaurantservice.application.handler.impl;

import com.pragma.plazadecomidas.restaurantservice.application.dto.request.RestaurantRequestDto;
import com.pragma.plazadecomidas.restaurantservice.application.dto.response.RestaurantResponseDto;
import com.pragma.plazadecomidas.restaurantservice.application.handler.IRestaurantHandler;
import com.pragma.plazadecomidas.restaurantservice.application.mapper.IRestaurantRequestMapper;
import com.pragma.plazadecomidas.restaurantservice.domain.api.IRestaurantServicePort;
import com.pragma.plazadecomidas.restaurantservice.domain.exception.PersonalizedBadRequestException;
import com.pragma.plazadecomidas.restaurantservice.domain.model.MessageEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestaurantHandlerImpl implements IRestaurantHandler {

    private final IRestaurantServicePort restaurantServicePort;
    private final IRestaurantRequestMapper restaurantRequestMapper;

    @Override
    public RestaurantResponseDto saveRestaurant(RestaurantRequestDto restaurantRequestDto) {
        return Optional.ofNullable(restaurantRequestDto)
                .map(restaurantRequestMapper::toRestaurant)
                .map(restaurantServicePort::saveRestaurant)
                .map(restaurantRequestMapper::toResponseDto)
                .orElseThrow(() -> new PersonalizedBadRequestException(MessageEnum.RESTAURANT_REQUEST_NULL.getMessage()));
    }
}
