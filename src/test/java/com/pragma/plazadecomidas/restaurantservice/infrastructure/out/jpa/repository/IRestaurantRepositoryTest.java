package com.pragma.plazadecomidas.restaurantservice.infrastructure.out.jpa.repository;

import com.pragma.plazadecomidas.restaurantservice.infrastructure.out.jpa.entity.RestaurantEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class IRestaurantRepositoryTest {

    @Autowired
    private IRestaurantRepository restaurantRepository;

    @Autowired
    private TestEntityManager entityManager;

    private RestaurantEntity restaurantEntity1;

    @BeforeEach
    void setUp() {

        entityManager.clear();

        restaurantEntity1 = new RestaurantEntity(
                null,
                "Restaurante Uno",
                "111111111",
                "Direccion 1",
                "3001111111",
                "http://logo1.png",
                1L
        );

    }

    @Test
    @DisplayName("Should save a restaurant entity successfully")
    void save_ShouldPersistRestaurantEntity() {
        // When
        RestaurantEntity savedEntity = restaurantRepository.save(restaurantEntity1);

        // Then
        assertNotNull(savedEntity.getId());
        assertEquals(restaurantEntity1.getName(), savedEntity.getName());
        assertEquals(restaurantEntity1.getNit(), savedEntity.getNit());
        assertEquals(restaurantEntity1.getOwnerId(), savedEntity.getOwnerId());


        Optional<RestaurantEntity> foundEntity = restaurantRepository.findById(savedEntity.getId());
        assertTrue(foundEntity.isPresent());
        assertEquals(savedEntity, foundEntity.get());
    }

    @Test
    @DisplayName("Should find a restaurant entity by ID")
    void findById_ShouldReturnRestaurantEntity() {
        // Given
        RestaurantEntity persistedEntity = entityManager.persistAndFlush(restaurantEntity1);

        // When
        Optional<RestaurantEntity> foundEntity = restaurantRepository.findById(persistedEntity.getId());

        // Then
        assertTrue(foundEntity.isPresent());
        assertEquals(persistedEntity, foundEntity.get());
    }

    @Test
    @DisplayName("Should return empty optional if restaurant entity not found by ID")
    void findById_ShouldReturnEmptyOptional_WhenNotFound() {
        // When
        Optional<RestaurantEntity> foundEntity = restaurantRepository.findById(99L);

        // Then
        assertFalse(foundEntity.isPresent());
    }

    @Test
    @DisplayName("Should update a restaurant entity")
    void update_ShouldModifyRestaurantEntity() {
        // Given
        RestaurantEntity persistedEntity = entityManager.persistAndFlush(restaurantEntity1);
        String newAddress = "Nueva Direccion Actualizada";
        persistedEntity.setAddress(newAddress);

        // When
        RestaurantEntity updatedEntity = restaurantRepository.save(persistedEntity);

        // Then
        assertNotNull(updatedEntity);
        assertEquals(newAddress, updatedEntity.getAddress());

        Optional<RestaurantEntity> foundEntity = restaurantRepository.findById(updatedEntity.getId());
        assertTrue(foundEntity.isPresent());
        assertEquals(newAddress, foundEntity.get().getAddress());
    }

    @Test
    @DisplayName("Should delete a restaurant entity by ID")
    void deleteById_ShouldRemoveRestaurantEntity() {
        // Given
        RestaurantEntity persistedEntity = entityManager.persistAndFlush(restaurantEntity1);
        Long idToDelete = persistedEntity.getId();

        // When
        restaurantRepository.deleteById(idToDelete);
        entityManager.flush();

        // Then
        Optional<RestaurantEntity> foundEntity = restaurantRepository.findById(idToDelete);
        assertFalse(foundEntity.isPresent());
    }

    @Test
    @DisplayName("Should find a restaurant entity by name")
    void findByName_ShouldReturnRestaurantEntity() {
        // Given
        RestaurantEntity persistedEntity = entityManager.persistAndFlush(restaurantEntity1);

        // When
        Optional<RestaurantEntity> foundEntity = restaurantRepository.findByName(persistedEntity.getName());

        // Then
        assertTrue(foundEntity.isPresent());
        assertEquals(persistedEntity, foundEntity.get());
    }

    @Test
    @DisplayName("Should return empty optional if restaurant entity not found by name")
    void findByName_ShouldReturnEmptyOptional_WhenNotFound() {
        // When
        Optional<RestaurantEntity> foundEntity = restaurantRepository.findByName("NonExistentName");

        // Then
        assertFalse(foundEntity.isPresent());
    }

    @Test
    @DisplayName("Should find a restaurant entity by NIT")
    void findByNit_ShouldReturnRestaurantEntity() {
        // Given
        RestaurantEntity persistedEntity = entityManager.persistAndFlush(restaurantEntity1);

        // When
        Optional<RestaurantEntity> foundEntity = restaurantRepository.findByNit(persistedEntity.getNit());

        // Then
        assertTrue(foundEntity.isPresent());
        assertEquals(persistedEntity, foundEntity.get());
    }

    @Test
    @DisplayName("Should return empty optional if restaurant entity not found by NIT")
    void findByNit_ShouldReturnEmptyOptional_WhenNotFound() {
        // When
        Optional<RestaurantEntity> foundEntity = restaurantRepository.findByNit("NonExistentNit");

        // Then
        assertFalse(foundEntity.isPresent());
    }

    @Test
    @DisplayName("Should throw DataIntegrityViolationException when saving restaurant with duplicate name")
    void save_DuplicateName_ShouldThrowException() {
        // Given
        entityManager.persistAndFlush(restaurantEntity1);
        RestaurantEntity duplicateNameEntity = new RestaurantEntity(
                null,
                "Restaurante Uno",
                "333333333",
                "Direccion 3",
                "3003333333",
                "http://logo3.png",
                3L
        );

        // When & Then
        assertThrows(DataIntegrityViolationException.class, () -> {
            restaurantRepository.save(duplicateNameEntity);
            entityManager.flush();
        });
    }

    @Test
    @DisplayName("Should throw DataIntegrityViolationException when saving restaurant with duplicate NIT")
    void save_DuplicateNit_ShouldThrowException() {
        // Given
        entityManager.persistAndFlush(restaurantEntity1); // Persistir el primero
        RestaurantEntity duplicateNitEntity = new RestaurantEntity(
                null,
                "Restaurante Cuatro",
                "111111111",
                "Direccion 4",
                "3004444444",
                "http://logo4.png",
                4L
        );

        // When & Then
        assertThrows(DataIntegrityViolationException.class, () -> {
            restaurantRepository.save(duplicateNitEntity);
            entityManager.flush();
        });
    }
}
