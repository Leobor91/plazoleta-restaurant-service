package com.pragma.plazadecomidas.restaurantservice.domain.usecase;

import com.pragma.plazadecomidas.restaurantservice.domain.api.IRestaurantServicePort;
import com.pragma.plazadecomidas.restaurantservice.domain.exception.PersonalizedBadRequestException;
import com.pragma.plazadecomidas.restaurantservice.domain.exception.PersonalizedException;
import com.pragma.plazadecomidas.restaurantservice.domain.exception.PersonalizedNotFoundException;
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

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RestaurantUseCase implements IRestaurantServicePort {

    private final ValidationUtils validationUtils;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IAuthService authService;


    @Override
    public Restaurant saveRestaurant(Restaurant restaurant) {

        User ownerUser = authService.findById(restaurant.getOwnerId())
                .orElseThrow(() -> new PersonalizedNotFoundException(
                        MessageEnum.OWNER_NOT_FOUND.getMessage()));

        if (!validationUtils.isValidateRole(ownerUser.getRoleName(), MessageEnum.PROPIETARIO.getMessage())) {
            throw new PersonalizedException(MessageEnum.OWNER_NOT_PROPRIETARIO.getMessage());
        }

        restaurantPersistencePort.findByName(restaurant.getName())
                .ifPresent(existingRestaurant -> {
                    throw new PersonalizedException(
                            String.format(MessageEnum.RESTAURANT_NAME_EXISTS.getMessage(), restaurant.getName()));
                });

        restaurantPersistencePort.findByNit(restaurant.getNit())
                .ifPresent(existingRestaurant -> {
                    throw new PersonalizedException(MessageEnum.RESTAURANT_NIT_EXISTS.getMessage());
                });

        return restaurantPersistencePort.save(restaurant).toBuilder()
                .ownerName(ownerUser.getName().concat(MessageEnum.EMPTY.getMessage()).concat(ownerUser.getLastName()))
                .build();

    }

}
