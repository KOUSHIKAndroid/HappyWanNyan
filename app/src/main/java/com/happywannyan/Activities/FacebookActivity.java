package com.happywannyan.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.happywannyan.Constant.AppConstant;
import com.happywannyan.Constant.ApplicationClass;
import com.happywannyan.POJO.SetGetAPIPostData;
import com.happywannyan.R;
import com.happywannyan.Utils.AppDataHolder;
import com.happywannyan.Utils.CustomJSONParser;
import com.happywannyan.Utils.Loger;
import com.happywannyan.Utils.MYAlert;
import org.json.JSONException;
import org.json.JSONObject;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;

public class FacebookActivity extends AppCompatActivity {
    public static final int FacebookResponse = 11;

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.happywannyan",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Loger.MSG("## KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_facebook);

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email", "user_friends"));

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Loger.MSG("@@ FB TOKEN", "" + loginResult.getAccessToken());

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {

//                                if(response.getJSONObject())


                                Loger.MSG("@@ FB DETAILS-->",""+object.toString());
                                Loger.MSG("@@ FB DETAILS_GRAPH-->",""+ response.getJSONObject());


                                LoginWithWanNyaan(response.getJSONObject());
                                // Application code
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,first_name,last_name,picture.type(large),email,name,gender,birthday,friendlists,age_range,friends");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(FacebookException error) {
                Loger.MSG("@@ FB ERROR", "" + error.toString());
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void LoginWithWanNyaan(JSONObject jsonObject) {

        ArrayList<SetGetAPIPostData> PostData = new ArrayList<>();
        try {
            SetGetAPIPostData FACE = new SetGetAPIPostData();
            FACE.setPARAMS("lang_id");
            FACE.setValues(AppConstant.Language);
            PostData.add(FACE);

            FACE = new SetGetAPIPostData();
            FACE.setPARAMS("f_name");
            FACE.setValues(jsonObject.getString("first_name"));
            PostData.add(FACE);

            FACE = new SetGetAPIPostData();
            FACE.setPARAMS("l_name");
            FACE.setValues(jsonObject.getString("last_name"));
            PostData.add(FACE);

            FACE = new SetGetAPIPostData();
            FACE.setPARAMS("email");
            FACE.setValues(jsonObject.getString("email"));
            PostData.add(FACE);
            FACE = new SetGetAPIPostData();
            FACE.setPARAMS("fb_id");
            FACE.setValues(jsonObject.getString("id"));
            PostData.add(FACE);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new CustomJSONParser().APIForPostMethod(FacebookActivity.this,AppConstant.BASEURL + "facebook_login", PostData, new CustomJSONParser.JSONResponseInterface() {
            @Override
            public void OnSuccess(String Result) {
                try {
                    new AppConstant(FacebookActivity.this).setShareDATA(AppDataHolder.UserData, Result);
                    JSONObject jsonObject1 = new JSONObject(Result);
                    Loger.MSG("FaceBookLogin-->",""+jsonObject1);
                    AppConstant.login_status=jsonObject1.getJSONObject("info_array").getString("login_status");
                    AppConstant.messageAndBookingConditionCheck=false;
                    switch (jsonObject1.getString("user_status")) {
                        case "not_verified_user":

                            break;
                        default:

                            break;
                    }

                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);

//                    startActivity(new Intent(FacebookActivity.this,BaseActivity.class));
                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void OnError(String Error, String Response) {
                new MYAlert(FacebookActivity.this).AlertOnly(getResources().getString(R.string.facebook), Error, new MYAlert.OnlyMessage() {
                    @Override
                    public void OnOk(boolean res) {
                        Intent intent = new Intent();
                        setResult(RESULT_CANCELED, intent);
                        finish();
                    }
                });
            }

            @Override
            public void OnError(String Error) {
                new MYAlert(FacebookActivity.this).AlertOnly(getResources().getString(R.string.facebook), Error, new MYAlert.OnlyMessage() {
                    @Override
                    public void OnOk(boolean res) {
                        Intent intent = new Intent();
                        setResult(RESULT_CANCELED, intent);
                        finish();
                    }
                });
            }
        });

    }


    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
