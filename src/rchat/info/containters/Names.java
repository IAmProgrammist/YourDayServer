package rchat.info.containters;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Names {
    public List<String> names;
    public List<String> girls;
    public List<String> boys;

    public Names(List<String> girls, List<String> boys) {
        this.girls = girls;
        this.boys = boys;
        names = new ArrayList<>();
        names.addAll(boys);
        names.addAll(girls);
    }

    public Names(JSONObject o) {
        names = new ArrayList<>();
        boys = new ArrayList<>();
        girls = new ArrayList<>();
        JSONArray p = o.getJSONArray("names");
        for (int i = 0; i < p.length(); i++) {
            String k = p.getString(i);
            names.add(k);
        }
        p = o.getJSONArray("boys");
        for (int i = 0; i < p.length(); i++) {
            String k = p.getString(i);
            boys.add(k);
        }
        p = o.getJSONArray("girls");
        for (int i = 0; i < p.length(); i++) {
            String k = p.getString(i);
            girls.add(k);
        }
    }

    public JSONObject createJSON() {
        JSONObject object = new JSONObject();
        JSONArray names = new JSONArray();
        for (String y : this.names) {
            names.put(y);
        }
        object.put("names", names);
        JSONArray boys = new JSONArray();
        for (String y : this.boys) {
            boys.put(y);
        }
        object.put("boys", boys);
        JSONArray girls = new JSONArray();
        for (String y : this.girls) {
            girls.put(y);
        }
        object.put("girls", girls);
        return object;
    }
}
