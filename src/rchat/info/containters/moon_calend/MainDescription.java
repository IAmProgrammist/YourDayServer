package rchat.info.containters.moon_calend;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class MainDescription {
    public String date;
    public int imgType;
    public String moonName;
    public String phaseName;
    public String dawn;
    public String sunset;
    public String visibility;
    public String fromAndTo;

    public MainDescription(JSONObject o) throws JSONException {
        date = o.getString("date");
        imgType = o.getInt("imgType");
        moonName = o.getString("moonName");
        phaseName = o.getString("phaseName");
        dawn = o.getString("dawn");
        sunset = o.getString("sunset");
        visibility = o.getString("visibility");
        fromAndTo = o.getString("fromAndTo");
    }

    public JSONObject createJSON() throws JSONException {
        JSONObject result = new JSONObject();
        result.put("date", date);
        result.put("imgType", imgType);
        result.put("moonName", moonName);
        result.put("phaseName", phaseName);
        result.put("dawn", dawn);
        result.put("sunset", sunset);
        result.put("visibility", visibility);
        result.put("fromAndTo", fromAndTo);
        return result;
    }

    public MainDescription(String date, int imgType, String moonName, String phaseName, String dawn, String sunset, String visibility, String fromAndTo) {
        this.date = date;
        this.imgType = imgType;
        this.moonName = moonName;
        this.phaseName = phaseName;
        this.dawn = dawn;
        this.sunset = sunset;
        this.visibility = visibility;
        this.fromAndTo = fromAndTo;
    }
}
