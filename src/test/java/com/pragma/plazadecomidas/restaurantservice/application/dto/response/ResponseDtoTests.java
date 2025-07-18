package com.pragma.plazadecomidas.restaurantservice.application.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResponseDtoTests {

    // --- Tests for UserResponseDto ---

    private UserResponseDto createUserResponseDtoBase() {
        return UserResponseDto.builder()
                .id(1L)
                .name("Juan")
                .lastName("Perez")
                .identityDocument("1234567890")
                .phoneNumber("3001112233")
                .email("juan.perez@example.com")
                .birthDate(LocalDate.of(1990, 1, 1))
                .role("PROPIETARIO")
                .build();
    }

    @Test
    @DisplayName("UserResponseDto: Should create an instance using no-args constructor and setters")
    void userResponseDto_NoArgsConstructorAndSettersTest() {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(1L);
        dto.setName("Juan");
        dto.setLastName("Perez");
        dto.setIdentityDocument("1234567890");
        dto.setPhoneNumber("3001112233");
        dto.setEmail("juan.perez@example.com");
        dto.setBirthDate(LocalDate.of(1990, 1, 1));
        dto.setRole("PROPIETARIO");

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Juan", dto.getName());
        assertEquals("Perez", dto.getLastName());
        assertEquals("1234567890", dto.getIdentityDocument());
        assertEquals("3001112233", dto.getPhoneNumber());
        assertEquals("juan.perez@example.com", dto.getEmail());
        assertEquals(LocalDate.of(1990, 1, 1), dto.getBirthDate());
        assertEquals("PROPIETARIO", dto.getRole());
    }

    @Test
    @DisplayName("UserResponseDto: Should create an instance using all-args constructor")
    void userResponseDto_AllArgsConstructorTest() {
        UserResponseDto dto = new UserResponseDto(
                1L, "Juan", "Perez", "1234567890", "3001112233",
                "juan.perez@example.com", LocalDate.of(1990, 1, 1), "PROPIETARIO"
        );

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Juan", dto.getName());
        assertEquals("Perez", dto.getLastName());
        assertEquals("1234567890", dto.getIdentityDocument());
        assertEquals("3001112233", dto.getPhoneNumber());
        assertEquals("juan.perez@example.com", dto.getEmail());
        assertEquals(LocalDate.of(1990, 1, 1), dto.getBirthDate());
        assertEquals("PROPIETARIO", dto.getRole());
    }

    @Test
    @DisplayName("UserResponseDto: Should create an instance using the Builder pattern")
    void userResponseDto_BuilderTest() {
        UserResponseDto dto = createUserResponseDtoBase();

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Juan", dto.getName());
        assertEquals("Perez", dto.getLastName());
        assertEquals("1234567890", dto.getIdentityDocument());
        assertEquals("3001112233", dto.getPhoneNumber());
        assertEquals("juan.perez@example.com", dto.getEmail());
        assertEquals(LocalDate.of(1990, 1, 1), dto.getBirthDate());
        assertEquals("PROPIETARIO", dto.getRole());
    }

    @Test
    @DisplayName("UserResponseDto: Builder should handle null values")
    void userResponseDto_BuilderWithNullsTest() {
        UserResponseDto dto = UserResponseDto.builder()
                .id(2L)
                .name("Pedro")
                .email("pedro@example.com")
                .build(); // Otros campos serán null por defecto

        assertNotNull(dto);
        assertEquals(2L, dto.getId());
        assertEquals("Pedro", dto.getName());
        assertNull(dto.getLastName());
        assertNull(dto.getIdentityDocument());
        assertNull(dto.getPhoneNumber());
        assertEquals("pedro@example.com", dto.getEmail());
        assertNull(dto.getBirthDate());
        assertNull(dto.getRole());
    }

    @Test
    @DisplayName("UserResponseDto: Equals and HashCode should work correctly for same objects")
    void userResponseDto_EqualsAndHashCode_SameObjects_ReturnTrue() {
        UserResponseDto dto1 = createUserResponseDtoBase();
        UserResponseDto dto2 = createUserResponseDtoBase();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertEquals(dto1, dto1); // Reflexividad
        assertNotEquals(null, dto1); // No nulo
        assertNotEquals(new Object(), dto1); // Diferente tipo
    }

    @Test
    @DisplayName("UserResponseDto: Equals should return false for objects with different properties")
    void userResponseDto_Equals_DifferentObjects_ReturnFalse() {
        UserResponseDto baseDto = createUserResponseDtoBase();

        assertNotEquals(baseDto, baseDto.toBuilder().id(99L).build());
        assertNotEquals(baseDto, baseDto.toBuilder().name("Different").build());
        assertNotEquals(baseDto, baseDto.toBuilder().email("different@example.com").build());
        // Añadir más assertNotEquals para cada campo si es necesario para cobertura de ramas
        assertNotEquals(baseDto, baseDto.toBuilder().lastName("Other").build());
        assertNotEquals(baseDto, baseDto.toBuilder().identityDocument("0987654321").build());
        assertNotEquals(baseDto, baseDto.toBuilder().phoneNumber("333").build());
        assertNotEquals(baseDto, baseDto.toBuilder().birthDate(LocalDate.of(2000,1,1)).build());
        assertNotEquals(baseDto, baseDto.toBuilder().role("ADMIN").build());
    }

    @Test
    @DisplayName("UserResponseDto: Equals should return false if one object's field is null and other's is not")
    void userResponseDto_Equals_NullVsNonNullFields_ReturnFalse() {
        UserResponseDto dto1 = createUserResponseDtoBase();
        UserResponseDto dto2 = createUserResponseDtoBase().toBuilder().phoneNumber(null).build();
        assertNotEquals(dto1, dto2);
    }

    @Test
    @DisplayName("UserResponseDto: Equals should return true if both objects have null for same fields")
    void userResponseDto_Equals_BothNullFields_ReturnTrue() {
        UserResponseDto dto1 = createUserResponseDtoBase().toBuilder().phoneNumber(null).build();
        UserResponseDto dto2 = createUserResponseDtoBase().toBuilder().phoneNumber(null).build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    @DisplayName("UserResponseDto: ToString should return a non-null and non-empty string")
    void userResponseDto_ToStringTest() {
        UserResponseDto dto = createUserResponseDtoBase();
        String toStringResult = dto.toString();
        assertNotNull(toStringResult);
        assertFalse(toStringResult.isEmpty());
        assertTrue(toStringResult.contains("name=Juan"));
        assertTrue(toStringResult.contains("email=juan.perez@example.com"));
    }

    // --- Tests for RestaurantResponseDto ---

    private RestaurantResponseDto createRestaurantResponseDtoBase() {
        return RestaurantResponseDto.builder()
                .id(1L)
                .name("Mi Restaurante")
                .nit("900123456")
                .address("Calle 123 #4-56")
                .phoneNumber("+573001234567")
                .urlLogo("http://example.com/logo.png")
                .ownerName("Sofia Ramirez")
                .build();
    }

    @Test
    @DisplayName("RestaurantResponseDto: Should create an instance using no-args constructor and setters")
    void restaurantResponseDto_NoArgsConstructorAndSettersTest() {
        RestaurantResponseDto dto = new RestaurantResponseDto();
        dto.setId(1L);
        dto.setName("Mi Restaurante");
        dto.setNit("900123456");
        dto.setAddress("Calle 123 #4-56");
        dto.setPhoneNumber("+573001234567");
        dto.setUrlLogo("http://example.com/logo.png");
        dto.setOwnerName("Sofia Ramirez");

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Mi Restaurante", dto.getName());
        assertEquals("900123456", dto.getNit());
        assertEquals("Calle 123 #4-56", dto.getAddress());
        assertEquals("+573001234567", dto.getPhoneNumber());
        assertEquals("http://example.com/logo.png", dto.getUrlLogo());
        assertEquals("Sofia Ramirez", dto.getOwnerName());
    }

    @Test
    @DisplayName("RestaurantResponseDto: Should create an instance using all-args constructor")
    void restaurantResponseDto_AllArgsConstructorTest() {
        RestaurantResponseDto dto = new RestaurantResponseDto(
                1L, "Mi Restaurante", "900123456", "Calle 123 #4-56",
                "+573001234567", "http://example.com/logo.png", "Sofia Ramirez"
        );

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Mi Restaurante", dto.getName());
        assertEquals("900123456", dto.getNit());
        assertEquals("Calle 123 #4-56", dto.getAddress());
        assertEquals("+573001234567", dto.getPhoneNumber());
        assertEquals("http://example.com/logo.png", dto.getUrlLogo());
        assertEquals("Sofia Ramirez", dto.getOwnerName());
    }

    @Test
    @DisplayName("RestaurantResponseDto: Should create an instance using the Builder pattern")
    void restaurantResponseDto_BuilderTest() {
        RestaurantResponseDto dto = createRestaurantResponseDtoBase();

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Mi Restaurante", dto.getName());
        assertEquals("900123456", dto.getNit());
        assertEquals("Calle 123 #4-56", dto.getAddress());
        assertEquals("+573001234567", dto.getPhoneNumber());
        assertEquals("http://example.com/logo.png", dto.getUrlLogo());
        assertEquals("Sofia Ramirez", dto.getOwnerName());
    }

    @Test
    @DisplayName("RestaurantResponseDto: Builder should handle null values")
    void restaurantResponseDto_BuilderWithNullsTest() {
        RestaurantResponseDto dto = RestaurantResponseDto.builder()
                .id(2L)
                .name("Otro Restaurante")
                .nit(null) // Campos que pueden ser null
                .address(null)
                .phoneNumber(null)
                .urlLogo(null)
                .ownerName(null)
                .build();

        assertNotNull(dto);
        assertEquals(2L, dto.getId());
        assertEquals("Otro Restaurante", dto.getName());
        assertNull(dto.getNit());
        assertNull(dto.getAddress());
        assertNull(dto.getPhoneNumber());
        assertNull(dto.getUrlLogo());
        assertNull(dto.getOwnerName());
    }

    @Test
    @DisplayName("RestaurantResponseDto: Equals and HashCode should work correctly for same objects")
    void restaurantResponseDto_EqualsAndHashCode_SameObjects_ReturnTrue() {
        RestaurantResponseDto dto1 = createRestaurantResponseDtoBase();
        RestaurantResponseDto dto2 = createRestaurantResponseDtoBase();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertEquals(dto1, dto1); // Reflexividad
        assertEquals(dto1, dto2); // No nulo
        assertEquals(dto2, dto1); // Diferente tipo
    }

    @Test
    @DisplayName("RestaurantResponseDto: Equals should return false for objects with different properties")
    void restaurantResponseDto_Equals_DifferentObjects_ReturnFalse() {
        RestaurantResponseDto baseDto = createRestaurantResponseDtoBase();

        assertNotEquals(baseDto, baseDto.toBuilder().id(99L).build());
        assertNotEquals(baseDto, baseDto.toBuilder().name("Different Name").build());
        assertNotEquals(baseDto, baseDto.toBuilder().nit("999").build());
        assertNotEquals(baseDto, baseDto.toBuilder().address("Different Address").build());
        assertNotEquals(baseDto, baseDto.toBuilder().phoneNumber("000").build());
        assertNotEquals(baseDto, baseDto.toBuilder().urlLogo("diff.png").build());
        assertNotEquals(baseDto, baseDto.toBuilder().ownerName("Different Owner").build());
    }

    @Test
    @DisplayName("RestaurantResponseDto: Equals should return false if one object's field is null and other's is not")
    void restaurantResponseDto_Equals_NullVsNonNullFields_ReturnFalse() {
        RestaurantResponseDto dto1 = createRestaurantResponseDtoBase();
        RestaurantResponseDto dto2 = createRestaurantResponseDtoBase().toBuilder().nit(null).build();
        assertNotEquals(dto1, dto2);
    }

    @Test
    @DisplayName("RestaurantResponseDto: Equals should return true if both objects have null for same fields")
    void restaurantResponseDto_Equals_BothNullFields_ReturnTrue() {
        RestaurantResponseDto dto1 = createRestaurantResponseDtoBase().toBuilder().nit(null).build();
        RestaurantResponseDto dto2 = createRestaurantResponseDtoBase().toBuilder().nit(null).build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    @DisplayName("RestaurantResponseDto: ToString should return a non-null and non-empty string")
    void restaurantResponseDto_ToStringTest() {
        RestaurantResponseDto dto = createRestaurantResponseDtoBase();
        String toStringResult = dto.toString();
        assertNotNull(toStringResult);
        assertFalse(toStringResult.isEmpty());
        assertTrue(toStringResult.contains("name=Mi Restaurante"));
        assertTrue(toStringResult.contains("nit=900123456"));
    }
}