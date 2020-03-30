package rchat.info.containters;

import org.json.JSONObject;

public class Quote {
    String quote;
    String quoter;

    public Quote(String quote, String quoter) {
        this.quote = quote;
        this.quoter = quoter;
    }

    public Quote(JSONObject o) {
        quote = o.getString("quote");
        quoter = o.getString("quoter");
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public JSONObject createJSON() {
        JSONObject o = new JSONObject();
        o.put("quoter", quoter);
        o.put("quote", quote);
        return o;
    }

    public String getQuoter() {
        return quoter;
    }

    public void setQuoter(String quoter) {
        this.quoter = quoter;
    }
}
