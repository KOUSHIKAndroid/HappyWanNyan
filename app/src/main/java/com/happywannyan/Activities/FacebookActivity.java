package com.happywannyan.Activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.happywannyan.Constant.AppContsnat;
import com.happywannyan.POJO.APIPOSTDATA;
import com.happywannyan.R;
import com.happywannyan.Utils.App_data_holder;
import com.happywannyan.Utils.JSONPerser;
import com.happywannyan.Utils.Loger;
import com.happywannyan.Utils.MYAlert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

public class FacebookActivity extends AppCompatActivity {
    public static final int FacebookResponse=11;

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
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        setContentView(R.layout.activity_facebook);



        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile","email","user_friends"));

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Loger.MSG("@@ FB TOLEN",""+loginResult.getAccessToken());

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {

//                                if(response.getJSONObject())


//                                Loger.MSG("@@ FB DETAILS",""+object.toString());
                                Loger.MSG("@@ FB DETAILS"," GARPH ---"+response.getJSONObject());


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

            }

            @Override
            public void onError(FacebookException error) {
                Loger.MSG("@@ FB ERROR",""+error.toString());
            }
        });
    }

    private void LoginWithWanNyaan(JSONObject jsonObject) {

        ArrayList<APIPOSTDATA> PostData=new ArrayList<>();
        try {


        APIPOSTDATA FACE=new APIPOSTDATA();
        FACE.setPARAMS("lang_id");
        FACE.setValues(AppContsnat.Language);
        PostData.add(FACE);

         FACE=new APIPOSTDATA();
        FACE.setPARAMS("f_name");
        FACE.setValues(jsonObject.getString("first_name"));
        PostData.add(FACE);

            FACE=new APIPOSTDATA();
            FACE.setPARAMS("l_name");
            FACE.setValues(jsonObject.getString("last_name"));
            PostData.add(FACE);

            FACE=new APIPOSTDATA();
            FACE.setPARAMS("email");
            FACE.setValues(jsonObject.getString("email"));
            PostData.add(FACE);
            FACE=new APIPOSTDATA();
            FACE.setPARAMS("fb_id");
            FACE.setValues(jsonObject.getString("id"));
            PostData.add(FACE);

        }catch (JSONException e){}

        new JSONPerser().API_FOR_POST(AppContsnat.BASEURL + "facebook_login", PostData, new JSONPerser.JSONRESPONSE() {
            @Override
            public void OnSuccess(String Result) {
                try {
                    new AppContsnat(FacebookActivity.this).SET_SHAREDATA(App_data_holder.UserData,Result);
                    JSONObject jsonObject1=new JSONObject(Result);
                    switch (jsonObject1.getString("user_status")){
                        case "not_verified_user":

                            break;
                        default:

                            break;
                    }

                    Intent intent=new Intent();
                   setResult(RESULT_OK,intent);

//                    startActivity(new Intent(FacebookActivity.this,BaseActivity.class));
                    finish();

                }catch (Exception e)
                {

                }

            }

            @Override
            public void OnError(String Error, String Response) {
                new MYAlert(FacebookActivity.this).AlertOnly(getResources().getString(R.string.facebook), Error, new MYAlert.OnlyMessage() {
                    @Override
                    public void OnOk(boolean res) {
                        Intent intent=new Intent();
                        setResult(RESULT_CANCELED,intent);
                        finish();
                    }
                });
            }

            @Override
            public void OnError(String Error) {
                new MYAlert(FacebookActivity.this).AlertOnly(getResources().getString(R.string.facebook), Error, new MYAlert.OnlyMessage() {
                    @Override
                    public void OnOk(boolean res) {
                        Intent intent=new Intent();
                        setResult(RESULT_CANCELED,intent);
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
