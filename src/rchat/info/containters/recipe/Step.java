package rchat.info.containters.recipe;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class Step {
    private String step;
    private List<String> images;

    public Step(String step, List<String> images) {
        this.step = step;
        this.images = images;
    }

    public Step(JSONObject o) {
        step = o.getString("step");
        JSONArray array = o.getJSONArray("images");
        for (Object a : array) {
            images.add((String) a);
        }
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public JSONObject createJSON() {
        JSONObject res = new JSONObject();
        res.put("step", step);
        JSONArray array = new JSONArray();
        for (String img : images) {
            array.put(img);
        }
        res.put("images", array);
        return res;
    }

    @Override
    public String toString() {
        return "Step{" +
                "step='" + step + '\'' +
                ", images=" + images +
                '}';
    }
}
