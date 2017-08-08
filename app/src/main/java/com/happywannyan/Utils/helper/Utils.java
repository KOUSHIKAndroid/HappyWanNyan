package com.happywannyan.Utils.helper;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by apple on 04/08/17.
 */

public class Utils {
    Context mContext;

    public Utils(Context mContext) {
        this.mContext = mContext;
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
