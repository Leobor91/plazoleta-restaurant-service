package com.pragma.plazadecomidas.restaurantservice.infrastructure.input.rest;

import com.pragma.plazadecomidas.restaurantservice.application.dto.request.RestaurantRequestDto;
import com.pragma.plazadecomidas.restaurantservice.application.dto.response.RestaurantResponseDto;
import com.pragma.plazadecomidas.restaurantservice.application.handler.IRestaurantHandler;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
@Tag(name = "Gestión de Restaurantes", description = "Operaciones relacionadas con la creación y administración de restaurantes.")
public class RestaurantController {

    private final IRestaurantHandler restaurantHandler;

    @Operation(summary = "Registrar un nuevo restaurante",
            description = "Permite registrar un nuevo restaurante. Solo los usuarios con rol ADMINISTRADOR pueden realizar esta operación.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Restaurante creado con éxito",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RestaurantResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto en la creación del restaurante",
                    content = @Content(mediaType = "application/json",
                            examples = {
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "Error de Validación - Nombre Requerido",
                                            summary = "El nombre del restaurante es obligatorio",
                                            value = "{ \"message\": \"El nombre del restaurante es obligatorio\" }"
                                    ),
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "Error de Validación - Nit Requerido",
                                            summary = "El NIT del restaurante es obligatorio",
                                            value = "{ \"message\": \"El NIT del restaurante es obligatorio\" }"
                                    ),
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "Error de Validación -La Direccion Requerida",
                                            summary = "La dirección del restaurante es obligatoria",
                                            value = "{ \"message\": \"La dirección del restaurante es obligatoria\" }"
                                    ),
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "Error de Validación - Formato del Nit",
                                            summary = "El NIT del restaurante debe contener solo números",
                                            value = "{ \"message\": \"El NIT del restaurante debe contener solo números\" }"
                                    ),
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "Error de Validación - Número de celular Requerido",
                                            summary = "El número de teléfono del restaurante es obligatorio",
                                            value = "{ \"message\": \"El número de teléfono del restaurante es obligatorio\" }"
                                    ),
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "Error de Validación - La Url del Logo Requerida",
                                            summary = "La URL del logo es obligatoria",
                                            value = "{ \"message\": \"La URL del logo es obligatoria\" }"
                                    ),
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "Error de Validación - El ID del propietario Requerido",
                                            summary = "El ID del propietario es obligatorio",
                                            value = "{ \"message\": \"El ID del propietario es obligatorio\" }"
                                    ),
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "Error de Validación - Sin Rol PROPIETARIO",
                                            summary = "El usuario no tiene el rol PROPRIETARIO",
                                            value = "{ \"message\": \"El usuario no tiene el rol PROPRIETARIO\" }"
                                    ),
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "Error de Validación - Formato Celular",
                                            summary = "El número de teléfono debe ser únicamente numérico y puede iniciar con '+'. Por ejemplo: +573005698325.",
                                            value = "{ \"message\": \"El número de teléfono debe ser únicamente numérico y puede iniciar con '+'. Por ejemplo: +573005698325.\" }"
                                    ),
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "Error de Validación - Formato Nombre",
                                            summary = "El nombre del restaurante no puede ser solo números. Debe contener  letras, números y espacios",
                                            value = "{ \"message\": \"El nombre del restaurante no puede ser solo números. Debe contener  letras, números y espacios\" }"
                                    )
                            }))
    })
    @PostMapping("/create-restaurant")
    public ResponseEntity<RestaurantResponseDto> save( @Parameter(description = "Datos del restaurante a registrar", required = true)
                                                       @Valid @RequestBody RestaurantRequestDto restaurantRequestDto){
        return ResponseEntity.status(
                HttpStatus.CREATED).body(
                        restaurantHandler.saveRestaurant(
                                restaurantRequestDto));

    }
}
