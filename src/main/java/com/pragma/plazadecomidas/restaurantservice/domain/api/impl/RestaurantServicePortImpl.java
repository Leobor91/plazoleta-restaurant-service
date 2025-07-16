package com.pragma.plazadecomidas.restaurantservice.domain.api.impl;

import com.pragma.plazadecomidas.restaurantservice.domain.api.IRestaurantServicePort;
import com.pragma.plazadecomidas.restaurantservice.domain.exception.PersonalizedException;
import com.pragma.plazadecomidas.restaurantservice.domain.model.MessageEnum;
import com.pragma.plazadecomidas.restaurantservice.domain.model.Restaurant;
import com.pragma.plazadecomidas.restaurantservice.domain.model.User;
import com.pragma.plazadecomidas.restaurantservice.domain.spi.IAuthService;
import com.pragma.plazadecomidas.restaurantservice.domain.spi.IRestaurantPersistencePort;
import com.pragma.plazadecomidas.restaurantservice.domain.util.ValidationUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RestaurantServicePortImpl implements IRestaurantServicePort {

    private final ValidationUtils validationUtils;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IAuthService authService;
    private Optional<User> userOptional;
    private User userValue;


    @Override
    public Restaurant saveRestaurant(Restaurant restaurant) {

        if(!validationUtils.isValid(restaurant.getName())){
            throw  new PersonalizedException(MessageEnum.NAME_REQUIRED.getMessage());
        }

        if(!validationUtils.isValid(restaurant.getNit())){
            throw  new PersonalizedException(MessageEnum.NIT_REQUIRED.getMessage());
        }

        if(!validationUtils.isValid(restaurant.getAddress())){
            throw  new PersonalizedException(MessageEnum.ADDRESS_REQUIRED.getMessage());
        }

        if(!validationUtils.isValid(restaurant.getPhoneNumber())){
            throw  new PersonalizedException(MessageEnum.PHONE_REQUIRED.getMessage());
        }

        if(!validationUtils.isValid(restaurant.getUrlLogo())){
            throw  new PersonalizedException(MessageEnum.URL_REQUIRED.getMessage());
        }

        if(!validationUtils.isValid(String.valueOf(restaurant.getOwnerId()))){
            throw  new PersonalizedException(MessageEnum.OWNER_ID_REQUIRED.getMessage());
        }

        if(!validationUtils.containsOnlyNumbers(restaurant.getNit())){
            throw  new PersonalizedException(MessageEnum.NIT_FORMAT.getMessage());
        }

        if (!validationUtils.isValidPhoneStructure(restaurant.getPhoneNumber())) {
            throw new PersonalizedException(MessageEnum.PHONE_FORMAT.getMessage());
        }

        if (!validationUtils.isValidUrl(restaurant.getUrlLogo())) {
            throw new PersonalizedException(MessageEnum.URL_FORMAT.getMessage());
        }

        if (!validationUtils.isValidNameStructure(restaurant.getName())) {
            throw new PersonalizedException(MessageEnum.NAME_FORMAT.getMessage());
        }

        userOptional = authService.findById(restaurant.getOwnerId())
                .map(user -> {
                    userValue = user;
                    return user;
                });

        if(!validationUtils.isValidateRole(userValue.getRoleName(), MessageEnum.PROPIETARIO.getMessage())){
            throw new PersonalizedException(MessageEnum.OWNER_NOT_PROPRIETARIO.getMessage());
        }

        return restaurantPersistencePort.save(restaurant).toBuilder()
                .ownerName(userValue.getName().concat(MessageEnum.EMPTY.getMessage()).concat(userValue.getLastName()))
                .build();

    }

}
