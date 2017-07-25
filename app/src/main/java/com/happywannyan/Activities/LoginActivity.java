package com.happywannyan.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.happywannyan.Constant.AppContsnat;
import com.happywannyan.POJO.APIPOSTDATA;
import com.happywannyan.R;
import com.happywannyan.Utils.AppLoader;
import com.happywannyan.Utils.App_data_holder;
import com.happywannyan.Utils.JSONPerser;
import com.happywannyan.Utils.Loger;
import com.happywannyan.Utils.MYAlert;
import com.happywannyan.Utils.Validation;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    EditText EDX_email,EDX_Password;
    CardView Card_Login;
    AppLoader appLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Card_Login=(CardView)findViewById(R.id.Card_Login);
        Card_Login.setOnClickListener(this);
        EDX_email=(EditText)findViewById(R.id.EDX_email);
        EDX_email.setText("koushik.sarkar@esolzmail.com");
        EDX_Password=(EditText)findViewById(R.id.EDX_Password);
        EDX_Password.setText("123456");
        appLoader=new AppLoader(LoginActivity.this);

        findViewById(R.id.FORGOT).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPassword.class));
            }
        });



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==FacebookActivity.FacebookResponse && resultCode==RESULT_OK)
        {
            startActivity(new Intent(LoginActivity.this,BaseActivity.class));
            finish();
        }

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        startActivity(new Intent(LoginActivity.this,LoginChoser.class));
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Card_Login:

                if(!EDX_email.getText().toString().trim().equals("") &&
                        !EDX_Password.getText().toString().trim().equalsIgnoreCase(""))
                {
                    if(!Validation.isValidEmail(EDX_email.getText()))
                    {
                        new MYAlert(LoginActivity.this).AlertOnly(getResources().getString(R.string.LoginAlertTitle), getResources().getString(R.string.Please_enter_valid_email), new MYAlert.OnlyMessage() {
                            @Override
                            public void OnOk(boolean res) {
                                if(res ){
                                    EDX_email.requestFocus();
                                }
                            }
                        });

                    }else {
                        ArrayList<APIPOSTDATA> apipostdataArrayList=new ArrayList<>();
                        APIPOSTDATA apipostdata=new APIPOSTDATA();
                        apipostdata.setPARAMS("user_email");
                        apipostdata.setValues(EDX_email.getText().toString());
                            apipostdataArrayList.add(apipostdata);
                        apipostdata=new APIPOSTDATA();
                        apipostdata.setPARAMS("password");
                        apipostdata.setValues(EDX_Password.getText().toString());
                        apipostdataArrayList.add(apipostdata);
                        appLoader.Show();
                        new JSONPerser().API_FOR_POST(AppContsnat.BASEURL + "app_login", apipostdataArrayList, new JSONPerser.JSONRESPONSE() {
                            @Override
                            public void OnSuccess(String Result) {
                                Loger.MSG("@@ LOGIN",Result);
                                new AppContsnat(LoginActivity.this).SET_SHAREDATA(App_data_holder.UserData,Result);
                                appLoader.Dismiss();
                                startActivity(new Intent(LoginActivity.this,BaseActivity.class));
                                finish();


                            }

                            @Override
                            public void OnError(String Error, String Response) {
                                appLoader.Dismiss();
                                new MYAlert(LoginActivity.this).AlertOnly(getResources().getString(R.string.LoginAlertTitle), Error, new MYAlert.OnlyMessage() {
                                    @Override
                                    public void OnOk(boolean res) {
                                    }
                                });
                            }

                            @Override
                            public void OnError(String Error) {
                                Loger.Error("@@ LOGIN",Error);
                                appLoader.Dismiss();
                                new MYAlert(LoginActivity.this).AlertOnly(getResources().getString(R.string.LoginAlertTitle), Error, new MYAlert.OnlyMessage() {
                                    @Override
                                    public void OnOk(boolean res) {
                                    }
                                });
                            }
                        });
                    }

                }else {
                    new MYAlert(LoginActivity.this).AlertOnly(getResources().getString(R.string.LoginAlertTitle), getResources().getString(R.string.Please_enter_both), new MYAlert.OnlyMessage() {
                        @Override
                        public void OnOk(boolean res) {
                            if(res && EDX_email.getText().toString().trim().equals("")){
                                EDX_email.requestFocus();
                            }else {
                                EDX_Password.requestFocus();
                            }


                        }
                    });
                }
                break;


        }
    }
}
