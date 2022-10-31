package com.abna.recipe.controller;

import com.abna.recipe.exceptions.*;
import com.abna.recipe.models.ErrorResponse;
import com.abna.recipe.models.Recipe;
import com.abna.recipe.service.RecipeManagerService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

import static com.abna.recipe.util.CommonUtil.sanitizeRecipe;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@OpenAPIDefinition(
        info = @Info(title = "Recipe Manager Service API",
                description = "Search and CRUD operation for Recipe Management")
)
@RestController
@RequestMapping("/api/v1")
@Slf4j
public class RecipesManagerController {

    private final RecipeManagerService recipeManagerService;

    public RecipesManagerController(RecipeManagerService recipeManagerService) {
        this.recipeManagerService = recipeManagerService;
    }

    @PostMapping(value = "/recipe", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @Operation(description = "Create a new recipe with the selected attributes.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "successfully created",
                    content = @Content(schema = @Schema(implementation = Recipe.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Recipe already present",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Recipe> createRecipe(@RequestBody @Valid Recipe recipe) {
        log.info("Create recipe request");

        if (!sanitizeRecipe(recipe)) {
            log.error("invalid recipe request params");
            throw new BadRequestException(ErrorMessages.BAD_REQUEST);
        } else if (!Objects.isNull(recipeManagerService.getRecipeById(recipe.getId()))) {
            log.error("recipe already available");
            throw new ResourceConflictException(ErrorMessages.RESOURCE_CONFLICT);
        } else {
            Recipe savedRecipe;
            try {
                log.debug("initiating save recipe service");
                savedRecipe = recipeManagerService.createRecipe(recipe);
            } catch (Exception e) {
                log.error("issue with saving recipe data");
                throw new RecipeDataException(ErrorMessages.INTERNAL_SERVER_ERROR);
            }
            log.info("recipe saved with recipeId: " + savedRecipe.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRecipe);
        }
    }

    @GetMapping(value = "/recipe/{id}", produces = APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve the recipe data based on ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "successfully retrieved",
                    content = @Content(schema = @Schema(implementation = Recipe.class))),
            @ApiResponse(responseCode = "404", description = "Recipe not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Recipe> getRecipe(@PathVariable Integer id) {
        log.info("get existing recipe request");

        Recipe recipe;
        try {
            log.info("initiating get service request for recipe id: " + id);
            recipe = recipeManagerService.getRecipeById(id);
            if (Objects.isNull(recipe)) {
                log.error("request recipe id: " + id + " not found");
                throw new NoSuchRecipeException(ErrorMessages.RECIPE_NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("issue with getting recipe data");
            throw new RecipeDataException(ErrorMessages.INTERNAL_SERVER_ERROR);
        }
        log.info("recipe found with recipeId: " + recipe.getId());
        return ResponseEntity.status(HttpStatus.OK).body(recipe);
    }

    @GetMapping(value = "/recipes", produces = APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve all the recipe data.")
    @Parameter(name = "name", description = "The recipe name can contain this value")
    @Parameter(name = "serving", description = "The recipe should have more than serving")
    @Parameter(name = "includeIngredients", description = "The recipe excludes mentioned ingredients")
    @Parameter(name = "excludeIngredients", description = "The recipe excludes mentioned ingredients")
    @Parameter(name = "type", description = "The recipe can be of this type")
    @Parameter(name = "instructions", description = "The recipe can contain these instructions")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "successfully retrieved all recipes",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Recipe.class)))),
            @ApiResponse(responseCode = "404", description = "Recipes not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<Recipe>> getAllRecipes(
            @Nullable @RequestParam String name,
            @Nullable @RequestParam Integer serving,
            @Nullable @RequestParam List<String> includeIngredients,
            @Nullable @RequestParam List<String> excludeIngredients,
            @Nullable @RequestParam String type,
            @Nullable @RequestParam String instructions) {
        log.info("get filter recipes request");

        List<Recipe> recipeList;
        try {
            log.info("initiating getAll service request for recipes");
            recipeList = recipeManagerService.getAllRecipes(name, serving, includeIngredients, excludeIngredients, type, instructions);
            if (CollectionUtils.isEmpty(recipeList)) {
                log.error("no recipes found");
                throw new NoRecipesFoundException(ErrorMessages.RECIPES_NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("issue with getting recipe data");
            throw new RecipeDataException(ErrorMessages.INTERNAL_SERVER_ERROR);
        }
        log.info("number of recipes retrieved: " + recipeList.size());
        return ResponseEntity.status(HttpStatus.OK).body(recipeList);
    }

    @PutMapping(value = "/recipe", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @Operation(description = "Update recipe data based on recipe ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "successfully updated the recipe",
                    content = @Content(schema = @Schema(implementation = Recipe.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Recipes not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Recipe> updateRecipe(@RequestBody @Valid Recipe recipe) {
        log.info("update existing recipe request");

        if (!sanitizeRecipe(recipe)) {
            log.error("invalid recipe request params");
            throw new BadRequestException(ErrorMessages.BAD_REQUEST);
        } else if (Objects.isNull(recipeManagerService.getRecipeById(recipe.getId()))) {
            log.error("recipe not available");
            throw new NoSuchRecipeException(ErrorMessages.RECIPE_NOT_FOUND);
        } else {
            Recipe updatedRecipe;
            try {
                log.debug("initiating update service for recipe");
                updatedRecipe = recipeManagerService.updateRecipe(recipe);
            } catch (Exception e) {
                log.error("issue with updating recipe data");
                throw new RecipeDataException(ErrorMessages.INTERNAL_SERVER_ERROR);
            }
            log.info("recipe updated with recipeId: " + updatedRecipe.getId());
            return ResponseEntity.status(HttpStatus.OK).body(updatedRecipe);
        }
    }

    @DeleteMapping(value = "/recipe/{id}", produces = APPLICATION_JSON_VALUE)
    @Operation(description = "Delete recipe data based on recipe ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "successfully deleted the recipe",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Recipes not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<String> deleteRecipe(@PathVariable Integer id) {
        log.info("delete existing recipe request");

        if (Objects.isNull(recipeManagerService.getRecipeById(id))) {
            log.error("recipe not available");
            throw new NoSuchRecipeException(ErrorMessages.RECIPE_NOT_FOUND);
        } else {
            try {
                log.debug("initiating delete service for recipe");
                recipeManagerService.deleteRecipeFromRepository(id);
            } catch (Exception e) {
                log.error("issue with deleting recipe data");
                throw new RecipeDataException(ErrorMessages.INTERNAL_SERVER_ERROR);
            }
        }
        log.info("recipe deleted with recipeId: " + id);
        return ResponseEntity.status(HttpStatus.OK).body("requested recipe deleted");
    }
}
