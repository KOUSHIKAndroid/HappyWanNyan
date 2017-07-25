package com.happywannyan.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.happywannyan.Constant.AppContsnat;
import com.happywannyan.POJO.APIPOSTDATA;
import com.happywannyan.R;
import com.happywannyan.Utils.AppLoader;
import com.happywannyan.Utils.JSONPerser;
import com.happywannyan.Utils.Loger;
import com.happywannyan.Utils.MYAlert;
import com.happywannyan.Utils.Validation;

import org.json.JSONObject;

import java.util.ArrayList;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener{
    EditText EDX_email,EDX_Password;
    AppLoader appLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        appLoader=new AppLoader(this);

        EDX_email=(EditText)findViewById(R.id.EDX_email);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.LL_SignUp:
                startActivity(new Intent(this,SignUpActivity.class));
                finish();
                break;
            case R.id.Card_Login:


                if(!EDX_email.getText().toString().trim().equals("")
                        )
                {
                    if(!Validation.isValidEmail(EDX_email.getText()))
                    {
                        new MYAlert(ForgotPassword.this).AlertOnly(getResources().getString(R.string.ForgotPassword), getResources().getString(R.string.Please_enter_valid_email), new MYAlert.OnlyMessage() {
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

                        appLoader.Show();
                        new JSONPerser().API_FOR_POST(AppContsnat.BASEURL + "app_forget_password", apipostdataArrayList, new JSONPerser.JSONRESPONSE() {
                            @Override
                            public void OnSuccess(String Result) {
                                Loger.MSG("@@ LOGIN",Result);
                                appLoader.Dismiss();

                                try{
                                    JSONObject response=new JSONObject(Result);
                                    if(response.getBoolean("response"))
                                    {
                                        new MYAlert(ForgotPassword.this).AlertOnly(getResources().getString(R.string.ForgotPassword), "Please Check your mail", new MYAlert.OnlyMessage() {
                                            @Override
                                            public void OnOk(boolean res) {
                                                startActivity(new Intent(ForgotPassword.this,ResetPassword.class));
                                                finish();
                                            }
                                        });

                                    }
                                }catch (Exception e){

                                }





                            }

                            @Override
                            public void OnError(String Error, String Response) {
                                appLoader.Dismiss();
                                new MYAlert(ForgotPassword.this).AlertOnly(getResources().getString(R.string.ForgotPassword), Error, new MYAlert.OnlyMessage() {
                                    @Override
                                    public void OnOk(boolean res) {

                                    }
                                });
                            }

                            @Override
                            public void OnError(String Error) {
                                Loger.Error("@@ LOGIN",Error);
                                appLoader.Dismiss();
                                new MYAlert(ForgotPassword.this).AlertOnly(getResources().getString(R.string.ForgotPassword), Error, new MYAlert.OnlyMessage() {
                                    @Override
                                    public void OnOk(boolean res) {

                                    }
                                });
                            }
                        });
                    }

                }else {
                    new MYAlert(ForgotPassword.this).AlertOnly(getResources().getString(R.string.ForgotPassword), getResources().getString(R.string.Please_enter_valid_email), new MYAlert.OnlyMessage() {
                        @Override
                        public void OnOk(boolean res) {
                            if(res && EDX_email.getText().toString().trim().equals("")){
                                EDX_email.requestFocus();
                            }


                        }
                    });
                }
                break;

            case R.id.LL_fb:
                startActivityForResult(new Intent(ForgotPassword.this,FacebookActivity.class),FacebookActivity.FacebookResponse);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==FacebookActivity.FacebookResponse && resultCode==RESULT_OK)
        {
            startActivity(new Intent(ForgotPassword.this,BaseActivity.class));
            finish();
        }

    }
}
