package com.abna.recipe.repository;

import com.abna.recipe.entity.RecipeData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<RecipeData,Integer>, JpaSpecificationExecutor<RecipeData> {

}
