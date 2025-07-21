package com.pragma.plazadecomidas.restaurantservice.application.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pragma.plazadecomidas.restaurantservice.infrastructure.util.ValidationConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Schema(description = ValidationConstants.SCHEMA_USER_RESPONSE_DESCRIPTION)
public class UserResponseDto {

    @JsonProperty(ValidationConstants.JSON_ID)
    @Schema(description = ValidationConstants.SCHEMA_ID_DESCRIPTION, example = ValidationConstants.SCHEMA_ID_EXAMPLE)
    private Long id;

    @JsonProperty(ValidationConstants.JSON_NOMBRE)
    @Schema(description = ValidationConstants.SCHEMA_NAME_DESCRIPTION, example = ValidationConstants.SCHEMA_NAME_EXAMPLE)
    private String name;

    @JsonProperty(ValidationConstants.JSON_APELLIDO)
    @Schema(description = ValidationConstants.SCHEMA_LAST_NAME_DESCRIPTION, example = ValidationConstants.SCHEMA_LAST_NAME_EXAMPLE)
    private String lastName;

    @JsonProperty(ValidationConstants.JSON_DNI)
    @Schema(description = ValidationConstants.SCHEMA_DNI_NUMBER_DESCRIPTION, example = ValidationConstants.SCHEMA_DNI_NUMBER_EXAMPLE)
    private String identityDocument;

    @JsonProperty(ValidationConstants.JSON_TELEFONO)
    @Schema(description = ValidationConstants.SCHEMA_PHONE_DESCRIPTION, example = ValidationConstants.SCHEMA_PHONE_EXAMPLE)
    private String phoneNumber;

    @JsonProperty(ValidationConstants.JSON_FECHA_NACIMIENTO)
    @Schema(description = ValidationConstants.SCHEMA_BIRTH_DATE_DESCRIPTION, example = ValidationConstants.SCHEMA_BIRTH_DATE_EXAMPLE)
    private LocalDate birthDate;

    @JsonProperty(ValidationConstants.JSON_CORREO)
    @Schema(description = ValidationConstants.SCHEMA_EMAIL_DESCRIPTION, example = ValidationConstants.SCHEMA_EMAIL_EXAMPLE)
    private String email;

    @JsonProperty(ValidationConstants.JSON_NOMBRE_ROL)
    @Schema(description = ValidationConstants.SCHEMA_ROLE_NAME_DESCRIPTION, example = ValidationConstants.SCHEMA_ROLE_NAME_EXAMPLE)
    private String roleName;

}