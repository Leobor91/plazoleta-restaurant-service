package com.pragma.plazadecomidas.restaurantservice.domain.exception;

public class PersonalizedBadRequestException extends RuntimeException{

    public PersonalizedBadRequestException(String message) {
        super(message);
    }

}
