package com.happywannyan.POJO;

import org.json.JSONObject;

/**
 * Created by su on 9/15/17.
 */

public class SetGetStripData {
    JSONObject jsonObjectUserStripeData;
    boolean isCheck = false;

    public JSONObject getJsonObjectUserStripeData() {
        return jsonObjectUserStripeData;
    }

    public void setJsonObjectUserStripeData(JSONObject jsonObjectUserStripeData) {
        this.jsonObjectUserStripeData = jsonObjectUserStripeData;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
