package rchat.info.containters;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Omen {
    public List<String> omens = new ArrayList<>();

    public Omen(List<String> omens) {
        this.omens = omens;
    }

    public Omen(JSONObject o) {
        JSONArray array = o.getJSONArray("omens");
        for (int i = 0; i < array.length(); i++) {
            omens.add(array.getString(i));
        }
    }

    public JSONObject createJSON() {
        JSONArray array = new JSONArray();
        for (String a : omens) {
            array.put(a);
        }
        JSONObject res = new JSONObject();
        res.put("omens", array);
        return res;
    }
}
