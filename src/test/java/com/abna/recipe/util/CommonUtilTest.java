package com.abna.recipe.util;

import com.abna.recipe.entity.RecipeData;
import com.abna.recipe.models.Recipe;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommonUtilTest {

    @Test
    void testMapToRecipeData() {
        final Recipe recipe = new Recipe(0, "name", "type", 0, List.of("ingredients"), "instructions",
                LocalDateTime.of(2020, 1, 1, 0, 0, 0), LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        final RecipeData expectedResult = new RecipeData(0, "name", "type", 0, "ingredients", "instructions",
                LocalDateTime.of(2020, 1, 1, 0, 0, 0), LocalDateTime.of(2020, 1, 1, 0, 0, 0));

        final RecipeData result = CommonUtil.mapToRecipeData(recipe);

        assertEquals(expectedResult.getId(), result.getId());
        assertEquals(expectedResult.getName(), result.getName());
        assertEquals(expectedResult.getServing(), result.getServing());
        assertEquals(expectedResult.getIngredients(), result.getIngredients());
        assertEquals(expectedResult.getType(), result.getType());

    }

    @Test
    void testMapToRecipeModel() {
        final RecipeData recipeData = new RecipeData(0, "name", "type", 0, "ingredients", "instructions",
                LocalDateTime.of(2020, 1, 1, 0, 0, 0), LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        final Recipe expectedResult = new Recipe(0, "name", "type", 0, List.of("ingredients"), "instructions",
                LocalDateTime.of(2020, 1, 1, 0, 0, 0), LocalDateTime.of(2020, 1, 1, 0, 0, 0));

        final Recipe result = CommonUtil.mapToRecipeModel(recipeData);

        assertEquals(expectedResult, result);
    }

    @Test
    void testSanitizeRecipeFalse() {
        final Recipe recipe = new Recipe(0, "name", "", 0, List.of("ingredients"), "instructions",
                LocalDateTime.of(2020, 1, 1, 0, 0, 0), LocalDateTime.of(2020, 1, 1, 0, 0, 0));

        final Boolean result = CommonUtil.sanitizeRecipe(recipe);

        assertFalse(result);
    }

    @Test
    void testSanitizeRecipeTrue() {
        final Recipe recipe = new Recipe(0, "name", "type", 0, List.of("ingredients"), "instructions",
                LocalDateTime.of(2020, 1, 1, 0, 0, 0), LocalDateTime.of(2020, 1, 1, 0, 0, 0));

        final Boolean result = CommonUtil.sanitizeRecipe(recipe);

        assertTrue(result);
    }
}
