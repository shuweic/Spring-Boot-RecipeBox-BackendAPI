package com.springboot.recipebox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.recipebox.payload.IngredientDTO;
import com.springboot.recipebox.payload.ResponseMessageDTO;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

@RunWith(SpringRunner.class)
public class RecipeTest
{
    @LocalServerPort
    int port;

    @Before
    public void setUp()
    {
        RestAssured.port = port;
//        RestAssured.baseURI = "http://coms-309-027.class.las.iastate.edu";
        RestAssured.baseURI = "http://localhost";
    }

    int recipeID;

    @Test
    public void recipeCreateThenUpdateThenDeleteTest() throws JSONException, JsonProcessingException {
        // used for testing data already in DB with no existing data about recipe
        String username = "John9287";
        String recipeName = "Sisig";

        // create recipe
        Response response = RestAssured.given().
                header("Content-Type", "application/json").
                queryParams("recipeName", recipeName).
                when().
                post("/recipe-management/users/" + username + "/recipe");

        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(201, statusCode); // created

        recipeID = Integer.parseInt(response.getBody().asString());

        // GET request to check if added successfully
        response = RestAssured.given().
                header("Content-Type", "application/json").
                when().
                get("/recipe-management/recipes/" + recipeID);

        // Check status code
        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String returnString = response.getBody().asString();
        JSONObject jsonObject = new JSONObject(returnString);

        assertEquals(recipeName, jsonObject.get("title"));

        // --------------------------------------------------------
        // Now, update general info, ingredients, and instructions
        // --------------------------------------------------------
        String newRecipeName = "Pork Sisig";
        int servings = 5;
        int cookingTime = 20;
        String summary = "Kampampangan dish";

        ArrayList<String> tags = new ArrayList<>();
        tags.add("Dinner");
        tags.add("Main Course");

        JSONArray paramIngredients = new JSONArray("[\n" +
                "   \n" +
                "    {\n" +
                "        \"name\": \"salt and pepper\",\n" +
                "        \"amount\": 9,\n" +
                "        \"unit\": \"grams\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"name\": \"salt pork\",\n" +
                "        \"amount\": 8,\n" +
                "        \"unit\": \"pieces\"\n" +
                "    }\n" +
                "]");

        ArrayList<String> instructions = new ArrayList<>();
        instructions.add("Boil pork");
        instructions.add("Cut pork");

        response = RestAssured.given().
                header("Content-Type", "application/json").
                queryParams("recipeName", newRecipeName).
                queryParams("servings", servings).
                queryParams("cookingTime", cookingTime).
                queryParams("summary", summary).
                queryParams("tags", new JSONArray(tags).toString()).
                when().
                put("/recipe-management/recipes/" + recipeID + "/update-general-info");

        // Check status code
        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        response = RestAssured.given().
                header("Content-Type", "application/json").
                queryParams("ingredients", paramIngredients.toString()).
                when().
                put("/recipe-management/recipes/" + recipeID + "/update-recipe-ingredients");

        // Check status code
        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        response = RestAssured.given().
                header("Content-Type", "application/json").
                queryParams("instructions", new JSONArray(instructions).toString()).
                when().
                put("/recipe-management/recipes/" + recipeID + "/update-recipe-instructions");

        // Check status code
        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // --------------------------------------------------------
        // Then, check if recipe info matches
        // --------------------------------------------------------

        // GET request to check if added successfully
        response = RestAssured.given().
                header("Content-Type", "application/json").
                when().
                get("/recipe-management/recipes/" + recipeID);

        // Check status code
        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        JSONObject recipeObject = new JSONObject(response.getBody().asString());

        assertEquals(newRecipeName, recipeObject.get("title"));
        assertEquals(summary, recipeObject.get("summary"));
        assertEquals(cookingTime, recipeObject.get("cookingMinutes"));
        assertEquals(servings, recipeObject.get("servings"));

        JSONArray jsonArr = new JSONArray(recipeObject.getString("instructionsList"));
        for (int i = 0; i < jsonArr.length(); i++)
        {
            assertEquals(instructions.get(i), jsonArr.getString(i));
        }

        jsonArr = new JSONArray(recipeObject.getString("tags"));

        for (int i = 0; i < jsonArr.length(); i++)
        {
            assertEquals(tags.get(i), jsonArr.getString(i));
        }

        jsonArr = new JSONArray(recipeObject.getString("ingredientList"));

        for (int i = 0; i < jsonArr.length(); i++)
        {
            assertEquals(paramIngredients.getJSONObject(i).get("name"), jsonArr.getJSONObject(i).get("ingredientName"));
            assertEquals(paramIngredients.getJSONObject(i).get("amount"), jsonArr.getJSONObject(i).get("amount"));
            assertEquals(paramIngredients.getJSONObject(i).get("unit"), jsonArr.getJSONObject(i).get("unit"));
        }

        // GET request to check if added successfully
        response = RestAssured.given().
                header("Content-Type", "application/json").
                queryParams("tagName", "Main Course").
                when().
                get("/tag-management/users/" + username + "/recipes/tag-search");

        // Check status code
        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // --------------------------------------------------------
        // From this point, everything about recipe creation works,
        // so test deleting a recipe
        // --------------------------------------------------------

        // delete the created tags

        for (int i = 1, j = 0; i >=0; i--, j++)
        {
            Response r = RestAssured.given().
                    header("Content-Type", "application/json").
                    queryParams("tagName", tags.get(j)).
                    when().
                    delete("/tag-management/users/" + username + "/tag");

            // Check status code
            int sc = r.getStatusCode();
            assertEquals(200, sc);

            // make a GET request of rating on recipe and make sure it's now empty
            r = RestAssured.given().
                    header("Content-Type", "application/json").
                    when().
                    get("/tag-management/users/" + username + "/tags");

            // Check status code
            sc = r.getStatusCode();
            assertEquals(200, sc);

            // compare response. should just be empty
            JSONArray returnArr;
            try {
                returnArr = new JSONArray(r.getBody().asString());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            // check if returned ingredients is only none
            assertEquals(i, returnArr.length());
        }

        // finally, delete recipe
        response = RestAssured.given().
                header("Content-Type", "application/json").
                when().
                delete("/recipe-management/recipes/" + recipeID);

        // Check status code
        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // make a GET request of recipe and make sure it's now empty
        response = RestAssured.given().
                header("Content-Type", "application/json").
                when().
                get("/recipe-management/recipes/" + recipeID);

        // Check status code; should be an error because id is not valid anymore
        statusCode = response.getStatusCode();
        assertEquals(500, statusCode);

    }
}
