package com.pragma.plazadecomidas.restaurantservice.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO de respuesta para los detalles de un restaurante")
public class RestaurantResponseDto {

    @Schema(description = "ID único del restaurante", example = "1")
    private Long id;

    @Schema(description = "Nombre del restaurante", example = "Mi Restaurante")
    private String name;

    @Schema(description = "Dirección física del restaurante", example = "Calle 123 #4-56")
    private String address;

    @Schema(description = "Número de teléfono del restaurante", example = "+573001234567")
    private String phoneNumber;

    @Schema(description = "URL del logo del restaurante", example = "http://example.com/logo.png")
    private String urlLogo;

    @Schema(description = "Nombre del propietario del restaurante", example = "Sofia Ramirez")
    private String ownerName;

}
