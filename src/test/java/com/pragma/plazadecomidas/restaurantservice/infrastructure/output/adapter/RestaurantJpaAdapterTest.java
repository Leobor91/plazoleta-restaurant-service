package com.pragma.plazadecomidas.restaurantservice.infrastructure.output.adapter;


import com.pragma.plazadecomidas.restaurantservice.application.handler.impl.RestaurantHandlerImpl;
import com.pragma.plazadecomidas.restaurantservice.domain.api.impl.RestaurantServicePortImpl;
import com.pragma.plazadecomidas.restaurantservice.domain.model.Restaurant;
import com.pragma.plazadecomidas.restaurantservice.infrastructure.configuration.BeanConfiguration;
import com.pragma.plazadecomidas.restaurantservice.infrastructure.output.jpa.entity.RestaurantEntity;
import com.pragma.plazadecomidas.restaurantservice.infrastructure.output.jpa.mapper.IRestaurantEntityMapper;
import com.pragma.plazadecomidas.restaurantservice.infrastructure.output.jpa.repository.IRestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest(
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                RestaurantJpaAdapter.class,
                IRestaurantEntityMapper.class
        }),
        // Excluir estas clases por su tipo específico para evitar la carga de sus dependencias no JPA
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                BeanConfiguration.class,
                RestaurantHandlerImpl.class,
                RestaurantServicePortImpl.class,
                AuthServiceImpl.class // ¡Ahora con el nombre de clase correcto!
        })
)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RestaurantJpaAdapterTest {

    @Autowired
    private IRestaurantRepository restaurantRepository;

    private final IRestaurantEntityMapper restaurantEntityMapper = Mappers.getMapper(IRestaurantEntityMapper.class);

    private RestaurantJpaAdapter restaurantJpaAdapter;

    @BeforeEach
    void setUp() {
        restaurantJpaAdapter = new RestaurantJpaAdapter(restaurantRepository, restaurantEntityMapper);
        restaurantRepository.deleteAll();
    }

    @Test
    @DisplayName("Should successfully save a new restaurant entity to the database")
    void save_Success() {
        Restaurant newRestaurant = new Restaurant(
                null,
                "Restaurante de Prueba",
                "123456789",
                "Dirección 123",
                "3001234567",
                "http://logo-test.png",
                1L,
                ""
        );

        Restaurant savedRestaurant = restaurantJpaAdapter.save(newRestaurant);

        assertNotNull(savedRestaurant);
        assertNotNull(savedRestaurant.getId());
        assertEquals(newRestaurant.getName(), savedRestaurant.getName());
        assertEquals(newRestaurant.getNit(), savedRestaurant.getNit());
        assertEquals(newRestaurant.getAddress(), savedRestaurant.getAddress());
        assertEquals(newRestaurant.getPhoneNumber(), savedRestaurant.getPhoneNumber());
        assertEquals(newRestaurant.getUrlLogo(), savedRestaurant.getUrlLogo());
        assertEquals(newRestaurant.getOwnerId(), savedRestaurant.getOwnerId());

        assertTrue(restaurantRepository.findById(savedRestaurant.getId()).isPresent());
        RestaurantEntity foundEntity = restaurantRepository.findById(savedRestaurant.getId()).get();
        assertEquals(newRestaurant.getName(), foundEntity.getName());
    }

}