package com.pragma.plazadecomidas.restaurantservice.domain.api.impl;

import com.pragma.plazadecomidas.restaurantservice.domain.exception.PersonalizedException;
import com.pragma.plazadecomidas.restaurantservice.domain.model.Restaurant;
import com.pragma.plazadecomidas.restaurantservice.domain.model.User;
import com.pragma.plazadecomidas.restaurantservice.domain.spi.IAuthService;
import com.pragma.plazadecomidas.restaurantservice.domain.spi.IRestaurantPersistencePort;
import com.pragma.plazadecomidas.restaurantservice.domain.util.ValidationUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RestaurantServicePortImplTest {  @Mock
private IRestaurantPersistencePort restaurantPersistencePort;

    @Mock
    private IAuthService authService;

    @Mock
    private ValidationUtils validationUtils;

    @InjectMocks
    private RestaurantServicePortImpl restaurantServicePort;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should successfully create a new restaurant when owner is valid and data is well-formed")
    void createRestaurant_Success() {
        Restaurant newRestaurant = new Restaurant(
                null,
                "NombreValido",
                "123456789",
                "Calle 123",
                "3001234567",
                "http://logo.png",
                1L,
                ""
        );

        User mockUser = new User(
                1L,
                "Juan",
                "Perez",
                "juan@mail.com",
                "3001234567",
                "12345678",
                "password",
                LocalDate.of(1990, 1, 1),
                "1",
                "PROPIETARIO"
        );

        when(authService.findById(1L)).thenReturn(Optional.of(mockUser));

        when(restaurantPersistencePort.save(any(Restaurant.class))).thenReturn(newRestaurant);
        when(validationUtils.isValidPhoneStructure(anyString())).thenReturn(true);
        when(validationUtils.isValidNameStructure(anyString())).thenReturn(true);
        when(validationUtils.isValid(anyString())).thenReturn(true);
        when(validationUtils.containsOnlyNumbers(anyString())).thenReturn(true);
        when(validationUtils.isValidUrl(anyString())).thenReturn(true);
        when(validationUtils.isValidateRole(anyString(), anyString())).thenReturn(true);


        Restaurant createdRestaurant = restaurantServicePort.saveRestaurant(newRestaurant);

        assertNotNull(createdRestaurant);
        assertEquals(newRestaurant.getName(), createdRestaurant.getName());
        verify(restaurantPersistencePort, times(1)).save(newRestaurant);
        verify(validationUtils, times(1)).isValidPhoneStructure(newRestaurant.getPhoneNumber());
        verify(validationUtils, times(1)).isValidNameStructure(newRestaurant.getName());
    }

    @Test
    @DisplayName("Should throw UserIsNotOwnerException when ownerId does not correspond to an OWNER role")
    void createRestaurant_OwnerIsNotOwner_ThrowsException() {
        Restaurant newRestaurant = new Restaurant(
                null, "My Restaurant", "123456789", "Calle 123", "3001234567", "http://logo.png", 1L,""
        );

        assertThrows(PersonalizedException.class, () -> restaurantServicePort.saveRestaurant(newRestaurant));
        verify(restaurantPersistencePort, never()).save(any(Restaurant.class));
    }

}
