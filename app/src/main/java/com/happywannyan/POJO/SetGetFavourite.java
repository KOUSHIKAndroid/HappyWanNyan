package com.happywannyan.POJO;

import org.json.JSONObject;

/**
 * Created by su on 6/26/17.
 */

public class SetGetFavourite {

    boolean checkRightValue;

JSONObject dataObject;
    public boolean isCheckRightValue() {
        return checkRightValue;
    }

    public void setCheckRightValue(boolean checkRightValue) {
        this.checkRightValue = checkRightValue;
    }

    public JSONObject getDataObject() {
        return dataObject;
    }

    public void setDataObject(JSONObject dataObject) {
        this.dataObject = dataObject;
    }
}
