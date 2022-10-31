package com.abna.recipe.util;

import com.abna.recipe.entity.RecipeData;
import com.abna.recipe.models.Recipe;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
public class CommonUtil {

    public static RecipeData mapToRecipeData(Recipe recipe) {
        RecipeData recipeData = RecipeData.builder()
                .id(recipe.getId())
                .name(recipe.getName())
                .type(recipe.getType())
                .serving(recipe.getServing())
                .instructions(recipe.getInstructions())
                .ingredients(String.join(", ", recipe.getIngredients()))
                .build();

        if (recipe.getCreateDateTime() == null) {
            log.debug("new recipe, need to set create time");
            LocalDateTime currentDateTime = LocalDateTime.now();
            recipeData.setCreateDateTime(currentDateTime);
            recipeData.setUpdateDateTime(currentDateTime);
        } else {
            log.debug("updating recipe, need to update update time");
            recipeData.setCreateDateTime(recipe.getCreateDateTime());
            recipeData.setUpdateDateTime(LocalDateTime.now());
        }

        return recipeData;
    }

    public static Recipe mapToRecipeModel(RecipeData recipeData) {

        return Recipe.builder()
                .id(recipeData.getId())
                .name(recipeData.getName())
                .type(recipeData.getType())
                .serving(recipeData.getServing())
                .instructions(recipeData.getInstructions())
                .createDateTime(recipeData.getCreateDateTime())
                .updateDateTime(recipeData.getUpdateDateTime())
                .ingredients(Arrays.asList(recipeData.getIngredients().split("\\s*, \\s*")))
                .build();
    }

    public static Boolean sanitizeRecipe(Recipe recipe) {
        if (Objects.isNull(recipe)) {
            log.error("recipe data is null");
            return false;
        } else if (Objects.isNull(recipe.getId())
                || recipe.getName().isBlank()
                || recipe.getType().isBlank()
                || Objects.isNull(recipe.getServing())) {
            log.error("required parameters are null");
            return false;
        } else
            return true;
    }

}
