package com.pragma.plazadecomidas.restaurantservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MessageEnumTest {

    @Test
    @DisplayName("Should verify messages for all enum values")
    void getMessageTest() {

        assertNotNull(MessageEnum.RESTAURANT_REQUEST_NULL.getMessage());
        assertEquals("El Restaurant no puede ser nulo", MessageEnum.RESTAURANT_REQUEST_NULL.getMessage());

        assertNotNull(MessageEnum.NAME_REQUIRED.getMessage());
        assertEquals("El nombre del restaurante es obligatorio", MessageEnum.NAME_REQUIRED.getMessage());

        assertNotNull(MessageEnum.NAME_STRUCTURE.getMessage());
        assertEquals("^(?=.*[a-zA-Z])[\\p{L}\\p{N}\\s'\\-]+$", MessageEnum.NAME_STRUCTURE.getMessage());

        assertNotNull(MessageEnum.NAME_FORMAT.getMessage());
        assertEquals("El nombre del restaurante no puede ser solo números. Debe contener  letras, números y espacios", MessageEnum.NAME_FORMAT.getMessage());

        assertNotNull(MessageEnum.NIT_REQUIRED.getMessage());
        assertEquals("El NIT del restaurante es obligatorio", MessageEnum.NIT_REQUIRED.getMessage());

        assertNotNull(MessageEnum.NIT_STRUCTURE.getMessage());
        assertEquals("^[0-9]+$", MessageEnum.NIT_STRUCTURE.getMessage());

        assertNotNull(MessageEnum.NIT_FORMAT.getMessage());
        assertEquals("El NIT del restaurante debe contener solo números", MessageEnum.NIT_FORMAT.getMessage());

        assertNotNull(MessageEnum.ADDRESS_REQUIRED.getMessage());
        assertEquals("La dirección del restaurante es obligatoria", MessageEnum.ADDRESS_REQUIRED.getMessage());

        assertNotNull(MessageEnum.PHONE_REQUIRED.getMessage());
        assertEquals("El número de teléfono del restaurante es obligatorio", MessageEnum.PHONE_REQUIRED.getMessage());

        assertNotNull(MessageEnum.PHONE_STRUCTURE.getMessage());
        assertEquals("^\\+?[0-9]{10,13}$", MessageEnum.PHONE_STRUCTURE.getMessage());

        assertNotNull(MessageEnum.PHONE_FORMAT.getMessage());
        assertEquals("El número de teléfono debe ser únicamente numérico y puede iniciar con '+'. Por ejemplo: +573005698325.", MessageEnum.PHONE_FORMAT.getMessage());

        assertNotNull(MessageEnum.URL_REQUIRED.getMessage());
        assertEquals("La URL del logo es obligatoria", MessageEnum.URL_REQUIRED.getMessage());

        assertNotNull(MessageEnum.URL_STRUCTURE.getMessage());
        assertEquals("^(http|https)://(?:[a-zA-Z0-9.-]+|localhost|\\d{1,3}(?:\\.\\d{1,3}){3})(?::\\d{1,5})?(?:/[^\\s]*)?$", MessageEnum.URL_STRUCTURE.getMessage());

        assertNotNull(MessageEnum.URL_FORMAT.getMessage());
        assertEquals("La URL del logo debe tener un formato válido", MessageEnum.URL_FORMAT.getMessage());

        assertNotNull(MessageEnum.OWNER_ID_REQUIRED.getMessage());
        assertEquals("El ID del propietario es obligatorio", MessageEnum.OWNER_ID_REQUIRED.getMessage());

        assertNotNull(MessageEnum.ERROR_4XX.getMessage());
        assertEquals("Error al validar rol de usuario en Auth Service", MessageEnum.ERROR_4XX.getMessage());

        assertNotNull(MessageEnum.ERROR_5XX.getMessage());
        assertEquals("Error interno en Auth Service", MessageEnum.ERROR_5XX.getMessage());

        assertNotNull(MessageEnum.PROPIETARIO.getMessage());
        assertEquals("PROPIETARIO", MessageEnum.PROPIETARIO.getMessage());

        assertNotNull(MessageEnum.OWNER_NOT_PROPRIETARIO.getMessage());
        assertEquals("El usuario no tiene el rol PROPRIETARIO", MessageEnum.OWNER_NOT_PROPRIETARIO.getMessage());

        assertNotNull(MessageEnum.OWNER_NOT_FOUND.getMessage());
        assertEquals("Propietario no encontrado", MessageEnum.OWNER_NOT_FOUND.getMessage());

        assertNotNull(MessageEnum.RESTAURANT_NAME_EXISTS.getMessage());
        assertEquals("El restaurante con nombre '%s' ya existe.", MessageEnum.RESTAURANT_NAME_EXISTS.getMessage());

        assertNotNull(MessageEnum.RESTAURANT_NIT_EXISTS.getMessage());
        assertEquals("El NIT del restaurante ya esta registrado.", MessageEnum.RESTAURANT_NIT_EXISTS.getMessage());

        assertNotNull(MessageEnum.EMPTY.getMessage());
        assertEquals(" ", MessageEnum.EMPTY.getMessage());
    }

    @Test
    @DisplayName("Should cover values() and valueOf() methods implicitly")
    void enumCoverageImplicitTest() {
        // Calling values() to ensure it's covered
        MessageEnum[] allMessages = MessageEnum.values();
        assertTrue(allMessages.length > 0); // At least one enum value exists

        // Calling valueOf() for one value
        MessageEnum singleMessage = MessageEnum.valueOf("RESTAURANT_REQUEST_NULL");
        assertEquals(MessageEnum.RESTAURANT_REQUEST_NULL, singleMessage);
    }
}
