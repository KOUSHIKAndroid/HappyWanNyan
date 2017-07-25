package com.happywannyan.POJO;

import org.json.JSONObject;

/**
 * Created by su on 5/22/17.
 */

public class PetService {

    String id;

    JSONObject jsondata;


    public JSONObject getJsondata() {
        return jsondata;
    }

    public void setJsondata(JSONObject jsondata) {
        this.jsondata = jsondata;
    }

    public boolean isTick_value() {
        return tick_value;
    }

    public void setTick_value(boolean tick_value) {
        this.tick_value = tick_value;
    }

    boolean tick_value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getTooltip_name() {
        return tooltip_name;
    }

    public void setTooltip_name(String tooltip_name) {
        this.tooltip_name = tooltip_name;
    }



    public String getDefault_image() {
        return default_image;
    }

    public void setDefault_image(String default_image) {
        this.default_image = default_image;
    }

    public String getSelected_image() {
        return selected_image;
    }

    public void setSelected_image(String selected_image) {
        this.selected_image = selected_image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    String name;
    String tooltip_name;
    String default_image;
    String selected_image;
    String status;
}
