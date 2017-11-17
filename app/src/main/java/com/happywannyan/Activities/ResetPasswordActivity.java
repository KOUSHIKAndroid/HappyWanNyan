package com.happywannyan.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.happywannyan.Activities.Booking.AcceptBookingActivity;
import com.happywannyan.Constant.AppConstant;
import com.happywannyan.POJO.SetGetAPIPostData;
import com.happywannyan.R;
import com.happywannyan.Utils.CustomJSONParser;
import com.happywannyan.Utils.MYAlert;
import com.happywannyan.Utils.Validation;

import java.util.ArrayList;

public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    EditText Code, Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Password = (EditText) findViewById(R.id.EDX_password);
        Code = (EditText) findViewById(R.id.EDX_code);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.Card_Ok) {

            if (Code.getText().length() <= 0) {
                new MYAlert(ResetPasswordActivity.this).AlertOnly(getResources().getString(R.string.ForgotPassword), getResources().getString(R.string.please_enter_code), new MYAlert.OnlyMessage() {
                    @Override
                    public void OnOk(boolean res) {
                        if (res) {
                            Code.requestFocus();
                        }
                    }
                });
            } else {
                if (
//                        !Validation.isPassword(Password.getText().toString()) ||
                        Password.getText().toString().trim().length() < 4) {
                    new MYAlert(ResetPasswordActivity.this).AlertOnly(getResources().getString(R.string.ResetPassword), getResources().getString(R.string.signup_password_checkingtext), new MYAlert.OnlyMessage() {
                        @Override
                        public void OnOk(boolean res) {
                            if (res) {
                                Password.requestFocus();
                            }
                        }
                    });

                } else {
                    ArrayList<SetGetAPIPostData> valuse = new ArrayList<>();

                    SetGetAPIPostData data = new SetGetAPIPostData();
                    data.setPARAMS("cur_code");
                    data.setValues(Code.getText().toString());
                    valuse.add(data);
                    data = new SetGetAPIPostData();
                    data.setPARAMS("new_pass");
                    data.setValues(Password.getText().toString());
                    valuse.add(data);

                    new CustomJSONParser().APIForPostMethod(ResetPasswordActivity.this,AppConstant.BASEURL + "resetpassword", valuse, new CustomJSONParser.JSONResponseInterface() {
                        @Override
                        public void OnSuccess(String Result) {

                            Toast.makeText(ResetPasswordActivity.this,getResources().getString(R.string.password_updated_sucessfully),Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
                            finish();

                        }

                        @Override
                        public void OnError(String Error, String Response) {
                            new MYAlert(ResetPasswordActivity.this).AlertOnly(getResources().getString(R.string.ResetPassword), Error, new MYAlert.OnlyMessage() {
                                @Override
                                public void OnOk(boolean res) {

                                }
                            });
                        }

                        @Override
                        public void OnError(String Error) {

                            if (Error.equalsIgnoreCase(getResources().getString(R.string.please_check_your_internet_connection))){
                                Toast.makeText(ResetPasswordActivity.this,Error,Toast.LENGTH_SHORT).show();
                            }

                            new MYAlert(ResetPasswordActivity.this).AlertOnly(getResources().getString(R.string.ResetPassword), Error, new MYAlert.OnlyMessage() {
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
