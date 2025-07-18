package com.pragma.plazadecomidas.restaurantservice.application.mapper;


import com.pragma.plazadecomidas.restaurantservice.application.dto.response.RestaurantResponseDto;
import com.pragma.plazadecomidas.restaurantservice.domain.model.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class IRestaurantResponseMapperTest {

    @Autowired
    private IRestaurantResponseMapper restaurantResponseMapper;

    @BeforeEach
    void setUp() {
        // No se necesita inicialización especial aquí.
    }

    @Test
    @DisplayName("toResponseDto: Should map Restaurant to RestaurantResponseDto correctly")
    void toResponseDto_ValidRestaurant_ReturnsResponseDto() {
        // GIVEN
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Mi Restaurante Fabuloso");
        restaurant.setNit("987654321");
        restaurant.setAddress("Avenida Siempre Viva 742");
        restaurant.setPhoneNumber("3209876543");
        restaurant.setUrlLogo("http://logo.com/fabuloso.png");
        restaurant.setOwnerId(5L); // ID del propietario
        restaurant.setOwnerName("Juan Owner"); // Simula que el servicio añade el nombre del propietario

        // WHEN
        RestaurantResponseDto responseDto = restaurantResponseMapper.toResponseDto(restaurant);

        // THEN
        assertNotNull(responseDto);
        assertEquals("Mi Restaurante Fabuloso", responseDto.getName());
        assertEquals("987654321", responseDto.getNit());
        assertEquals("Avenida Siempre Viva 742", responseDto.getAddress());
        assertEquals("3209876543", responseDto.getPhoneNumber());
        assertEquals("http://logo.com/fabuloso.png", responseDto.getUrlLogo());
        assertEquals("Juan Owner", responseDto.getOwnerName());
    }

    @Test
    @DisplayName("toResponseDto: Should handle null Restaurant gracefully")
    void toResponseDto_NullRestaurant_ReturnsNull() {
        // GIVEN
        Restaurant restaurant = null;

        // WHEN
        RestaurantResponseDto responseDto = restaurantResponseMapper.toResponseDto(restaurant);

        // THEN
        assertNull(responseDto);
    }

    @Test
    @DisplayName("toResponseDto: Should handle partially null fields in Restaurant")
    void toResponseDto_PartiallyNullRestaurant_MapsNonNullFields() {
        // GIVEN
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Restaurante Con Nulos");
        // nit es nulo
        restaurant.setAddress("Calle Sin Nombre");
        restaurant.setPhoneNumber("3112223344");
        // urlLogo es nulo
        restaurant.setOwnerId(6L);
        restaurant.setOwnerName("Samuel Owner"); // Simula que el servicio añade el nombre del propietario

        // WHEN
        RestaurantResponseDto responseDto = restaurantResponseMapper.toResponseDto(restaurant);

        // THEN
        assertNotNull(responseDto);
        assertEquals("Restaurante Con Nulos", responseDto.getName());
        assertNull(responseDto.getNit()); // Debe ser nulo
        assertEquals("Calle Sin Nombre", responseDto.getAddress());
        assertEquals("3112223344", responseDto.getPhoneNumber());
        assertNull(responseDto.getUrlLogo()); // Debe ser nulo
        assertEquals("Samuel Owner", responseDto.getOwnerName());
    }

}
