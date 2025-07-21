package com.pragma.plazadecomidas.restaurantservice.application.handler;

import com.pragma.plazadecomidas.restaurantservice.application.dto.request.RestaurantRequestDto;
import com.pragma.plazadecomidas.restaurantservice.application.dto.response.RestaurantResponseDto;
import com.pragma.plazadecomidas.restaurantservice.application.handler.impl.RestaurantHandlerImpl;
import com.pragma.plazadecomidas.restaurantservice.application.mapper.IRestaurantRequestMapper;
import com.pragma.plazadecomidas.restaurantservice.domain.api.IRestaurantServicePort;
import com.pragma.plazadecomidas.restaurantservice.domain.exception.PersonalizedBadRequestException;
import com.pragma.plazadecomidas.restaurantservice.domain.exception.PersonalizedException;
import com.pragma.plazadecomidas.restaurantservice.domain.exception.PersonalizedNotFoundException;
import com.pragma.plazadecomidas.restaurantservice.domain.model.MessageEnum;
import com.pragma.plazadecomidas.restaurantservice.domain.model.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantHandlerImplTest {


    @Mock
    private IRestaurantServicePort restaurantServicePort;

    @Mock
    private IRestaurantRequestMapper restaurantRequestMapper;    

    @InjectMocks
    private RestaurantHandlerImpl restaurantHandler;

    private RestaurantRequestDto restaurantRequestDto;
    private Restaurant restaurantModel;
    private RestaurantResponseDto restaurantResponseDto;

    @BeforeEach
    void setUp() {
        restaurantRequestDto = new RestaurantRequestDto(
                "Mi Restaurante", "123456789", "Calle 123", "3001234567", "http://logo.png", 1L
        );

        restaurantModel = new Restaurant(
                null, "Mi Restaurante", "123456789", "Calle 123", "3001234567", "http://logo.png", 1L, null
        );
        Restaurant savedRestaurantModel = new Restaurant(
                1L, "Mi Restaurante", "123456789", "Calle 123", "3001234567", "http://logo.png", 1L, "John Doe" // Simula que el servicio añade el nombre del propietario
        );


        restaurantResponseDto = new RestaurantResponseDto(
                savedRestaurantModel.getId(),
                savedRestaurantModel.getName(),
                savedRestaurantModel.getNit(),
                savedRestaurantModel.getAddress(),
                savedRestaurantModel.getPhoneNumber(),
                savedRestaurantModel.getUrlLogo(),
                savedRestaurantModel.getOwnerName()
        );
    }

    @Test
    @DisplayName("Should save a restaurant successfully and return response DTO")
    void saveRestaurant_Success() {
        // Given
        when(restaurantRequestMapper.toRestaurant(restaurantRequestDto)).thenReturn(restaurantModel);
        Restaurant serviceReturnedModel = new Restaurant(
                1L,
                "Mi Restaurante",
                "123456789",
                "Calle 123",
                "3001234567",
                "http://logo.png",
                1L,
                "Owner Name Example"
        );
        when(restaurantServicePort.saveRestaurant(restaurantModel)).thenReturn(serviceReturnedModel);
        when(restaurantRequestMapper.toResponseDto(serviceReturnedModel)).thenReturn(restaurantResponseDto);


        // When
        RestaurantResponseDto result = restaurantHandler.saveRestaurant(restaurantRequestDto);

        // Then
        assertNotNull(result);
        assertEquals(restaurantResponseDto.getId(), result.getId());
        assertEquals(restaurantResponseDto.getName(), result.getName());
        assertEquals(restaurantResponseDto.getOwnerName(), result.getOwnerName());

        // Verify
        verify(restaurantRequestMapper, times(1)).toRestaurant(restaurantRequestDto);
        verify(restaurantServicePort, times(1)).saveRestaurant(restaurantModel);
        verify(restaurantRequestMapper, times(1)).toResponseDto(serviceReturnedModel);
    }

    @Test
    @DisplayName("Should throw PersonalizedBadRequestException when restaurantRequestDto is null")
    void saveRestaurant_ThrowsException_RequestDtoNull() {
        // Given
        restaurantRequestDto = null;

        // When & Then
        PersonalizedBadRequestException exception = assertThrows(PersonalizedBadRequestException.class,
                () -> restaurantHandler.saveRestaurant(restaurantRequestDto));

        assertEquals(MessageEnum.RESTAURANT_REQUEST_NULL.getMessage(), exception.getMessage());

        // Verify
        verify(restaurantServicePort, times(0)).saveRestaurant(any());
    }

    @Test
    @DisplayName("Should throw PersonalizedException when service throws it (e.g., NAME_REQUIRED)")
    void saveRestaurant_ThrowsPersonalizedException_NameRequired() {
        // Given
        when(restaurantRequestMapper.toRestaurant(restaurantRequestDto)).thenReturn(restaurantModel);
        when(restaurantServicePort.saveRestaurant(restaurantModel))
                .thenThrow(new PersonalizedException(MessageEnum.NAME_REQUIRED.getMessage()));

        // When & Then
        PersonalizedException exception = assertThrows(PersonalizedException.class,
                () -> restaurantHandler.saveRestaurant(restaurantRequestDto));

        assertEquals(MessageEnum.NAME_REQUIRED.getMessage(), exception.getMessage());

        // Verify
        verify(restaurantRequestMapper, times(1)).toRestaurant(restaurantRequestDto);
        verify(restaurantServicePort, times(1)).saveRestaurant(restaurantModel);
        verifyNoMoreInteractions(restaurantRequestMapper, restaurantServicePort);
    }

    @Test
    @DisplayName("Should throw PersonalizedException when service throws it (e.g., OWNER_NOT_FOUND)")
    void saveRestaurant_ThrowsPersonalizedException_OwnerNotFound() {
        // Given
        when(restaurantRequestMapper.toRestaurant(restaurantRequestDto)).thenReturn(restaurantModel);
        when(restaurantServicePort.saveRestaurant(restaurantModel))
                .thenThrow(new PersonalizedNotFoundException(MessageEnum.OWNER_NOT_FOUND.getMessage()));

        // When & Then
        PersonalizedNotFoundException exception = assertThrows(PersonalizedNotFoundException.class,
                () -> restaurantHandler.saveRestaurant(restaurantRequestDto));

        assertEquals(MessageEnum.OWNER_NOT_FOUND.getMessage(), exception.getMessage());

        // Verify
        verify(restaurantRequestMapper, times(1)).toRestaurant(restaurantRequestDto);
        verify(restaurantServicePort, times(1)).saveRestaurant(restaurantModel);
        verifyNoMoreInteractions(restaurantRequestMapper, restaurantServicePort);
    }
}
