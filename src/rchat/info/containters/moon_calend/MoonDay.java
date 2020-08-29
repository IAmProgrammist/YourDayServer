package rchat.info.containters.moon_calend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import rchat.info.containters.day.Description;

import java.util.ArrayList;
import java.util.List;

public class MoonDay {
    public MainDescription description;
    public List<Tip> tips;

    public MoonDay(MainDescription description) {
        this.description = description;
        tips = new ArrayList<>();
    }

    public MoonDay(MainDescription description, List<Tip> tips) {
        this.description = description;
        this.tips = tips;
    }

    public MoonDay(JSONObject o) throws JSONException {
        description = new MainDescription(o.getJSONObject("description"));
        JSONArray array = o.getJSONArray("tips");
        for (int i = 0; i < array.length(); i++) {
            JSONObject t = array.getJSONObject(i);
            addTip(t);
        }
    }

    public void addTip(String name, String desc, List<Description> fullDesc, TipTypes type) {
        tips.add(new Tip(name, desc, fullDesc, type));
    }

    public void addTip(JSONObject o) throws JSONException {
        tips.add(new Tip(o));
    }

    public JSONObject createJSON() throws JSONException {
        JSONObject result = new JSONObject();
        result.put("description", description.createJSON());
        JSONArray array = new JSONArray();
        for (Tip a : tips) {
            array.put(a.createJSON());
        }
        result.put("tips", array);
        return result;
    }
}
