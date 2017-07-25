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
import com.happywannyan.Utils.JSONPerser;
import com.happywannyan.Utils.MYAlert;
import com.happywannyan.Utils.Validation;

import java.util.ArrayList;

public class ResetPassword extends AppCompatActivity implements View.OnClickListener{
    EditText Code,Password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Password=(EditText)findViewById(R.id.EDX_password);
        Code=(EditText)findViewById(R.id.EDX_code);
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.Card_Ok)
        {

            if(Code.getText().length()<=0)
            {
                new MYAlert(ResetPassword.this).AlertOnly(getResources().getString(R.string.ForgotPassword), getResources().getString(R.string.please_enter_code), new MYAlert.OnlyMessage() {
                    @Override
                    public void OnOk(boolean res) {
                        if(res ){
                            Code.requestFocus();
                        }
                    }
                });
            }else {
                if (!Validation.isPassword(Password.getText().toString()) || Password.getText().toString().trim().length() < 6) {
                    new MYAlert(ResetPassword.this).AlertOnly(getResources().getString(R.string.ResetPassword), getResources().getString(R.string.signup_password_checkingtext), new MYAlert.OnlyMessage() {
                        @Override
                        public void OnOk(boolean res) {
                            if (res) {
                                Password.requestFocus();
                            }
                        }
                    });

                }else{
                    ArrayList<APIPOSTDATA> valuse=new ArrayList<>();

                    APIPOSTDATA data=new APIPOSTDATA();
                    data.setPARAMS("cur_code");
                    data.setValues(Code.getText().toString());
                    valuse.add(data);
                    data=new APIPOSTDATA();
                    data.setPARAMS("new_pass");
                    data.setValues(Password.getText().toString());
                    valuse.add(data);

                    new JSONPerser().API_FOR_POST(AppContsnat.BASEURL + "resetpassword", valuse, new JSONPerser.JSONRESPONSE() {
                        @Override
                        public void OnSuccess(String Result) {

                            startActivity(new Intent(ResetPassword.this,LoginActivity.class));
                            finish();

                        }

                        @Override
                        public void OnError(String Error, String Response) {
                            new MYAlert(ResetPassword.this).AlertOnly(getResources().getString(R.string.ResetPassword), Error, new MYAlert.OnlyMessage() {
                                @Override
                                public void OnOk(boolean res) {

                                }
                            });
                        }

                        @Override
                        public void OnError(String Error) {
                            new MYAlert(ResetPassword.this).AlertOnly(getResources().getString(R.string.ResetPassword), Error, new MYAlert.OnlyMessage() {
                                @Override
                                public void OnOk(boolean res) {

                                }
                            });
                        }
                    });



                }
            }


        }


    }
}
