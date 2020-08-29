package rchat.info.containters;

import org.json.JSONObject;

public class Fact {
    String fact;

    public Fact(String fact) {
        this.fact = fact;
    }

    public Fact(JSONObject o) {
        this.fact = o.getString("fact");
    }

    public String getFact() {
        return fact;
    }

    public void setFact(String fact) {
        this.fact = fact;
    }

    public JSONObject createJSON() {
        JSONObject object = new JSONObject();
        object.put("fact", fact);
        return object;
    }
}
