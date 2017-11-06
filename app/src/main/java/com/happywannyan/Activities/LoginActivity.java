package com.happywannyan.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.happywannyan.Constant.AppConstant;
import com.happywannyan.POJO.SetGetAPIPostData;
import com.happywannyan.R;
import com.happywannyan.Utils.AppDataHolder;
import com.happywannyan.Utils.AppLoader;
import com.happywannyan.Utils.CustomJSONParser;
import com.happywannyan.Utils.Loger;
import com.happywannyan.Utils.MYAlert;
import com.happywannyan.Utils.Validation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText EDX_email, EDX_Password;
    CardView Card_Login;
    AppLoader appLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Card_Login = (CardView) findViewById(R.id.Card_Login);
        Card_Login.setOnClickListener(this);
        EDX_email = (EditText) findViewById(R.id.EDX_email);
        EDX_Password = (EditText) findViewById(R.id.EDX_Password);

        new AppConstant(LoginActivity.this).getRememberShare(new AppDataHolder.AppSharePreferenceRememberInterface() {
            @Override
            public void available(String id, String password) {

                Loger.MSG("id", id);
                EDX_email.setText(id);
                EDX_Password.setText(password);
                ((CheckBox) findViewById(R.id.check_remember)).setChecked(true);
            }

            @Override
            public void notAvailable(String Error) {
                Loger.MSG("Error", Error);
            }
        });

        //EDX_email.setText("koushik.sarkar@esolzmail.com");
        //EDX_Password.setText("123456");
        appLoader = new AppLoader(LoginActivity.this);

        findViewById(R.id.FORGOT).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FacebookActivity.FacebookResponse && resultCode == RESULT_OK) {


            AppConstant.alwaysRedirectAfterLogin = true;

            Loger.MSG("RedirectAfterLogin-->","True");

            startActivity(new Intent(LoginActivity.this, SearchResultActivity.class));
            finish();
//
//            startActivity(new Intent(LoginActivity.this, BaseActivity.class));
//            finish();
        }

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        startActivity(new Intent(LoginActivity.this, LoginChooserActivity.class));
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Card_Login:

                if (!EDX_email.getText().toString().trim().equals("") &&
                        !EDX_Password.getText().toString().trim().equalsIgnoreCase("")) {
                    if (!Validation.isValidEmail(EDX_email.getText())) {
                        new MYAlert(LoginActivity.this).AlertOnly(getResources().getString(R.string.LoginAlertTitle), getResources().getString(R.string.Please_enter_valid_email), new MYAlert.OnlyMessage() {
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
                        setGetAPIPostData = new SetGetAPIPostData();
                        setGetAPIPostData.setPARAMS("password");
                        setGetAPIPostData.setValues(EDX_Password.getText().toString());
                        setGetAPIPostDataArrayList.add(setGetAPIPostData);
                        appLoader.Show();
                        new CustomJSONParser().APIForPostMethod(LoginActivity.this, AppConstant.BASEURL + "app_login", setGetAPIPostDataArrayList, new CustomJSONParser.JSONResponseInterface() {
                            @Override
                            public void OnSuccess(String Result) {
                                Loger.MSG("@@ LOGIN", Result);
                                appLoader.Dismiss();
                                try {

                                    JSONObject jsonObject = new JSONObject(Result);
                                    if (jsonObject.getString("email_verified_status").equals("1")) {

                                        ////////////Share Data/////////////////////////////////////////////////////////////////
                                        new AppConstant(LoginActivity.this).setShareDATA(AppDataHolder.UserData, Result);
                                        //////////////////////////////////////////End//////////////////////////////////////////

                                        AppConstant.UserEmail = jsonObject.getJSONObject("info_array").getString("emailid");

                                        if (!jsonObject.getJSONObject("info_array").getString("lastname").equals("")) {
                                            AppConstant.UserName = jsonObject.getJSONObject("info_array").getString("firstname") + " " + jsonObject.getJSONObject("info_array").getString("lastname");
                                        } else {
                                            AppConstant.UserName = jsonObject.getJSONObject("info_array").getString("firstname");
                                        }

                                        /////////////////For Remember me ///////////////////////////
                                        if (((CheckBox) findViewById(R.id.check_remember)).isChecked()) {
                                            Loger.MSG("remember", "" + ((CheckBox) findViewById(R.id.check_remember)).isChecked());
                                            new AppConstant(LoginActivity.this).setRememberShare(EDX_email.getText().toString().trim(),
                                                    EDX_Password.getText().toString().trim());
                                        } else {
                                            new AppConstant(LoginActivity.this).clearRememberMe();
                                        }
                                        /////////////////////End////////////////////////////////////

                                        AppConstant.login_status = jsonObject.getJSONObject("info_array").getString("login_status");

//                                        startActivity(new Intent(LoginActivity.this, BaseActivity.class));
//                                        finish();

                                        AppConstant.alwaysRedirectAfterLogin = true;
                                        startActivity(new Intent(LoginActivity.this, SearchResultActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.email_confirm), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
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
                                Loger.Error("@@ LOGIN", Error);
                                appLoader.Dismiss();
                                new MYAlert(LoginActivity.this).AlertOnly(getResources().getString(R.string.LoginAlertTitle), Error, new MYAlert.OnlyMessage() {
                                    @Override
                                    public void OnOk(boolean res) {
                                    }
                                });
                            }
                        });
                    }

                } else {
                    new MYAlert(LoginActivity.this).AlertOnly(getResources().getString(R.string.LoginAlertTitle), getResources().getString(R.string.Please_enter_both), new MYAlert.OnlyMessage() {
                        @Override
                        public void OnOk(boolean res) {
                            if (res && EDX_email.getText().toString().trim().equals("")) {
                                EDX_email.requestFocus();
                            } else {
                                EDX_Password.requestFocus();
                            }
                        }
                    });
                }
                break;
        }
    }
}
