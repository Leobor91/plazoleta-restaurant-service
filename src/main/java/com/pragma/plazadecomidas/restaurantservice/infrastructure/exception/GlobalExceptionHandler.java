package com.pragma.plazadecomidas.restaurantservice.infrastructure.exception;

import com.pragma.plazadecomidas.restaurantservice.domain.exception.DomainException;
import com.pragma.plazadecomidas.restaurantservice.domain.exception.PersonalizedBadRequestException;
import com.pragma.plazadecomidas.restaurantservice.domain.exception.PersonalizedNotFoundException;
import com.pragma.plazadecomidas.restaurantservice.domain.model.MessageEnum;
import jakarta.validation.UnexpectedTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = "Error de validación en la solicitud.";
        FieldError fieldError = ex.getBindingResult().getFieldError();
        if (fieldError != null) {
            errorMessage = fieldError.getDefaultMessage();
        } else if (ex.getBindingResult().hasGlobalErrors()) {
            errorMessage = ex.getBindingResult().getGlobalError().getDefaultMessage();
        }
        ExceptionResponse response = new ExceptionResponse(errorMessage);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ExceptionResponse> handleDomainException(DomainException ex) {
        ExceptionResponse response = new ExceptionResponse(ex.getMessage());

        if (ex instanceof PersonalizedNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else if (ex instanceof PersonalizedBadRequestException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ExceptionResponse response = new ExceptionResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(WebClientRequestException.class)
    public ResponseEntity<ExceptionResponse> handleWebClientRequestException(WebClientRequestException ex) {
        ExceptionResponse response = new ExceptionResponse(MessageEnum.ERROR_WEB_CLIENT_REQUEST_EXCEPTION.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        ExceptionResponse response = new ExceptionResponse("Solicitud con JSON mal formado o datos ilegibles. " +
                "Verifique la sintaxis y tipos de datos."); // Mensaje un poco más descriptivo
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(UnexpectedTypeException.class)
    public ResponseEntity<ExceptionResponse> handleUnexpectedTypeException(UnexpectedTypeException ex) {
        ExceptionResponse response = new ExceptionResponse("Tipo de dato inesperado en la solicitud. " +
                "Asegúrese de que los valores coincidan con los tipos esperados.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ExceptionResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        ExceptionResponse errorResponse = new ExceptionResponse(String.format("Parámetro de consulta '%s' es requerido.", ex.getParameterName()));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Manejador genérico para cualquier otra excepción no capturada
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGenericException(Exception ex) {
        ExceptionResponse response = new ExceptionResponse("Error interno del servidor inesperado. Por favor, contacte a soporte.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}
