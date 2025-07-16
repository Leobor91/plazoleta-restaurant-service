package com.pragma.plazadecomidas.restaurantservice.domain.model;

public enum MessageEnum {

    RESTAURANT_REQUEST_NULL("El Restaurant no puede ser nulo"),
    NAME_REQUIRED("El nombre del restaurante es obligatorio"),
    NAME_STRUCTURE("^(?!\\\\d+$)[a-zA-Z0-9 ]+$"),
    NAME_FORMAT("El nombre del restaurante no puede ser solo números. Debe contener  letras, números y espacios"),
    NIT_REQUIRED("El NIT del restaurante es obligatorio"),
    NIT_STRUCTURE("^[0-9]+$"),
    NIT_FORMAT("El NIT del restaurante debe contener solo números"),
    ADDRESS_REQUIRED("La dirección del restaurante es obligatoria"),
    PHONE_REQUIRED("El número de teléfono del restaurante es obligatorio"),
    PHONE_STRUCTURE("^\\+?\\d{1,12}$"),
    PHONE_FORMAT("El número de teléfono debe ser únicamente numérico y puede iniciar con '+'. Por ejemplo: +573005698325."),
    URL_REQUIRED("La URL del logo es obligatoria"),
    URL_STRUCTURE("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"),
    URL_FORMAT("La URL del logo debe tener un formato válido"),
    OWNER_ID_REQUIRED("El ID del propietario es obligatorio"),
    ERROR_4XX("Error al validar rol de usuario en Auth Service"),
    ERROR_5XX("Error interno en Auth Service"),
    PROPIETARIO("PROPIETARIO"),
    OWNER_NOT_PROPRIETARIO("El usuario no tiene el rol PROPRIETARIO"),
    USER_NOT_FOUND("El usuario no fue encontrado"),
    EMPTY(" ");

    private final String message;

    MessageEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
