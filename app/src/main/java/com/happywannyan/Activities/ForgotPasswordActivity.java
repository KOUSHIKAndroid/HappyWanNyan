package com.happywannyan.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.happywannyan.Activities.Booking.BookingDetailsActivity;
import com.happywannyan.Constant.AppConstant;
import com.happywannyan.POJO.SetGetAPIPostData;
import com.happywannyan.R;
import com.happywannyan.Utils.AppLoader;
import com.happywannyan.Utils.CustomJSONParser;
import com.happywannyan.Utils.Loger;
import com.happywannyan.Utils.MYAlert;
import com.happywannyan.Utils.Validation;

import org.json.JSONObject;

import java.util.ArrayList;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    EditText EDX_email, EDX_Password;
    AppLoader appLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        appLoader = new AppLoader(this);

        EDX_email = (EditText) findViewById(R.id.EDX_email);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.LL_SignUp:
                startActivity(new Intent(this, SignUpActivity.class));
                finish();
                break;
            case R.id.Card_Login:


                if (!EDX_email.getText().toString().trim().equals("")
                        ) {
                    if (!Validation.isValidEmail(EDX_email.getText())) {
                        new MYAlert(ForgotPasswordActivity.this).AlertOnly(getResources().getString(R.string.ForgotPassword), getResources().getString(R.string.Please_enter_valid_email), new MYAlert.OnlyMessage() {
                            @Override
                            public void OnOk(boolean res) {
                                if (res) {
                                    EDX_email.requestFocus();
                                }
                            }
                        });

                    } else {
                        ArrayList<SetGetAPIPostData> setGetAPIPostDataArrayList = new ArrayList<>();
                        SetGetAPIPostData setGetAPIPostData = new SetGetAPIPostData();
                        setGetAPIPostData.setPARAMS("user_email");
                        setGetAPIPostData.setValues(EDX_email.getText().toString());
                        setGetAPIPostDataArrayList.add(setGetAPIPostData);

                        appLoader.Show();
                        new CustomJSONParser().APIForPostMethod(ForgotPasswordActivity.this,AppConstant.BASEURL + "app_forget_password", setGetAPIPostDataArrayList, new CustomJSONParser.JSONResponseInterface() {
                            @Override
                            public void OnSuccess(String Result) {
                                Loger.MSG("@@ LOGIN", Result);
                                appLoader.Dismiss();

                                try {
                                    JSONObject response = new JSONObject(Result);
                                    if (response.getBoolean("response")) {
                                        new MYAlert(ForgotPasswordActivity.this).AlertOnly(getResources().getString(R.string.ForgotPassword), getResources().getString(R.string.please_check_your_mail), new MYAlert.OnlyMessage() {
                                            @Override
                                            public void OnOk(boolean res) {
                                                startActivity(new Intent(ForgotPasswordActivity.this, ResetPasswordActivity.class));
                                                finish();
                                            }
                                        });

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void OnError(String Error, String Response) {
                                appLoader.Dismiss();
                                new MYAlert(ForgotPasswordActivity.this).AlertOnly(getResources().getString(R.string.ForgotPassword), Error, new MYAlert.OnlyMessage() {
                                    @Override
                                    public void OnOk(boolean res) {

                                    }
                                });
                            }

                            @Override
                            public void OnError(String Error) {
                                Loger.Error("@@ LOGIN", Error);
                                appLoader.Dismiss();

                                if (Error.equalsIgnoreCase(getResources().getString(R.string.please_check_your_internet_connection))){
                                    Toast.makeText(ForgotPasswordActivity.this,Error,Toast.LENGTH_SHORT).show();
                                }

                                new MYAlert(ForgotPasswordActivity.this).AlertOnly(getResources().getString(R.string.ForgotPassword), Error, new MYAlert.OnlyMessage() {
                                    @Override
                                    public void OnOk(boolean res) {

                                    }
                                });
                            }
                        });
                    }

                } else {
                    new MYAlert(ForgotPasswordActivity.this).AlertOnly(getResources().getString(R.string.ForgotPassword), getResources().getString(R.string.Please_enter_valid_email), new MYAlert.OnlyMessage() {
                        @Override
                        public void OnOk(boolean res) {
                            if (res && EDX_email.getText().toString().trim().equals("")) {
                                EDX_email.requestFocus();
                            }


                        }
                    });
                }
                break;

            case R.id.LL_fb:
                startActivityForResult(new Intent(ForgotPasswordActivity.this, FacebookActivity.class), FacebookActivity.FacebookResponse);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FacebookActivity.FacebookResponse && resultCode == RESULT_OK) {
            startActivity(new Intent(ForgotPasswordActivity.this, BaseActivity.class));
            finish();
        }

    }
}
