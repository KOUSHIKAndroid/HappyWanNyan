package com.happywannyan.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by apple on 25/05/17.
 */

public abstract class App_data_holder {
//    ******* Golbal Identifire ***************
    public static final int UserData=1;


public abstract Void UserDetaisl(String UserId);


    SharedPreferences AppUserData;
    Activity activity;

    public void LogOut_ClearAllData() {
        SharedPreferences.Editor editor = AppUserData.edit();
        editor.clear();
        editor.commit();

    }

    public interface App_sharePrefData{
        void Avialable(boolean avilavle, JSONObject data);
        void NotAvilable(String Error);
    }


    public App_data_holder(Activity activity) {
        this.activity = activity;
        AppUserData = PreferenceManager.getDefaultSharedPreferences(activity);
    }

    public void SET_SHAREDATA(int DataName, String Data){
        SharedPreferences.Editor editor = AppUserData.edit();
        switch (DataName)
        {
            case UserData:
                Loger.MSG("@@ "," DADAD "+Data);
                editor.putString("UserData", Data);
                editor.commit();
                break;
        }


    }



    public void GET_SHAREDATA(int DataName, App_sharePrefData app_sharePrefData){

        switch (DataName){
            case UserData:
              String USERCREDINTIAL= AppUserData.getString("UserData","NO");

                Loger.MSG("@@ "," DADAD 2 "+USERCREDINTIAL);
                if(USERCREDINTIAL.equalsIgnoreCase("NO")){
                    app_sharePrefData.NotAvilable("NODATAFOUND");
                }else  if(USERCREDINTIAL.trim().length()>0){
                    try {
                        app_sharePrefData.Avialable(true,new JSONObject(USERCREDINTIAL));
                        UserDetaisl(USERCREDINTIAL);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    app_sharePrefData.NotAvilable("NODATAFOUND");
                }

        }


    }



}
