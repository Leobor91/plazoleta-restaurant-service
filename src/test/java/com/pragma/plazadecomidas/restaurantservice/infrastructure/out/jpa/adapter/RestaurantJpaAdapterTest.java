package com.pragma.plazadecomidas.restaurantservice.infrastructure.out.jpa.adapter;


import com.pragma.plazadecomidas.restaurantservice.domain.model.Restaurant;
import com.pragma.plazadecomidas.restaurantservice.infrastructure.out.jpa.entity.RestaurantEntity;
import com.pragma.plazadecomidas.restaurantservice.infrastructure.out.jpa.mapper.IRestaurantEntityMapper;
import com.pragma.plazadecomidas.restaurantservice.infrastructure.out.jpa.repository.IRestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantJpaAdapterTest {

    @Mock
    private IRestaurantRepository restaurantRepository;

    @Mock
    private IRestaurantEntityMapper restaurantEntityMapper;

    @InjectMocks
    private RestaurantJpaAdapter restaurantJpaAdapter;

    private Restaurant restaurantDomain;
    private RestaurantEntity restaurantEntity;

    @BeforeEach
    void setUp() {
        // Objeto de dominio Restaurant
        restaurantDomain = Restaurant.builder()
                .id(1L)
                .name("Mi Restaurante")
                .nit("123456789")
                .address("Calle Falsa 123")
                .phoneNumber("3001234567")
                .urlLogo("http://logo.com/mi_restaurante.png")
                .ownerId(10L)
                .build();

        // Objeto de entidad JPA
        restaurantEntity = new RestaurantEntity();
        restaurantEntity.setId(1L);
        restaurantEntity.setName("Mi Restaurante");
        restaurantEntity.setNit("123456789");
        restaurantEntity.setAddress("Calle Falsa 123");
        restaurantEntity.setPhoneNumber("3001234567");
        restaurantEntity.setUrlLogo("http://logo.com/mi_restaurante.png");
        restaurantEntity.setOwnerId(10L);
    }

    @Test
    @DisplayName("save: Should save a restaurant and return the domain model")
    void save_ValidRestaurant_ReturnsSavedRestaurant() {
        // GIVEN
        // Cuando el mapper convierta de dominio a entidad, devuelve nuestra entidad mock
        when(restaurantEntityMapper.toRestaurantEntity(restaurantDomain)).thenReturn(restaurantEntity);
        // Cuando el repositorio guarde la entidad, devuelve la entidad mock guardada
        when(restaurantRepository.save(restaurantEntity)).thenReturn(restaurantEntity);
        // Cuando el mapper convierta de entidad a dominio, devuelve nuestro dominio mock
        when(restaurantEntityMapper.toRestaurant(restaurantEntity)).thenReturn(restaurantDomain);

        // WHEN
        Restaurant savedRestaurant = restaurantJpaAdapter.save(restaurantDomain);

        // THEN
        assertNotNull(savedRestaurant);
        assertEquals(restaurantDomain.getName(), savedRestaurant.getName());
        assertEquals(restaurantDomain.getNit(), savedRestaurant.getNit());
        assertEquals(restaurantDomain.getId(), savedRestaurant.getId()); // Verifica el ID si es asignado en el save
        verify(restaurantEntityMapper).toRestaurantEntity(restaurantDomain); // Verifica que el mapeo de entrada fue llamado
        verify(restaurantRepository).save(restaurantEntity); // Verifica que el save del repositorio fue llamado
        verify(restaurantEntityMapper).toRestaurant(restaurantEntity); // Verifica que el mapeo de salida fue llamado
    }

    @Test
    @DisplayName("findByName: Should return Optional with Restaurant if found")
    void findByName_RestaurantFound_ReturnsOptionalOfRestaurant() {
        // GIVEN
        String name = "Mi Restaurante";
        when(restaurantRepository.findByName(name)).thenReturn(Optional.of(restaurantEntity));
        when(restaurantEntityMapper.toRestaurant(restaurantEntity)).thenReturn(restaurantDomain);

        // WHEN
        Optional<Restaurant> foundRestaurant = restaurantJpaAdapter.findByName(name);

        // THEN
        assertTrue(foundRestaurant.isPresent());
        assertEquals(restaurantDomain.getName(), foundRestaurant.get().getName());
        verify(restaurantRepository).findByName(name);
        verify(restaurantEntityMapper).toRestaurant(restaurantEntity);
    }

    @Test
    @DisplayName("findByName: Should return Optional.empty if not found")
    void findByName_RestaurantNotFound_ReturnsEmptyOptional() {
        // GIVEN
        String name = "Restaurante No Existente";
        when(restaurantRepository.findByName(name)).thenReturn(Optional.empty());

        // WHEN
        Optional<Restaurant> foundRestaurant = restaurantJpaAdapter.findByName(name);

        // THEN
        assertFalse(foundRestaurant.isPresent());
        verify(restaurantRepository).findByName(name);
        verify(restaurantEntityMapper, never()).toRestaurant(any(RestaurantEntity.class)); // Mapper no debe ser llamado
    }

    @Test
    @DisplayName("findByNit: Should return Optional with Restaurant if found")
    void findByNit_RestaurantFound_ReturnsOptionalOfRestaurant() {
        // GIVEN
        String nit = "123456789";
        when(restaurantRepository.findByNit(nit)).thenReturn(Optional.of(restaurantEntity));
        when(restaurantEntityMapper.toRestaurant(restaurantEntity)).thenReturn(restaurantDomain);

        // WHEN
        Optional<Restaurant> foundRestaurant = restaurantJpaAdapter.findByNit(nit);

        // THEN
        assertTrue(foundRestaurant.isPresent());
        assertEquals(restaurantDomain.getNit(), foundRestaurant.get().getNit());
        verify(restaurantRepository).findByNit(nit);
        verify(restaurantEntityMapper).toRestaurant(restaurantEntity);
    }

    @Test
    @DisplayName("findByNit: Should return Optional.empty if not found")
    void findByNit_RestaurantNotFound_ReturnsEmptyOptional() {
        // GIVEN
        String nit = "999999999";
        when(restaurantRepository.findByNit(nit)).thenReturn(Optional.empty());

        // WHEN
        Optional<Restaurant> foundRestaurant = restaurantJpaAdapter.findByNit(nit);

        // THEN
        assertFalse(foundRestaurant.isPresent());
        verify(restaurantRepository).findByNit(nit);
        verify(restaurantEntityMapper, never()).toRestaurant(any(RestaurantEntity.class)); // Mapper no debe ser llamado
    }
}