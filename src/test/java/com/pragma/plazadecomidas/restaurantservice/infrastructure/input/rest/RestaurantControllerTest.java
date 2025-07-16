package com.pragma.plazadecomidas.restaurantservice.infrastructure.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.plazadecomidas.restaurantservice.application.dto.request.RestaurantRequestDto;
import com.pragma.plazadecomidas.restaurantservice.domain.model.MessageEnum;
import com.pragma.plazadecomidas.restaurantservice.domain.model.Restaurant;
import com.pragma.plazadecomidas.restaurantservice.domain.model.User;
import com.pragma.plazadecomidas.restaurantservice.domain.spi.IAuthService;
import com.pragma.plazadecomidas.restaurantservice.domain.spi.IRestaurantPersistencePort;
import com.pragma.plazadecomidas.restaurantservice.domain.util.ValidationUtils;
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


import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean(name = "restaurantPersistencePort")
    private IRestaurantPersistencePort restaurantPersistencePort;

    @MockBean
    private IAuthService authService;

    @MockBean
    private ValidationUtils validationUtils;


    @Test
    @DisplayName("Should return 201 Created when a valid restaurant request is made and owner is valid")
    void createRestaurant_ValidRequest_ReturnsCreated() throws Exception {
        RestaurantRequestDto requestDto = new RestaurantRequestDto(
                "Mi Nuevo Restaurante", "123456789", "Calle Falsa 123", "3001234567", "http://logo.com/new.png", 1L
        );
        Restaurant savedRestaurant = new Restaurant(
                1L, requestDto.getName(), requestDto.getNit(), requestDto.getAddress(),
                requestDto.getPhoneNumber(), requestDto.getUrlLogo(), requestDto.getOwnerId(), ""
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

        when(validationUtils.isValid("Mi Nuevo Restaurante")).thenReturn(true);
        when(validationUtils.isValid("123456789")).thenReturn(true);
        when(validationUtils.isValid("Calle Falsa 123")).thenReturn(true);
        when(validationUtils.isValid("3001234567")).thenReturn(true);
        when(validationUtils.isValid("http://logo.com/new.png")).thenReturn(true);
        when(validationUtils.isValid("1")).thenReturn(true);
        when(validationUtils.containsOnlyNumbers("123456789")).thenReturn(true);
        when(validationUtils.isValidPhoneStructure("3001234567")).thenReturn(true);
        when(validationUtils.isValidUrl("http://logo.com/new.png")).thenReturn(true);
        when(validationUtils.isValidNameStructure("Mi Nuevo Restaurante")).thenReturn(true);
        when(validationUtils.isValidateRole("PROPIETARIO", "PROPIETARIO")).thenReturn(true);

        when(restaurantPersistencePort.save(any(Restaurant.class))).thenReturn(savedRestaurant);

        mockMvc.perform(post("/api/v1/restaurants/create-restaurant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Mi Nuevo Restaurante"));
    }

    @Test
    @DisplayName("Should return 409 Conflict for invalid phone number format due to service validation")
    void createRestaurant_InvalidPhoneNumberFormat_ReturnsBadRequest() throws Exception {
        RestaurantRequestDto requestDto = new RestaurantRequestDto(
                "Otro Restaurante", "987654321", "Carrera 45", "ABCDEFG", "http://logo.com/otro.png", 2L
        );

        when(authService.findById(2L)).thenReturn(
                java.util.Optional.of(
                        new com.pragma.plazadecomidas.restaurantservice.domain.model.User(
                                2L, "Nombre", "Apellido", "mail@mail.com", "3000000000", "12345678", "password",
                                java.time.LocalDate.of(1990, 1, 1), "1", "PROPIETARIO"
                        )
                )
        );


        when(validationUtils.isValid("Otro Restaurante")).thenReturn(true);
        when(validationUtils.isValid("987654321")).thenReturn(true);
        when(validationUtils.isValid("Carrera 45")).thenReturn(true);
        when(validationUtils.isValid("ABCDEFG")).thenReturn(true);
        when(validationUtils.isValid("http://logo.com/otro.png")).thenReturn(true);
        when(validationUtils.isValid("2")).thenReturn(true);
        when(validationUtils.containsOnlyNumbers("987654321")).thenReturn(true);
        when(validationUtils.isValidUrl("http://logo.com/otro.png")).thenReturn(true);
        when(validationUtils.isValidNameStructure("Otro Restaurante")).thenReturn(true);
        when(validationUtils.isValidateRole("PROPIETARIO", "PROPIETARIO")).thenReturn(true);
        when(validationUtils.isValidPhoneStructure("ABCDEFG")).thenReturn(false);

        mockMvc.perform(post("/api/v1/restaurants/create-restaurant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensaje").value(MessageEnum.PHONE_FORMAT.getMessage()));
    }

    @Test
    @DisplayName("Should return 403 Forbidden when ownerId does not correspond to an OWNER role")
    void createRestaurant_OwnerNotOwner_ReturnsForbidden() throws Exception {
        RestaurantRequestDto requestDto = new RestaurantRequestDto(
                "Restaurante Prohibido", "444555666", "Avenida Siempre Viva", "3006665544", "http://logo.com/prohibido.png", 4L
        );



        mockMvc.perform(post("/api/v1/restaurants/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isForbidden());
    }

}