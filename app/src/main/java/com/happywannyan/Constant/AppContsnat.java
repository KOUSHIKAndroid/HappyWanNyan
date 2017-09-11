package com.happywannyan.Constant;

import android.app.Activity;

import com.happywannyan.Utils.AppDataHolder;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by apple on 22/05/17.
 */

public class AppContsnat extends AppDataHolder {
    public static String BASEURL=" http://esolz.co.in/lab6/HappywanNyan/";
    public static String Language="en";
    public static String UserId="";
    public static String STRIPE_PUBLISH_KEY="pk_test_xlxSWhvDxMIkyQYmgmHEWRoM";
    public static String STRIPE_SECRATE_KEY="sk_test_1HkmfB8tpsf5yd1f8DUmRLdS";

    public static String go_to="";


    public AppContsnat(Activity activity) {
        super(activity);
        GET_SHAREDATA(AppDataHolder.UserData, new AppDataHolder.App_sharePrefData() {
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
