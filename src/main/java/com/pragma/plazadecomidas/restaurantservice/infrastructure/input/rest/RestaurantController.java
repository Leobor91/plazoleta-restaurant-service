package com.pragma.plazadecomidas.restaurantservice.infrastructure.input.rest;

import com.pragma.plazadecomidas.restaurantservice.application.dto.request.RestaurantRequestDto;
import com.pragma.plazadecomidas.restaurantservice.application.dto.response.RestaurantResponseDto;
import com.pragma.plazadecomidas.restaurantservice.application.handler.IRestaurantHandler;
import com.pragma.plazadecomidas.restaurantservice.infrastructure.exception.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            @ApiResponse(responseCode = "400", description = "Solicitud inválida o datos faltantes/incorrectos",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class),
                            examples = {
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "Error de Validación - Nombre Requerido",
                                            summary = "El nombre del restaurante es obligatorio",
                                            value = "{ \"mensaje\": \"El nombre del restaurante es requerido\" }"
                                    ),
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "Error de Validación - NIT Requerido",
                                            summary = "El NIT del restaurante es obligatorio",
                                            value = "{ \"mensaje\": \"El NIT del restaurante es requerido\" }"
                                    ),
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "Error de Validación - Dirección Requerida",
                                            summary = "La dirección del restaurante es obligatoria",
                                            value = "{ \"mensaje\": \"La dirección del restaurante es requerida\" }"
                                    ),
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "Error de Validación - Teléfono Requerido",
                                            summary = "El número de teléfono del restaurante es obligatorio",
                                            value = "{ \"mensaje\": \"El número de teléfono del restaurante es obligatorio\" }"
                                    ),
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "Error de Validación - URL Logo Requerida",
                                            summary = "La URL del logo es obligatoria",
                                            value = "{ \"mensaje\": \"La URL del logo es requerida\" }"
                                    ),
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "Error de Validación - ID Propietario Requerido",
                                            summary = "El ID del propietario es obligatorio",
                                            value = "{ \"mensaje\": \"El ID del propietario es requerido\" }"
                                    ),
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "Error de Formato - NIT",
                                            summary = "El NIT del restaurante debe contener solo números",
                                            value = "{ \"mensaje\": \"El NIT del reataurante no debe ser vació y debe contener solo caracteres numéricos.\" }"
                                    ),
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "Error de Formato - Celular",
                                            summary = "El número de teléfono debe ser numérico y puede iniciar con '+'.",
                                            value = "{ \"mensaje\": \"El número de teléfono debe ser únicamente numérico y puede iniciar con '+'. Por ejemplo: +573005698325.\" }"
                                    ),
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "Error de Formato - Nombre",
                                            summary = "El nombre del restaurante no puede ser solo números.",
                                            value = "{ \"mensaje\": \"El nombre del restaurante no puede ser solo números. Debe contener letras, números y espacios\" }"
                                    ),
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "Error de Formato - URL Logo",
                                            summary = "La URL del logo no tiene un formato válido",
                                            value = "{ \"mensaje\": \"La URL del logo no tiene un formato válido\" }"
                                    )
                            })),
            @ApiResponse(responseCode = "404", description = "Propietario no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class),
                            examples = {
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "Error - Propietario No Encontrado",
                                            summary = "El ID del propietario no corresponde a un usuario existente.",
                                            value = "{ \"mensaje\": \"Propietario no encontrado\" }"
                                    )
                            })),

            @ApiResponse(responseCode = "409", description = "Conflicto con el estado actual del recurso o reglas de negocio",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class),
                            examples = {
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "Error de Negocio - Rol Incorrecto",
                                            summary = "El usuario no tiene el rol PROPIETARIO",
                                            value = "{ \"mensaje\": \"El usuario no tiene el rol PROPIETARIO\" }"
                                    ),
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "Error de Negocio - Nombre de Restaurante Existente",
                                            summary = "Ya existe un restaurante con este nombre",
                                            value = "{ \"mensaje\": \"Ya existe un restaurante con el nombre [nombre_restaurante_ejemplo]\" }"
                                    ),
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "Error de Negocio - NIT de Restaurante Existente",
                                            summary = "Ya existe un restaurante con este NIT",
                                            value = "{ \"mensaje\": \"Ya existe un restaurante con este NIT\" }"
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
