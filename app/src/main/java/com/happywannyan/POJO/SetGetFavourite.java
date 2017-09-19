package com.happywannyan.POJO;

import org.json.JSONObject;

/**
 * Created by su on 6/26/17.
 */

public class SetGetFavourite {

    String tagName = "";

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    JSONObject dataObject;

    public JSONObject getDataObject() {
        return dataObject;
    }

    public void setDataObject(JSONObject dataObject) {
        this.dataObject = dataObject;
    }
}
