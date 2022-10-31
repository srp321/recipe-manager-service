package com.abna.recipe.service;

import com.abna.recipe.entity.RecipeData;
import com.abna.recipe.models.Recipe;
import com.abna.recipe.repository.RecipeRepository;
import com.abna.recipe.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.abna.recipe.repository.RecipeDataSpecification.*;
import static com.abna.recipe.util.CommonUtil.mapToRecipeData;
import static com.abna.recipe.util.CommonUtil.mapToRecipeModel;

@Service
@Slf4j
public class RecipeManagerService {

    private final RecipeRepository recipeRepository;

    public RecipeManagerService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Recipe createRecipe(Recipe newRecipe) {
        log.info("saving recipe with recipeId: " + newRecipe.getId());

        RecipeData recipeData = mapToRecipeData(newRecipe);
        RecipeData savedRecipe = recipeRepository.save(recipeData);
        return mapToRecipeModel(savedRecipe);
    }

    public Recipe getRecipeById(Integer id) {
        log.info("getting recipe with recipeId: " + id);

        Optional<RecipeData> recipeDataOpt = recipeRepository.findById(id);
        return recipeDataOpt.map(CommonUtil::mapToRecipeModel).orElse(null);
    }

    public List<Recipe> getAllRecipes(String name, Integer serving, List<String> includeIngredients,
                                      List<String> excludeIngredients, String type, String instructions) {

        List<RecipeData> allRecipes;

        log.info("Building search criteria");
        Specification<RecipeData> spec = Specification.where(getRecipeByIdNotNull());

        if (!StringUtils.isEmpty(name)) {
            spec = spec.and(getByName(name));
        }
        if (!Objects.isNull(serving) && serving > 0) {
            spec = spec.and(getByServing(serving));
        }
        if (!StringUtils.isEmpty(type)) {
            spec = spec.and(getByType(type));
        }
        if (!StringUtils.isEmpty(instructions)) {
            spec = spec.and(getByInstructions(instructions));
        }
        if (!CollectionUtils.isEmpty(includeIngredients)) {
            String include = String.join(", ", includeIngredients);
            spec = spec.and(getByIncludeIngredients(include));
        }
        if (!CollectionUtils.isEmpty(excludeIngredients)) {
            String exclude = String.join(", ", excludeIngredients);
            spec = spec.and(getByExcludeIngredients(exclude));
        }

        allRecipes = recipeRepository.findAll(spec);
        log.debug("total recipes: " + allRecipes.size());

        List<Recipe> recipesList = allRecipes.stream().map(CommonUtil::mapToRecipeModel).collect(Collectors.toList());
        log.debug("total mapped recipes: " + recipesList.size());

        return recipesList;
    }

    public Recipe updateRecipe(Recipe recipe) {
        log.info("updating recipe with recipeId: " + recipe.getId());

        return createRecipe(recipe);
    }

    public void deleteRecipeFromRepository(Integer id) {
        log.info("deleting recipe with recipeId: " + id);

        recipeRepository.deleteById(id);
    }
}
