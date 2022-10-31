package com.abna.recipe.service;

import com.abna.recipe.entity.RecipeData;
import com.abna.recipe.models.Recipe;
import com.abna.recipe.repository.RecipeRepository;
import com.abna.recipe.util.CommonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeManagerServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    private RecipeManagerService recipeManagerService;

    @BeforeEach
    void setUp() {
        recipeManagerService = new RecipeManagerService(recipeRepository);
    }

    @Test
    void createRecipeWhenRecipeIsInvalidThenThrowException() {
        Recipe recipe = Recipe.builder().build();
        assertThrows(NullPointerException.class, () -> recipeManagerService.createRecipe(recipe));
    }

    @Test
    void createRecipeWhenRecipeIsValid() {
        Recipe recipe =
                Recipe.builder()
                        .id(1)
                        .name("Chicken Biryani")
                        .type("Indian")
                        .serving(4)
                        .ingredients(Arrays.asList("Chicken", "Rice", "Spices"))
                        .instructions("Cook the chicken and rice together")
                        .build();

        RecipeData recipeData = CommonUtil.mapToRecipeData(recipe);

        when(recipeRepository.save(any(RecipeData.class))).thenReturn(recipeData);

        Recipe savedRecipe = recipeManagerService.createRecipe(recipe);

        assertEquals(recipe.getName(), savedRecipe.getName());
        assertEquals(recipe.getType(), savedRecipe.getType());
        assertEquals(recipe.getInstructions(), savedRecipe.getInstructions());
    }

    @Test
    void getRecipeByIdWhenRecipeIsFoundThenReturnTheRecipe() {
        RecipeData recipeData =
                RecipeData.builder()
                        .id(1)
                        .name("Chicken Biryani")
                        .type("Indian")
                        .serving(4)
                        .ingredients("Chicken, Rice, Spices")
                        .instructions("Cook the chicken and rice together")
                        .build();

        when(recipeRepository.findById(1)).thenReturn(Optional.of(recipeData));

        Recipe recipe = recipeManagerService.getRecipeById(1);

        assertNotNull(recipe);
        assertEquals(1, recipe.getId());
        assertEquals("Chicken Biryani", recipe.getName());
        assertEquals("Indian", recipe.getType());
        assertEquals(4, recipe.getServing());
        assertEquals("Cook the chicken and rice together", recipe.getInstructions());
        assertEquals(Arrays.asList("Chicken", "Rice", "Spices"), recipe.getIngredients());

        verify(recipeRepository, times(1)).findById(1);
    }

    @Test
    void testGetRecipeById() {
        final Recipe expectedResult = new Recipe(0, "name", "type", 0, List.of("ingredients"), "instructions",
                LocalDateTime.of(2020, 1, 1, 0, 0, 0), LocalDateTime.of(2020, 1, 1, 0, 0, 0));

        final Optional<RecipeData> recipeData = Optional.of(
                new RecipeData(0, "name", "type", 0, "ingredients", "instructions",
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0), LocalDateTime.of(2020, 1, 1, 0, 0, 0)));
        when(recipeRepository.findById(0)).thenReturn(recipeData);

        final Recipe result = recipeManagerService.getRecipeById(0);

        assertEquals(expectedResult, result);
    }

    @Test
    void testGetRecipeByIdRecipeRepositoryReturnsAbsent() {
        when(recipeRepository.findById(0)).thenReturn(Optional.empty());

        final Recipe result = recipeManagerService.getRecipeById(0);

        assertNull(result);
    }

    @Test
    void testGetAllRecipes() {
        final List<Recipe> expectedResult = List.of(new Recipe(0, "name", "type", 0, List.of("ingredients"), "instructions",
                LocalDateTime.of(2020, 1, 1, 0, 0, 0), LocalDateTime.of(2020, 1, 1, 0, 0, 0)));

        final List<RecipeData> recipeData = List.of(new RecipeData(0, "name", "type", 0, "ingredients", "instructions",
                LocalDateTime.of(2020, 1, 1, 0, 0, 0), LocalDateTime.of(2020, 1, 1, 0, 0, 0)));
        when(recipeRepository.findAll(any(Specification.class))).thenReturn(recipeData);

        final List<Recipe> result = recipeManagerService.getAllRecipes("name", 0, List.of("ingredients"),
                List.of("ingredients"), "type", "instructions");

        assertEquals(expectedResult, result);
    }

    @Test
    void testGetAllRecipesRecipeRepositoryReturnsNoItems() {
        final List<Recipe> expectedResult = Collections.emptyList();
        when(recipeRepository.findAll(any(Specification.class))).thenReturn(Collections.emptyList());

        final List<Recipe> result = recipeManagerService.getAllRecipes("", 0, null, null, null, null);

        assertEquals(expectedResult, result);
    }

    @Test
    void getRecipeByIdWhenRecipeIsNotFoundThenReturnNull() {
        Integer id = 1;
        when(recipeRepository.findById(id)).thenReturn(Optional.empty());

        Recipe recipe = recipeManagerService.getRecipeById(id);

        assertNull(recipe);
    }

    @Test
    void getAllRecipesWhenServingIsProvided() {
        RecipeData recipeData =
                RecipeData.builder()
                        .id(1)
                        .name("test")
                        .type("test")
                        .serving(2)
                        .instructions("test")
                        .ingredients("test")
                        .build();

        lenient().when(recipeRepository.findAll((Specification<RecipeData>) any())).thenReturn(List.of(recipeData));

        Recipe recipe = CommonUtil.mapToRecipeModel(recipeData);

        assertEquals(recipeManagerService.getAllRecipes("", 2, null, null, "", "").get(0), recipe);
    }

    @Test
    void deleteRecipeFromRepositoryWhenIdIsValid() {
        Integer id = 1;

        recipeRepository.findById(id);

        recipeManagerService.deleteRecipeFromRepository(id);

        verify(recipeRepository, times(1)).deleteById(id);
    }
}
