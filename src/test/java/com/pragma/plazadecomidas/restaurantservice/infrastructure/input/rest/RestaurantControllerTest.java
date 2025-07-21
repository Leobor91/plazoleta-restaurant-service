package com.pragma.plazadecomidas.restaurantservice.infrastructure.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.plazadecomidas.restaurantservice.application.dto.request.RestaurantRequestDto;
import com.pragma.plazadecomidas.restaurantservice.application.dto.response.RestaurantResponseDto;
import com.pragma.plazadecomidas.restaurantservice.application.handler.IRestaurantHandler;
import com.pragma.plazadecomidas.restaurantservice.domain.exception.PersonalizedBadRequestException;
import com.pragma.plazadecomidas.restaurantservice.domain.exception.PersonalizedException;
import com.pragma.plazadecomidas.restaurantservice.domain.exception.PersonalizedNotFoundException;
import com.pragma.plazadecomidas.restaurantservice.domain.model.MessageEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@WebMvcTest(RestaurantController.class)
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser(roles = "ADMINISTRADOR")
class RestaurantControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IRestaurantHandler restaurantHandler;

    private RestaurantRequestDto validRequestDto;
    private RestaurantResponseDto validResponseDto;

    {
        validRequestDto = new RestaurantRequestDto(
                "Mi Restaurante Feliz",
                "1234567890",
                "Av. Siempre Viva 742",
                "3001234567",
                "http://logo.com/feliz.png",
                1L
        );

        validResponseDto = new RestaurantResponseDto(
                1L,
                "Mi Restaurante Feliz",
                "1234567890",
                "Av. Siempre Viva 742",
                "3001234567",
                "http://logo.com/feliz.png",
                "Juan Perez"
        );
    }

    @Test
    @DisplayName("save: Should return 201 CREATED when restaurant is saved successfully")
    void save_ValidRestaurant_ReturnsCreated() throws Exception {
        // GIVEN
        // Para asegurar que este stub sea prioritario para un DTO válido específico,
        // o si prefieres dejarlo genérico con any(), asegúrate de que no haya conflictos
        // con otros stubs más específicos para casos de error.
        when(restaurantHandler.saveRestaurant(any(RestaurantRequestDto.class))).thenReturn(validResponseDto);

        // WHEN & THEN
        mockMvc.perform(post("/api/v1/restaurants/create-restaurant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(validResponseDto.getName()))
                .andExpect(jsonPath("$.nit").value(validResponseDto.getNit()))
                .andExpect(jsonPath("$.ownerName").value(validResponseDto.getOwnerName()));
    }

    @Test
    @DisplayName("save: Should return 400 BAD REQUEST when PersonalizedBadRequestException is thrown")
    void save_InvalidRequest_ReturnsBadRequest() throws Exception {
        // GIVEN
        String errorMessage = MessageEnum.NAME_REQUIRED.getMessage();
        when(restaurantHandler.saveRestaurant(any(RestaurantRequestDto.class)))
                .thenThrow(new PersonalizedBadRequestException(errorMessage));

        // WHEN & THEN
        mockMvc.perform(post("/api/v1/restaurants/create-restaurant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value(errorMessage));
    }

    @Test
    @DisplayName("save: Should return 404 NOT FOUND when PersonalizedNotFoundException is thrown")
    void save_OwnerNotFound_ReturnsNotFound() throws Exception {
        // GIVEN
        String errorMessage = MessageEnum.OWNER_NOT_FOUND.getMessage();
        when(restaurantHandler.saveRestaurant(any(RestaurantRequestDto.class)))
                .thenThrow(new PersonalizedNotFoundException(errorMessage));

        // WHEN & THEN
        mockMvc.perform(post("/api/v1/restaurants/create-restaurant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequestDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value(errorMessage));
    }

    @Test
    @DisplayName("save: Should return 409 CONFLICT when PersonalizedException is thrown (business rule violation)")
    void save_BusinessRuleViolation_ReturnsConflict() throws Exception {
        // GIVEN
        String errorMessage = MessageEnum.RESTAURANT_NAME_EXISTS.getMessage();
        when(restaurantHandler.saveRestaurant(any(RestaurantRequestDto.class)))
                .thenThrow(new PersonalizedException(errorMessage));

        // WHEN & THEN
        mockMvc.perform(post("/api/v1/restaurants/create-restaurant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequestDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensaje").value(errorMessage));
    }

    @Test
    @DisplayName("save: Should return 400 BAD REQUEST when restaurant name is null (handled by domain validation)")
    void save_NullName_ReturnsBadRequestFromDomain() throws Exception {
        // GIVEN
        RestaurantRequestDto invalidRequestDto = new RestaurantRequestDto(
                null, // Nombre nulo para simular un error que el dominio validaría
                "1234567890",
                "Av. Siempre Viva 742",
                "3001234567",
                "http://logo.com/feliz.png",
                1L
        );
        String errorMessage = MessageEnum.NAME_REQUIRED.getMessage();

        // *** CAMBIO CLAVE AQUÍ: Usamos ArgumentMatchers.argThat para una coincidencia más precisa ***
        // Esto asegura que el mock lance la excepción SOLO cuando el DTO recibido tenga el 'name' nulo.
        when(restaurantHandler.saveRestaurant(ArgumentMatchers.argThat(dto -> dto.getName() == null)))
                .thenThrow(new PersonalizedBadRequestException(errorMessage));


        // WHEN & THEN
        mockMvc.perform(post("/api/v1/restaurants/create-restaurant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value(errorMessage));
    }
}