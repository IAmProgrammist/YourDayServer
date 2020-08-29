package rchat.info.containters;

import org.json.JSONObject;

public class Goroscope {
    private String sign;
    private String prediction;

    public Goroscope(JSONObject o) {
        this.sign = o.getString("sign");
        this.prediction = o.getString("prediction");
    }

    public Goroscope(String siqn, String prediction) {
        this.sign = siqn;
        this.prediction = prediction;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPrediction() {
        return prediction;
    }

    public void setPrediction(String prediction) {
        this.prediction = prediction;
    }

    public JSONObject createJSON() {
        JSONObject res = new JSONObject();
        res.put("sign", sign);
        res.put("prediction", prediction);
        return res;
    }

}
