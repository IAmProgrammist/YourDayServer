package rchat.info.containters.moon_calend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import rchat.info.containters.day.Description;

import java.util.List;

public class Tip {
    public String heading;
    public String descritpion;
    public TipTypes type;
    public List<Description> fullDesc;

    public Tip(String heading, String descritpion, List<Description> fullDesc, TipTypes type) {
        this.heading = heading;
        this.descritpion = descritpion;
        this.fullDesc = fullDesc;
        this.type = type;
    }

    public Tip(JSONObject o) throws JSONException {
        heading = o.getString("heading");
        descritpion = o.getString("description");
        JSONArray array = o.getJSONArray("fullDesc");
        for (int i = 0; i < array.length(); i++) {
            fullDesc.add(new Description(array.getJSONObject(i)));
        }
        type = TipTypes.valueOf(o.getString("type"));
    }

    public JSONObject createJSON() throws JSONException {
        JSONObject result = new JSONObject();
        result.put("heading", heading);
        result.put("description", descritpion);
        JSONArray array = new JSONArray();
        for (Description a : fullDesc) {
            array.put(a.createJSON());
        }
        result.put("fullDesc", array);
        result.put("type", type);
        return result;
    }
}
