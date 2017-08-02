package com.happywannyan.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.happywannyan.Constant.AppContsnat;
import com.happywannyan.R;
import com.happywannyan.Utils.App_data_holder;

import io.fabric.sdk.android.Fabric;
import org.json.JSONObject;

import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);
       AppContsnat.Language= Locale.getDefault().getLanguage();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new AppContsnat(SplashActivity.this).GET_SHAREDATA(App_data_holder.UserData, new App_data_holder.App_sharePrefData() {
                    @Override
                    public void Avialable(boolean avilavle, JSONObject data) {
                        startActivity( new Intent(SplashActivity.this,BaseActivity.class));
                        finish();
                    }

                    @Override
                    public void NotAvilable(String Error) {
                        startActivity( new Intent(SplashActivity.this,LoginChoser.class));
                        finish();
                    }
                });


            }
        },800);
    }
}
