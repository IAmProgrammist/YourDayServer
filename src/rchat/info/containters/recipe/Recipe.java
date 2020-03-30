package rchat.info.containters.recipe;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class Recipe {
    private String imagePreview;
    private String name;
    private String desc;
    private List<String> ingredients;
    private List<Step> steps;

    public Recipe() {
    }

    public Recipe(String imagePreview, String name, String desc, List<String> ingredients, List<Step> steps) {
        this.imagePreview = imagePreview;
        this.name = name;
        this.desc = desc;
        this.ingredients = ingredients;
        this.steps = steps;
    }


    public Recipe(JSONObject o) {
        imagePreview = o.getString("imagePreview");
        name = o.getString("name");
        desc = o.getString("desc");
        JSONArray array = o.getJSONArray("ingredients");
        for (Object a : array) {
            ingredients.add((String) a);
        }
        array = o.getJSONArray("steps");
        for (Object a : array) {
            steps.add(new Step((JSONObject) a));
        }
    }

    public String getImagePreview() {
        return imagePreview;
    }

    public void setImagePreview(String imagePreview) {
        this.imagePreview = imagePreview;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public JSONObject createJSON() {
        JSONObject root = new JSONObject();
        root.put("imagePreview", imagePreview);
        root.put("name", name);
        root.put("desc", desc);
        JSONArray array = new JSONArray();
        for (String a : ingredients) {
            array.put(a);
        }
        root.put("ingredients", array);
        array = new JSONArray();
        for (Step step : steps) {
            array.put(step.createJSON());
        }
        root.put("steps", array);
        return root;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "imagePreview='" + imagePreview + '\'' +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", ingredients=" + ingredients +
                ", steps=" + steps +
                '}';
    }
}
