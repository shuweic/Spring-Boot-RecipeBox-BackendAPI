package com.springboot.recipebox;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

@RunWith(SpringRunner.class)
public class PantryTest
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

    @Test
    public void addingThenDeletingIngredients() throws JSONException
    {
        String randomUsername = "John9287";
//
//        JSONObject user = new JSONObject();
//        try
//        {
//            user.put("username", randomUsername);
//            user.put("password", "123");
//            user.put("user_type", 1);
//        }
//        catch (JSONException e)
//        {
//            throw new RuntimeException(e);
//        }
//
//        // Initialize random username because a user is required for using pantry
//        Response response = RestAssured.given().
//                header("Content-Type", "application/json").
//                body(user.toString()).
//                when().
//                post("/user/signup");
//
//        int statusCode = response.getStatusCode();
//
//        if (statusCode != 201)
//            throw new RuntimeException("Error signing in random username: " + randomUsername);
//
        String addDeleteIngredientURL = "/ingredient-management/users/"
                + randomUsername + "/ingredients";

        // add ingredient to user pantry
        String ingredient = "newsalt1234";

        Response response = RestAssured.given().
                header("Content-Type", "application/json").
                queryParams("name", ingredient).
                when().
                post(addDeleteIngredientURL);

        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(201, statusCode); // created

        // make a GET request of ingredients by user
        response = RestAssured.given().
                header("Content-Type", "application/json").
                when().
                get("/ingredient-management/users/" + randomUsername + "/ingredients");

        // Check status code
        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String returnString = response.getBody().asString();

        try
        {
            JSONArray returnArr = new JSONArray(returnString);

            // check if returned ingredients is only one
            assertEquals(1, returnArr.length());

            // check if ingredient is correct
            JSONObject returnObj = returnArr.getJSONObject(0);

            assertEquals(ingredient, returnObj.get("name"));
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

        // From this point, we know that add ingredient works
        // Now, we will test delete ingredient

        response = RestAssured.given().
                header("Content-Type", "application/json").
                queryParams("name", ingredient).
                when().
                delete(addDeleteIngredientURL);

        // Check status code
        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // make a GET request of ingredients by user
        response = RestAssured.given().
                header("Content-Type", "application/json").
                when().
                get("/ingredient-management/users/" + randomUsername + "/ingredients");

        // Check status code
        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // compare response. should just be empty
        JSONArray returnArr = new JSONArray(response.getBody().asString());

        // check if returned ingredients is only one
        assertEquals(0, returnArr.length());

    }

    @Test
    public void ingredientSearchTest() throws JSONException
    {
        // make a GET request of ingredients by user
        Response response = RestAssured.given().
                header("Content-Type", "application/json").
                when().
                get("/ingredient-search/patis");

        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String returnString = response.getBody().asString();

        JSONArray returnArr = new JSONArray(returnString);

        // check if returned ingredients is only one
        assertEquals(1, returnArr.length());

        // check if ingredient is correct
        JSONObject returnObj = returnArr.getJSONObject(0);

        assertEquals("fish sauce", returnObj.get("name"));

    }
}
