package com.pragma.plazadecomidas.restaurantservice.application.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para la solicitud de creación de un restaurante")
public class RestaurantRequestDto {

    @JsonProperty("nombre")
    @Schema(description = "Nombre del restaurante", example = "Mi Restaurante 123")
    private String name;

    @JsonProperty("nit")
    @Schema(description = "Número de Identificación Tributaria del restaurante", example = "900123456")
    private String nit;

    @JsonProperty("direccion")
    @Schema(description = "Dirección física del restaurante", example = "Calle 123 #4-56")
    private String address;

    @JsonProperty("Celular")
    @Schema(description = "Número de teléfono del restaurante (formato internacional)", example = "+5730012345")
    private String phoneNumber;

    @JsonProperty("url_del_logo")
    @Schema(description = "URL del logo del restaurante", example = "http://example.com/logo.png")
    private String urlLogo;

    @JsonProperty("id_propietario")
    @Schema(description = "ID del usuario propietario del restaurante (obtenido del Auth Service)", example = "1")
    private Long ownerId;

}