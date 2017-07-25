package com.happywannyan.POJO;

import java.util.ArrayList;

/**
 * Created by apple on 24/05/17.
 */

public class SuperCalender {
    int Month;
    int Year;
    ArrayList<SetGetCalender> MonthBoject;

    public int getMonth() {
        return Month;
    }

    public void setMonth(int month) {
        Month = month;
    }

    public int getYear() {
        return Year;
    }

    public void setYear(int year) {
        Year = year;
    }

    public ArrayList<SetGetCalender> getMonthBoject() {
        return MonthBoject;
    }

    public void setMonthBoject(ArrayList<SetGetCalender> monthBoject) {
        MonthBoject = monthBoject;
    }
}
