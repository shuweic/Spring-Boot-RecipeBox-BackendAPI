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
public class ReportTest
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
    public void createThenUpdateAndDeleteReport() throws JSONException
    {
        String randomUsername = "users";
        String createReportURL = "/api/" + randomUsername + "/recipe/report";
        int randomRecipeId = 115;

        JSONObject report = new JSONObject();
        try
        {
            report.put("title", "test report title1");
            report.put("detail", "test report detail");
            report.put("recipeID", randomRecipeId);
        }
        catch (JSONException e)
        {
            throw new RuntimeException(e);
        }

        Response response = RestAssured.given().
                header("Content-Type", "application/json").
                body(report.toString()).
                when().
                post(createReportURL);

        int statusCode = response.getStatusCode();
        assertEquals(201, statusCode);


        String getAllReportByIDURL = "/api/recipes/"+randomRecipeId+"/reports";
        response = RestAssured.given().
                header("Content-Type", "application/json").
                when().
                get(getAllReportByIDURL);

        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);




        // delete
        String getReturnReport = response.getBody().asString();
        JSONArray getReturnReportJSON = new JSONArray(getReturnReport);

        Object returnReportId = getReturnReportJSON.getJSONObject(0).get("reportID");

        String TestReportId = returnReportId.toString();

        String deleteReportURL = "/api/recipes/"+randomRecipeId+"/reports/"+TestReportId;


        response = RestAssured.given().
                header("Content-Type", "application/json").
                when().
                get(deleteReportURL);

        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);


        // update admin


        response = RestAssured.given().
                header("Content-Type", "application/json").
                body(report.toString()).
                when().
                put(deleteReportURL+"/admin");

        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        response = RestAssured.given().
                header("Content-Type", "application/json").
                body(report.toString()).
                when().
                put(deleteReportURL+"/user");

        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        response = RestAssured.given().
                header("Content-Type", "application/json").
                when().
                delete(deleteReportURL);

        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);




    }

}
