package com.abna.recipe.exceptions;

import com.abna.recipe.models.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class RecipeManagerExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildErrorResponse(e.getMessage(), status);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleUncaughtExceptions(Exception e) {
        return buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleBadRequestException(BadRequestException e) {
        return buildErrorResponse(e.getMessage(), e.getStatus());
    }

    @ExceptionHandler(NoSuchRecipeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleRecipeNotFoundException(NoSuchRecipeException e) {
        return buildErrorResponse(e.getMessage(), e.getStatus());
    }

    @ExceptionHandler(RecipeDataException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleResourceNotCreatedException(RecipeDataException e) {
        return buildErrorResponse(e.getMessage(), e.getStatus());
    }

    @ExceptionHandler(ResourceConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleResourceConflictException(ResourceConflictException e) {
        return buildErrorResponse(e.getMessage(), e.getStatus());
    }

    private ResponseEntity<Object> buildErrorResponse(String message, HttpStatus status) {
        ErrorResponse errorResponse = new ErrorResponse(status.value(), message, LocalDateTime.now());
        return ResponseEntity.status(status).body(errorResponse);
    }
}
