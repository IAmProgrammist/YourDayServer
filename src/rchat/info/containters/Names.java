package rchat.info.containters;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class Names {
    List<String> names;

    public Names(List<String> names) {
        this.names = names;
    }

    public Names(JSONObject o) {
        JSONArray p = o.getJSONArray("names");
        for (int i = 0; i < p.length(); i++) {
            String k = p.getString(i);
            names.add(k);
        }
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public JSONArray createJSON() {
        JSONArray g = new JSONArray();
        for (String y : names) {
            g.put(y);
        }
        return g;
    }
}
