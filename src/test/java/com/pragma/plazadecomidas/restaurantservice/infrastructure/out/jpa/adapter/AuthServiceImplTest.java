package com.pragma.plazadecomidas.restaurantservice.infrastructure.out.jpa.adapter;

import com.pragma.plazadecomidas.restaurantservice.application.dto.response.UserResponseDto;
import com.pragma.plazadecomidas.restaurantservice.application.mapper.IUserResponseMapper;
import com.pragma.plazadecomidas.restaurantservice.domain.exception.PersonalizedException;
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

        // URL base de nuestro servidor mock (ej. http://localhost:XXXX/)
        String authServiceHost = mockWebServer.url("/").toString();
        // Path específico del endpoint dentro del servicio Auth
        String authServiceUrlPath = "/users/by-id";

        // Inicializamos AuthServiceImpl con el WebClient apuntando al MockWebServer
        authService = new AuthServiceImpl(
                WebClient.builder(),
                authServiceHost,
                authServiceUrlPath,
                userResponseMapper
        );

        // --- Datos de prueba para UserResponseDto y User de dominio ---
        mockUserResponseDto = UserResponseDto.builder()
                .id(1L)
                .name("Juan")
                .lastName("Perez")
                .dniNumber("1234567890")
                .phone("+573001234567")
                .email("juan.perez@example.com")
                .birthDate(LocalDate.of(1985, 1, 1))
                .roleName("ADMIN") // Campo 'role' en el UserResponseDto
                .build();

        mockUserDomain = User.builder()
                .id(1L)
                .name("Juan")
                .lastName("Perez")
                .roleName("ADMIN") // Campo 'roleName' en el User de dominio
                .build();

        // --- Configuración por defecto para el mapper mock (haciéndolo lenient) ---
        // Se usa Mockito.lenient() para que Mockito no genere UnnecessaryStubbingException
        // si este stub no se usa en un test específico (ej. tests que lanzan excepción antes de mapear).
        Mockito.lenient().when(userResponseMapper.toUser(mockUserResponseDto)).thenReturn(mockUserDomain);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown(); // Detiene el servidor mock después de cada test
    }

    @Test
    @DisplayName("findById: Should return User Optional when Auth Service returns 200 OK")
    void findById_UserFound_ReturnsUserOptional() {
        // GIVEN
        // JSON simulado del Auth Service, las claves coinciden con @JsonProperty del UserResponseDto
        String jsonResponse = "{" +
                "\"id\": 1," +
                "\"nombre\": \"Juan\"," +
                "\"apellido\": \"Perez\"," +
                "\"documento_de_identidad\": \"1234567890\"," +
                "\"celular\": \"+573001234567\"," +
                "\"correo\": \"juan.perez@example.com\"," +
                "\"fecha_de_nacimiento\": \"1985-01-01\"," + // Formato de fecha en JSON (ISO-8601)
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
        // Verificamos que el mapeo fue exitoso y los datos coinciden con el User de dominio esperado
        assertEquals(mockUserDomain, result.get());
        assertEquals(1L, result.get().getId());
        assertEquals("Juan", result.get().getName());
        assertEquals("Perez", result.get().getLastName());
        assertEquals("ADMIN", result.get().getRoleName()); // Se espera 'roleName' en el dominio User

        // Verificamos que la solicitud enviada por WebClient es la esperada
        try {
            assertEquals("/users/by-id?userId=1", mockWebServer.takeRequest().getPath());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("findById: Should throw PersonalizedException when Auth Service returns 4xx error")
    void findById_ClientError_ThrowsPersonalizedException() {
        // GIVEN
        // Configura el MockWebServer para devolver una respuesta 404 Not Found
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(404)
                .setHeader("Content-Type", "application/json")
                .setBody("{\"message\": \"User not found\"}")); // Cuerpo de error opcional

        // WHEN & THEN
        PersonalizedException exception = assertThrows(PersonalizedException.class,
                () -> authService.findById(1L));

        assertEquals(MessageEnum.ERROR_4XX.getMessage(), exception.getMessage());

        // Verificamos que la solicitud se realizó aunque hubo un error
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
        // Configura el MockWebServer para devolver una respuesta 500 Internal Server Error
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500)
                .setHeader("Content-Type", "application/json")
                .setBody("{\"message\": \"Internal server error\"}"));

        // WHEN & THEN
        PersonalizedException exception = assertThrows(PersonalizedException.class,
                () -> authService.findById(1L));

        assertEquals(MessageEnum.ERROR_5XX.getMessage(), exception.getMessage());

        // Verificamos que la solicitud se realizó aunque hubo un error
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
        // Simula una respuesta con un cuerpo "null" o vacío que WebClient podría mapear a null
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody("null")); // O un cuerpo vacío: .setBody("")

        // Cuando el mapper reciba 'null' (porque el WebClient devolvió un cuerpo nulo),
        // debe devolver 'null' para que Optional.ofNullable lo convierta en Optional.empty()
        when(userResponseMapper.toUser(null)).thenReturn(null);

        // WHEN
        Optional<User> result = authService.findById(1L);

        // THEN
        assertFalse(result.isPresent()); // Se espera un Optional vacío

        // Verificamos que la solicitud se realizó
        try {
            mockWebServer.takeRequest();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

}
