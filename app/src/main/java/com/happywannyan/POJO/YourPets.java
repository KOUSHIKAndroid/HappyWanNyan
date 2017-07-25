package com.happywannyan.POJO;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by su on 5/26/17.
 */

public class YourPets {
    String edit_id,pet_type_id,pet_name,pet_image;
    JSONObject Otherinfo;

    public String getEdit_id() {
        return edit_id;
    }

    public void setEdit_id(String edit_id) {
        this.edit_id = edit_id;
    }

    public String getPet_type_id() {
        return pet_type_id;
    }

    public void setPet_type_id(String pet_type_id) {
        this.pet_type_id = pet_type_id;
    }

    public String getPet_name() {
        return pet_name;
    }

    public void setPet_name(String pet_name) {
        this.pet_name = pet_name;
    }

    public String getPet_image() {
        return pet_image;
    }

    public void setPet_image(String pet_image) {
        this.pet_image = pet_image;
    }

    public JSONObject getOtherinfo() {
        return Otherinfo;
    }

    public void setOtherinfo(JSONObject otherinfo) {
        Otherinfo = otherinfo;
    }
}
