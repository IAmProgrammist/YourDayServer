package rchat.info.containters.day;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class Day {
    private Date date;
    private boolean isWeekend;
    private String presentName;
    private Present present;
    private String presentChurchName;

    public Day(JSONObject object) throws JSONException {
        date = new Date(object.getLong("long"));
        isWeekend = object.getBoolean("isWeekend");
        presentName = object.getString("presentName");
        present = new Present(object.getJSONObject("present"));
        presentChurchName = object.getString("presentChurchName");
    }

    public Day(Date date, boolean isWeekend, Present present, String presentChurchName) {
        this.date = date;
        this.isWeekend = isWeekend;
        this.presentName = present.name;
        this.present = present;
        this.presentChurchName = presentChurchName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isWeekend() {
        return isWeekend;
    }

    public void setWeekend(boolean weekend) {
        isWeekend = weekend;
    }

    public String getPresentName() {
        return presentName;
    }

    public void setPresentName(String presentName) {
        this.presentName = presentName;
    }

    public String getPresentChurchName() {
        return presentChurchName;
    }

    public void setPresentChurchName(String presentChurchName) {
        this.presentChurchName = presentChurchName;
    }

    public Present getPresent() {
        return present;
    }

    public void setPresent(Present present) {
        this.present = present;
    }

    public JSONObject createJSON() {
        JSONObject result = new JSONObject();
        result.put("long", date.getTime());
        result.put("isWeekend", isWeekend);
        result.put("presentName", presentName);
        result.put("presentChurchName", presentChurchName);
        result.put("present", present.createJSON());
        return result;
    }

}
