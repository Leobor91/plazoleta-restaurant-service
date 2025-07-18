package com.pragma.plazadecomidas.restaurantservice.infrastructure.exception;

public class PersonalizedBadRequestException extends RuntimeException{

    public PersonalizedBadRequestException(String message) {
        super(message);
    }

}
