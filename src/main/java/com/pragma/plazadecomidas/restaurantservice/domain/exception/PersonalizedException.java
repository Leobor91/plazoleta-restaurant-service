package com.pragma.plazadecomidas.restaurantservice.domain.exception;



public class PersonalizedException extends RuntimeException {

    public PersonalizedException(String message) {
        super(message);
    }

    public PersonalizedException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersonalizedException() {
        super();
    }

    public PersonalizedException(Throwable cause) {
        super(cause);
    }

}
