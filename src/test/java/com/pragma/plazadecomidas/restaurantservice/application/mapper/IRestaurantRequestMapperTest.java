package com.pragma.plazadecomidas.restaurantservice.application.mapper;

import com.pragma.plazadecomidas.restaurantservice.application.dto.request.RestaurantRequestDto;
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
class IRestaurantRequestMapperTest {

    // MapStruct genera una implementación de esta interfaz que Spring inyecta.
    @Autowired
    private IRestaurantRequestMapper restaurantRequestMapper;

    @BeforeEach
    void setUp() {
        // No se necesita inicialización especial aquí, Spring se encarga de la inyección.
    }

    @Test
    @DisplayName("toRestaurant: Should map RestaurantRequestDto to Restaurant correctly")
    void toRestaurant_ValidDto_ReturnsRestaurant() {
        // GIVEN
        RestaurantRequestDto requestDto = new RestaurantRequestDto(
                "Mi Restaurante Test",
                "1234567890",
                "Calle Falsa 123",
                "3101234567",
                "http://logo.com/test.png",
                1L // ID del propietario
        );

        // WHEN
        Restaurant restaurant = restaurantRequestMapper.toRestaurant(requestDto);

        // THEN
        assertNotNull(restaurant);
        assertEquals("Mi Restaurante Test", restaurant.getName());
        assertEquals("1234567890", restaurant.getNit());
        assertEquals("Calle Falsa 123", restaurant.getAddress());
        assertEquals("3101234567", restaurant.getPhoneNumber());
        assertEquals("http://logo.com/test.png", restaurant.getUrlLogo());
        assertEquals(1L, restaurant.getOwnerId());
    }

    @Test
    @DisplayName("toRestaurant: Should handle null RestaurantRequestDto gracefully")
    void toRestaurant_NullDto_ReturnsNull() {
        // GIVEN
        RestaurantRequestDto requestDto = null;

        // WHEN
        Restaurant restaurant = restaurantRequestMapper.toRestaurant(requestDto);

        // THEN
        assertNull(restaurant);
    }

    @Test
    @DisplayName("toRestaurant: Should handle partially null fields in RestaurantRequestDto")
    void toRestaurant_PartiallyNullDto_MapsNonNullFields() {
        // GIVEN
        RestaurantRequestDto requestDto = new RestaurantRequestDto(
                "Restaurante Parcial",
                null, // NIT nulo
                "Otra Dirección",
                "3009876543",
                null, // UrlLogo nula
                2L
        );

        // WHEN
        Restaurant restaurant = restaurantRequestMapper.toRestaurant(requestDto);

        // THEN
        assertNotNull(restaurant);
        assertEquals("Restaurante Parcial", restaurant.getName());
        assertNull(restaurant.getNit()); // Debe ser nulo
        assertEquals("Otra Dirección", restaurant.getAddress());
        assertEquals("3009876543", restaurant.getPhoneNumber());
        assertNull(restaurant.getUrlLogo()); // Debe ser nulo
        assertEquals(2L, restaurant.getOwnerId());
    }
}