package com.abna.recipe.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Recipe {

    @NotBlank
    @Min(1)
    @Max(1000000)
    @Schema(description = "ID of the Recipe, user-defined and is not auto-generated.")
    private Integer id;

    @NotBlank
    @Size(min = 5, max = 100)
    @Schema(description = "Name of recipe, could be very fancy, but let's not keep it too long.")
    private String name;

    @NotBlank
    @Size(min = 1, max = 30)
    @Schema(description = "Recipe type defines the cuisine or context of the meal.")
    private String type;

    @Min(1)
    @Max(10)
    @NotEmpty
    @Schema(description = "Serving size of the recipe; catering to number of people who will be eating it.")
    private Integer serving;

    @NotEmpty
    @Schema(description = "List of ingredients used in the recipe, as detailed as possible.")
    private List<String> ingredients = new ArrayList<>();

    @NotBlank
    @Size(min = 1, max = 500)
    @Schema(description = "Steps to make the recipe in a certain defined and standard way, easy enough to understand by any person.")
    private String instructions;

    @JsonIgnore
    private LocalDateTime createDateTime;

    @JsonIgnore
    private LocalDateTime updateDateTime;
}
