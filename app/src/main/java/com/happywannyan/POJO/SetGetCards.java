package com.happywannyan.POJO;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by su on 9/7/17.
 */

public class SetGetCards {
    String user_email, communication_messege_email;
    JSONArray JsonArrayCardDetails;
    ArrayList<SetGetStripData> stripDataArrayList;

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getCommunication_messege_email() {
        return communication_messege_email;
    }

    public void setCommunication_messege_email(String communication_messege_email) {
        this.communication_messege_email = communication_messege_email;
    }

    public JSONArray getJsonArrayCardDetails() {
        return JsonArrayCardDetails;
    }

    public void setJsonArrayCardDetails(JSONArray jsonArrayCardDetails) {
        JsonArrayCardDetails = jsonArrayCardDetails;
    }

    public ArrayList<SetGetStripData> getStripDataArrayList() {
        return stripDataArrayList;
    }

    public void setStripDataArrayList(ArrayList<SetGetStripData> stripDataArrayList) {
        this.stripDataArrayList = stripDataArrayList;
    }
}
