package com.pragma.plazadecomidas.restaurantservice.application.dto.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RestaurantRequestDtoTest {

    // Helper method to create a base DTO for comparisons
    private RestaurantRequestDto createBaseDto() {
        return RestaurantRequestDto.builder()
                .name("Test Restaurant")
                .nit("987654321")
                .address("Test Address 123")
                .phoneNumber("3001234567")
                .urlLogo("http://test.com/logo.png")
                .ownerId(1L)
                .build();
    }

    @Test
    @DisplayName("Should create RestaurantRequestDto with all arguments and verify getters")
    void allArgsConstructorAndGettersTest() {
        // Given
        String name = "Restaurant Name";
        String nit = "123456789";
        String address = "123 Main St";
        String phoneNumber = "555-1234";
        String urlLogo = "http://logo.com/logo.png";
        Long ownerId = 1L;

        // When
        RestaurantRequestDto dto = new RestaurantRequestDto(name, nit, address, phoneNumber, urlLogo, ownerId);

        // Then
        assertNotNull(dto);
        assertEquals(name, dto.getName());
        assertEquals(nit, dto.getNit());
        assertEquals(address, dto.getAddress());
        assertEquals(phoneNumber, dto.getPhoneNumber());
        assertEquals(urlLogo, dto.getUrlLogo());
        assertEquals(ownerId, dto.getOwnerId());
    }

    @Test
    @DisplayName("Should create RestaurantRequestDto with no arguments and verify default nulls")
    void noArgsConstructorTest() {
        // When
        RestaurantRequestDto dto = new RestaurantRequestDto();

        // Then
        assertNotNull(dto);
        assertNull(dto.getName());
        assertNull(dto.getNit());
        assertNull(dto.getAddress());
        assertNull(dto.getPhoneNumber());
        assertNull(dto.getUrlLogo());
        assertNull(dto.getOwnerId());
    }

    @Test
    @DisplayName("Should set values using setters and verify with getters")
    void settersTest() {
        // Given
        RestaurantRequestDto dto = new RestaurantRequestDto(); // Usar no-args constructor para empezar con nulls

        // When
        String newName = "Updated Name";
        String newNit = "987654321";
        String newAddress = "New Address";
        String newPhoneNumber = "555-9876";
        String newUrlLogo = "http://newlogo.com/new.png";
        Long newOwnerId = 2L;

        dto.setName(newName);
        dto.setNit(newNit);
        dto.setAddress(newAddress);
        dto.setPhoneNumber(newPhoneNumber);
        dto.setUrlLogo(newUrlLogo);
        dto.setOwnerId(newOwnerId);

        // Then
        assertEquals(newName, dto.getName());
        assertEquals(newNit, dto.getNit());
        assertEquals(newAddress, dto.getAddress());
        assertEquals(newPhoneNumber, dto.getPhoneNumber());
        assertEquals(newUrlLogo, dto.getUrlLogo());
        assertEquals(newOwnerId, dto.getOwnerId());
    }

    @Test
    @DisplayName("Should create an instance using the Builder pattern")
    void builderTest() {
        // GIVEN
        // WHEN
        RestaurantRequestDto dto = createBaseDto();

        // THEN
        assertNotNull(dto);
        assertEquals("Test Restaurant", dto.getName());
        assertEquals("987654321", dto.getNit());
        assertEquals("Test Address 123", dto.getAddress());
        assertEquals("3001234567", dto.getPhoneNumber());
        assertEquals("http://test.com/logo.png", dto.getUrlLogo());
        assertEquals(1L, dto.getOwnerId());
    }

    @Test
    @DisplayName("Builder should handle null values for applicable fields")
    void builderWithNullFieldsTest() {
        // GIVEN
        // WHEN
        // Si tienes campos que pueden ser nulos según tu validación, prueba ese escenario
        // NOTA: Para RestaurantRequestDto, la mayoría son @NotBlank/@NotNull, así que pocos campos pueden ser null
        // Si nit, name, address, etc. son @NotBlank, pasar null causaría un error de validación en tiempo de ejecución de la app.
        // Pero Lombok permite construirlos con null. Para cobertura, podemos forzar un null si la propiedad no es primitiva.
        RestaurantRequestDto dto = RestaurantRequestDto.builder()
                .name("Null Fields Test")
                .nit("111222333")
                .address("Some Address")
                .phoneNumber(null) // Si phoneNumber fuera @Nullable o no tuviera @NotBlank
                .urlLogo(null) // Si urlLogo fuera @Nullable o no tuviera @NotBlank
                .ownerId(null) // Si ownerId fuera @Nullable o no tuviera @NotNull
                .build();

        // THEN
        assertNotNull(dto);
        assertEquals("Null Fields Test", dto.getName());
        assertNotNull(dto.getNit()); // NIT debe ser no nulo por @NotBlank
        assertNotNull(dto.getAddress()); // Address debe ser no nulo por @NotBlank
        assertNull(dto.getPhoneNumber()); // Si se permite null
        assertNull(dto.getUrlLogo()); // Si se permite null
        assertNull(dto.getOwnerId()); // Si se permite null
    }


    @Test
    @DisplayName("Equals and HashCode should work correctly for same objects")
    @SuppressWarnings("SonarLint")
    void equalsAndHashCode_SameObjects_ReturnTrue() {
        // GIVEN
        RestaurantRequestDto dto1 = createBaseDto();
        RestaurantRequestDto dto2 = createBaseDto();

        // WHEN & THEN
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertEquals(dto1, dto1); // Reflexividad
        assertNotEquals(null, dto1); // No nulo
        assertNotEquals(new Object(), dto1); // Diferente tipo
    }

    @Test
    @DisplayName("Equals should return false for objects with different names")
    void equals_DifferentName_ReturnFalse() {
        RestaurantRequestDto dto1 = createBaseDto();
        RestaurantRequestDto dto2 = createBaseDto().toBuilder().name("Different Name").build();
        assertNotEquals(dto1, dto2);
        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    @DisplayName("Equals should return false for objects with different NITs")
    void equals_DifferentNit_ReturnFalse() {
        RestaurantRequestDto dto1 = createBaseDto();
        RestaurantRequestDto dto2 = createBaseDto().toBuilder().nit("Different Nit").build();
        assertNotEquals(dto1, dto2);
    }

    @Test
    @DisplayName("Equals should return false for objects with different addresses")
    void equals_DifferentAddress_ReturnFalse() {
        RestaurantRequestDto dto1 = createBaseDto();
        RestaurantRequestDto dto2 = createBaseDto().toBuilder().address("Different Address").build();
        assertNotEquals(dto1, dto2);
    }

    @Test
    @DisplayName("Equals should return false for objects with different phone numbers")
    void equals_DifferentPhoneNumber_ReturnFalse() {
        RestaurantRequestDto dto1 = createBaseDto();
        RestaurantRequestDto dto2 = createBaseDto().toBuilder().phoneNumber("Different Phone").build();
        assertNotEquals(dto1, dto2);
    }

    @Test
    @DisplayName("Equals should return false for objects with different URL logos")
    void equals_DifferentUrlLogo_ReturnFalse() {
        RestaurantRequestDto dto1 = createBaseDto();
        RestaurantRequestDto dto2 = createBaseDto().toBuilder().urlLogo("http://different.png").build();
        assertNotEquals(dto1, dto2);
    }

    @Test
    @DisplayName("Equals should return false for objects with different owner IDs")
    void equals_DifferentOwnerId_ReturnFalse() {
        RestaurantRequestDto dto1 = createBaseDto();
        RestaurantRequestDto dto2 = createBaseDto().toBuilder().ownerId(999L).build();
        assertNotEquals(dto1, dto2);
    }

    @Test
    @DisplayName("Equals should return false if one object's field is null and other's is not (for nullable fields)")
    void equals_NullVsNonNullFields_ReturnFalse() {
        // Asumiendo que phoneNumber y urlLogo pueden ser null en el DTO (aunque con @NotBlank/NotNull esto no aplicaría si es por validación)
        // Si tu DTO REALMENTE NO PERMITE NULOS EN ESTOS CAMPOS, este test podría ser redundante o no aplicable.
        // Lo mantengo para cobertura de ramas de equals en caso de que alguna lógica de Lombok lo genere así.
        RestaurantRequestDto dto1 = createBaseDto().toBuilder().phoneNumber("111").build();
        RestaurantRequestDto dto2 = createBaseDto().toBuilder().phoneNumber(null).build();
        assertNotEquals(dto1, dto2);

        dto1 = createBaseDto().toBuilder().urlLogo("logo.png").build();
        dto2 = createBaseDto().toBuilder().urlLogo(null).build();
        assertNotEquals(dto1, dto2);

        dto1 = createBaseDto().toBuilder().ownerId(123L).build();
        RestaurantRequestDto dtoWithNullOwnerId = createBaseDto().toBuilder().ownerId(null).build();
        assertNotEquals(dto1, dtoWithNullOwnerId);
    }

    @Test
    @DisplayName("Equals should return true if both objects have null for same fields (for nullable fields)")
    void equals_BothNullFields_ReturnTrue() {
        // Similar al comentario anterior, si los campos no pueden ser null, este test no es tan crítico.
        // Pero ayuda a la cobertura de equals si Lombok genera esa rama.
        RestaurantRequestDto dto1 = createBaseDto().toBuilder().phoneNumber(null).urlLogo(null).ownerId(null).build();
        RestaurantRequestDto dto2 = createBaseDto().toBuilder().phoneNumber(null).urlLogo(null).ownerId(null).build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }


    @Test
    @DisplayName("toBuilder() should create a builder with current object's properties")
    void toBuilderTest() {
        // GIVEN
        RestaurantRequestDto originalDto = createBaseDto();

        // WHEN
        RestaurantRequestDto.RestaurantRequestDtoBuilder builder = originalDto.toBuilder();
        RestaurantRequestDto builtDto = builder.build();

        // THEN
        assertNotNull(builder);
        assertEquals(originalDto, builtDto); // El objeto construido debe ser igual al original
        assertEquals(originalDto.hashCode(), builtDto.hashCode());

        // Modificamos el builder y construimos para asegurar que es una copia y no la misma instancia
        RestaurantRequestDto modifiedBuiltDto = builder.name("Modified Name").build();
        assertNotEquals(originalDto, modifiedBuiltDto);
        assertEquals("Modified Name", modifiedBuiltDto.getName());
    }

    @Test
    @DisplayName("ToString should return a non-null and non-empty string and contain relevant data")
    void toStringTest() {
        // GIVEN
        RestaurantRequestDto dto = createBaseDto();

        // WHEN
        String toStringResult = dto.toString();

        // THEN
        assertNotNull(toStringResult);
        assertFalse(toStringResult.isEmpty());
        assertTrue(toStringResult.contains("name=Test Restaurant"));
        assertTrue(toStringResult.contains("nit=987654321"));
        // Puedes añadir más asserts para verificar la presencia de otros campos si lo deseas.
    }
}
