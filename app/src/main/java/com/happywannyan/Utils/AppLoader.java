package com.happywannyan.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.happywannyan.R;

/**
 * Created by apple on 26/05/17.
 */

public class AppLoader {
    Context mContext;
    AlertDialog Dailog;

    public AppLoader(Context mContext) {
        this.mContext = mContext;
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
        View view=inflater.inflate(R.layout.loader_view,null);
        ImageView Loader=(ImageView)view.findViewById(R.id.IMGLoader);
        Glide.with(mContext).load(R.drawable.cat_loader).into(Loader);
        builder.setView(view);
        Dailog=builder.create();
        Dailog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Dailog.setCanceledOnTouchOutside(false);
    }


    public void Show(){
    Dailog.show();
    }
    public void Dismiss(){
        Dailog.dismiss();
    }
}
