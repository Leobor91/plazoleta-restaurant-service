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

@ExtendWith(MockitoExtension.class) // Habilita Mockito para JUnit 5
class RestaurantUseCaseTest {

    @Mock // Crea un mock de ValidationUtils
    private ValidationUtils validationUtils;

    @Mock // Crea un mock de IRestaurantPersistencePort
    private IRestaurantPersistencePort restaurantPersistencePort;

    @Mock // Crea un mock de IAuthService
    private IAuthService authService;

    @InjectMocks // Inyecta los mocks en esta instancia
    private RestaurantUseCase restaurantService;

    private Restaurant validRestaurant;
    private User ownerUser;

    @BeforeEach
    void setUp() {
        // Inicializa un restaurante válido para usar en los tests
        validRestaurant = Restaurant.builder()
                .name("Restaurante Test")
                .nit("1234567890")
                .address("Calle Falsa 123")
                .phoneNumber("3001234567")
                .urlLogo("http://logo.com/test.png")
                .ownerId(1L)
                .build();

        // Inicializa un usuario propietario válido
        ownerUser = User.builder()
                .id(1L)
                .name("Juan")
                .lastName("Perez")
                .roleName(MessageEnum.PROPIETARIO.getMessage())
                .build();

        // Comportamiento predeterminado de los mocks para evitar excepciones en rutas exitosas
        // Usar Mockito.lenient() para evitar UnnecessaryStubbingException en tests específicos
        Mockito.lenient().when(validationUtils.isValidName(anyString())).thenReturn(true);
        Mockito.lenient().when(validationUtils.isValidNit(anyString())).thenReturn(true);
        Mockito.lenient().when(validationUtils.isValidAdress(anyString())).thenReturn(true);
        Mockito.lenient().when(validationUtils.isValidPhoneNumber(anyString())).thenReturn(true);
        Mockito.lenient().when(validationUtils.isValidUrl(anyString())).thenReturn(true);
        Mockito.lenient().when(validationUtils.isValidOwnerId(anyString())).thenReturn(true); // ownerId es String en validación
        Mockito.lenient().when(validationUtils.containsOnlyNumbers(anyString())).thenReturn(true);
        Mockito.lenient().when(validationUtils.isValidPhoneStructure(anyString())).thenReturn(true);
        Mockito.lenient().when(validationUtils.isValidNameStructure(anyString())).thenReturn(true);
        Mockito.lenient().when(validationUtils.isValidateRole(anyString(), anyString())).thenReturn(true);

        Mockito.lenient().when(authService.findById(anyLong())).thenReturn(Optional.of(ownerUser)); // Propietario encontrado
        Mockito.lenient().when(restaurantPersistencePort.findByName(anyString())).thenReturn(Optional.empty()); // No existe por nombre
        Mockito.lenient().when(restaurantPersistencePort.findByNit(anyString())).thenReturn(Optional.empty()); // No existe por NIT
        Mockito.lenient().when(restaurantPersistencePort.save(validRestaurant)).thenReturn(validRestaurant); // Guardado exitoso
    }

    @Test
    @DisplayName("saveRestaurant: Should save a restaurant successfully with valid data")
    void saveRestaurant_ValidData_ReturnsRestaurant() {
        // GIVEN: Setup en BeforeEach

        // WHEN
        Restaurant savedRestaurant = restaurantService.saveRestaurant(validRestaurant);

        // THEN
        assertNotNull(savedRestaurant);
        assertEquals(validRestaurant.getName(), savedRestaurant.getName());
        // Verifica que se llamó al método save del puerto de persistencia
        verify(restaurantPersistencePort).save(validRestaurant);
        // Verifica que se obtuvo el propietario y se validó el rol
        verify(authService).findById(validRestaurant.getOwnerId());
        verify(validationUtils).isValidateRole(ownerUser.getRoleName(), MessageEnum.PROPIETARIO.getMessage());
        // CAMBIO AQUÍ: Verifica que se construyó el ownerName correctamente con espacio
        assertEquals("Juan Perez", savedRestaurant.getOwnerName());
    }

    // --- Tests para validaciones de campos obligatorios (isValid) ---

    @Test
    @DisplayName("saveRestaurant: Should throw PersonalizedBadRequestException if name is invalid")
    void saveRestaurant_InvalidName_ThrowsPersonalizedBadRequestException() {
        // GIVEN
        when(validationUtils.isValidName(anyString())).thenReturn(false);

        // WHEN & THEN
        PersonalizedBadRequestException exception = assertThrows(PersonalizedBadRequestException.class,
                () -> restaurantService.saveRestaurant(validRestaurant));
        assertEquals(MessageEnum.NAME_REQUIRED.getMessage(), exception.getMessage());
        verify(restaurantPersistencePort, never()).save(any(Restaurant.class)); // No se debe guardar
    }

    @Test
    @DisplayName("saveRestaurant: Should throw PersonalizedBadRequestException if nit is invalid")
    void saveRestaurant_InvalidNit_ThrowsPersonalizedBadRequestException() {
        // GIVEN
        when(validationUtils.isValidNit(anyString())).thenReturn(false);

        // WHEN & THEN
        PersonalizedBadRequestException exception = assertThrows(PersonalizedBadRequestException.class,
                () -> restaurantService.saveRestaurant(validRestaurant));
        assertEquals(MessageEnum.NIT_REQUIRED.getMessage(), exception.getMessage());
        verify(restaurantPersistencePort, never()).save(any(Restaurant.class));
    }

    @Test
    @DisplayName("saveRestaurant: Should throw PersonalizedBadRequestException if address is invalid")
    void saveRestaurant_InvalidAddress_ThrowsPersonalizedBadRequestException() {
        // GIVEN
        when(validationUtils.isValidAdress(anyString())).thenReturn(false);

        // WHEN & THEN
        PersonalizedBadRequestException exception = assertThrows(PersonalizedBadRequestException.class,
                () -> restaurantService.saveRestaurant(validRestaurant));
        assertEquals(MessageEnum.ADDRESS_REQUIRED.getMessage(), exception.getMessage());
        verify(restaurantPersistencePort, never()).save(any(Restaurant.class));
    }

    @Test
    @DisplayName("saveRestaurant: Should throw PersonalizedBadRequestException if phoneNumber is invalid")
    void saveRestaurant_InvalidPhoneNumber_ThrowsPersonalizedBadRequestException() {
        // GIVEN
        when(validationUtils.isValidPhoneNumber(anyString())).thenReturn(false);

        // WHEN & THEN
        PersonalizedBadRequestException exception = assertThrows(PersonalizedBadRequestException.class,
                () -> restaurantService.saveRestaurant(validRestaurant));
        assertEquals(MessageEnum.PHONE_REQUIRED.getMessage(), exception.getMessage());
        verify(restaurantPersistencePort, never()).save(any(Restaurant.class));
    }

    @Test
    @DisplayName("saveRestaurant: Should throw PersonalizedBadRequestException if urlLogo is invalid")
    void saveRestaurant_InvalidUrlLogo_ThrowsPersonalizedBadRequestException() {
        // GIVEN
        when(validationUtils.isValidUrl(anyString())).thenReturn(false);

        // WHEN & THEN
        PersonalizedBadRequestException exception = assertThrows(PersonalizedBadRequestException.class,
                () -> restaurantService.saveRestaurant(validRestaurant));
        assertEquals(MessageEnum.URL_FORMAT.getMessage(), exception.getMessage()); // Revisa este mensaje
        verify(restaurantPersistencePort, never()).save(any(Restaurant.class));
    }

    @Test
    @DisplayName("saveRestaurant: Should throw PersonalizedBadRequestException if ownerId is invalid")
    void saveRestaurant_InvalidOwnerId_ThrowsPersonalizedBadRequestException() {
        // GIVEN
        when(validationUtils.isValidOwnerId(anyString())).thenReturn(false);

        // WHEN & THEN
        PersonalizedBadRequestException exception = assertThrows(PersonalizedBadRequestException.class,
                () -> restaurantService.saveRestaurant(validRestaurant));
        assertEquals(MessageEnum.OWNER_ID_REQUIRED.getMessage(), exception.getMessage());
        verify(restaurantPersistencePort, never()).save(any(Restaurant.class));
    }

    // --- Tests para validaciones de formato (containsOnlyNumbers, isValidPhoneStructure, isValidNameStructure) ---

    @Test
    @DisplayName("saveRestaurant: Should throw PersonalizedBadRequestException if NIT format is invalid")
    void saveRestaurant_InvalidNitFormat_ThrowsPersonalizedBadRequestException() {
        // GIVEN
        when(validationUtils.containsOnlyNumbers(anyString())).thenReturn(false);

        // WHEN & THEN
        PersonalizedBadRequestException exception = assertThrows(PersonalizedBadRequestException.class,
                () -> restaurantService.saveRestaurant(validRestaurant));
        assertEquals(MessageEnum.NIT_FORMAT.getMessage(), exception.getMessage());
        verify(restaurantPersistencePort, never()).save(any(Restaurant.class));
    }

    @Test
    @DisplayName("saveRestaurant: Should throw PersonalizedBadRequestException if Phone number format is invalid")
    void saveRestaurant_InvalidPhoneFormat_ThrowsPersonalizedBadRequestException() {
        // GIVEN
        when(validationUtils.isValidPhoneStructure(anyString())).thenReturn(false);

        // WHEN & THEN
        PersonalizedBadRequestException exception = assertThrows(PersonalizedBadRequestException.class,
                () -> restaurantService.saveRestaurant(validRestaurant));
        assertEquals(MessageEnum.PHONE_FORMAT.getMessage(), exception.getMessage());
        verify(restaurantPersistencePort, never()).save(any(Restaurant.class));
    }

    @Test
    @DisplayName("saveRestaurant: Should throw PersonalizedBadRequestException if Name format is invalid")
    void saveRestaurant_InvalidNameFormat_ThrowsPersonalizedBadRequestException() {
        // GIVEN
        when(validationUtils.isValidNameStructure(anyString())).thenReturn(false);

        // WHEN & THEN
        PersonalizedBadRequestException exception = assertThrows(PersonalizedBadRequestException.class,
                () -> restaurantService.saveRestaurant(validRestaurant));
        assertEquals(MessageEnum.NAME_FORMAT.getMessage(), exception.getMessage());
        verify(restaurantPersistencePort, never()).save(any(Restaurant.class));
    }

    // --- Tests para validación del propietario ---

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

    // --- Tests para validación de existencia de restaurante ---

    @Test
    @DisplayName("saveRestaurant: Should throw PersonalizedException if restaurant with same name already exists")
    void saveRestaurant_RestaurantNameExists_ThrowsPersonalizedException() {
        // GIVEN
        when(restaurantPersistencePort.findByName(anyString())).thenReturn(Optional.of(validRestaurant)); // Ya existe

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
        when(restaurantPersistencePort.findByNit(anyString())).thenReturn(Optional.of(validRestaurant)); // Ya existe

        // WHEN & THEN
        PersonalizedException exception = assertThrows(PersonalizedException.class,
                () -> restaurantService.saveRestaurant(validRestaurant));
        assertEquals(MessageEnum.RESTAURANT_NIT_EXISTS.getMessage(), exception.getMessage());
        verify(restaurantPersistencePort, never()).save(any(Restaurant.class));
    }
}


