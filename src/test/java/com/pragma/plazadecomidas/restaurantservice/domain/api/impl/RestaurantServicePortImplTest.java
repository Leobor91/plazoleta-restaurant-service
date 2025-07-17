package com.pragma.plazadecomidas.restaurantservice.domain.api.impl;

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
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantServicePortImplTest {

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;
    @Mock
    private IAuthService authService;
    @Mock
    private ValidationUtils validationUtils;

    @InjectMocks
    private RestaurantServicePortImpl restaurantService;

    private Restaurant validRestaurant;
    private User adminUser;
    private User nonAdminUser;

    @BeforeEach
    void setUp() {
        validRestaurant = new Restaurant(
                null,
                "Mi Restaurante",
                "123456789",
                "Calle 123 #4-56",
                "3001234567",
                "http://logo.png",
                1L,
                ""
        );

        adminUser = new User();
        adminUser.setId(1L);
        adminUser.setName("Admin");
        adminUser.setLastName("User");
        adminUser.setRoleName("ADMINISTRADOR");

        nonAdminUser = new User();
        nonAdminUser.setId(2L);
        nonAdminUser.setName("Non");
        nonAdminUser.setLastName("Owner");
        nonAdminUser.setRoleName("CLIENTE");
    }

    @Test
    @DisplayName("Should successfully save a new restaurant when owner is PROPIETARIO and all validations pass")
    void saveRestaurant_Success_WhenOwnerIsProprietarioAndValidationsPass() {
        when(validationUtils.isValid(anyString())).thenReturn(true);
        when(validationUtils.containsOnlyNumbers(anyString())).thenReturn(true);
        when(validationUtils.isValidPhoneStructure(anyString())).thenReturn(true);
        when(validationUtils.isValidUrl(anyString())).thenReturn(true);
        when(validationUtils.isValidNameStructure(anyString())).thenReturn(true);
        when(validationUtils.isValidateRole(adminUser.getRoleName(), MessageEnum.PROPIETARIO.getMessage())).thenReturn(true);

        when(authService.findById(validRestaurant.getOwnerId())).thenReturn(Optional.of(adminUser));

        Restaurant savedRestaurant = validRestaurant.toBuilder().id(10L).build();
        when(restaurantPersistencePort.save(any(Restaurant.class))).thenReturn(savedRestaurant);

        Restaurant result = restaurantService.saveRestaurant(validRestaurant);

        assertNotNull(result);
        assertEquals(savedRestaurant.getId(), result.getId());
        assertEquals("Admin User", result.getOwnerName());

        verify(validationUtils, times(6)).isValid(anyString());
        verify(validationUtils, times(1)).containsOnlyNumbers(validRestaurant.getNit());
        verify(validationUtils, times(1)).isValidPhoneStructure(validRestaurant.getPhoneNumber());
        verify(validationUtils, times(1)).isValidUrl(validRestaurant.getUrlLogo());
        verify(validationUtils, times(1)).isValidNameStructure(validRestaurant.getName());
        verify(validationUtils, times(1)).isValidateRole(adminUser.getRoleName(), MessageEnum.PROPIETARIO.getMessage());

        verify(authService, times(1)).findById(validRestaurant.getOwnerId());
        verify(restaurantPersistencePort, times(1)).save(validRestaurant);
    }

    @Test
    @DisplayName("Should throw PersonalizedException for NAME_REQUIRED when name is invalid")
    void saveRestaurant_ThrowsException_NameRequired() {
        validRestaurant.setName(null);
        when(validationUtils.isValid(null)).thenReturn(false);

        PersonalizedBadRequestException exception = assertThrows(PersonalizedBadRequestException.class,
                () -> restaurantService.saveRestaurant(validRestaurant));
        assertEquals(MessageEnum.NAME_REQUIRED.getMessage(), exception.getMessage());
        verify(validationUtils, times(1)).isValid(null);
        verifyNoInteractions(authService, restaurantPersistencePort);
    }

    @Test
    @DisplayName("Should throw PersonalizedException for NIT_REQUIRED when NIT is invalid")
    void saveRestaurant_ThrowsException_NitRequired() {
        validRestaurant.setNit(null);
        when(validationUtils.isValid(validRestaurant.getName())).thenReturn(true);
        when(validationUtils.isValid(null)).thenReturn(false);
        PersonalizedBadRequestException exception = assertThrows(PersonalizedBadRequestException.class,
                () -> restaurantService.saveRestaurant(validRestaurant));
        assertEquals(MessageEnum.NIT_REQUIRED.getMessage(), exception.getMessage());
        verify(validationUtils, atLeastOnce()).isValid(anyString());
        verifyNoInteractions(authService, restaurantPersistencePort);
    }

    @Test
    @DisplayName("Should throw PersonalizedException for ADDRESS_REQUIRED when address is invalid")
    void saveRestaurant_ThrowsException_AddressRequired() {
        validRestaurant.setAddress(null);
        when(validationUtils.isValid(validRestaurant.getName())).thenReturn(true);
        when(validationUtils.isValid(validRestaurant.getNit())).thenReturn(true);
        when(validationUtils.isValid(null)).thenReturn(false);
        PersonalizedBadRequestException exception = assertThrows(PersonalizedBadRequestException.class,
                () -> restaurantService.saveRestaurant(validRestaurant));
        assertEquals(MessageEnum.ADDRESS_REQUIRED.getMessage(), exception.getMessage());
        verify(validationUtils, atLeastOnce()).isValid(anyString());
        verifyNoInteractions(authService, restaurantPersistencePort);
    }

    @Test
    @DisplayName("Should throw PersonalizedException for PHONE_REQUIRED when phone number is invalid")
    void saveRestaurant_ThrowsException_PhoneRequired() {
        validRestaurant.setPhoneNumber(null);
        when(validationUtils.isValid(validRestaurant.getName())).thenReturn(true);
        when(validationUtils.isValid(validRestaurant.getNit())).thenReturn(true);
        when(validationUtils.isValid(validRestaurant.getAddress())).thenReturn(true);
        when(validationUtils.isValid(null)).thenReturn(false);
        PersonalizedBadRequestException exception = assertThrows(PersonalizedBadRequestException.class,
                () -> restaurantService.saveRestaurant(validRestaurant));
        assertEquals(MessageEnum.PHONE_REQUIRED.getMessage(), exception.getMessage());
        verify(validationUtils, atLeastOnce()).isValid(anyString());
        verifyNoInteractions(authService, restaurantPersistencePort);
    }

    @Test
    @DisplayName("Should throw PersonalizedException for URL_REQUIRED when URL logo is invalid")
    void saveRestaurant_ThrowsException_UrlRequired() {
        validRestaurant.setUrlLogo(null);
        when(validationUtils.isValid(validRestaurant.getName())).thenReturn(true);
        when(validationUtils.isValid(validRestaurant.getNit())).thenReturn(true);
        when(validationUtils.isValid(validRestaurant.getAddress())).thenReturn(true);
        when(validationUtils.isValid(validRestaurant.getPhoneNumber())).thenReturn(true);
        when(validationUtils.isValid(null)).thenReturn(false);
        PersonalizedBadRequestException exception = assertThrows(PersonalizedBadRequestException.class,
                () -> restaurantService.saveRestaurant(validRestaurant));
        assertEquals(MessageEnum.URL_REQUIRED.getMessage(), exception.getMessage());
        verify(validationUtils, atLeastOnce()).isValid(anyString());
        verifyNoInteractions(authService, restaurantPersistencePort);
    }

    @Test
    @DisplayName("Should throw PersonalizedException for OWNER_ID_REQUIRED when owner ID is null")
    void saveRestaurant_ThrowsException_OwnerIdRequired() {
        // Given
        validRestaurant.setOwnerId(null);

        when(validationUtils.isValid(anyString())).thenReturn(true);
        when(validationUtils.isValid("null")).thenReturn(false);


        PersonalizedBadRequestException exception = assertThrows(PersonalizedBadRequestException.class,
                () -> restaurantService.saveRestaurant(validRestaurant));
        assertEquals(MessageEnum.OWNER_ID_REQUIRED.getMessage(), exception.getMessage());

        // Verify:
        verify(validationUtils, times(6)).isValid(anyString());

        verifyNoMoreInteractions(validationUtils);
        verifyNoInteractions(authService, restaurantPersistencePort);
    }

    @Test
    @DisplayName("Should throw PersonalizedException for NIT_FORMAT when NIT is not numeric")
    void saveRestaurant_ThrowsException_NitFormat() {
        validRestaurant.setNit("ABC123456");
        when(validationUtils.isValid(anyString())).thenReturn(true);
        when(validationUtils.containsOnlyNumbers(validRestaurant.getNit())).thenReturn(false);

        PersonalizedBadRequestException exception = assertThrows(PersonalizedBadRequestException.class,
                () -> restaurantService.saveRestaurant(validRestaurant));
        assertEquals(MessageEnum.NIT_FORMAT.getMessage(), exception.getMessage());

        verify(validationUtils, times(6)).isValid(anyString());
        verify(validationUtils, times(1)).containsOnlyNumbers(validRestaurant.getNit());
        verifyNoMoreInteractions(validationUtils);
        verifyNoInteractions(authService, restaurantPersistencePort);
    }

    @Test
    @DisplayName("Should throw PersonalizedException for PHONE_FORMAT when phone number structure is invalid")
    void saveRestaurant_ThrowsException_PhoneFormat() {
        validRestaurant.setPhoneNumber("invalid-phone");
        when(validationUtils.isValid(anyString())).thenReturn(true);
        when(validationUtils.containsOnlyNumbers(validRestaurant.getNit())).thenReturn(true);
        when(validationUtils.isValidPhoneStructure(validRestaurant.getPhoneNumber())).thenReturn(false);

        PersonalizedBadRequestException exception = assertThrows(PersonalizedBadRequestException.class,
                () -> restaurantService.saveRestaurant(validRestaurant));
        assertEquals(MessageEnum.PHONE_FORMAT.getMessage(), exception.getMessage());

        verify(validationUtils, times(6)).isValid(anyString());
        verify(validationUtils, times(1)).containsOnlyNumbers(validRestaurant.getNit());
        verify(validationUtils, times(1)).isValidPhoneStructure(validRestaurant.getPhoneNumber());
        verifyNoMoreInteractions(validationUtils);
        verifyNoInteractions(authService, restaurantPersistencePort);
    }

    @Test
    @DisplayName("Should throw PersonalizedException for URL_FORMAT when URL logo is invalid")
    void saveRestaurant_ThrowsException_UrlFormat() {
        validRestaurant.setUrlLogo("not-a-valid-url");
        when(validationUtils.isValid(anyString())).thenReturn(true);
        when(validationUtils.containsOnlyNumbers(validRestaurant.getNit())).thenReturn(true);
        when(validationUtils.isValidPhoneStructure(validRestaurant.getPhoneNumber())).thenReturn(true);
        when(validationUtils.isValidUrl(validRestaurant.getUrlLogo())).thenReturn(false);

        PersonalizedBadRequestException exception = assertThrows(PersonalizedBadRequestException.class,
                () -> restaurantService.saveRestaurant(validRestaurant));
        assertEquals(MessageEnum.URL_FORMAT.getMessage(), exception.getMessage());

        verify(validationUtils, times(6)).isValid(anyString());
        verify(validationUtils, times(1)).containsOnlyNumbers(validRestaurant.getNit());
        verify(validationUtils, times(1)).isValidPhoneStructure(validRestaurant.getPhoneNumber());
        verify(validationUtils, times(1)).isValidUrl(validRestaurant.getUrlLogo());
        verifyNoMoreInteractions(validationUtils);
        verifyNoInteractions(authService, restaurantPersistencePort);
    }

    @Test
    @DisplayName("Should throw PersonalizedException for NAME_FORMAT when name structure is invalid (e.g., only numbers)")
    void saveRestaurant_ThrowsException_NameFormat() {
        validRestaurant.setName("12345");
        when(validationUtils.isValid(anyString())).thenReturn(true);
        when(validationUtils.containsOnlyNumbers(anyString())).thenReturn(true);
        when(validationUtils.isValidPhoneStructure(anyString())).thenReturn(true);
        when(validationUtils.isValidUrl(anyString())).thenReturn(true);
        when(validationUtils.isValidNameStructure(validRestaurant.getName())).thenReturn(false);

        PersonalizedBadRequestException exception = assertThrows(PersonalizedBadRequestException.class,
                () -> restaurantService.saveRestaurant(validRestaurant));
        assertEquals(MessageEnum.NAME_FORMAT.getMessage(), exception.getMessage());

        verify(validationUtils, times(6)).isValid(anyString());
        verify(validationUtils, times(1)).containsOnlyNumbers(validRestaurant.getNit());
        verify(validationUtils, times(1)).isValidPhoneStructure(validRestaurant.getPhoneNumber());
        verify(validationUtils, times(1)).isValidUrl(validRestaurant.getUrlLogo());
        verify(validationUtils, times(1)).isValidNameStructure(validRestaurant.getName());
        verifyNoMoreInteractions(validationUtils);
        verifyNoInteractions(authService, restaurantPersistencePort);
    }

    @Test
    @DisplayName("Should throw PersonalizedException for OWNER_NOT_PROPRIETARIO when owner's role is not PROPIETARIO")
    void saveRestaurant_ThrowsException_OwnerNotProprietario() {
        when(validationUtils.isValid(anyString())).thenReturn(true);
        when(validationUtils.containsOnlyNumbers(anyString())).thenReturn(true);
        when(validationUtils.isValidPhoneStructure(anyString())).thenReturn(true);
        when(validationUtils.isValidUrl(anyString())).thenReturn(true);
        when(validationUtils.isValidNameStructure(anyString())).thenReturn(true);

        when(authService.findById(validRestaurant.getOwnerId())).thenReturn(Optional.of(nonAdminUser));
        when(validationUtils.isValidateRole(nonAdminUser.getRoleName(), MessageEnum.PROPIETARIO.getMessage())).thenReturn(false);

        PersonalizedException exception = assertThrows(PersonalizedException.class,
                () -> restaurantService.saveRestaurant(validRestaurant));
        assertEquals(MessageEnum.OWNER_NOT_PROPRIETARIO.getMessage(), exception.getMessage());

        verify(validationUtils, times(6)).isValid(anyString());
        verify(validationUtils, times(1)).containsOnlyNumbers(validRestaurant.getNit());
        verify(validationUtils, times(1)).isValidPhoneStructure(validRestaurant.getPhoneNumber());
        verify(validationUtils, times(1)).isValidUrl(validRestaurant.getUrlLogo());
        verify(validationUtils, times(1)).isValidNameStructure(validRestaurant.getName());
        verify(authService, times(1)).findById(validRestaurant.getOwnerId());
        verify(validationUtils, times(1)).isValidateRole(nonAdminUser.getRoleName(), MessageEnum.PROPIETARIO.getMessage());
        verifyNoInteractions(restaurantPersistencePort);
    }

    @Test
    @DisplayName("Should throw PersonalizedException for OWNER_NOT_FOUND when owner ID is not found by Auth Service")
    void saveRestaurant_ThrowsException_UserNotFound() {
        when(validationUtils.isValid(anyString())).thenReturn(true);
        when(validationUtils.containsOnlyNumbers(anyString())).thenReturn(true);
        when(validationUtils.isValidPhoneStructure(anyString())).thenReturn(true);
        when(validationUtils.isValidUrl(anyString())).thenReturn(true);
        when(validationUtils.isValidNameStructure(anyString())).thenReturn(true);

        when(authService.findById(validRestaurant.getOwnerId())).thenReturn(Optional.empty());

        PersonalizedNotFoundException exception = assertThrows(PersonalizedNotFoundException.class,
                () -> restaurantService.saveRestaurant(validRestaurant));

        assertEquals(MessageEnum.OWNER_NOT_FOUND.getMessage(), exception.getMessage());

        verify(validationUtils, times(6)).isValid(anyString());
        verify(validationUtils, times(1)).containsOnlyNumbers(validRestaurant.getNit());
        verify(validationUtils, times(1)).isValidPhoneStructure(validRestaurant.getPhoneNumber());
        verify(validationUtils, times(1)).isValidUrl(validRestaurant.getUrlLogo());
        verify(validationUtils, times(1)).isValidNameStructure(validRestaurant.getName());
        verify(authService, times(1)).findById(validRestaurant.getOwnerId());
        verifyNoInteractions(restaurantPersistencePort);
    }
}


