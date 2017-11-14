package com.happywannyan.Constant;

import android.app.Activity;

import com.happywannyan.Utils.AppDataHolder;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by apple on 22/05/17.
 */

public class AppConstant extends AppDataHolder {

    public static String BASEURL = "http://esolz.co.in/lab6/HappywanNyan/";
//    public static String BASEURL = "https://www.happywannyan.com/";
    public static String Language = "en";
    public static String UserId = "";
    public static String UserName = "";
    public static String UserEmail = "";
    public static String STRIPE_PUBLISH_KEY = "pk_test_xlxSWhvDxMIkyQYmgmHEWRoM";
    public static String STRIPE_SECRATE_KEY = "sk_test_1HkmfB8tpsf5yd1f8DUmRLdS";

    public static String go_to = "";
    public static String login_status = "1";
    public static boolean messageAndBookingConditionCheck = false;
    public static String message_object_string = "";
    public static boolean alwaysRedirectAfterLogin=false;
    public static String SearchJSONSitter="";
    public static String SearchJSONSitterLanguageChange="";

    public AppConstant(Activity activity) {
        super(activity);
        getShareData(AppDataHolder.UserData, new AppDataHolder.AppSharePreferenceDataInterface() {
            @Override
            public void available(boolean available, JSONObject data) {
                try {
                    UserId = data.getJSONObject("info_array").getString("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void notAvailable(String Error) {
            }
        });
    }


    @Override
    public Void userDetailsAbstract(String UserId) {
        return null;
    }
}
