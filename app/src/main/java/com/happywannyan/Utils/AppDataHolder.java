package com.happywannyan.Utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by apple on 25/05/17.
 */

public abstract class AppDataHolder {
//    ******* Global Identifires ***************
    public static final int UserData=1;


public abstract Void userDetailsAbstract(String UserId);

    SharedPreferences AppUserData;
    Activity activity;

    public void logOutClearAllData() {
        SharedPreferences.Editor editor = AppUserData.edit();
        editor.clear();
        editor.commit();
    }

    public interface AppSharePreferenceDataInterface{
        void available(boolean available, JSONObject data);
        void notAvailable(String Error);
    }


    public AppDataHolder(Activity activity) {
        this.activity = activity;
        AppUserData = PreferenceManager.getDefaultSharedPreferences(activity);
    }

    public void setShareDATA(int DataName, String Data){
        SharedPreferences.Editor editor = AppUserData.edit();
        switch (DataName)
        {
            case UserData:
                editor.putString("UserData", Data);
                editor.commit();
                break;
        }
    }



    public void getShareData(int DataName, AppSharePreferenceDataInterface appSharePreferenceDataInterface){
        switch (DataName){
            case UserData:
              String userCredentialString= AppUserData.getString("UserData","NO");

                Loger.MSG("@@ "," DADAD 2 "+userCredentialString);
                if(userCredentialString.equalsIgnoreCase("NO")){
                    appSharePreferenceDataInterface.notAvailable("NODATAFOUND");
                }else  if(userCredentialString.trim().length()>0){
                    try {
                        appSharePreferenceDataInterface.available(true,new JSONObject(userCredentialString));
                        userDetailsAbstract(userCredentialString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    appSharePreferenceDataInterface.notAvailable("NODATAFOUND");
                }
        }
    }
}
