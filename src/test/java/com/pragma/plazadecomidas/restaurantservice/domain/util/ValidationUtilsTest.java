package com.pragma.plazadecomidas.restaurantservice.domain.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidationUtilsTest {

    private ValidationUtils validationUtils;

    @BeforeEach
    void setUp() {
        validationUtils = new ValidationUtils();
    }

    // --- Tests para isValid(String value) ---
    @Test
    @DisplayName("isValid: Debería retornar true para un string válido no vacío")
    void isValid_ValidString_ReturnsTrue() {
        assertTrue(validationUtils.isValid("algun valor"));
    }

    @Test
    @DisplayName("isValid: Debería retornar false para un string nulo")
    void isValid_NullString_ReturnsFalse() {
        assertFalse(validationUtils.isValid(null));
    }

    @Test
    @DisplayName("isValid: Debería retornar false para un string vacío")
    void isValid_EmptyString_ReturnsFalse() {
        assertFalse(validationUtils.isValid(""));
    }

    @Test
    @DisplayName("isValid: Debería retornar false para un string en blanco (solo espacios)")
    void isValid_BlankString_ReturnsFalse() {
        assertFalse(validationUtils.isValid("   "));
    }

    // --- Tests para isValidNameStructure(String name) ---
    @Test
    @DisplayName("isValidNameStructure: Debería retornar true para nombres válidos con letras y números")
    void isValidNameStructure_ValidNameWithLettersAndNumbers_ReturnsTrue() {
        assertTrue(validationUtils.isValidNameStructure("Mi Restaurante 123"));
        assertTrue(validationUtils.isValidNameStructure("RestaurantXYZ1"));
        assertTrue(validationUtils.isValidNameStructure("Cafe Bar 77"));
        assertTrue(validationUtils.isValidNameStructure("juan perez")); // Ahora es válido según el criterio
        assertTrue(validationUtils.isValidNameStructure("El-Puesto")); // Guiones
        assertTrue(validationUtils.isValidNameStructure("O'Reilly's")); // Apóstrofes
    }

    @Test
    @DisplayName("isValidNameStructure: Debería retornar false para nombres que contengan solo números")
    void isValidNameStructure_OnlyNumbers_ReturnsFalse() {
        assertFalse(validationUtils.isValidNameStructure("123"));
        assertFalse(validationUtils.isValidNameStructure("0000"));
        assertFalse(validationUtils.isValidNameStructure("998877"));
    }

    @Test
    @DisplayName("isValidNameStructure: Debería retornar false para nombres vacíos, en blanco o nulos")
    void isValidNameStructure_EmptyBlankNull_ReturnsFalse() {
        assertFalse(validationUtils.isValidNameStructure(""));
        assertFalse(validationUtils.isValidNameStructure("   "));
        assertFalse(validationUtils.isValidNameStructure(null));
    }

    // --- Tests para isValidPhoneStructure(String phone) ---
    @Test
    @DisplayName("isValidPhoneStructure: Debería retornar true para un formato de número de teléfono válido")
    void isValidPhoneStructure_ValidPhone_ReturnsTrue() {
        assertTrue(validationUtils.isValidPhoneStructure("3101234567"));
        assertTrue(validationUtils.isValidPhoneStructure("+573101234567"));
        assertTrue(validationUtils.isValidPhoneStructure("1234567890123")); // 13 dígitos
        assertTrue(validationUtils.isValidPhoneStructure("+1234567890123")); // 13 dígitos con "+"
    }

    @Test
    @DisplayName("isValidPhoneStructure: Debería retornar false para un formato de número de teléfono inválido")
    void isValidPhoneStructure_InvalidPhone_ReturnsFalse() {
        assertFalse(validationUtils.isValidPhoneStructure("123")); // Muy corto
        assertFalse(validationUtils.isValidPhoneStructure("abcde")); // Letras
        assertFalse(validationUtils.isValidPhoneStructure("310 123 4567")); // Con espacios
        assertFalse(validationUtils.isValidPhoneStructure("")); // Vacío
        assertFalse(validationUtils.isValidPhoneStructure(null)); // Nulo
        assertFalse(validationUtils.isValidPhoneStructure("3101234567890123456")); // Muy largo (más de 15)
        assertFalse(validationUtils.isValidPhoneStructure("+310123")); // Prefijo pero muy corto
    }

    // --- Tests para containsOnlyNumbers(String nit) ---
    @Test
    @DisplayName("containsOnlyNumbers: Debería retornar true para un string que contenga solo dígitos")
    void containsOnlyNumbers_ValidNit_ReturnsTrue() {
        assertTrue(validationUtils.containsOnlyNumbers("1234567890"));
        assertTrue(validationUtils.containsOnlyNumbers("0"));
        assertTrue(validationUtils.containsOnlyNumbers("99999"));
    }

    @Test
    @DisplayName("containsOnlyNumbers: Debería retornar false para un string con caracteres no numéricos")
    void containsOnlyNumbers_InvalidNit_ReturnsFalse() {
        assertFalse(validationUtils.containsOnlyNumbers("123A456"));
        assertFalse(validationUtils.containsOnlyNumbers("ABC"));
        assertFalse(validationUtils.containsOnlyNumbers(""));
        assertFalse(validationUtils.containsOnlyNumbers(null));
        assertFalse(validationUtils.containsOnlyNumbers(" ")); // Espacio no es un número
        assertFalse(validationUtils.containsOnlyNumbers("12.34")); // Punto decimal
    }

    // --- Tests para isValidUrl(String url) ---
    @Test
    @DisplayName("isValidUrl: Debería retornar true para un formato de URL válido")
    void isValidUrl_ValidUrl_ReturnsTrue() {
        assertTrue(validationUtils.isValidUrl("http://www.example.com"));
        assertTrue(validationUtils.isValidUrl("https://sub.domain.com/path/to/resource?query=param#fragment")); // <- Si esta falló
        assertTrue(validationUtils.isValidUrl("http://localhost:8080/image.jpg"));
        assertTrue(validationUtils.isValidUrl("https://example.com/"));
    }

    @Test
    @DisplayName("isValidUrl: Debería retornar false si la URL es nula o vacía")
    void isValidUrl_NullOrEmpty_ReturnsFalse() {
        assertFalse(validationUtils.isValidUrl("")); // Posible línea 127 si el orden se mantuvo
        assertFalse(validationUtils.isValidUrl("   ")); // Posible línea 127 si el orden se mantuvo
        assertFalse(validationUtils.isValidUrl(null)); // Posible línea 127 si el orden se mantuvo
    }

    // --- Tests para isValidateRole(String userRole, String roleName) ---
    @Test
    @DisplayName("isValidateRole: Debería retornar true cuando el rol del usuario coincide con el rol esperado (sin distinguir mayúsculas/minúsculas)")
    void isValidateRole_MatchingRoles_ReturnsTrue() {
        assertTrue(validationUtils.isValidateRole("PROPIETARIO", "PROPIETARIO"));
        assertTrue(validationUtils.isValidateRole("propietario", "PROPIETARIO"));
        assertTrue(validationUtils.isValidateRole("PROPIETARIO", "propietario"));
    }

    @Test
    @DisplayName("isValidateRole: Debería retornar false cuando el rol del usuario no coincide con el rol esperado")
    void isValidateRole_NonMatchingRoles_ReturnsFalse() {
        assertFalse(validationUtils.isValidateRole("ADMIN", "PROPIETARIO"));
        assertFalse(validationUtils.isValidateRole("EMPLOYEE", "PROPIETARIO"));
        assertFalse(validationUtils.isValidateRole("PropietarioX", "PROPIETARIO"));
    }

    @Test
    @DisplayName("isValidateRole: Debería retornar false cuando userRole es nulo")
    void isValidateRole_UserRoleNull_ReturnsFalse() {
        assertFalse(validationUtils.isValidateRole(null, "PROPIETARIO"));
    }

    @Test
    @DisplayName("isValidateRole: Debería retornar false cuando roleName es nulo")
    void isValidateRole_RoleNameNull_ReturnsFalse() {
        assertFalse(validationUtils.isValidateRole("PROPIETARIO", null));
    }

    @Test
    @DisplayName("isValidateRole: Debería retornar false cuando ambos roles son nulos")
    void isValidateRole_BothRolesNull_ReturnsFalse() {
        assertFalse(validationUtils.isValidateRole(null, null));
    }

    @Test
    @DisplayName("should validate all new isValid methods correctly (non-null/non-empty)")
    void allNewIsValidMethods_ValidAndInvalidInputs_ReturnsCorrectly() {
        // Test con valores válidos (no nulos ni vacíos)
        assertTrue(validationUtils.isValidName("NombreValido"));
        assertTrue(validationUtils.isValidNit("123456"));
        assertTrue(validationUtils.isValidAdress("Direccion Valida"));
        assertTrue(validationUtils.isValidPhoneNumber("3001234567"));
        assertTrue(validationUtils.isValidOwnerId("123"));

        // Test con valores nulos o vacíos
        assertFalse(validationUtils.isValidName(null));
        assertFalse(validationUtils.isValidName(""));
        assertFalse(validationUtils.isValidName("   "));

        assertFalse(validationUtils.isValidNit(null));
        assertFalse(validationUtils.isValidNit(""));
        assertFalse(validationUtils.isValidNit("   "));

        assertFalse(validationUtils.isValidAdress(null));
        assertFalse(validationUtils.isValidAdress(""));
        assertFalse(validationUtils.isValidAdress("   "));

        assertFalse(validationUtils.isValidPhoneNumber(null));
        assertFalse(validationUtils.isValidPhoneNumber(""));
        assertFalse(validationUtils.isValidPhoneNumber("   "));

        assertFalse(validationUtils.isValidOwnerId(null));
        assertFalse(validationUtils.isValidOwnerId(""));
        assertFalse(validationUtils.isValidOwnerId("   "));
    }

}
