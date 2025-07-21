package com.pragma.plazadecomidas.restaurantservice.infrastructure.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.plazadecomidas.restaurantservice.application.dto.request.RestaurantRequestDto;
import com.pragma.plazadecomidas.restaurantservice.application.dto.response.RestaurantResponseDto;
import com.pragma.plazadecomidas.restaurantservice.application.handler.IRestaurantHandler;
import com.pragma.plazadecomidas.restaurantservice.domain.exception.PersonalizedException;
import com.pragma.plazadecomidas.restaurantservice.domain.exception.PersonalizedNotFoundException;
import com.pragma.plazadecomidas.restaurantservice.domain.model.MessageEnum;
import com.pragma.plazadecomidas.restaurantservice.infrastructure.configuration.SecurityConfig;
import com.pragma.plazadecomidas.restaurantservice.infrastructure.util.ValidationConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RestaurantController.class)
@Import(SecurityConfig.class)
class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IRestaurantHandler restaurantHandler;

    @Autowired
    private ObjectMapper objectMapper;

    private RestaurantRequestDto validRestaurantRequestDto;
    private RestaurantResponseDto savedRestaurantResponseDto;

    @BeforeEach
    void setUp() {
        validRestaurantRequestDto = RestaurantRequestDto.builder()
                .name("Restaurante Nuevo")
                .nit("123456789")
                .address("Av. Principal 123")
                .phoneNumber("+573101234567")
                .urlLogo("http://logo.com/new.png")
                .ownerId(1L)
                .build();

        savedRestaurantResponseDto = RestaurantResponseDto.builder()
                .id(1L)
                .name("Restaurante Nuevo")
                .nit("123456789")
                .address("Av. Principal 123")
                .phoneNumber("+573101234567")
                .urlLogo("http://logo.com/new.png")
                .ownerName("Juan Perez")
                .build();

        when(restaurantHandler.saveRestaurant(any(RestaurantRequestDto.class))).thenReturn(savedRestaurantResponseDto);
    }

    @Test
    @DisplayName("save: Should return 201 CREATED when restaurant is saved successfully")
    @WithMockUser(roles = "ADMINISTRADOR")
    void save_ValidRestaurant_ReturnsCreated() throws Exception {
        mockMvc.perform(post("/api/v1/restaurants/create-restaurant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRestaurantRequestDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$." + ValidationConstants.JSON_NOMBRE).value(validRestaurantRequestDto.getName()))
                .andExpect(jsonPath("$." + ValidationConstants.JSON_NIT).value(validRestaurantRequestDto.getNit()))
                .andExpect(jsonPath("$." + ValidationConstants.JSON_DIRECCION).value(validRestaurantRequestDto.getAddress()))
                .andExpect(jsonPath("$." + ValidationConstants.JSON_CELULAR).value(validRestaurantRequestDto.getPhoneNumber()))
                .andExpect(jsonPath("$." + ValidationConstants.JSON_URL_LOGO).value(validRestaurantRequestDto.getUrlLogo()))
                .andExpect(jsonPath("$." + ValidationConstants.JSON_OWNER_NAME).value(savedRestaurantResponseDto.getOwnerName()));
    }

    @Test
    @DisplayName("save: Should return 400 BAD REQUEST when restaurant name is null (handled by DTO validation)")
    @WithMockUser(roles = "ADMINISTRADOR")
    void save_NullName_ReturnsBadRequestFromDtoValidation() throws Exception {
        RestaurantRequestDto invalidDto = validRestaurantRequestDto.toBuilder().name(null).build();

        mockMvc.perform(post("/api/v1/restaurants/create-restaurant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value(ValidationConstants.RESTAURANT_NAME_REQUIRED_MESSAGE));
    }

    @Test
    @DisplayName("save: Should return 400 BAD REQUEST when restaurant nit is invalid format")
    @WithMockUser(roles = "ADMINISTRADOR")
    void save_InvalidNitFormat_ReturnsBadRequestFromDtoValidation() throws Exception {
        RestaurantRequestDto invalidDto = validRestaurantRequestDto.toBuilder().nit("ABC").build();

        mockMvc.perform(post("/api/v1/restaurants/create-restaurant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                // AHORA USA ValidationConstants.JSON_NIT
                .andExpect(jsonPath("$.mensaje").value(ValidationConstants.RESTAURANT_NIT_FORMAT_MESSAGE));
    }

    @Test
    @DisplayName("save: Should return 404 NOT FOUND if owner is not found")
    @WithMockUser(roles = "ADMINISTRADOR")
    void save_OwnerNotFound_ReturnsNotFound() throws Exception {
        when(restaurantHandler.saveRestaurant(any(RestaurantRequestDto.class)))
                .thenThrow(new PersonalizedNotFoundException(MessageEnum.OWNER_NOT_FOUND.getMessage()));

        mockMvc.perform(post("/api/v1/restaurants/create-restaurant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRestaurantRequestDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value(MessageEnum.OWNER_NOT_FOUND.getMessage()));
    }

    @Test
    @DisplayName("save: Should return 409 CONFLICT if restaurant name already exists")
    @WithMockUser(roles = "ADMINISTRADOR")
    void save_RestaurantNameExists_ReturnsConflict() throws Exception {
        when(restaurantHandler.saveRestaurant(any(RestaurantRequestDto.class)))
                .thenThrow(new PersonalizedException(String.format(MessageEnum.RESTAURANT_NAME_EXISTS.getMessage(), validRestaurantRequestDto.getName())));

        mockMvc.perform(post("/api/v1/restaurants/create-restaurant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRestaurantRequestDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensaje").value(String.format(MessageEnum.RESTAURANT_NAME_EXISTS.getMessage(), validRestaurantRequestDto.getName())));
    }

}