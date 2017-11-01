package com.happywannyan.Constant;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.happywannyan.Utils.Loger;

import java.util.Locale;


public class ApplicationClass extends Application {

    private static ApplicationClass instance = null;
    private RequestQueue mRequestQueue;
    public SharedPreferences everyTimeRedirectAfterLoginPreference ;

    public static final String TAG = ApplicationClass.class.getSimpleName();
    //    public static int firstDayPosition = calendar.get(Calendar.DAY_OF_WEEK);


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }


    public static synchronized ApplicationClass getInstance() {
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

}
