package com.happywannyan.Font;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by apple on 18/05/17.
 */

public class SFNFEditText extends AppCompatEditText {

    public SFNFEditText(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init();
    }

    public SFNFEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public SFNFEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "System San Francisco Display Regular.ttf");
        setTypeface(tf);
//        if (!isInEditMode()) {
//            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "System San Francisco Display Regular.ttf");
//            setTypeface(tf);
//        }
//        super.setTypeface(Typeface.createFromAsset(getContext().getAssets(),
//                "System San Francisco Display Regular.ttf"));

    }




    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        if(focused)
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    @Override
    public void onWindowFocusChanged(boolean focused) {
        if(focused)
            super.onWindowFocusChanged(focused);
    }


    @Override
    public boolean isFocused() {
        return true;
    }

}
