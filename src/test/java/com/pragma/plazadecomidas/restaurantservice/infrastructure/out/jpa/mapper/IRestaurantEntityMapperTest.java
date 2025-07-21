package com.pragma.plazadecomidas.restaurantservice.infrastructure.out.jpa.mapper;

import com.pragma.plazadecomidas.restaurantservice.domain.model.Restaurant;
import com.pragma.plazadecomidas.restaurantservice.infrastructure.out.jpa.entity.RestaurantEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest // Necesario para que Spring inyecte el mapper generado por MapStruct
class IRestaurantEntityMapperTest {

    @Autowired
    private IRestaurantEntityMapper restaurantEntityMapper;

    @BeforeEach
    void setUp() {
        // No se necesita inicialización especial aquí; Spring se encarga de la inyección.
    }

    @Test
    @DisplayName("toRestaurantEntity: Should map Domain Restaurant to JPA Entity correctly")
    void toRestaurantEntity_ValidDomainRestaurant_ReturnsEntity() {
        // GIVEN
        Restaurant domainRestaurant = Restaurant.builder()
                .id(1L) // Los mappers pueden copiar IDs si existen
                .name("Restaurante Mapper")
                .nit("9876543210")
                .address("Avenida Siempre Test 123")
                .phoneNumber("3123456789")
                .urlLogo("http://logo.com/mapper.png")
                .ownerId(10L)
                .ownerName("Owner Del Mapper") // Este campo no se mapea a la entidad RestaurantEntity
                .build();

        // WHEN
        RestaurantEntity entity = restaurantEntityMapper.toRestaurantEntity(domainRestaurant);

        // THEN
        assertNotNull(entity);
        assertEquals(domainRestaurant.getId(), entity.getId());
        assertEquals(domainRestaurant.getName(), entity.getName());
        assertEquals(domainRestaurant.getNit(), entity.getNit());
        assertEquals(domainRestaurant.getAddress(), entity.getAddress());
        assertEquals(domainRestaurant.getPhoneNumber(), entity.getPhoneNumber());
        assertEquals(domainRestaurant.getUrlLogo(), entity.getUrlLogo());
        assertEquals(domainRestaurant.getOwnerId(), entity.getOwnerId());
        // ownerName no debería ser mapeado a la entidad si no existe el campo en RestaurantEntity
        // Si existe, y no se mapea explícitamente en el mapper, será null o su valor por defecto.
    }

    @Test
    @DisplayName("toRestaurant: Should map JPA Entity to Domain Restaurant correctly")
    void toRestaurant_ValidEntity_ReturnsDomainRestaurant() {
        // GIVEN
        RestaurantEntity entity = new RestaurantEntity();
        entity.setId(2L);
        entity.setName("Entidad Mapeada");
        entity.setNit("1098765432");
        entity.setAddress("Calle Entidad 456");
        entity.setPhoneNumber("3019876543");
        entity.setUrlLogo("http://logo.com/entity.png");
        entity.setOwnerId(20L);

        // WHEN
        Restaurant domainRestaurant = restaurantEntityMapper.toRestaurant(entity);

        // THEN
        assertNotNull(domainRestaurant);
        assertEquals(entity.getId(), domainRestaurant.getId());
        assertEquals(entity.getName(), domainRestaurant.getName());
        assertEquals(entity.getNit(), domainRestaurant.getNit());
        assertEquals(entity.getAddress(), domainRestaurant.getAddress());
        assertEquals(entity.getPhoneNumber(), domainRestaurant.getPhoneNumber());
        assertEquals(entity.getUrlLogo(), domainRestaurant.getUrlLogo());
        assertEquals(entity.getOwnerId(), domainRestaurant.getOwnerId());
        // ownerName no debería ser mapeado desde la entidad a menos que tengas lógica para ello
        assertNull(domainRestaurant.getOwnerName()); // O el valor por defecto si lo manejas
    }

    @Test
    @DisplayName("toRestaurantEntity: Should handle null Domain Restaurant gracefully")
    void toRestaurantEntity_NullDomainRestaurant_ReturnsNull() {
        // GIVEN
        Restaurant domainRestaurant = null;

        // WHEN
        RestaurantEntity entity = restaurantEntityMapper.toRestaurantEntity(domainRestaurant);

        // THEN
        assertNull(entity);
    }

    @Test
    @DisplayName("toRestaurant: Should handle null JPA Entity gracefully")
    void toRestaurant_NullEntity_ReturnsNull() {
        // GIVEN
        RestaurantEntity entity = null;

        // WHEN
        Restaurant domainRestaurant = restaurantEntityMapper.toRestaurant(entity);

        // THEN
        assertNull(domainRestaurant);
    }
}
