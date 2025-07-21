package com.pragma.plazadecomidas.restaurantservice.domain.usecase;

import com.pragma.plazadecomidas.restaurantservice.domain.exception.PersonalizedBadRequestException;
import com.pragma.plazadecomidas.restaurantservice.domain.exception.PersonalizedException;
import com.pragma.plazadecomidas.restaurantservice.domain.exception.PersonalizedNotFoundException;
import com.pragma.plazadecomidas.restaurantservice.domain.model.MessageEnum;
import com.pragma.plazadecomidas.restaurantservice.domain.model.Restaurant;
import com.pragma.plazadecomidas.restaurantservice.domain.model.User;
import com.pragma.plazadecomidas.restaurantservice.domain.spi.IAuthService;
import com.pragma.plazadecomidas.restaurantservice.domain.spi.IRestaurantPersistencePort;
import com.pragma.plazadecomidas.restaurantservice.domain.util.ValidationUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantUseCaseTest {

    @Mock
    private ValidationUtils validationUtils;

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @Mock
    private IAuthService authService;

    @InjectMocks
    private RestaurantUseCase restaurantService;

    private Restaurant validRestaurant;
    private User ownerUser;

    @BeforeEach
    void setUp() {
        validRestaurant = Restaurant.builder()
                .name("Restaurante Test")
                .nit("1234567890")
                .address("Calle Falsa 123")
                .phoneNumber("3001234567")
                .urlLogo("http://logo.com/test.png")
                .ownerId(1L)
                .build();

        ownerUser = User.builder()
                .id(1L)
                .name("Juan")
                .lastName("Perez")
                .roleName(MessageEnum.PROPIETARIO.getMessage())
                .build();

        Mockito.lenient().when(validationUtils.isValidateRole(anyString(), anyString())).thenReturn(true);

        Mockito.lenient().when(authService.findById(anyLong())).thenReturn(Optional.of(ownerUser));
        Mockito.lenient().when(restaurantPersistencePort.findByName(anyString())).thenReturn(Optional.empty());
        Mockito.lenient().when(restaurantPersistencePort.findByNit(anyString())).thenReturn(Optional.empty());
        Mockito.lenient().when(restaurantPersistencePort.save(any(Restaurant.class))).thenReturn(validRestaurant);
    }

    @Test
    @DisplayName("saveRestaurant: Should save a restaurant successfully with valid data")
    void saveRestaurant_ValidData_ReturnsRestaurant() {

        // WHEN
        Restaurant savedRestaurant = restaurantService.saveRestaurant(validRestaurant);

        // THEN
        assertNotNull(savedRestaurant);
        assertEquals(validRestaurant.getName(), savedRestaurant.getName());
        assertEquals("Juan Perez", savedRestaurant.getOwnerName());

        verify(authService).findById(validRestaurant.getOwnerId());
        verify(validationUtils).isValidateRole(ownerUser.getRoleName(), MessageEnum.PROPIETARIO.getMessage());
        verify(restaurantPersistencePort).findByName(validRestaurant.getName());
        verify(restaurantPersistencePort).findByNit(validRestaurant.getNit());
        verify(restaurantPersistencePort).save(validRestaurant);
    }

    @Test
    @DisplayName("saveRestaurant: Should throw PersonalizedNotFoundException if owner user is not found")
    void saveRestaurant_OwnerNotFound_ThrowsPersonalizedNotFoundException() {
        // GIVEN
        when(authService.findById(anyLong())).thenReturn(Optional.empty());

        // WHEN & THEN
        PersonalizedNotFoundException exception = assertThrows(PersonalizedNotFoundException.class,
                () -> restaurantService.saveRestaurant(validRestaurant));
        assertEquals(MessageEnum.OWNER_NOT_FOUND.getMessage(), exception.getMessage());
        verify(restaurantPersistencePort, never()).save(any(Restaurant.class));
    }

    @Test
    @DisplayName("saveRestaurant: Should throw PersonalizedException if owner user does not have PROPIETARIO role")
    void saveRestaurant_OwnerNotProprietario_ThrowsPersonalizedException() {
        // GIVEN
        User nonOwnerUser = User.builder().id(1L).roleName("CLIENT").build();
        when(authService.findById(anyLong())).thenReturn(Optional.of(nonOwnerUser));
        when(validationUtils.isValidateRole(nonOwnerUser.getRoleName(), MessageEnum.PROPIETARIO.getMessage())).thenReturn(false);

        // WHEN & THEN
        PersonalizedException exception = assertThrows(PersonalizedException.class,
                () -> restaurantService.saveRestaurant(validRestaurant));
        assertEquals(MessageEnum.OWNER_NOT_PROPRIETARIO.getMessage(), exception.getMessage());
        verify(restaurantPersistencePort, never()).save(any(Restaurant.class));
    }

    @Test
    @DisplayName("saveRestaurant: Should throw PersonalizedException if restaurant with same name already exists")
    void saveRestaurant_RestaurantNameExists_ThrowsPersonalizedException() {
        // GIVEN
        when(restaurantPersistencePort.findByName(anyString())).thenReturn(Optional.of(validRestaurant));

        // WHEN & THEN
        PersonalizedException exception = assertThrows(PersonalizedException.class,
                () -> restaurantService.saveRestaurant(validRestaurant));
        assertEquals(String.format(MessageEnum.RESTAURANT_NAME_EXISTS.getMessage(), validRestaurant.getName()), exception.getMessage());
        verify(restaurantPersistencePort, never()).save(any(Restaurant.class));
    }

    @Test
    @DisplayName("saveRestaurant: Should throw PersonalizedException if restaurant with same NIT already exists")
    void saveRestaurant_RestaurantNitExists_ThrowsPersonalizedException() {
        // GIVEN
        when(restaurantPersistencePort.findByNit(anyString())).thenReturn(Optional.of(validRestaurant));

        // WHEN & THEN
        PersonalizedException exception = assertThrows(PersonalizedException.class,
                () -> restaurantService.saveRestaurant(validRestaurant));
        assertEquals(MessageEnum.RESTAURANT_NIT_EXISTS.getMessage(), exception.getMessage());
        verify(restaurantPersistencePort, never()).save(any(Restaurant.class));
    }

}


