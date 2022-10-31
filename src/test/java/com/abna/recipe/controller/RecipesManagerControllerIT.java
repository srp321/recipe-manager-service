package com.abna.recipe.controller;

import com.abna.recipe.entity.RecipeData;
import com.abna.recipe.exceptions.ErrorMessages;
import com.abna.recipe.models.ErrorResponse;
import com.abna.recipe.models.Recipe;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class RecipesManagerControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String buildApiPath(String endPoint) {
        String baseURL = "http://localhost:" + port;
        return baseURL + endPoint;
    }

    @Test
    void WhenTriedToRequestResourceWithInvalidRecipeId_ThenResponseIsNotFound_Test() {

        String apiPath = buildApiPath("/api/v1/recipe/10");
        HttpHeaders headers = new HttpHeaders();

        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<ErrorResponse> getResponse = restTemplate.exchange(apiPath, HttpMethod.GET, request, ErrorResponse.class);

        assertThat(getResponse.getStatusCode()).as("Http Status is not as expected").isEqualTo(HttpStatus.NOT_FOUND);

        assertThat(getResponse.getBody().getMessage()).as("Error message returned in response entity is not as expected")
                .contains(ErrorMessages.RECIPE_NOT_FOUND);
    }

    @Test
    void GivenRecipeWithNulls_WhenPosted_ThenResponseIsBadRequest_Test() {

        String apiPath = buildApiPath("/api/v1/recipe/");
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<RecipeData> request = new HttpEntity<>(new RecipeData(), headers);

        ResponseEntity<ErrorResponse> errorResponse = restTemplate.postForEntity(apiPath, request, ErrorResponse.class);

        assertThat(errorResponse.getStatusCode()).as("Http Status is not as expected").isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(errorResponse.getBody().getMessage()).as("Error message returned in response entity is not as expected")
                .contains(ErrorMessages.BAD_REQUEST);
    }

    @Test
    void GivenValidRecipeID_WhenRequested_ThenResponseIsOK() {

        String apiPath = buildApiPath("/api/v1/recipe/100");
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Recipe> request = new HttpEntity<>(new Recipe(), headers);

        ResponseEntity<Recipe> getResponse = restTemplate.exchange(apiPath, HttpMethod.GET, request, Recipe.class);

        assertThat(getResponse.getStatusCode()).as("Http Status is not as expected").isEqualTo(HttpStatus.OK);

    }

    @Test
    void GivenNoRecipesInDB_WhenTriedToRequestAllRecipes_ThenResponseIsNotFound_Test() {

        String apiPath = buildApiPath("/api/v1/recipes");
        HttpHeaders headers = new HttpHeaders();

        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<ErrorResponse> getResponse = restTemplate.exchange(apiPath, HttpMethod.GET, request, ErrorResponse.class);

        assertThat(getResponse.getStatusCode()).as("Http Status is not as expected").isEqualTo(HttpStatus.NOT_FOUND);

        assertThat(getResponse.getBody().getMessage()).as("Error message returned in response entity is not as expected")
                .contains(ErrorMessages.RECIPES_NOT_FOUND);
    }

    @Test
    void GivenRecipeNotPresentInDB_WhenTriedToDeleteRecipe_ThenResponseIsNotFound_Test() {

        String apiPath = buildApiPath("/api/v1/recipe/10");
        HttpHeaders headers = new HttpHeaders();
        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<ErrorResponse> getResponse = restTemplate.exchange(apiPath, HttpMethod.DELETE, request, ErrorResponse.class);

        assertThat(getResponse.getStatusCode()).as("Http Status is not as expected").isEqualTo(HttpStatus.NOT_FOUND);

        assertThat(getResponse.getBody().getMessage()).as("Error message returned in response entity is not as expected")
                .contains(ErrorMessages.RECIPE_NOT_FOUND);
    }

}
