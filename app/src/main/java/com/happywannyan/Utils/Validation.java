package com.happywannyan.Utils;

import android.app.Activity;
import android.text.TextUtils;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by apple on 22/05/17.
 */

public class Validation {

    public final static boolean isValidEmail(CharSequence target) {

        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    public static boolean isPassword(String text) {
         String SPECIAL = "^(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
        String NUBER="^(?=.*[0-9])";
        Pattern p = Pattern.compile(SPECIAL);
        Matcher m = p.matcher(text);

        Pattern num=Pattern.compile(NUBER);
        Matcher N=num.matcher(text);


        int SP_count = 0;
        while (m.find()) {
            SP_count = SP_count+1;
            System.out.println("position "  + m.start() + ": " + text.charAt(m.start()));
        }
        System.out.println("There are " + SP_count + " special characters");


        int NU_Count=0;
        while (N.find()) {
            NU_Count = NU_Count+1;
            System.out.println("position "  + N.start() + ": " + text.charAt(N.start()));
        }
        System.out.println("There are " + NU_Count + " special Number");


        return (NU_Count>0 && SP_count>0);
    }

    public static boolean isPlayServicesAvailable(Activity context) {
        int resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            GoogleApiAvailability.getInstance().getErrorDialog((Activity) context, resultCode, 2).show();
            return false;
        }
        return true;
    }


}
