package com.pragma.plazadecomidas.restaurantservice.infrastructure.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.plazadecomidas.restaurantservice.application.dto.request.RestaurantRequestDto;
import com.pragma.plazadecomidas.restaurantservice.application.dto.response.RestaurantResponseDto;
import com.pragma.plazadecomidas.restaurantservice.application.handler.IRestaurantHandler;
import com.pragma.plazadecomidas.restaurantservice.domain.exception.PersonalizedBadRequestException;
import com.pragma.plazadecomidas.restaurantservice.domain.exception.PersonalizedException;
import com.pragma.plazadecomidas.restaurantservice.domain.exception.PersonalizedNotFoundException;
import com.pragma.plazadecomidas.restaurantservice.domain.model.MessageEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IRestaurantHandler restaurantHandler;

    @Autowired
    private ObjectMapper objectMapper;



    private RestaurantRequestDto validRequestDto;
    private RestaurantResponseDto successResponseDto;

    private static final String BASE_URL = "/api/v1/restaurants";
    private static final String CREATE_RESTAURANT_URL = BASE_URL + "/create-restaurant";

    @BeforeEach
    void setUp() {
        validRequestDto = new RestaurantRequestDto(
                "Mi Restaurante",
                "123456789",
                "Calle Falsa 123",
                "3101234567",
                "http://logo.com/logo.png",
                1L
        );

        successResponseDto = new RestaurantResponseDto(
                1L,
                "Mi Restaurante",
                "123456789",
                "Calle Falsa 123",
                "3101234567",
                "http://logo.com/logo.png",
                "Owner Name"
        );
    }

    @Test
    @DisplayName("Should return 201 Created and the saved restaurant details when valid request is provided")
    void saveRestaurant_ValidRequest_ReturnsCreatedAndRestaurant() throws Exception {
        // Given
        when(restaurantHandler.saveRestaurant(any(RestaurantRequestDto.class))).thenReturn(successResponseDto);

        // When & Then
        mockMvc.perform(post(CREATE_RESTAURANT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(successResponseDto.getId()))
                .andExpect(jsonPath("$.name").value(successResponseDto.getName()))
                .andExpect(jsonPath("$.nit").value(successResponseDto.getNit()))
                .andExpect(jsonPath("$.ownerName").value(successResponseDto.getOwnerName()));

        verify(restaurantHandler, times(1)).saveRestaurant(any(RestaurantRequestDto.class));
    }

    @Test
    @DisplayName("Should return 400 Bad Request when name is missing ")
    void saveRestaurant_MissingName_ReturnsBadRequest() throws Exception {
        // Given
        RestaurantRequestDto invalidRequestDto = new RestaurantRequestDto(
                null,
                "123456789",
                "Calle Falsa 123",
                "3101234567",
                "http://logo.com/logo.png",
                1L
        );

        doThrow(new PersonalizedBadRequestException(MessageEnum.NAME_REQUIRED.getMessage()))
                .when(restaurantHandler).saveRestaurant(any(RestaurantRequestDto.class));


        // When & Then
        mockMvc.perform(post(CREATE_RESTAURANT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").isNotEmpty());

        verify(restaurantHandler, times(1)).saveRestaurant(any(RestaurantRequestDto.class));
    }

    @Test
    @DisplayName("Should return 400 Bad Request when NIT is missing")
    void saveRestaurant_MissingNit_ReturnsBadRequest() throws Exception {
        // Given
        RestaurantRequestDto invalidRequestDto = new RestaurantRequestDto(
                "Test Name",
                null,
                "Calle Falsa 123",
                "3101234567",
                "http://logo.com/logo.png",
                1L
        );

        doThrow(new PersonalizedBadRequestException(MessageEnum.NIT_REQUIRED.getMessage()))
                .when(restaurantHandler).saveRestaurant(any(RestaurantRequestDto.class));


        // When & Then
        mockMvc.perform(post(CREATE_RESTAURANT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").isNotEmpty());

        verify(restaurantHandler, times(1)).saveRestaurant(any(RestaurantRequestDto.class));
    }

    @Test
    @DisplayName("Should return 400 Bad Request when ownerId is missing ")
    void saveRestaurant_MissingOwnerId_ReturnsBadRequest() throws Exception {
        // Given
        RestaurantRequestDto invalidRequestDto = new RestaurantRequestDto(
                "Test Name",
                "12345",
                "Calle Falsa 123",
                "3101234567",
                "http://logo.com/logo.png",
                null
        );

        doThrow(new PersonalizedBadRequestException(MessageEnum.OWNER_ID_REQUIRED.getMessage()))
                .when(restaurantHandler).saveRestaurant(any(RestaurantRequestDto.class));


        // When & Then
        mockMvc.perform(post(CREATE_RESTAURANT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").isNotEmpty());

        verify(restaurantHandler, times(1)).saveRestaurant(any(RestaurantRequestDto.class));
    }


    @Test
    @DisplayName("Should return 409 Conflict when handler throws PersonalizedException for NAME_REQUIRED")
    void saveRestaurant_HandlerThrowsNameRequiredException_ReturnsConflict() throws Exception {
        // Given
        doThrow(new PersonalizedException(MessageEnum.NAME_REQUIRED.getMessage()))
                .when(restaurantHandler).saveRestaurant(any(RestaurantRequestDto.class));

        // When & Then
        mockMvc.perform(post(CREATE_RESTAURANT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequestDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensaje").value(MessageEnum.NAME_REQUIRED.getMessage()));

        verify(restaurantHandler, times(1)).saveRestaurant(any(RestaurantRequestDto.class));
    }

    @Test
    @DisplayName("Should return 409 Conflict when handler throws PersonalizedException for NIT_REQUIRED")
    void saveRestaurant_HandlerThrowsNitRequiredException_ReturnsConflict() throws Exception {
        // Given
        doThrow(new PersonalizedException(MessageEnum.NIT_REQUIRED.getMessage()))
                .when(restaurantHandler).saveRestaurant(any(RestaurantRequestDto.class));

        // When & Then
        mockMvc.perform(post(CREATE_RESTAURANT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequestDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensaje").value(MessageEnum.NIT_REQUIRED.getMessage()));

        verify(restaurantHandler, times(1)).saveRestaurant(any(RestaurantRequestDto.class));
    }

    @Test
    @DisplayName("Should return 409 Conflict when handler throws PersonalizedException for ADDRESS_REQUIRED")
    void saveRestaurant_HandlerThrowsAddressRequiredException_ReturnsConflict() throws Exception {
        // Given
        doThrow(new PersonalizedException(MessageEnum.ADDRESS_REQUIRED.getMessage()))
                .when(restaurantHandler).saveRestaurant(any(RestaurantRequestDto.class));

        // When & Then
        mockMvc.perform(post(CREATE_RESTAURANT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequestDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensaje").value(MessageEnum.ADDRESS_REQUIRED.getMessage()));

        verify(restaurantHandler, times(1)).saveRestaurant(any(RestaurantRequestDto.class));
    }

    @Test
    @DisplayName("Should return 409 Conflict when handler throws PersonalizedException for PHONE_REQUIRED")
    void saveRestaurant_HandlerThrowsPhoneRequiredException_ReturnsConflict() throws Exception {
        // Given
        doThrow(new PersonalizedException(MessageEnum.PHONE_REQUIRED.getMessage()))
                .when(restaurantHandler).saveRestaurant(any(RestaurantRequestDto.class));

        // When & Then
        mockMvc.perform(post(CREATE_RESTAURANT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequestDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensaje").value(MessageEnum.PHONE_REQUIRED.getMessage()));

        verify(restaurantHandler, times(1)).saveRestaurant(any(RestaurantRequestDto.class));
    }

    @Test
    @DisplayName("Should return 409 Conflict when handler throws PersonalizedException for URL_REQUIRED")
    void saveRestaurant_HandlerThrowsUrlRequiredException_ReturnsConflict() throws Exception {
        // Given
        doThrow(new PersonalizedException(MessageEnum.URL_REQUIRED.getMessage()))
                .when(restaurantHandler).saveRestaurant(any(RestaurantRequestDto.class));

        // When & Then
        mockMvc.perform(post(CREATE_RESTAURANT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequestDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensaje").value(MessageEnum.URL_REQUIRED.getMessage()));

        verify(restaurantHandler, times(1)).saveRestaurant(any(RestaurantRequestDto.class));
    }

    @Test
    @DisplayName("Should return 409 Conflict when handler throws PersonalizedException for OWNER_ID_REQUIRED")
    void saveRestaurant_HandlerThrowsOwnerIdRequiredException_ReturnsConflict() throws Exception {
        // Given
        doThrow(new PersonalizedException(MessageEnum.OWNER_ID_REQUIRED.getMessage()))
                .when(restaurantHandler).saveRestaurant(any(RestaurantRequestDto.class));

        // When & Then
        mockMvc.perform(post(CREATE_RESTAURANT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequestDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensaje").value(MessageEnum.OWNER_ID_REQUIRED.getMessage()));

        verify(restaurantHandler, times(1)).saveRestaurant(any(RestaurantRequestDto.class));
    }

    @Test
    @DisplayName("Should return 409 Conflict when handler throws PersonalizedException for NIT_FORMAT")
    void saveRestaurant_HandlerThrowsNitFormatException_ReturnsConflict() throws Exception {
        // Given
        doThrow(new PersonalizedException(MessageEnum.NIT_FORMAT.getMessage()))
                .when(restaurantHandler).saveRestaurant(any(RestaurantRequestDto.class));

        // When & Then
        mockMvc.perform(post(CREATE_RESTAURANT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequestDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensaje").value(MessageEnum.NIT_FORMAT.getMessage()));

        verify(restaurantHandler, times(1)).saveRestaurant(any(RestaurantRequestDto.class));
    }

    @Test
    @DisplayName("Should return 409 Conflict when handler throws PersonalizedException for PHONE_FORMAT")
    void saveRestaurant_HandlerThrowsPhoneFormatException_ReturnsConflict() throws Exception {
        // Given
        doThrow(new PersonalizedException(MessageEnum.PHONE_FORMAT.getMessage()))
                .when(restaurantHandler).saveRestaurant(any(RestaurantRequestDto.class));

        // When & Then
        mockMvc.perform(post(CREATE_RESTAURANT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequestDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensaje").value(MessageEnum.PHONE_FORMAT.getMessage()));

        verify(restaurantHandler, times(1)).saveRestaurant(any(RestaurantRequestDto.class));
    }

    @Test
    @DisplayName("Should return 409 Conflict when handler throws PersonalizedException for NAME_FORMAT")
    void saveRestaurant_HandlerThrowsNameFormatException_ReturnsConflict() throws Exception {
        // Given
        doThrow(new PersonalizedException(MessageEnum.NAME_FORMAT.getMessage()))
                .when(restaurantHandler).saveRestaurant(any(RestaurantRequestDto.class));

        // When & Then
        mockMvc.perform(post(CREATE_RESTAURANT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequestDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensaje").value(MessageEnum.NAME_FORMAT.getMessage()));

        verify(restaurantHandler, times(1)).saveRestaurant(any(RestaurantRequestDto.class));
    }

    @Test
    @DisplayName("Should return 409 Conflict when handler throws PersonalizedException for OWNER_NOT_PROPRIETARIO")
    void saveRestaurant_HandlerThrowsOwnerNotProprietarioException_ReturnsConflict() throws Exception {
        // Given
        doThrow(new PersonalizedException(MessageEnum.OWNER_NOT_PROPRIETARIO.getMessage()))
                .when(restaurantHandler).saveRestaurant(any(RestaurantRequestDto.class));

        // When & Then
        mockMvc.perform(post(CREATE_RESTAURANT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequestDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensaje").value(MessageEnum.OWNER_NOT_PROPRIETARIO.getMessage()));

        verify(restaurantHandler, times(1)).saveRestaurant(any(RestaurantRequestDto.class));
    }

    @Test
    @DisplayName("Should return 404 Not Found when handler throws PersonalizedException for OWNER_NOT_FOUND")
    void saveRestaurant_HandlerThrowsOwnerNotFoundException_ReturnsNotFound() throws Exception {
        // Given
        doThrow(new PersonalizedNotFoundException(MessageEnum.OWNER_NOT_FOUND.getMessage()))
                .when(restaurantHandler).saveRestaurant(any(RestaurantRequestDto.class));

        // When & Then
        mockMvc.perform(post(CREATE_RESTAURANT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequestDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value(MessageEnum.OWNER_NOT_FOUND.getMessage()));

        verify(restaurantHandler, times(1)).saveRestaurant(any(RestaurantRequestDto.class));
    }

    @Test
    @DisplayName("Should return 400 Bad Request when handler throws PersonalizedException for RESTAURANT_REQUEST_NULL")
    void saveRestaurant_HandlerThrowsRequestNull_ReturnsBadRequest() throws Exception {
        // Given
        doThrow(new PersonalizedBadRequestException(MessageEnum.RESTAURANT_REQUEST_NULL.getMessage()))
                .when(restaurantHandler).saveRestaurant(any(RestaurantRequestDto.class));

        // When & Then
        mockMvc.perform(post(CREATE_RESTAURANT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value(MessageEnum.RESTAURANT_REQUEST_NULL.getMessage()));

        verify(restaurantHandler, times(1)).saveRestaurant(any(RestaurantRequestDto.class));
    }

}