package com.abna.recipe;

import com.abna.recipe.controller.RecipesManagerController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RecipeManagerServiceApplicationTests {

    @Autowired
    private RecipesManagerController recipesManagerController;

    @Test
    void contextLoads() {
        assertThat(recipesManagerController).as("Application Context failed to load").isNotNull();
    }

}
