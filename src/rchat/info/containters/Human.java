package rchat.info.containters;

import org.json.JSONObject;

public class Human {
    String name;
    String description;
    String imageUrl;

    public Human(String name, String description, String imageUrl) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public Human(JSONObject o) {
        name = o.getString("name");
        description = o.getString("description");
        imageUrl = o.getString("imageUrl");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public JSONObject createJSON() {
        JSONObject a = new JSONObject();
        a.put("name", name);
        a.put("description", description);
        a.put("imageUrl", imageUrl);
        return a;
    }

    @Override
    public String toString() {
        return "Human{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
