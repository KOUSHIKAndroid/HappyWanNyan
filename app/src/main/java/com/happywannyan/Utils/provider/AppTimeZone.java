package com.happywannyan.Utils.provider;

import android.content.Context;

import java.util.TimeZone;

/**
 * Created by su on 6/23/17.
 */

public class AppTimeZone {
    Context context;

    public AppTimeZone(Context context) {
        this.context = context;
    }

    public static String GetTimeZone()
    {
        TimeZone tz = TimeZone.getDefault();
        return tz.getID();

    }
}
