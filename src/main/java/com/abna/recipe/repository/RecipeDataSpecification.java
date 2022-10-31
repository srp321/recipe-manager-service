package com.abna.recipe.repository;

import com.abna.recipe.entity.RecipeData;
import org.springframework.data.jpa.domain.Specification;

public class RecipeDataSpecification {

    public static Specification<RecipeData> getRecipeByIdNotNull() {
        return (root, criQuery, criteriaBuilder) -> criteriaBuilder.isNotNull(
                root.get("id"));
    }

    public static Specification<RecipeData> getByName(String name) {
        return (root, criQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<RecipeData> getByServing(Integer serving) {
        return (root, criQuery, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("serving"), serving - 1);
    }

    public static Specification<RecipeData> getByType(String type) {
        return (root, criQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("type"), "%" + type + "%");

    }

    public static Specification<RecipeData> getByInstructions(String instructions) {
        return (root, criQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("instructions"), "%" + instructions + "%");
    }

    public static Specification<RecipeData> getByIncludeIngredients(String include) {
        return (root, criQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("ingredients"), "%" + include + "%");
    }

    public static Specification<RecipeData> getByExcludeIngredients(String exclude) {
        return (root, criQuery, criteriaBuilder) -> criteriaBuilder.notLike(root.get("ingredients"), "%" + exclude + "%");
    }
}
