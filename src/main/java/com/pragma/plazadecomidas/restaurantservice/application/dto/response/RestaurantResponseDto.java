package com.pragma.plazadecomidas.restaurantservice.application.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pragma.plazadecomidas.restaurantservice.infrastructure.util.ValidationConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Schema(description = ValidationConstants.SCHEMA_RESTAURANT_RESPONSE_DESCRIPTION)
public class RestaurantResponseDto {

    @JsonProperty(ValidationConstants.JSON_ID)
    @Schema(description = ValidationConstants.SCHEMA_ID_DESCRIPTION, example = ValidationConstants.SCHEMA_ID_EXAMPLE)
    private Long id;

    @JsonProperty(ValidationConstants.JSON_NOMBRE)
    @Schema(description = ValidationConstants.SCHEMA_RESTAURANT_NAME_DESCRIPTION, example = ValidationConstants.SCHEMA_RESTAURANT_NAME_EXAMPLE)
    private String name;

    @JsonProperty(ValidationConstants.JSON_NIT)
    @Schema(description = ValidationConstants.SCHEMA_RESTAURANT_NIT_DESCRIPTION, example = ValidationConstants.SCHEMA_RESTAURANT_NIT_EXAMPLE)
    private String nit;

    @JsonProperty(ValidationConstants.JSON_DIRECCION)
    @Schema(description = ValidationConstants.SCHEMA_RESTAURANT_ADDRESS_DESCRIPTION, example = ValidationConstants.SCHEMA_RESTAURANT_ADDRESS_EXAMPLE)
    private String address;

    @JsonProperty(ValidationConstants.JSON_CELULAR) // Coherente con RestaurantRequestDto
    @Schema(description = ValidationConstants.SCHEMA_RESTAURANT_PHONE_DESCRIPTION, example = ValidationConstants.SCHEMA_RESTAURANT_PHONE_EXAMPLE)
    private String phoneNumber;

    @JsonProperty(ValidationConstants.JSON_URL_LOGO)
    @Schema(description = ValidationConstants.SCHEMA_RESTAURANT_LOGO_URL_DESCRIPTION, example = ValidationConstants.SCHEMA_RESTAURANT_LOGO_URL_EXAMPLE)
    private String urlLogo;

    @JsonProperty(ValidationConstants.JSON_OWNER_NAME)
    @Schema(description = ValidationConstants.SCHEMA_RESTAURANT_NAME_OWNER_EXAMPLE, example = ValidationConstants.SCHEMA_RESTAURANT_OWNER_NAME)
    private String ownerName;
}
