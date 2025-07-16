package com.pragma.plazadecomidas.restaurantservice.application.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UserResponseDto {

    @JsonProperty("id")
    @Schema(description = "Id del usuario", example = "1")
    private Long id;

    @JsonProperty("nombre")
    @Schema(description = "Nombre del usuario", example = "Juan")
    private String name;

    @JsonProperty("apellido")
    @Schema(description = "Apellido del usuario", example = "Pérez")
    private String lastName;

    @JsonProperty("documento_de_identidad")
    @Schema(description = "Número de documento de identidad del usuario", example = "1234567890")
    private String identityDocument;

    @JsonProperty("celular")
    @Schema(description = "Número de teléfono del usuario", example = "+573101234567")
    private String phoneNumber;

    @JsonProperty("correo")
    @Schema(description = "Correo electrónico del usuario (debe ser único)", example = "juan.perez@example.com")
    private String email;

    @JsonProperty("fecha_de_nacimiento")
    @Schema(description = "Fecha de nacimiento del usuario", example = "1990-01-01")
    private LocalDate birthDate;

    @JsonProperty("rol")
    @Schema(description = "Rol del usuario", example = "PROPIETARIO")
    private String role;

}
