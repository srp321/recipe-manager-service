package com.abna.recipe.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NoSuchRecipeException extends ResponseStatusException {

    public NoSuchRecipeException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

}
