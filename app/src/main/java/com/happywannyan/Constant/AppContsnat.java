package com.happywannyan.Constant;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.bumptech.glide.Glide;
import com.happywannyan.Activities.AddAnotherPets;
import com.happywannyan.Activities.BaseActivity;
import com.happywannyan.Activities.LoginActivity;
import com.happywannyan.R;
import com.happywannyan.Utils.App_data_holder;
import com.happywannyan.Utils.CircleTransform;
import com.happywannyan.Utils.Loger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by apple on 22/05/17.
 */

public class AppContsnat extends App_data_holder{
    public static String BASEURL=" http://esolz.co.in/lab6/HappywanNyan/";
    public static String Language="en";
    public static String UserId="";

    public AppContsnat(Activity activity) {
        super(activity);
        GET_SHAREDATA(App_data_holder.UserData, new App_data_holder.App_sharePrefData() {
            @Override
            public void Avialable(boolean avilavle, JSONObject data) {
                try {
                    UserId=data.getJSONObject("info_array").getString("id");

                } catch (JSONException e) {

                }
            }

            @Override
            public void NotAvilable(String Error) {



            }
        });
    }



    @Override
    public Void UserDetaisl(String UserId) {
        return null;
    }
}
