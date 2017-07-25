package com.happywannyan.Utils;

import android.util.Log;

/**
 * Created by apple on 19/05/17.
 */

public class Loger {
    public static void MSG(String TAG,String MSG){
        Log.d(TAG,MSG);
    }
    public static void Error(String TAG,String MSG){
        Log.e(TAG,MSG);
    }
}
