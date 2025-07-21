package com.pragma.plazadecomidas.restaurantservice.application.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pragma.plazadecomidas.restaurantservice.infrastructure.util.ValidationConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Schema(description = ValidationConstants.SCHEMA_RESTAURANT_REQUEST_DESCRIPTION)
public class RestaurantRequestDto {

    @JsonProperty(ValidationConstants.JSON_NOMBRE) 
    @Schema(description = ValidationConstants.SCHEMA_RESTAURANT_NAME_DESCRIPTION, example = ValidationConstants.SCHEMA_RESTAURANT_NAME_EXAMPLE)
    @NotBlank(message = ValidationConstants.RESTAURANT_NAME_REQUIRED_MESSAGE)
    @Pattern(regexp = "^(?![0-9]+$).*$", message = ValidationConstants.RESTAURANT_NAME_ONLY_NUMBERS_MESSAGE)
    private String name;

    @JsonProperty(ValidationConstants.JSON_NIT) 
    @Schema(description = ValidationConstants.SCHEMA_RESTAURANT_NIT_DESCRIPTION, example = ValidationConstants.SCHEMA_RESTAURANT_NIT_EXAMPLE)
    @NotBlank(message = ValidationConstants.RESTAURANT_NIT_REQUIRED_MESSAGE)
    @Pattern(regexp = "\\d+", message = ValidationConstants.RESTAURANT_NIT_FORMAT_MESSAGE)
    private String nit;

    @JsonProperty(ValidationConstants.JSON_DIRECCION) 
    @Schema(description = ValidationConstants.SCHEMA_RESTAURANT_ADDRESS_DESCRIPTION, example = ValidationConstants.SCHEMA_RESTAURANT_ADDRESS_EXAMPLE)
    @NotBlank(message = ValidationConstants.RESTAURANT_ADDRESS_REQUIRED_MESSAGE)
    private String address;

    @JsonProperty(ValidationConstants.JSON_CELULAR) 
    @Schema(description = ValidationConstants.SCHEMA_RESTAURANT_PHONE_DESCRIPTION, example = ValidationConstants.SCHEMA_RESTAURANT_PHONE_EXAMPLE)
    @NotBlank(message = ValidationConstants.RESTAURANT_PHONE_REQUIRED_MESSAGE)
    @Size(max = 13, message = ValidationConstants.RESTAURANT_PHONE_SIZE_MESSAGE)
    @Pattern(regexp = "^\\+?\\d{1,12}$", message = ValidationConstants.RESTAURANT_PHONE_FORMAT_MESSAGE)
    private String phoneNumber;

    @JsonProperty(ValidationConstants.JSON_URL_LOGO) 
    @Schema(description = ValidationConstants.SCHEMA_RESTAURANT_LOGO_URL_DESCRIPTION, example = ValidationConstants.SCHEMA_RESTAURANT_LOGO_URL_EXAMPLE)
    @NotBlank(message = ValidationConstants.RESTAURANT_LOGO_URL_REQUIRED_MESSAGE)
    @URL(message = ValidationConstants.RESTAURANT_LOGO_URL_FORMAT_MESSAGE)
    private String urlLogo;

    @JsonProperty(ValidationConstants.JSON_ID_PROPIETARIO) 
    @Schema(description = ValidationConstants.SCHEMA_RESTAURANT_OWNER_ID_DESCRIPTION, example = ValidationConstants.SCHEMA_RESTAURANT_OWNER_ID_EXAMPLE)
    @NotNull(message = ValidationConstants.OWNER_ID_REQUIRED_MESSAGE)
    @Positive(message = ValidationConstants.OWNER_ID_POSITIVE_MESSAGE)
    private Long ownerId;
}
