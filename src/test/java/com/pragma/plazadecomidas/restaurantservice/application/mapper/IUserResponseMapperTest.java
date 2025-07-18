package com.pragma.plazadecomidas.restaurantservice.application.mapper;

import com.pragma.plazadecomidas.restaurantservice.application.dto.response.UserResponseDto;
import com.pragma.plazadecomidas.restaurantservice.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class IUserResponseMapperTest {

    @Autowired
    private IUserResponseMapper userResponseMapper;

    @BeforeEach
    void setUp() {
        // No se necesita inicialización especial aquí.
    }

    @Test
    @DisplayName("toResponseDto: Should map User to UserResponseDto correctly, including roleName to role")
    void toResponseDto_ValidUser_ReturnsUserResponseDto() {
        // GIVEN
        User user = new User();
        user.setId(1L);
        user.setName("Juan");
        user.setLastName("Perez");
        user.setIdentityDocument("123456789");
        user.setPhoneNumber("3001112233");
        user.setEmail("juan.perez@example.com");
        user.setRoleName("ADMIN"); // Aquí es roleName

        // WHEN
        UserResponseDto responseDto = userResponseMapper.toResponseDto(user);

        // THEN
        assertNotNull(responseDto);
        assertEquals(1L, responseDto.getId());
        assertEquals("Juan", responseDto.getName());
        assertEquals("Perez", responseDto.getLastName());
        assertEquals("123456789", responseDto.getIdentityDocument());
        assertEquals("3001112233", responseDto.getPhoneNumber());
        assertEquals("juan.perez@example.com", responseDto.getEmail());
        assertEquals("ADMIN", responseDto.getRole()); // Aquí es 'role' en el DTO
    }

    @Test
    @DisplayName("toUser: Should map UserResponseDto to User correctly, including role to roleName")
    void toUser_ValidUserResponseDto_ReturnsUser() {
        // GIVEN
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(2L);
        responseDto.setName("Maria");
        responseDto.setLastName("Gomez");
        responseDto.setIdentityDocument("987654321");
        responseDto.setPhoneNumber("3104445566");
        responseDto.setEmail("maria.gomez@example.com");
        responseDto.setRole("EMPLOYEE"); // Aquí es 'role' en el DTO

        // WHEN
        User user = userResponseMapper.toUser(responseDto);

        // THEN
        assertNotNull(user);
        assertEquals(2L, user.getId());
        assertEquals("Maria", user.getName());
        assertEquals("Gomez", user.getLastName());
        assertEquals("987654321", user.getIdentityDocument());
        assertEquals("3104445566", user.getPhoneNumber());
        assertEquals("maria.gomez@example.com", user.getEmail());
        assertEquals("EMPLOYEE", user.getRoleName()); // Aquí es roleName en el dominio
    }

    @Test
    @DisplayName("toResponseDto: Should handle null User gracefully")
    void toResponseDto_NullUser_ReturnsNull() {
        // GIVEN
        User user = null;

        // WHEN
        UserResponseDto responseDto = userResponseMapper.toResponseDto(user);

        // THEN
        assertNull(responseDto);
    }

    @Test
    @DisplayName("toUser: Should handle null UserResponseDto gracefully")
    void toUser_NullUserResponseDto_ReturnsNull() {
        // GIVEN
        UserResponseDto responseDto = null;

        // WHEN
        User user = userResponseMapper.toUser(responseDto);

        // THEN
        assertNull(user);
    }
}
