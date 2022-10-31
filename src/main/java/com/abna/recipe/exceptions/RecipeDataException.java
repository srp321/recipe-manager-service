package com.abna.recipe.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class RecipeDataException extends ResponseStatusException {

	public RecipeDataException(String message) {
		super(HttpStatus.INTERNAL_SERVER_ERROR, message);
	}

}
