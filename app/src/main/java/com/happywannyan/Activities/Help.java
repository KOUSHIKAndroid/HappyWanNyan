package com.happywannyan.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.happywannyan.R;

public class Help extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        ((WebView)findViewById(R.id.Web)).loadUrl("http://esolz.co.in/lab6/HappywanNyan/support-center");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.IMG_icon_back:
                finish();
                break;
        }
    }
}
