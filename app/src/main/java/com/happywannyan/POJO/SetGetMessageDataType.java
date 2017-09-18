package com.happywannyan.POJO;

import org.json.JSONObject;

/**
 * Created by apple on 04/08/17.
 */

public class SetGetMessageDataType {
    JSONObject jsonObject;
    boolean scrooll=false;

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public boolean isScrooll() {
        return scrooll;
    }

    public void setScrooll(boolean scrooll) {
        this.scrooll = scrooll;
    }
}
