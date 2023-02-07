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
public class CommentTest
{
    @LocalServerPort
    int port;

    @Before
    public void setUp()
    {
        RestAssured.port = port;
        //        RestAssured.baseURI = "http://coms-309-027.class.las.iastate.edu";
        RestAssured.baseURI = "http://localhost";   }

    @Test
    public void createThenUpdateAndDeleteComment() throws Exception
    {
        String randomRecipeId = "2";
        String createCommentURL = "/api/recipes/" + randomRecipeId + "/comments";

        JSONObject comment = new JSONObject();
        try
        {
            comment.put("body", "test comment content");
            comment.put("name", "test commnet title");
        }
        catch (JSONException e)
        {
            throw new RuntimeException(e);
        }

        Response response = RestAssured.given().
                header("Content-Type", "application/json").
                body(comment.toString()).
                when().
                post(createCommentURL);

        int statusCode = response.getStatusCode();
        assertEquals(201, statusCode);

        String createCommentReturn = response.getBody().asString();
        Object TestCommentId = new JSONObject(createCommentReturn).get("id");

        response = RestAssured.given().
                header("Content-Type", "application/json").
                when().
                get(createCommentURL+"/"+TestCommentId);

        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);


        // Update Comment method testing
        JSONObject updateComment = new JSONObject();
        try
        {
            updateComment.put("body", "test update for demo4 testing");
            updateComment.put("name", "test update test case title");
        }
        catch (JSONException e)
        {
            throw new RuntimeException(e);
        }

        response = RestAssured.given().
                header("Content-Type", "application/json").
                body(updateComment.toString()).
                when().
                put(createCommentURL+"/"+TestCommentId);


        String updateCommentReturn = response.getBody().asString();
        Object updateCommentTitle = new JSONObject(updateCommentReturn).get("name");

        assertEquals("test update test case title", updateCommentTitle);
        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);



        // delete comment method testing
        response = RestAssured.given().
                header("Content-Type", "application/json").
                when().
                delete(createCommentURL+"/"+TestCommentId);

        try {
            statusCode = response.getStatusCode();
            assertEquals(200, statusCode);
        } catch (Exception e) {
            System.out.println(createCommentURL+"/"+TestCommentId);
        }
    }

}
