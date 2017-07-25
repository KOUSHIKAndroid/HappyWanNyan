package com.happywannyan.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    EditText EDX_email, EDX_Password;
    CardView Card_Login;
    AppLoader appLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Card_Login = (CardView) findViewById(R.id.Card_Login);
        Card_Login.setOnClickListener(this);
        EDX_email = (EditText) findViewById(R.id.EDX_email);
        EDX_Password = (EditText) findViewById(R.id.EDX_Password);
        appLoader=new AppLoader(this);

    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Card_Login:


                Loger.MSG("###", "- " + Validation.isPassword(EDX_Password.getText().toString()));
                if (!EDX_email.getText().toString().trim().equals("") &&
                        !EDX_Password.getText().toString().trim().equalsIgnoreCase("")) {
                    if (!Validation.isValidEmail(EDX_email.getText())) {
                        new MYAlert(SignUpActivity.this).AlertOnly(getResources().getString(R.string.LoginAlertTitle), getResources().getString(R.string.Please_enter_valid_email), new MYAlert.OnlyMessage() {
                            @Override
                            public void OnOk(boolean res) {
                                if (res) {
                                    EDX_email.requestFocus();
                                }
                            }
                        });

                    } else if (!Validation.isPassword(EDX_Password.getText().toString()) || EDX_Password.getText().toString().trim().length() < 6) {
                        new MYAlert(SignUpActivity.this).AlertOnly(getResources().getString(R.string.SignUp), getResources().getString(R.string.signup_password_checkingtext), new MYAlert.OnlyMessage() {
                            @Override
                            public void OnOk(boolean res) {
                                if (res) {
                                    EDX_Password.requestFocus();
                                }
                            }
                        });

                    } else {
                        appLoader.Show();
                        ArrayList<APIPOSTDATA> apipostdataArrayList = new ArrayList<>();
                        APIPOSTDATA apipostdata = new APIPOSTDATA();
                        apipostdata.setPARAMS("user_email");
                        apipostdata.setValues(EDX_email.getText().toString());
                        apipostdataArrayList.add(apipostdata);
                        apipostdata = new APIPOSTDATA();
                        apipostdata.setPARAMS("password");
                        apipostdata.setValues(EDX_Password.getText().toString());
                        apipostdataArrayList.add(apipostdata);
                        apipostdata = new APIPOSTDATA();
                        apipostdata.setPARAMS("langid");
                        apipostdata.setValues(AppContsnat.Language);
                        Loger.MSG("@@ LANG-", " " + AppContsnat.Language);
                        apipostdataArrayList.add(apipostdata);
                        new JSONPerser().API_FOR_POST(AppContsnat.BASEURL + "app_regirsation\n", apipostdataArrayList, new JSONPerser.JSONRESPONSE() {
                            @Override
                            public void OnSuccess(String Result) {
                                Loger.MSG("@@ REG", Result);
                                try {
                                    appLoader.Dismiss();
                                    JSONObject object=new JSONObject(Result);
                                    new MYAlert(SignUpActivity.this).AlertOnly(getResources().getString(R.string.SignUp), object.getString("message"), new MYAlert.OnlyMessage() {
                                        @Override
                                        public void OnOk(boolean res) {
                                            startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
                                            finish();
                                        }
                                    });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }

                            @Override
                            public void OnError(String Error, String Response) {
                                Loger.Error("@@ REG", Error);
                                new MYAlert(SignUpActivity.this).AlertOnly(getResources().getString(R.string.SignUp), Error, new MYAlert.OnlyMessage() {
                                    @Override
                                    public void OnOk(boolean res) {
                                        appLoader.Dismiss();
                                        EDX_email.setText("");
                                        EDX_Password.setText("");
//                                        startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
//                                        finish();
                                    }
                                });
                            }

                            @Override
                            public void OnError(String Error) {
                                Loger.Error("@@ REG", Error);
                                new MYAlert(SignUpActivity.this).AlertOnly(getResources().getString(R.string.SignUp), Error, new MYAlert.OnlyMessage() {
                                    @Override
                                    public void OnOk(boolean res) {
                                        appLoader.Dismiss();
                                        EDX_email.setText("");
                                        EDX_Password.setText("");
//                                        startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
//                                        finish();
                                    }
                                });
                            }
                        });
                    }

                } else {
                    new MYAlert(SignUpActivity.this).AlertOnly(getResources().getString(R.string.LoginAlertTitle), getResources().getString(R.string.Please_enter_both), new MYAlert.OnlyMessage() {
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

//            case R.id.LL_LoginNow:
//                startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
//                finish();
//                break;
//            case R.id.LL_fb:
//                startActivityForResult(new Intent(SignUpActivity.this,FacebookActivity.class),FacebookActivity.FacebookResponse);
//                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==FacebookActivity.FacebookResponse && resultCode==RESULT_OK)
        {
            startActivity(new Intent(SignUpActivity.this,BaseActivity.class));
            finish();
        }

    }

}
