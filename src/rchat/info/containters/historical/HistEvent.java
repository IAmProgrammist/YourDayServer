package rchat.info.containters.historical;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class HistEvent {
    public String urlImage;
    public String date;
    public List<String> desc;

    public HistEvent(String urlImage, String date, List<String> desc) {
        this.urlImage = urlImage;
        this.date = date;
        this.desc = desc;
    }

    public JSONObject createJSON(){
        JSONObject root = new JSONObject();
        root.put("urlImage", urlImage);
        root.put("date", date);
        JSONArray desc = new JSONArray();
        for(String a: this.desc){
            desc.put(a);
        }
        root.put("desc", desc);
        return root;
    }
}
