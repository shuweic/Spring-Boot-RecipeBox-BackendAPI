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
public class TagTest
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
    public void createUserTagThenDelete() throws JSONException
    {
        // used for testing data already in DB with no data related to rating
        String username = "John9287";
        String tag = "Ilocano";

        Response response = RestAssured.given().
                header("Content-Type", "application/json").
                queryParams("tagName", tag).
                when().
                post("/tag-management/users/" + username + "/tag");

        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(201, statusCode); // created

        // GET request to check if added successfully
        response = RestAssured.given().
                header("Content-Type", "application/json").
                when().
                get("/tag-management/users/" + username + "/tags");

        // Check status code
        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String returnString = response.getBody().asString();

        JSONArray returnArr = new JSONArray(returnString);

        // check if returned rating is only one
        assertEquals(1, returnArr.length());

        assertEquals(tag, returnArr.getString(0));

        // --------------------------------------------------------
        // From this point, we know that add rating works
        // Now, we will test delete rating

        response = RestAssured.given().
                header("Content-Type", "application/json").
                queryParams("tagName", tag).
                when().
                delete("/tag-management/users/" + username + "/tag");

        // Check status code
        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // make a GET request of rating on recipe and make sure it's now empty
        response = RestAssured.given().
                header("Content-Type", "application/json").
                when().
                get("/tag-management/users/" + username + "/tags");

        // Check status code
        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // compare response. should just be empty
        returnArr = new JSONArray(response.getBody().asString());

        // check if returned ingredients is only none
        assertEquals(0, returnArr.length());

    }
}
