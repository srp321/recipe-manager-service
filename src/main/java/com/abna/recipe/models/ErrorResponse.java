package com.abna.recipe.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {

    @Schema(description = "Error status code.")
    private final int status;

    @Schema(description = "Error status message with additional information.")
    private final String message;

    @JsonIgnore
    private final LocalDateTime dateTime;

}
