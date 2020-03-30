package rchat.info.containters.day;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Present {
    public String name;
    List<Description> description = new ArrayList<>();

    public Present(String name, List<Description> description) {
        this.name = name;
        this.description = description;
    }

    public Present(String name) {
        this.name = name;
        description.add(new Description());
    }

    public Present(JSONObject o) {
        name = o.getString("name");
        JSONArray array = o.getJSONArray("description");
        for (int i = 0; i < array.length(); i++) {
            description.add(new Description(array.getJSONObject(i)));
        }
    }

    public JSONObject createJSON() {
        JSONObject o = new JSONObject();
        o.put("name", name);
        JSONArray array = new JSONArray();
        for (Description d : description) {
            array.put(d.createJSON());
        }
        o.put("description", array);
        return o;
    }
}
