package com.pragma.plazadecomidas.restaurantservice.infrastructure.util;

public class ValidationConstants {

    private ValidationConstants() {
        // Constructor privado para evitar instanciación
    }

    // --- Mensajes de Excepción Genéricos y de Fallback ---
    public static final String MESSAGE_KEY = "mensaje"; // Clave estándar para mensajes de error en JSON
    public static final String ERROR_GENERIC_MESSAGE = "Error interno del servidor inesperado.";
    public static final String ERROR_VALIDATION_MESSAGE = "Error de validación en la solicitud.";
    public static final String JSON_MALFORMED_MESSAGE = "Solicitud con JSON mal formado o datos ilegibles.";
    public static final String UNEXPECTED_TYPE_MESSAGE = "Tipo de dato inesperado en la solicitud.";
    public static final String MISSING_PARAM_MESSAGE_FORMAT = "Parámetro '%s' es requerido";

    // --- Mensajes de Validación para Usuario ---
    public static final String NAME_REQUIRED_MESSAGE = "El nombre es obligatorio.";
    public static final String LAST_NAME_REQUIRED_MESSAGE = "El apellido es obligatorio.";
    public static final String DNI_NUMBER_REQUIRED_MESSAGE = "El número de documento de identidad es obligatorio.";
    public static final String DNI_NUMBER_FORMAT_MESSAGE = "El número de documento de identidad es inválido.";
    public static final String PHONE_REQUIRED_MESSAGE = "El número de teléfono es obligatorio.";
    public static final String PHONE_SIZE_MESSAGE = "El número de teléfono no puede tener más de 13 caracteres.";
    public static final String PHONE_FORMAT_MESSAGE = "El formato del número de teléfono es inválido. Debe iniciar opcionalmente con '+' y contener solo números, con un máximo de 13 caracteres en total.";
    public static final String BIRTH_DATE_REQUIRED_MESSAGE = "La fecha de nacimiento es obligatoria.";
    public static final String BIRTH_DATE_PAST_MESSAGE = "La fecha de nacimiento debe ser en el pasado.";
    public static final String EMAIL_REQUIRED_MESSAGE = "El correo electrónico es obligatorio.";
    public static final String EMAIL_FORMAT_MESSAGE = "El formato del correo electrónico es inválido.";
    public static final String PASSWORD_REQUIRED_MESSAGE = "La contraseña es obligatoria.";
    public static final String PASSWORD_FORMAT_MESSAGE = "La contraseña debe tener al menos 8 caracteres, al menos una mayúscula, una minúscula, un número y un carácter especial.";
    public static final String ROLE_ID_REQUIRED_MESSAGE = "El ID del rol es obligatorio.";
    public static final String ROLE_ID_POSITIVE_MESSAGE = "El ID del rol debe ser un número positivo.";
    public static final String OWNER_ID_REQUIRED_MESSAGE = "El ID del propietario es obligatorio.";
    public static final String OWNER_ID_POSITIVE_MESSAGE = "El ID del propietario debe ser un número positivo.";


    // --- Mensajes de Excepciones de Dominio para Usuario ---
    public static final String USER_NOT_FOUND_MESSAGE = "Usuario no encontrado.";
    public static final String USER_ALREADY_EXISTS_EMAIL_MESSAGE = "El correo electrónico ya está registrado.";
    public static final String USER_ALREADY_EXISTS_DNI_MESSAGE = "El número de documento de identidad ya está registrado.";
    public static final String USER_ALREADY_EXISTS_PHONE_MESSAGE = "El número de teléfono ya está registrado.";
    public static final String USER_MUST_BE_ADULT_MESSAGE = "El usuario debe ser mayor de 18 años.";
    public static final String USER_IS_NOT_OWNER_MESSAGE = "El usuario no tiene el rol de propietario.";
    public static final String USER_IS_NOT_EMPLOYEE_MESSAGE = "El usuario no tiene el rol de empleado.";
    public static final String INVALID_AGE_MESSAGE = "Edad inválida.";
    public static final String DNI_FORMAT_MESSAGE = "Formato de DNI inválido.";
    public static final String PHONE_INVALID_FORMAT_MESSAGE = "Formato de teléfono inválido.";


    // --- Mensajes de Validación para Restaurante ---
    public static final String RESTAURANT_NAME_REQUIRED_MESSAGE = "El nombre del restaurante es obligatorio.";
    public static final String RESTAURANT_NAME_ONLY_NUMBERS_MESSAGE = "El nombre del restaurante no puede contener solo números.";
    public static final String RESTAURANT_NIT_REQUIRED_MESSAGE = "El NIT del restaurante es obligatorio.";
    public static final String RESTAURANT_NIT_FORMAT_MESSAGE = "El NIT del reataurante no debe ser vació y debe contener solo caracteres numéricos.";
    public static final String RESTAURANT_ADDRESS_REQUIRED_MESSAGE = "La dirección del restaurante es obligatoria.";
    public static final String RESTAURANT_PHONE_REQUIRED_MESSAGE = "El teléfono del restaurante es obligatorio.";
    public static final String RESTAURANT_PHONE_SIZE_MESSAGE = "El teléfono del restaurante no puede tener más de 13 caracteres.";
    public static final String RESTAURANT_PHONE_FORMAT_MESSAGE = "El teléfono del restaurante solo puede contener números y el símbolo '+' al inicio, con un máximo de 13 caracteres.";
    public static final String RESTAURANT_LOGO_URL_REQUIRED_MESSAGE = "La URL del logo es obligatoria.";
    public static final String RESTAURANT_LOGO_URL_FORMAT_MESSAGE = "La URL del logo debe tener un formato válido.";

    // --- Mensajes de Excepciones de Dominio para Restaurante ---
    public static final String RESTAURANT_NOT_FOUND_MESSAGE = "Restaurante no encontrado.";
    public static final String RESTAURANT_ALREADY_EXISTS_MESSAGE = "Ya existe un restaurante con el nombre o NIT proporcionado.";


    // --- Nombres de Propiedades JSON (para @JsonProperty) en español ---
    public static final String JSON_ID = "id";
    public static final String JSON_NOMBRE = "nombre";
    public static final String JSON_APELLIDO = "apellido";
    public static final String JSON_DNI = "documento_de_identidad";
    public static final String JSON_TELEFONO = "celular";
    public static final String JSON_FECHA_NACIMIENTO = "fecha_de_nacimiento";
    public static final String JSON_CORREO = "correo";
    public static final String JSON_PASSWORD = "contrasena";
    public static final String JSON_ID_ROL = "idRol";
    public static final String JSON_NOMBRE_ROL = "rol";
    public static final String JSON_ID_PROPIETARIO = "id_propietario";
    public static final String JSON_OWNER_NAME = "nombre_propietario";

    // Propiedades específicas del restaurante
    public static final String JSON_NIT = "nit";
    public static final String JSON_DIRECCION = "direccion";
    public static final String JSON_URL_LOGO = "url_del_logo";
    public static final String JSON_CELULAR = "Celular";

    // --- Descripciones y Ejemplos para Schema (Swagger/OpenAPI) ---
    // Generales
    public static final String SCHEMA_ID_DESCRIPTION = "ID único.";
    public static final String SCHEMA_ID_EXAMPLE = "1";

    // Usuario
    public static final String SCHEMA_USER_RESPONSE_DESCRIPTION = "Representación de los datos de un usuario en la respuesta de la API.";
    public static final String SCHEMA_NAME_DESCRIPTION = "Nombre del usuario.";
    public static final String SCHEMA_NAME_EXAMPLE = "Juan";
    public static final String SCHEMA_LAST_NAME_DESCRIPTION = "Apellido del usuario.";
    public static final String SCHEMA_LAST_NAME_EXAMPLE = "Pérez";
    public static final String SCHEMA_DNI_NUMBER_DESCRIPTION = "Número de documento de identidad del usuario.";
    public static final String SCHEMA_DNI_NUMBER_EXAMPLE = "1234567890";
    public static final String SCHEMA_PHONE_DESCRIPTION = "Número de teléfono del usuario (formato internacional).";
    public static final String SCHEMA_PHONE_EXAMPLE = "+573001234567";
    public static final String SCHEMA_BIRTH_DATE_DESCRIPTION = "Fecha de nacimiento del usuario (YYYY-MM-DD).";
    public static final String SCHEMA_BIRTH_DATE_EXAMPLE = "2000-01-01";
    public static final String SCHEMA_EMAIL_DESCRIPTION = "Correo electrónico del usuario.";
    public static final String SCHEMA_EMAIL_EXAMPLE = "usuario@example.com";
    public static final String SCHEMA_ROLE_NAME_DESCRIPTION = "Nombre del rol del usuario.";
    public static final String SCHEMA_ROLE_NAME_EXAMPLE = "ROLE_PROPIETARIO";

    // Restaurante
    public static final String SCHEMA_RESTAURANT_REQUEST_DESCRIPTION = "Representación de los datos de un restaurante para su creación.";
    public static final String SCHEMA_RESTAURANT_RESPONSE_DESCRIPTION = "Representación de los datos de un restaurante en la respuesta de la API.";
    public static final String SCHEMA_RESTAURANT_NAME_DESCRIPTION = "Nombre del restaurante.";
    public static final String SCHEMA_RESTAURANT_NAME_EXAMPLE = "Mi Restaurante 123";
    public static final String SCHEMA_RESTAURANT_NIT_DESCRIPTION = "Número de Identificación Tributaria del restaurante.";
    public static final String SCHEMA_RESTAURANT_NIT_EXAMPLE = "900123456";
    public static final String SCHEMA_RESTAURANT_ADDRESS_DESCRIPTION = "Dirección física del restaurante.";
    public static final String SCHEMA_RESTAURANT_ADDRESS_EXAMPLE = "Calle 123 #4-56";
    public static final String SCHEMA_RESTAURANT_PHONE_DESCRIPTION = "Número de teléfono del restaurante (formato internacional).";
    public static final String SCHEMA_RESTAURANT_PHONE_EXAMPLE = "+5730012345";
    public static final String SCHEMA_RESTAURANT_LOGO_URL_DESCRIPTION = "URL del logo del restaurante.";
    public static final String SCHEMA_RESTAURANT_NAME_OWNER_EXAMPLE = "Nombre del propietario del restaurante";
    public static final String SCHEMA_RESTAURANT_LOGO_URL_EXAMPLE = "http://example.com/logo.png";
    public static final String SCHEMA_RESTAURANT_OWNER_NAME = "Sofia Ramirez.";
    public static final String SCHEMA_RESTAURANT_OWNER_ID_DESCRIPTION = "ID del usuario propietario del restaurante (obtenido del Auth Service).";
    public static final String SCHEMA_RESTAURANT_OWNER_ID_EXAMPLE = "1";
}