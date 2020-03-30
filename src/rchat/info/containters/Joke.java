package rchat.info.containters;

import org.json.JSONObject;

public class Joke {
    public String joke;

    public Joke(String joke) {
        this.joke = joke;
    }

    public Joke(JSONObject o) {
        joke = o.getString("joke");
    }

    public JSONObject createJSON() {
        JSONObject object = new JSONObject();
        object.put("joke", joke);
        return object;
    }
}
