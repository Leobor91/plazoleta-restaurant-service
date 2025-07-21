package com.pragma.plazadecomidas.restaurantservice.infrastructure.out.jpa.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RestaurantEntityTest {


    // Helper method to create a base entity for comparisons
    private RestaurantEntity createBaseEntity() {
        return RestaurantEntity.builder()
                .id(1L)
                .name("Base Restaurant")
                .nit("NIT123")
                .address("Base Address")
                .phoneNumber("1112223333")
                .urlLogo("http://logo.com/base.png")
                .ownerId(100L)
                .build();
    }

    @Test
    @DisplayName("Should create an instance using no-args constructor and setters")
    void noArgsConstructorAndSettersTest() {
        // GIVEN
        RestaurantEntity entity = new RestaurantEntity();

        // WHEN
        entity.setId(1L);
        entity.setName("Restaurante Prueba");
        entity.setNit("123456789");
        entity.setAddress("Calle Falsa 123");
        entity.setPhoneNumber("3001234567");
        entity.setUrlLogo("http://logo.com/prueba.png");
        entity.setOwnerId(10L);

        // THEN
        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals("Restaurante Prueba", entity.getName());
        assertEquals("123456789", entity.getNit());
        assertEquals("Calle Falsa 123", entity.getAddress());
        assertEquals("3001234567", entity.getPhoneNumber());
        assertEquals("http://logo.com/prueba.png", entity.getUrlLogo());
        assertEquals(10L, entity.getOwnerId());
    }

    @Test
    @DisplayName("Should create an instance using all-args constructor")
    void allArgsConstructorTest() {
        // GIVEN
        Long id = 2L;
        String name = "Restaurante Completo";
        String nit = "987654321";
        String address = "Avenida Siempre Viva 789";
        String phoneNumber = "3109876543";
        String urlLogo = "http://logo.com/completo.png";
        Long ownerId = 20L;

        // WHEN
        RestaurantEntity entity = new RestaurantEntity(id, nit, name, address, phoneNumber, urlLogo, ownerId);

        // THEN
        assertNotNull(entity);
        assertEquals(id, entity.getId());
        assertEquals(name, entity.getName());
        assertEquals(nit, entity.getNit());
        assertEquals(address, entity.getAddress());
        assertEquals(phoneNumber, entity.getPhoneNumber());
        assertEquals(urlLogo, entity.getUrlLogo());
        assertEquals(ownerId, entity.getOwnerId());
    }

    @Test
    @DisplayName("Should create an instance using the Builder pattern")
    void builderTest() {
        // GIVEN
        // WHEN
        RestaurantEntity entity = RestaurantEntity.builder()
                .id(3L)
                .name("Restaurante Builder")
                .nit("112233445")
                .address("Calle Builder 456")
                .phoneNumber("3201112233")
                .urlLogo("http://logo.com/builder.png")
                .ownerId(30L)
                .build();

        // THEN
        assertNotNull(entity);
        assertEquals(3L, entity.getId());
        assertEquals("Restaurante Builder", entity.getName());
        assertEquals("112233445", entity.getNit());
        assertEquals("Calle Builder 456", entity.getAddress());
        assertEquals("3201112233", entity.getPhoneNumber());
        assertEquals("http://logo.com/builder.png", entity.getUrlLogo());
        assertEquals(30L, entity.getOwnerId());
    }

    @Test
    @DisplayName("Builder should handle null values for optional fields")
    void builderWithNullFieldsTest() {
        // GIVEN
        // WHEN
        RestaurantEntity entity = RestaurantEntity.builder()
                .id(4L)
                .name("Restaurante Con Nulos")
                .nit("NIT_TEST_CON_NULOS") // Valor no nulo, ya que en la DB es NotNull
                .address("Direccion Nula")
                .phoneNumber(null) // Este sí puede ser null
                .urlLogo(null) // Este sí puede ser null
                .ownerId(40L)
                .build();

        // THEN
        assertNotNull(entity);
        assertEquals(4L, entity.getId());
        assertEquals("Restaurante Con Nulos", entity.getName());
        assertEquals("NIT_TEST_CON_NULOS", entity.getNit());
        assertEquals("Direccion Nula", entity.getAddress());
        assertNull(entity.getPhoneNumber());
        assertNull(entity.getUrlLogo());
        assertEquals(40L, entity.getOwnerId());
    }

    @Test
    @DisplayName("Equals and HashCode should work correctly for same objects")
    void equalsAndHashCode_SameObjects_ReturnTrue() {
        // GIVEN
        RestaurantEntity entity1 = createBaseEntity();
        RestaurantEntity entity2 = createBaseEntity(); // Crea otro objeto idéntico

        // WHEN & THEN
        assertEquals(entity1, entity2);
        assertEquals(entity1.hashCode(), entity2.hashCode());
        assertEquals(entity1, entity1); // Reflexividad
    }

    @Test
    @DisplayName("Equals should return false for null object")
    void equals_NullObject_ReturnFalse() {
        RestaurantEntity entity1 = createBaseEntity();
        assertNotEquals(null, entity1);
    }

    @Test
    @DisplayName("Equals should return false for object of different class")
    void equals_DifferentClass_ReturnFalse() {
        RestaurantEntity entity1 = createBaseEntity();
        assertNotEquals(new Object(), entity1);
    }

    @Test
    @DisplayName("Equals should return false for objects with different IDs")
    void equals_DifferentId_ReturnFalse() {
        RestaurantEntity entity1 = createBaseEntity();
        RestaurantEntity entity2 = createBaseEntity().toBuilder().id(99L).build(); // Different ID
        assertNotEquals(entity1, entity2);
        assertNotEquals(entity1.hashCode(), entity2.hashCode());
    }

    @Test
    @DisplayName("Equals should return false for objects with different names")
    void equals_DifferentName_ReturnFalse() {
        RestaurantEntity entity1 = createBaseEntity();
        RestaurantEntity entity2 = createBaseEntity().toBuilder().name("Different Name").build(); // Different name
        assertNotEquals(entity1, entity2);
    }

    @Test
    @DisplayName("Equals should return false for objects with different NITs")
    void equals_DifferentNit_ReturnFalse() {
        RestaurantEntity entity1 = createBaseEntity();
        RestaurantEntity entity2 = createBaseEntity().toBuilder().nit("Different Nit").build(); // Different NIT
        assertNotEquals(entity1, entity2);
    }

    @Test
    @DisplayName("Equals should return false for objects with different addresses")
    void equals_DifferentAddress_ReturnFalse() {
        RestaurantEntity entity1 = createBaseEntity();
        RestaurantEntity entity2 = createBaseEntity().toBuilder().address("Different Address").build(); // Different address
        assertNotEquals(entity1, entity2);
    }

    @Test
    @DisplayName("Equals should return false for objects with different phone numbers")
    void equals_DifferentPhoneNumber_ReturnFalse() {
        RestaurantEntity entity1 = createBaseEntity();
        RestaurantEntity entity2 = createBaseEntity().toBuilder().phoneNumber("Different Phone").build(); // Different phone
        assertNotEquals(entity1, entity2);
    }

    @Test
    @DisplayName("Equals should return false for objects with different URL logos")
    void equals_DifferentUrlLogo_ReturnFalse() {
        RestaurantEntity entity1 = createBaseEntity();
        RestaurantEntity entity2 = createBaseEntity().toBuilder().urlLogo("http://different.png").build(); // Different URL logo
        assertNotEquals(entity1, entity2);
    }

    @Test
    @DisplayName("Equals should return false for objects with different owner IDs")
    void equals_DifferentOwnerId_ReturnFalse() {
        RestaurantEntity entity1 = createBaseEntity();
        RestaurantEntity entity2 = createBaseEntity().toBuilder().ownerId(999L).build(); // Different owner ID
        assertNotEquals(entity1, entity2);
    }

    @Test
    @DisplayName("Equals should return false if one object's field is null and other's is not (for nullable fields)")
    void equals_NullVsNonNullFields_ReturnFalse() {
        RestaurantEntity entity1 = createBaseEntity().toBuilder().phoneNumber("111").build();
        RestaurantEntity entity2 = createBaseEntity().toBuilder().phoneNumber(null).build();
        assertNotEquals(entity1, entity2);

        entity1 = createBaseEntity().toBuilder().urlLogo("logo.png").build();
        entity2 = createBaseEntity().toBuilder().urlLogo(null).build();
        assertNotEquals(entity1, entity2);
    }

    @Test
    @DisplayName("Equals should return true if both objects have null for nullable fields and other fields are equal")
    void equals_BothNullFields_ReturnTrue() {
        RestaurantEntity entity1 = createBaseEntity().toBuilder().phoneNumber(null).urlLogo(null).build();
        RestaurantEntity entity2 = createBaseEntity().toBuilder().phoneNumber(null).urlLogo(null).build();
        assertEquals(entity1, entity2);
        assertEquals(entity1.hashCode(), entity2.hashCode());
    }

    @Test
    @DisplayName("toBuilder() should create a builder with current object's properties")
    void toBuilderTest() {
        // GIVEN
        RestaurantEntity originalEntity = createBaseEntity();

        // WHEN
        RestaurantEntity.RestaurantEntityBuilder builder = originalEntity.toBuilder();
        RestaurantEntity builtEntity = builder.build();

        // THEN
        assertNotNull(builder);
        assertEquals(originalEntity, builtEntity); // El objeto construido debe ser igual al original
        assertEquals(originalEntity.hashCode(), builtEntity.hashCode());

        // Modificamos el builder y construimos para asegurar que es una copia y no la misma instancia
        RestaurantEntity modifiedBuiltEntity = builder.name("Modified Name").build();
        assertNotEquals(originalEntity, modifiedBuiltEntity);
        assertEquals("Modified Name", modifiedBuiltEntity.getName());
    }

    @Test
    @DisplayName("ToString should return a non-null and non-empty string")
    void toStringTest() {
        // GIVEN
        RestaurantEntity entity = createBaseEntity();

        // WHEN
        String toStringResult = entity.toString();

        // THEN
        assertNotNull(toStringResult);
        assertFalse(toStringResult.isEmpty());
        assertTrue(toStringResult.contains("id=1"));
        assertTrue(toStringResult.contains("name=Base Restaurant"));
        assertTrue(toStringResult.contains("nit=NIT123"));
        // Se pueden añadir más asserts para campos específicos si se desea mayor granularidad
    }

    @Test
    @DisplayName("canEqual should return true for same class and false for different class")
    void canEqualTest() {
        RestaurantEntity entity1 = createBaseEntity();
        RestaurantEntity entity2 = createBaseEntity();
        Object object = new Object();

        assertTrue(entity1.canEqual(entity2)); // Should be true for same type
        assertFalse(entity1.canEqual(object)); // Should be false for different type
    }
}
