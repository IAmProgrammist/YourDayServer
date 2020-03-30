package rchat.info.containters.recipe;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewRecipes {
    List<NewRecipe> recipes = new ArrayList<>();

    public NewRecipes() {
    }

    public void addRecipe(String name, String url, String everything, NewRecipeType type) {
        recipes.add(new NewRecipe(name, url, everything, type));
    }

    public JSONObject createJSON() {
        JSONArray array = new JSONArray();
        for (NewRecipe a : recipes) {
            array.put(a.createObject());
        }
        JSONObject res = new JSONObject();
        res.put("recipes", array);
        return res;
    }
}
