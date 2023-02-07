package com.springboot.recipebox.helper;

import com.fasterxml.jackson.databind.JsonNode;
import com.springboot.recipebox.payload.RecipeDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

public class APIHelper {
    public static final String APIKey = "bbb351c2f5c64ca5ba36822de01d7d5c";

    public static String RandomSearchURL = "https://api.spoonacular.com/recipes/random?number=5&apiKey=" + APIHelper.APIKey;

    public static String getIngredientSearchURL(String searchStr)
    {
        String url = "https://api.spoonacular.com/food/ingredients/autocomplete?query="
                + searchStr + "&number=5&metaInformation=true&apiKey=" + APIKey;
       return url;
    }

    public static RecipeDTO parseJsonNodeToRecipe(JsonNode node)
    {
        RecipeDTO recipeDTO = new RecipeDTO();

        recipeDTO.setTitle(node.get("title").asText());
        recipeDTO.setSummary(node.path("summary").asText());
        recipeDTO.setServings(node.path("servings").asInt());

        // instructions list
        String instructionsNode = node.path("instructions").asText();
        ArrayList<String> instructionsList = new ArrayList<>();


//        String html = "<li><span class=\"foo\"><img src=\"bar\" class=\"img\"></span><span class=\"bar\">Collins</span><i class=\"baz1\"><img src=\"baz1\" class=\"img\"></i><i class=\"baz2\"><img src=\"baz2\" class=\"img\"></i><i class=\"baz3\"><img src=\"baz3\" class=\"img\"></i></li>";
//
//        Document document = Jsoup.parse(html);
//
//        Element element = document.selectFirst("li");
//        element.children().forEach(child -> {
//            // do your processing here - this is just an example:
//            if (child.hasText()) {
//                System.out.println(child.text());
//            } else {
//                System.out.println(child.html());
//            }
//        });

        Document document = Jsoup.parse(instructionsNode);
        Element element = document.selectFirst("body");

        element.children().forEach(child ->
        {
            if (child.hasText())
            {
                instructionsList.add(child.text());
            }
        });

        recipeDTO.setInstructionsList(instructionsList);

        // Note: May be -1 if no info from API
        recipeDTO.setCookingMinutes(node.path("cookingMinutes").asInt());

        // Look later because returns array type, can be from multiple cuisines
        // may be null from API results
//        if(node.path("cuisines").isArray())
//            recipeDTO.setCuisineType("");
//
//        // same for course type ^^ look into this!
//        if(node.path("dishType").isArray())
//            recipeDTO.setDishType("");

        return recipeDTO;
    }
}