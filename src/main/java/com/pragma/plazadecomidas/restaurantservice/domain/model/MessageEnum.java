package com.pragma.plazadecomidas.restaurantservice.domain.model;

public enum MessageEnum {

    RESTAURANT_REQUEST_NULL("El Restaurant no puede ser nulo"),
    NAME_REQUIRED("El nombre del restaurante es obligatorio"),
    NAME_STRUCTURE("^(?=.*[a-zA-Z])[\\p{L}\\p{N}\\s'\\-]+$"),
    NAME_FORMAT("El nombre del restaurante no puede ser solo números. Debe contener  letras, números y espacios"),
    NIT_REQUIRED("El NIT del restaurante es obligatorio"),
    NIT_STRUCTURE("^[0-9]+$"),
    NIT_FORMAT("El NIT del restaurante debe contener solo números"),
    ADDRESS_REQUIRED("La dirección del restaurante es obligatoria"),
    PHONE_REQUIRED("El número de teléfono del restaurante es obligatorio"),
    PHONE_STRUCTURE("^\\+?[0-9]{10,13}$"),
    PHONE_FORMAT("El número de teléfono debe ser únicamente numérico y puede iniciar con '+'. Por ejemplo: +573005698325."),
    URL_REQUIRED("La URL del logo es obligatoria"),
    URL_STRUCTURE("^(http|https)://(?:[a-zA-Z0-9.-]+|localhost|\\d{1,3}(?:\\.\\d{1,3}){3})(?::\\d{1,5})?(?:/[^\\s]*)?$"),
    URL_FORMAT("La URL del logo debe tener un formato válido"),
    OWNER_ID_REQUIRED("El ID del propietario es obligatorio"),
    ERROR_4XX("Propietario no encontrado"),
    ERROR_5XX("Error interno en Auth Service"),
    PROPIETARIO("PROPIETARIO"),
    OWNER_NOT_PROPRIETARIO("El usuario no tiene el rol PROPRIETARIO"),
    OWNER_NOT_FOUND("Propietario no encontrado"),
    RESTAURANT_NAME_EXISTS("El restaurante con nombre '%s' ya existe."),
    RESTAURANT_NIT_EXISTS("El NIT del restaurante ya esta registrado."),
    EMPTY(" "),
    ERROR_WEB_CLIENT_REQUEST_EXCEPTION("Error al realizar la petición al servicio de autenticación");

    private final String message;

    MessageEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
