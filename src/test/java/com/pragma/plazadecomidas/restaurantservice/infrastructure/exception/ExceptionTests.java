package com.pragma.plazadecomidas.restaurantservice.infrastructure.exception;

import com.pragma.plazadecomidas.restaurantservice.domain.exception.PersonalizedBadRequestException;
import com.pragma.plazadecomidas.restaurantservice.domain.exception.PersonalizedException;
import com.pragma.plazadecomidas.restaurantservice.domain.exception.PersonalizedNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExceptionTests {

    @Test
    @DisplayName("PersonalizedException should be instantiated with a message")
    void personalizedException_ShouldHaveCorrectMessage() {
        // GIVEN
        String expectedMessage = "This is a personalized exception message.";

        // WHEN
        PersonalizedException exception = new PersonalizedException(expectedMessage);

        // THEN
        assertNotNull(exception);
        assertEquals(expectedMessage, exception.getMessage());
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("PersonalizedNotFoundException should be instantiated with a message")
    void personalizedNotFoundException_ShouldHaveCorrectMessage() {
        // GIVEN
        String expectedMessage = "Resource not found.";

        // WHEN
        PersonalizedNotFoundException exception = new PersonalizedNotFoundException(expectedMessage);

        // THEN
        assertNotNull(exception);
        assertEquals(expectedMessage, exception.getMessage());
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("PersonalizedBadRequestException should be instantiated with a message")
    void personalizedBadRequestException_ShouldHaveCorrectMessage() {
        // GIVEN
        String expectedMessage = "Bad request data provided.";

        // WHEN
        PersonalizedBadRequestException exception = new PersonalizedBadRequestException(expectedMessage);

        // THEN
        assertNotNull(exception);
        assertEquals(expectedMessage, exception.getMessage());
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("ExceptionResponse should be instantiated with a message and have correct properties")
    void exceptionResponse_ShouldHaveCorrectMessage() {
        // GIVEN
        String expectedMessage = "An API error occurred.";

        // WHEN
        ExceptionResponse response = new ExceptionResponse(expectedMessage);

        // THEN
        assertNotNull(response);
        assertEquals(expectedMessage, response.getMessage());
        // Debido a @Data de Lombok, podemos probar getters/setters si no se usan en el constructor
        response.setMessage("New message");
        assertEquals("New message", response.getMessage());
        assertNotNull(response.toString()); // Cubre toString()
        response.hashCode();// Cubre hashCode()
    }
}
