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
public class UserTest {
    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
//        RestAssured.baseURI = "http://coms-309-027.class.las.iastate.edu";
        RestAssured.baseURI = "http://localhost";
    }


    @Test
    public void UserSignupThenLoginAndUpdate() throws JSONException {

        JSONObject user = new JSONObject();
        String randomUsername = "Junit";
        try {
            user.put("username", randomUsername);
            user.put("password", "123");
            user.put("user_type", 1);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        // Initialize random username because a user is required for using pantry
        Response response = RestAssured.given().
                header("Content-Type", "application/json").
                body(user.toString()).
                when().
                post("/user/signup");

        int statusCode = response.getStatusCode();

        if (statusCode != 201)
            throw new RuntimeException("Error signing in random username: " + randomUsername);

        response = RestAssured.given().
                header("Content-Type", "application/json").
                body(user.toString()).
                when().
                post("/user/login");

        statusCode = response.getStatusCode();

        if (statusCode != 200)
            throw new RuntimeException("Error signing in random username: " + randomUsername);


        // update
        response = RestAssured.given().
                header("Content-Type", "application/json").
                body(user.toString()).
                when().
                put("/user/editAccount/Junit");

        statusCode = response.getStatusCode();

        if (statusCode != 200)
            throw new RuntimeException("Error signing in random username: " + randomUsername);


    }

}
