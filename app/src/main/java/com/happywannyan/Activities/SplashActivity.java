package com.happywannyan.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.crashlytics.android.Crashlytics;
import com.happywannyan.Constant.AppConstant;
import com.happywannyan.R;
import com.happywannyan.Utils.AppDataHolder;
import com.happywannyan.Utils.Loger;

import org.json.JSONObject;

import java.util.Locale;

import io.fabric.sdk.android.Fabric;

public class SplashActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);

        sharedPreferences = getSharedPreferences("AutoLanguage", MODE_PRIVATE);

        if (!sharedPreferences.getString("shortLanguage", "").equals("")) {
            setLangRecreate(sharedPreferences.getString("shortLanguage", ""));
            AppConstant.Language = sharedPreferences.getString("shortLanguage", "");
        }
        else {
            AppConstant.Language = Locale.getDefault().getLanguage();
            Loger.MSG("Device language", ":::" + AppConstant.Language );
            setLangRecreate(AppConstant.Language );
        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new AppConstant(SplashActivity.this).getShareData(AppDataHolder.UserData, new AppDataHolder.AppSharePreferenceDataInterface() {
                    @Override
                    public void available(boolean available, JSONObject data) {
                        startActivity(new Intent(SplashActivity.this, BaseActivity.class));
                        finish();
                    }

                    @Override
                    public void notAvailable(String Error) {
                        startActivity(new Intent(SplashActivity.this, LoginChooserActivity.class));
                        finish();
                    }
                });
            }
        }, 800);
    }

    public void setLangRecreate(String langval) {
        Configuration config = getBaseContext().getResources().getConfiguration();
        Locale locale = new Locale(langval);
        Locale.setDefault(locale);
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        // recreate();
    }
}
