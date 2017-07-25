package com.happywannyan.POJO;

/**
 * Created by su on 5/18/17.
 */

public class SetGetCalender {

    String day;
    boolean selected;
    boolean startdate;
    boolean enddate;

    public boolean isStartdate() {
        return startdate;
    }

    public void setStartdate(boolean startdate) {
        this.startdate = startdate;
    }

    public boolean isEnddate() {
        return enddate;
    }

    public void setEnddate(boolean enddate) {
        this.enddate = enddate;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
