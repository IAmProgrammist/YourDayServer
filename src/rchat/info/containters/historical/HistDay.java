package rchat.info.containters.historical;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HistDay {
    public List<HistEvent> histEvents = new ArrayList<>();

    public HistDay() {
    }

    public void addEvent(HistEvent event){
        histEvents.add(event);
    }

    public JSONObject createJSON(){
        JSONObject root = new JSONObject();
        JSONArray array = new JSONArray();
        for(HistEvent e: histEvents){
            array.put(e.createJSON());
        }
        root.put("histEvents", array);
        return root;
    }
}
