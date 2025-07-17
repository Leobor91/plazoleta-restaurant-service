package com.pragma.plazadecomidas.restaurantservice.domain.exception;

public class PersonalizedNotFoundException extends RuntimeException{

    public PersonalizedNotFoundException(String message) {
        super(message);
    }

    public PersonalizedNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersonalizedNotFoundException() {
        super();
    }

    public PersonalizedNotFoundException(Throwable cause) {
        super(cause);
    }
}
