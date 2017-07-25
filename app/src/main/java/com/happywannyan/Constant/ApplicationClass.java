package com.happywannyan.Constant;

import android.app.Application;
import java.util.Calendar;
import java.util.Locale;


public class ApplicationClass extends Application {

    private static ApplicationClass instance=null;




    public static final String TAG = ApplicationClass.class.getSimpleName();
    //    public static int firstDayPosition = calendar.get(Calendar.DAY_OF_WEEK);


    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
    }






}
