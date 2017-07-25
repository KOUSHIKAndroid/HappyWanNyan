package com.happywannyan.Utils.provider;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;

/**
 * Created by su on 6/23/17.
 */

public class RatingColor {
    public static void SETRatingColor(LayerDrawable stars) {
        stars.getDrawable(2).setColorFilter(Color.parseColor("#FFFFA500"),
                PorterDuff.Mode.SRC_ATOP); // for filled stars
        stars.getDrawable(1).setColorFilter(Color.parseColor("#FFFFA500"),
                PorterDuff.Mode.SRC_ATOP); // for half filled stars
        stars.getDrawable(0).setColorFilter(Color.parseColor("#FFD8D8D8"),
                PorterDuff.Mode.SRC_ATOP); // for empty stars
    }
}
