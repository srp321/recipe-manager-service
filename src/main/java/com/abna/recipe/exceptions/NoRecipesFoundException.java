package com.abna.recipe.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NoRecipesFoundException extends ResponseStatusException {

    public NoRecipesFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

}
