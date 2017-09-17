package com.happywannyan.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.crashlytics.android.Crashlytics;
import com.happywannyan.Constant.AppConstant;
import com.happywannyan.R;
import com.happywannyan.Utils.AppDataHolder;

import io.fabric.sdk.android.Fabric;
import org.json.JSONObject;

import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);
       AppConstant.Language= Locale.getDefault().getLanguage();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new AppConstant(SplashActivity.this).getShareData(AppDataHolder.UserData, new AppDataHolder.AppSharePreferenceDataInterface() {
                    @Override
                    public void available(boolean available, JSONObject data) {
                        startActivity( new Intent(SplashActivity.this,BaseActivity.class));
                        finish();
                    }

                    @Override
                    public void notAvailable(String Error) {
                        startActivity( new Intent(SplashActivity.this,LoginChooserActivity.class));
                        finish();
                    }
                });
            }
        },800);
    }
}
