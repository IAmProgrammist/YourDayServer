package rchat.info.containters.recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewRecipe {
    String name;
    String url;
    String everything;
    NewRecipeType type;

    public NewRecipe(String name, String url, String everything, NewRecipeType type1) {
        this.name = name;
        this.url = url;
        this.everything = everything;
        type = type1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEverything() {
        return everything;
    }

    public void setEverything(String everything) {
        this.everything = everything;
    }

    public JSONObject createObject() throws JSONException {
        JSONObject res = new JSONObject();
        res.put("name", name);
        res.put("url", url);
        res.put("everything", everything);
        res.put("type", type);
        return res;
    }

    public NewRecipe(JSONObject o) throws JSONException {
        name = o.getString("name");
        url = o.getString("url");
        everything = o.getString("everything");
        type = NewRecipeType.valueOf(o.getString("type"));
    }
}
