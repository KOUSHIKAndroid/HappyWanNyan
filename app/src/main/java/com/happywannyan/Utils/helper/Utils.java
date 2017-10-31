package com.happywannyan.Utils.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;

import com.happywannyan.Utils.Loger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by apple on 04/08/17.
 */

public class Utils {
    Context mContext;

    public Utils(Context mContext) {
        this.mContext = mContext;
    }

    public Utils() {

    }

    public  int dpToPx(int dp) {
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }


    public int GetWeidth() {
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        return width;
    }


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        for (Network network : cm.getAllNetworks()) {
            if (cm.getNetworkInfo(network).isAvailable() && cm.getNetworkInfo(network).isConnected()) {
                return true;
            }
        }
        return false;
    }

}
