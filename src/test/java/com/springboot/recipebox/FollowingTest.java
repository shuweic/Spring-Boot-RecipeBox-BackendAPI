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
public class FollowingTest
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
    public void followUserbyUsernameThenUnfollowUser() throws JSONException
    {
        String randomUsername1 = "users";
        String randomUsername2 = "test1";
        String CurrentUserfollowOtherUserURL = "/api/users/" + randomUsername1 + "/follow/" + randomUsername2;
        String CurrentUserUnfollowOtherUserURL = "/api/users/" + randomUsername1 + "/followings/" + randomUsername2 + "/unfollow";


        Response response = RestAssured.given().
                header("Content-Type", "application/json").
//                body(comment.toString()).
                when().
                post(CurrentUserfollowOtherUserURL);
        int statusCode = response.getStatusCode();
        assertEquals(201, statusCode);

        response = RestAssured.given().
                header("Content-Type", "application/json").
//                body(comment.toString()).
                when().
                delete(CurrentUserUnfollowOtherUserURL);
        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

    }

}
