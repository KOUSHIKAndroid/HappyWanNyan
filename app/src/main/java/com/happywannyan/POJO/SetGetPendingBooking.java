package com.happywannyan.POJO;

/**
 * Created by su on 10/20/17.
 */

public class SetGetPendingBooking {
    String pet_id,pet_details;
    boolean isChecked;

    public String getPet_id() {
        return pet_id;
    }

    public void setPet_id(String pet_id) {
        this.pet_id = pet_id;
    }

    public String getPet_details() {
        return pet_details;
    }

    public void setPet_details(String pet_details) {
        this.pet_details = pet_details;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
