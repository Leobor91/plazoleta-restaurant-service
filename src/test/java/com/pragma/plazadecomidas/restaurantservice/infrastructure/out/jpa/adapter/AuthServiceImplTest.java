package com.pragma.plazadecomidas.restaurantservice.infrastructure.out.jpa.adapter;

import com.pragma.plazadecomidas.restaurantservice.application.dto.response.UserResponseDto;
import com.pragma.plazadecomidas.restaurantservice.application.mapper.IUserResponseMapper;
import com.pragma.plazadecomidas.restaurantservice.domain.exception.PersonalizedException;
import com.pragma.plazadecomidas.restaurantservice.domain.exception.PersonalizedNotFoundException;
import com.pragma.plazadecomidas.restaurantservice.domain.model.MessageEnum;
import com.pragma.plazadecomidas.restaurantservice.domain.model.User;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    public static MockWebServer mockWebServer;

    private AuthServiceImpl authService;

    @Mock
    private IUserResponseMapper userResponseMapper;

    private UserResponseDto mockUserResponseDto;
    private User mockUserDomain;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        String authServiceHost = mockWebServer.url("/").toString();
        String authServiceUrlPath = "/users/by-id";

        authService = new AuthServiceImpl(
                WebClient.builder(),
                authServiceHost,
                authServiceUrlPath,
                userResponseMapper
        );

        mockUserResponseDto = UserResponseDto.builder()
                .id(1L)
                .name("Juan")
                .lastName("Perez")
                .identityDocument("1234567890")
                .phoneNumber("+573001234567")
                .email("juan.perez@example.com")
                .birthDate(LocalDate.of(1985, 1, 1))
                .roleName("ADMIN")
                .build();

        mockUserDomain = User.builder()
                .id(1L)
                .name("Juan")
                .lastName("Perez")
                .roleName("ADMIN")
                .build();


        Mockito.lenient().when(userResponseMapper.toUser(mockUserResponseDto)).thenReturn(mockUserDomain);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    @DisplayName("findById: Should return User Optional when Auth Service returns 200 OK")
    void findById_UserFound_ReturnsUserOptional() {

        // GIVEN
        String jsonResponse = "{" +
                "\"id\": 1," +
                "\"nombre\": \"Juan\"," +
                "\"apellido\": \"Perez\"," +
                "\"documento_de_identidad\": \"1234567890\"," +
                "\"celular\": \"+573001234567\"," +
                "\"correo\": \"juan.perez@example.com\"," +
                "\"fecha_de_nacimiento\": \"1985-01-01\"," +
                "\"rol\": \"ADMIN\"" +
                "}";

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(jsonResponse));

        // WHEN
        Optional<User> result = authService.findById(1L);

        // THEN
        assertTrue(result.isPresent());
        assertEquals(mockUserDomain, result.get());
        assertEquals(1L, result.get().getId());
        assertEquals("Juan", result.get().getName());
        assertEquals("Perez", result.get().getLastName());
        assertEquals("ADMIN", result.get().getRoleName());

        try {
            assertEquals("/users/by-id?userId=1", mockWebServer.takeRequest().getPath());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("findById: Should throw PersonalizedNotFoundException when Auth Service returns 4xx error")
    void findById_ClientError_ThrowsPersonalizedNotFoundException() {
        // GIVEN
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(404)
                .setHeader("Content-Type", "application/json")
                .setBody("{\"message\": \"User not found\"}"));

        // WHEN & THEN
        PersonalizedNotFoundException exception = assertThrows(PersonalizedNotFoundException.class,
                () -> authService.findById(1L));

        assertEquals(MessageEnum.ERROR_4XX.getMessage(), exception.getMessage());

        try {
            mockWebServer.takeRequest();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("findById: Should throw PersonalizedException when Auth Service returns 5xx error")
    void findById_ServerError_ThrowsPersonalizedException() {
        // GIVEN
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500)
                .setHeader("Content-Type", "application/json")
                .setBody("{\"message\": \"Internal server error\"}"));

        // WHEN & THEN
        PersonalizedException exception = assertThrows(PersonalizedException.class,
                () -> authService.findById(1L));

        assertEquals(MessageEnum.ERROR_5XX.getMessage(), exception.getMessage());

        try {
            mockWebServer.takeRequest();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("findById: Should return empty Optional if userResponseDto is null after WebClient call")
    void findById_NullUserResponseDto_ReturnsEmptyOptional() {
        // GIVEN
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody("null"));


        when(userResponseMapper.toUser(null)).thenReturn(null);

        // WHEN
        Optional<User> result = authService.findById(1L);

        // THEN
        assertFalse(result.isPresent());

        try {
            mockWebServer.takeRequest();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

}
