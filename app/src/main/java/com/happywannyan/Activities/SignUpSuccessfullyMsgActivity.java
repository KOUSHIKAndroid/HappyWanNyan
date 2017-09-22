package com.happywannyan.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.happywannyan.R;

/**
 * Created by su on 9/22/17.
 */

public class SignUpSuccessfullyMsgActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_succefully);

        findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignUpSuccessfullyMsgActivity.this,LoginChooserActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
