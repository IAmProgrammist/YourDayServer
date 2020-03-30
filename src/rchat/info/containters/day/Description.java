package rchat.info.containters.day;

import org.json.JSONObject;

public class Description {
    Type type;
    String desc;

    public Description(Type type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public Description() {
        type = Type.NO_INFO;
        desc = "NO_INFO";
    }

    public Description(JSONObject jsonObject) {
        type = Type.valueOf(jsonObject.getString("type"));
        desc = jsonObject.getString("desc");
    }

    public JSONObject createJSON() {
        JSONObject o = new JSONObject();
        o.put("type", type);
        o.put("desc", desc);
        return o;
    }
}
