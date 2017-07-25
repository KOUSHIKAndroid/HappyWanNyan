package com.happywannyan.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.happywannyan.Constant.AppContsnat;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.POJO.APIPOSTDATA;
import com.happywannyan.R;
import com.happywannyan.Utils.AppLoader;
import com.happywannyan.Utils.ImageFilePath;
import com.happywannyan.Utils.JSONPerser;
import com.happywannyan.Utils.Loger;
import com.happywannyan.Utils.MYAlert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EditAnotherPets extends AppCompatActivity implements View.OnClickListener {
    JSONArray other_info;
    AppLoader appLoader;
    JSONArray PetService;
    String PetTypeId = "",EditId="";
    EditText TXTName;
    JSONArray SelectObject;
    RadioGroup Radio_Catspayed, Rad_catf;
    JSONArray Text, InputArea, Select, RadioArray;
String TAG="@@@Edit";
    AlertDialog Dialog;
    ImageView img_pet;
    private static final int REQUEST_WRITE_PERMISSION1 = 3000;
    private static final int REQUEST_WRITE_PERMISSION2 = 4000;
    final String BITMAP_STORAGE_URL = "IMAGE_URL";
    private int PICK_IMAGE_REQUEST = 100;
    private int CAMERA_CAPTURE = 200;
    JSONObject MainObject;
    File photofile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_another_pets);
        appLoader = new AppLoader(this);
//        appLoader.Show();
        TXTName = (EditText) findViewById(R.id.TXTName);
        Radio_Catspayed = (RadioGroup) findViewById(R.id.Radio_Catspayed);
        Rad_catf = (RadioGroup) findViewById(R.id.Rad_catf);
         img_pet = (ImageView) findViewById(R.id.img_pet);
        findViewById(R.id.IMG_ARROW).setVisibility(View.INVISIBLE);
        new AppContsnat(this);

        try {
            MainObject=new JSONObject(getIntent().getStringExtra("Data"));
            Loger.MSG(TAG,MainObject.toString());
            PetTypeId=MainObject.getString("pet_type_id");
            EditId=MainObject.getString("edit_id");
            ((SFNFTextView) findViewById(R.id.Txt_type)).setText(MainObject.getString("pet_name"));
            Glide.with(getApplicationContext()).load(MainObject.getString("pet_image")).into(img_pet);
            SETDatField(MainObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }


//        new JSONPerser().API_FOR_GET(AppContsnat.BASEURL + "parent_service", new ArrayList<APIPOSTDATA>(), new JSONPerser.JSONRESPONSE() {
//            @Override
//            public void OnSuccess(String Result) {
//                try {
//                    JSONObject object = new JSONObject(Result);
//                    Loger.MSG("@@" + getClass().getName(), object.toString());
//                    appLoader.Dismiss();
//                    PetService = object.getJSONArray("allPetDetails");
//
//                } catch (JSONException e) {
//                    appLoader.Dismiss();
//                }
//            }
//
//            @Override
//            public void OnError(String Error, String Response) {
//                appLoader.Dismiss();
//            }
//
//            @Override
//            public void OnError(String Error) {
//                appLoader.Dismiss();
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        new AppContsnat(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Hea:

//                new MYAlert(EditAnotherPets.this).AlertTextLsit("" + getResources().getString(R.string.Chosepettype),
//                        PetService, "name", new MYAlert.OnSignleListTextSelected() {
//                            @Override
//                            public void OnSelectedTEXT(JSONObject jsonObject) {
//                                Loger.MSG("@@ Data", "" + jsonObject);
//                                try {
//                                    PetTypeId = jsonObject.getString("id");
//                                    ((SFNFTextView) findViewById(R.id.Txt_type)).setText(jsonObject.getString("name"));
//                                    ArrayList<APIPOSTDATA> params = new ArrayList<>();
//                                    APIPOSTDATA apipostdata = new APIPOSTDATA();
//                                    apipostdata.setPARAMS("user_id");
//                                    apipostdata.setValues(AppContsnat.UserId);
//                                    params.add(apipostdata);
//                                    apipostdata = new APIPOSTDATA();
//                                    apipostdata.setPARAMS("lang_id");
//                                    apipostdata.setValues(AppContsnat.Language);
//                                    params.add(apipostdata);
//                                    apipostdata = new APIPOSTDATA();
//                                    apipostdata.setPARAMS("pet_type_id");
//                                    apipostdata.setValues(PetTypeId);
//                                    params.add(apipostdata);
//                                    appLoader.Show();
//                                    new JSONPerser().API_FOR_GET(AppContsnat.BASEURL + "app_users_petinfo?", params, new JSONPerser.JSONRESPONSE() {
//                                        @Override
//                                        public void OnSuccess(String Result) {
//                                            findViewById(R.id.Body).setVisibility(View.VISIBLE);
//                                            SETDatField(Result);
//                                            appLoader.Dismiss();
//                                        }
//
//                                        @Override
//                                        public void OnError(String Error, String Response) {
//                                            appLoader.Dismiss();
//                                        }
//
//                                        @Override
//                                        public void OnError(String Error) {
//                                            appLoader.Dismiss();
//                                        }
//                                    });
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });


                break;

            case R.id.IMG_icon_back:
                finish();
                break;
            case R.id.SE_Year:
                try {
                    new MYAlert(EditAnotherPets.this).AlertTextLsit(SelectObject.getJSONObject(0).getString("input_name"), SelectObject.getJSONObject(0).getJSONArray("input_option"), "option_name", new MYAlert.OnSignleListTextSelected() {
                        @Override
                        public void OnSelectedTEXT(JSONObject jsonObject) {
                            try {
                                ((SFNFTextView) findViewById(R.id.TXT_Year)).setText(jsonObject.getString("option_name"));
                                ((SFNFTextView) findViewById(R.id.TXT_Year)).setTag(jsonObject);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.SE_Month:
                try {
                    new MYAlert(EditAnotherPets.this).AlertTextLsit(SelectObject.getJSONObject(1).getString("input_name"), SelectObject.getJSONObject(1).getJSONArray("input_option"), "option_name", new MYAlert.OnSignleListTextSelected() {
                        @Override
                        public void OnSelectedTEXT(JSONObject jsonObject) {
                            try {
                                ((SFNFTextView) findViewById(R.id.TXT_Month)).setText(jsonObject.getString("option_name"));
                                ((SFNFTextView) findViewById(R.id.TXT_Month)).setTag(jsonObject);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.RL_Gender:
                try {
                    new MYAlert(EditAnotherPets.this).AlertTextLsit(SelectObject.getJSONObject(2).getString("input_name"), SelectObject.getJSONObject(2).getJSONArray("input_option"), "option_name", new MYAlert.OnSignleListTextSelected() {
                        @Override
                        public void OnSelectedTEXT(JSONObject jsonObject) {
                            try {
                                ((SFNFTextView) findViewById(R.id.TXT_gender)).setText(jsonObject.getString("option_name"));
                                ((SFNFTextView) findViewById(R.id.TXT_gender)).setTag(jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.RL_petBreed:
                try {
                    new MYAlert(EditAnotherPets.this).AlertTextLsit(SelectObject.getJSONObject(3).getString("input_name"), SelectObject.getJSONObject(3).getJSONArray("input_option"), "option_name", new MYAlert.OnSignleListTextSelected() {
                        @Override
                        public void OnSelectedTEXT(JSONObject jsonObject) {
                            try {
                                ((SFNFTextView) findViewById(R.id.TXT_petbreed)).setText(jsonObject.getString("option_name"));
                                ((SFNFTextView) findViewById(R.id.TXT_petbreed)).setTag(jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.RL_petSize:
                try {
                    new MYAlert(EditAnotherPets.this).AlertTextLsit(SelectObject.getJSONObject(4).getString("input_name"), SelectObject.getJSONObject(4).getJSONArray("input_option"), "option_name", new MYAlert.OnSignleListTextSelected() {
                        @Override
                        public void OnSelectedTEXT(JSONObject jsonObject) {
                            try {
                                ((SFNFTextView) findViewById(R.id.TXT_petsize)).setText(jsonObject.getString("option_name"));
                                ((SFNFTextView) findViewById(R.id.TXT_petsize)).setTag(jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.Card_next:
                checkValidationAndNext();
                break;

            case R.id.img_pet:
                showPhotoDialog();
                break;


        }
    }

    private void SETDatField(JSONObject result) {
        try {
            findViewById(R.id.Body).setVisibility(View.VISIBLE);
            other_info = result.getJSONArray("other_info");
            SelectObject = new JSONArray();

            ((SFNFTextView) findViewById(R.id.TXT_petsize)).setText("");
            ((SFNFTextView) findViewById(R.id.TXT_petsize)).setTag(null);

            ((SFNFTextView) findViewById(R.id.TXT_petbreed)).setText("");
            ((SFNFTextView) findViewById(R.id.TXT_petbreed)).setTag(null);

            ((SFNFTextView) findViewById(R.id.TXT_gender)).setText("");
            ((SFNFTextView) findViewById(R.id.TXT_gender)).setTag(null);

            ((SFNFTextView) findViewById(R.id.TXT_Month)).setText("");
            ((SFNFTextView) findViewById(R.id.TXT_Month)).setTag(null);

            ((SFNFTextView) findViewById(R.id.TXT_Year)).setText("");
            ((SFNFTextView) findViewById(R.id.TXT_Year)).setTag(null);


            findViewById(R.id.RADIO1).setVisibility(View.GONE);
            findViewById(R.id.Radio_div1).setVisibility(View.GONE);
            findViewById(R.id.Radio2).setVisibility(View.GONE);
            Radio_Catspayed.setTag("A");
            Rad_catf.setTag("A");
            findViewById(R.id.Raddio_deiver2).setVisibility(View.GONE);
            ((RelativeLayout) findViewById(R.id.RL_petSize)).setVisibility(View.GONE);
            for (int j = 0; j < other_info.length(); j++) {
                JSONObject jsonObject = other_info.getJSONObject(j);

                if (jsonObject.getString("input_field_type").equals("3")) {
                    SelectObject.put(jsonObject);
                }


                if (jsonObject.getString("input_field_type").equals("1")) {
                    TXTName.setText(jsonObject.getString("show_name"));
                    JSONObject object = new JSONObject();
                    object.put("id", jsonObject.getString("option_id"));
                    object.put("value", "");
                    TXTName.setTag(object);
                }
                if (jsonObject.getString("input_field_type").equals("2")) {
                    JSONObject object = new JSONObject();
                    object.put("id", jsonObject.getString("option_id"));
                    object.put("value", "");
                    ((EditText) findViewById(R.id.EditDescribe)).setTag(object);
                    ((EditText) findViewById(R.id.EditDescribe)).setText(jsonObject.getString("show_name"));
                }
                if (jsonObject.getString("input_field_type").equals("5") && Radio_Catspayed.getTag().toString().equals("A")) {
                    findViewById(R.id.RADIO1).setVisibility(View.VISIBLE);
                    Radio_Catspayed.setTag(jsonObject.getString("option_id"));
                    findViewById(R.id.Radio_div1).setVisibility(View.VISIBLE);
                    ((SFNFTextView) findViewById(R.id.B1)).setText(jsonObject.getString("input_name"));
                    JSONArray array = jsonObject.getJSONArray("input_option");
                    Radio_Catspayed.removeAllViewsInLayout();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        RadioButton btn = new RadioButton(this);
                        btn.setText(object.getString("option_name"));
                        btn.setPadding(0, 25, 25, 25);
                        btn.setTag(object);


                        Radio_Catspayed.addView(btn);
                        if(object.getInt("option_status")==1)
                        {
                            Radio_Catspayed.check(btn.getId());
                        }
                    }

                } else if (jsonObject.getString("input_field_type").equals("5")) {
                    findViewById(R.id.Radio2).setVisibility(View.VISIBLE);
                    findViewById(R.id.Raddio_deiver2).setVisibility(View.VISIBLE);
                    ((SFNFTextView) findViewById(R.id.C1)).setText(jsonObject.getString("input_name"));
                    JSONArray array = jsonObject.getJSONArray("input_option");
                    Rad_catf.removeAllViewsInLayout();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        RadioButton btn = new RadioButton(this);
                        btn.setPadding(0, 25, 25, 25);
                        btn.setTag(object);
                        btn.setText(object.getString("option_name"));

                        Rad_catf.addView(btn);
                        if(object.getInt("option_status")==1)
                        {
                            Rad_catf.check(btn.getId());
                        }else {
                            btn.setChecked(false);
                        }

                    }
                }
            }


            ((SFNFTextView) findViewById(R.id.T4)).setText(SelectObject.getJSONObject(0).getString("input_name"));
            for(int i=0;i<SelectObject.getJSONObject(0).getJSONArray("input_option").length();i++)
            {
                JSONObject obj=SelectObject.getJSONObject(0).getJSONArray("input_option").getJSONObject(i);
                if(obj.getInt("option_status")==1) {
                    ((SFNFTextView) findViewById(R.id.TXT_Year)).setText(obj.getString("option_name"));
                    ((SFNFTextView) findViewById(R.id.TXT_Year)).setTag(obj);
                    break;
                }
            }



            ((SFNFTextView) findViewById(R.id.T3)).setText(SelectObject.getJSONObject(1).getString("input_name"));
            for(int i=0;i<SelectObject.getJSONObject(1).getJSONArray("input_option").length();i++)
            {
                JSONObject obj=SelectObject.getJSONObject(1).getJSONArray("input_option").getJSONObject(i);
                if(obj.getInt("option_status")==1) {
                    ((SFNFTextView) findViewById(R.id.TXT_Month)).setText(obj.getString("option_name"));
                    ((SFNFTextView) findViewById(R.id.TXT_Month)).setTag(obj);
                    break;
                }
            }


            ((SFNFTextView) findViewById(R.id.T5)).setText(SelectObject.getJSONObject(2).getString("input_name"));
            for(int i=0;i<SelectObject.getJSONObject(2).getJSONArray("input_option").length();i++)
            {
                JSONObject obj=SelectObject.getJSONObject(2).getJSONArray("input_option").getJSONObject(i);
                if(obj.getInt("option_status")==1) {
                    ((SFNFTextView) findViewById(R.id.TXT_gender)).setText(obj.getString("option_name"));
                    ((SFNFTextView) findViewById(R.id.TXT_gender)).setTag(obj);
                    break;
                }
            }

            ((SFNFTextView) findViewById(R.id.T6)).setText(SelectObject.getJSONObject(3).getString("input_name"));
            for(int i=0;i<SelectObject.getJSONObject(3).getJSONArray("input_option").length();i++)
            {
                JSONObject obj=SelectObject.getJSONObject(3).getJSONArray("input_option").getJSONObject(i);
                if(obj.getInt("option_status")==1) {
                    ((SFNFTextView) findViewById(R.id.TXT_petbreed)).setText(obj.getString("option_name"));
                    ((SFNFTextView) findViewById(R.id.TXT_petbreed)).setTag(obj);
                    break;
                }
            }



            if (SelectObject.length() > 3) {
                ((SFNFTextView) findViewById(R.id.A1)).setText(SelectObject.getJSONObject(4).getString("input_name"));
                for(int i=0;i<SelectObject.getJSONObject(4).getJSONArray("input_option").length();i++)
                {
                    JSONObject obj=SelectObject.getJSONObject(4).getJSONArray("input_option").getJSONObject(i);
                    if(obj.getInt("option_status")==1) {
                        ((SFNFTextView) findViewById(R.id.TXT_petsize)).setText(obj.getString("option_name"));
                        ((SFNFTextView) findViewById(R.id.TXT_petsize)).setTag(obj);
                        break;
                    }
                }
//                ((SFNFTextView)findViewById(R.id.TXT_breed)).setText(SelectObject.getJSONObject(4).getString("input_name"));
                ((RelativeLayout) findViewById(R.id.RL_petSize)).setVisibility(View.VISIBLE);
            }


//
//            if (other_info.length() >= 6 && other_info.getJSONObject(7).getString("input_field_type").equals("5")) {
//                findViewById(R.id.RADIO1).setVisibility(View.VISIBLE);
//                findViewById(R.id.Radio_div1).setVisibility(View.VISIBLE);
//                ((SFNFTextView) findViewById(R.id.B1)).setText(other_info.getJSONObject(7).getString("input_name"));
//                JSONArray array = other_info.getJSONObject(7).getJSONArray("input_option");
//                Radio_Catspayed.removeAllViewsInLayout();
//                for (int i = 0; i < array.length(); i++) {
//                    JSONObject object = array.getJSONObject(i);
//                    RadioButton btn = new RadioButton(this);
//                    btn.setText(object.getString("option_name"));
//                    btn.setPadding(0, 25, 25, 25);
//                    btn.setTag(object);
//                    Radio_Catspayed.addView(btn);
//                }
//
//            } else {
//                findViewById(R.id.RADIO1).setVisibility(View.GONE);
//                findViewById(R.id.Radio_div1).setVisibility(View.GONE);
//            }
//            if (other_info.length() >= 9 && other_info.getJSONObject(8).getString("input_field_type").equals("5")) {
//                findViewById(R.id.Radio2).setVisibility(View.VISIBLE);
//                findViewById(R.id.Raddio_deiver2).setVisibility(View.VISIBLE);
//                ((SFNFTextView) findViewById(R.id.C1)).setText(other_info.getJSONObject(8).getString("input_name"));
//                JSONArray array = other_info.getJSONObject(8).getJSONArray("input_option");
//                Rad_catf.removeAllViewsInLayout();
//                for (int i = 0; i < array.length(); i++) {
//                    JSONObject object = array.getJSONObject(i);
//                    RadioButton btn = new RadioButton(this);
//                    btn.setPadding(0, 25, 25, 25);
//                    btn.setTag(object);
//                    btn.setText(object.getString("option_name"));
//                    Rad_catf.addView(btn);
//                }
//
//            } else {
//                findViewById(R.id.Radio2).setVisibility(View.GONE);
//                findViewById(R.id.Raddio_deiver2).setVisibility(View.GONE);
//            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void checkValidationAndNext() {

        ((SFNFTextView) findViewById(R.id.Txt_type)).setHintTextColor(ContextCompat.getColor(this, R.color.text_dark_gray));
        ((EditText) findViewById(R.id.TXTName)).setHintTextColor(ContextCompat.getColor(this, R.color.text_dark_gray));
        ((SFNFTextView) findViewById(R.id.TXT_Year)).setHintTextColor(ContextCompat.getColor(this, R.color.text_dark_gray));
        ((SFNFTextView) findViewById(R.id.TXT_Month)).setHintTextColor(ContextCompat.getColor(this, R.color.text_dark_gray));
        ((SFNFTextView) findViewById(R.id.TXT_gender)).setHintTextColor(ContextCompat.getColor(this, R.color.text_dark_gray));
        ((SFNFTextView) findViewById(R.id.TXT_petbreed)).setHintTextColor(ContextCompat.getColor(this, R.color.text_dark_gray));
        ((SFNFTextView) findViewById(R.id.TXT_petsize)).setHintTextColor(ContextCompat.getColor(this, R.color.text_dark_gray));
        ((EditText) findViewById(R.id.EditDescribe)).setHintTextColor(ContextCompat.getColor(this, R.color.text_dark_gray));


        if (((SFNFTextView) findViewById(R.id.Txt_type)).getText().toString().trim().equals("")) {
            ((SFNFTextView) findViewById(R.id.Txt_type)).setHintTextColor(ContextCompat.getColor(this, R.color.btn_red));
            Log.i("Txt_type", "Txt_type");
        } else {


                Log.i("TXTName", "TXTName");
                ((SFNFTextView) findViewById(R.id.Txt_type)).setHintTextColor(ContextCompat.getColor(this, R.color.text_dark_gray));
                if (((EditText) findViewById(R.id.TXTName)).getText().toString().trim().equals("")) {

                    ((EditText) findViewById(R.id.TXTName)).setHintTextColor(ContextCompat.getColor(this, R.color.btn_red));
                    ((EditText) findViewById(R.id.TXTName)).requestFocus();
                } else {
                    Log.i("TXT_Year", "TXT_Year");
                    ((EditText) findViewById(R.id.TXTName)).setHintTextColor(ContextCompat.getColor(this, R.color.text_dark_gray));

                    if (((SFNFTextView) findViewById(R.id.TXT_Year)).getText().toString().trim().equals("")) {
                        ((SFNFTextView) findViewById(R.id.TXT_Year)).setHintTextColor(ContextCompat.getColor(this, R.color.btn_red));
                    } else {
                        Log.i("TXT_Month", "TXT_Month");
                        ((SFNFTextView) findViewById(R.id.TXT_Year)).setHintTextColor(ContextCompat.getColor(this, R.color.text_dark_gray));
                        if (((SFNFTextView) findViewById(R.id.TXT_Month)).getText().toString().trim().equals("")) {
                            ((SFNFTextView) findViewById(R.id.TXT_Month)).setHintTextColor(ContextCompat.getColor(this, R.color.btn_red));
                        } else {
                            Log.i("TXT_gender", "TXT_gender");
                            ((SFNFTextView) findViewById(R.id.TXT_Month)).setHintTextColor(ContextCompat.getColor(this, R.color.text_dark_gray));
                            if (((SFNFTextView) findViewById(R.id.TXT_gender)).getText().toString().trim().equals("")) {
                                ((SFNFTextView) findViewById(R.id.TXT_gender)).setHintTextColor(ContextCompat.getColor(this, R.color.btn_red));
                            } else {
                                Log.i("TXT_breed", "TXT_breed");
                                ((SFNFTextView) findViewById(R.id.TXT_gender)).setHintTextColor(ContextCompat.getColor(this, R.color.text_dark_gray));
                                if (((SFNFTextView) findViewById(R.id.TXT_petbreed)).getText().toString().trim().equals("")) {
                                    ((SFNFTextView) findViewById(R.id.TXT_petbreed)).setHintTextColor(ContextCompat.getColor(this, R.color.btn_red));
                                } else {
                                    Log.i("TXT_petsize", "TXT_petsize");
                                    ((SFNFTextView) findViewById(R.id.TXT_petbreed)).setHintTextColor(ContextCompat.getColor(this, R.color.text_dark_gray));
                                    if (((SFNFTextView) findViewById(R.id.TXT_petsize)).getText().toString().trim().equals("")
                                            && findViewById(R.id.RL_petSize).getVisibility() == View.VISIBLE) {
                                        ((SFNFTextView) findViewById(R.id.TXT_petsize)).setHintTextColor(ContextCompat.getColor(this, R.color.btn_red));
                                    } else {
                                        ((SFNFTextView) findViewById(R.id.TXT_petsize)).setHintTextColor(ContextCompat.getColor(this, R.color.text_dark_gray));
                                        if (((EditText) findViewById(R.id.EditDescribe)).getText().toString().trim().equals("")) {
                                            ((EditText) findViewById(R.id.EditDescribe)).setHintTextColor(ContextCompat.getColor(this, R.color.btn_red));
                                            ((EditText) findViewById(R.id.EditDescribe)).requestFocus();
                                        } else {
                                            ((EditText) findViewById(R.id.EditDescribe)).setHintTextColor(ContextCompat.getColor(this, R.color.text_dark_gray));
                                            InputArea = new JSONArray();
                                            RadioArray = new JSONArray();
                                            Text = new JSONArray();
                                            Select = new JSONArray();


                                            if (Radio_Catspayed.getCheckedRadioButtonId() == -1) {
                                                // No item selected
                                                Toast.makeText(this, "No item selected in pet friendly", Toast.LENGTH_SHORT).show();
                                            } else {
                                                String radiovalue1 = ((RadioButton) findViewById(Radio_Catspayed.getCheckedRadioButtonId())).getTag().toString();
                                                try {
                                                    RadioArray.put(new JSONObject(radiovalue1));
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                                if (findViewById(R.id.Radio2).getVisibility() == View.VISIBLE && Rad_catf.getCheckedRadioButtonId() == -1) {
                                                    Toast.makeText(this, "No item selected in pet spayed or neutered", Toast.LENGTH_SHORT).show();
                                                } else {

                                                    if(findViewById(R.id.Radio2).getVisibility() == View.VISIBLE) {
                                                        String radiovalue2 = ((RadioButton) findViewById(Rad_catf.getCheckedRadioButtonId())).getTag().toString();
                                                        try {
                                                            RadioArray.put(new JSONObject(radiovalue2));
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }



                                                    try {
                                                        JSONObject textarea = new JSONObject();
                                                        textarea.put("id", new JSONObject(((EditText) findViewById(R.id.EditDescribe)).getTag().toString()).getString("id"));
                                                        textarea.put("value", ((EditText) findViewById(R.id.EditDescribe)).getText());
                                                        InputArea.put(textarea);

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }


                                                    try {
                                                        JSONObject textarea = new JSONObject();
                                                        textarea.put("id", new JSONObject(((EditText) findViewById(R.id.TXTName)).getTag().toString()).getString("id"));
                                                        textarea.put("value", ((EditText) findViewById(R.id.TXTName)).getText());
                                                        Text.put(textarea);

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }



                                                    try {
                                                        Select.put(new JSONObject(((SFNFTextView) findViewById(R.id.TXT_gender)).getTag().toString()));
                                                        Select.put(new JSONObject(((SFNFTextView) findViewById(R.id.TXT_petbreed)).getTag().toString()));

                                                        if(findViewById(R.id.RL_petSize).getVisibility()==View.VISIBLE) {
                                                            Select.put(new JSONObject(((SFNFTextView) findViewById(R.id.TXT_petsize)).getTag().toString()));
                                                        }

                                                        Select.put(new JSONObject(((SFNFTextView) findViewById(R.id.TXT_Month)).getTag().toString()));
                                                        Select.put(new JSONObject(((SFNFTextView) findViewById(R.id.TXT_Year)).getTag().toString()));
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }



                                                    Loger.MSG(" Name-", Text.toString());
                                                    Loger.MSG(" TeatArea-", InputArea.toString());
                                                    Loger.MSG(" Radio-", RadioArray.toString());
                                                    Loger.MSG(" Select-", Select.toString());


                                                    ArrayList<APIPOSTDATA> Params = new ArrayList<>();
                                                    APIPOSTDATA apipostdata = new APIPOSTDATA();
                                                    apipostdata.setPARAMS("user_id");
                                                    Loger.MSG("user_id", "" + AppContsnat.UserId);
                                                    apipostdata.setValues(AppContsnat.UserId);
                                                    Params.add(apipostdata);

                                                    apipostdata = new APIPOSTDATA();
                                                    apipostdata.setPARAMS("langid");
                                                    apipostdata.setValues(AppContsnat.Language);
                                                    Params.add(apipostdata);

                                                    apipostdata = new APIPOSTDATA();
                                                    apipostdata.setPARAMS("pettypeid");
                                                    apipostdata.setValues(AppContsnat.Language);
                                                    Params.add(apipostdata);

                                                    apipostdata = new APIPOSTDATA();
                                                    apipostdata.setPARAMS("text");
                                                    apipostdata.setValues(Text.toString());
                                                    Params.add(apipostdata);

                                                    apipostdata = new APIPOSTDATA();
                                                    apipostdata.setPARAMS("textarea");
                                                    apipostdata.setValues(InputArea.toString());
                                                    Params.add(apipostdata);

                                                    apipostdata = new APIPOSTDATA();
                                                    apipostdata.setPARAMS("radio");
                                                    apipostdata.setValues(RadioArray.toString());
                                                    Params.add(apipostdata);

                                                    apipostdata = new APIPOSTDATA();
                                                    apipostdata.setPARAMS("select");
                                                    apipostdata.setValues(Select.toString());
                                                    Params.add(apipostdata);
                                                    apipostdata = new APIPOSTDATA();
                                                    apipostdata.setPARAMS("pettypeid");
                                                    apipostdata.setValues(PetTypeId);
                                                    Params.add(apipostdata);
                                                    apipostdata = new APIPOSTDATA();
                                                    apipostdata.setPARAMS("edit_pet_id");
                                                    apipostdata.setValues(EditId);
                                                    Params.add(apipostdata);

                                                    ArrayList<File> PhotoFiles=new ArrayList<>();
                                                    PhotoFiles.add(photofile);
                                                    Loger.MSG("@@"," SIZE-"+PhotoFiles.size()+"");



                                                    appLoader.Show();
                                                    new JSONPerser().API_FOR_With_Photo_POST(AppContsnat.BASEURL + "app_users_addpetinfo?", Params,  PhotoFiles, new JSONPerser.JSONRESPONSE() {
                                                        @Override
                                                        public void OnSuccess(String Result) {
                                                            appLoader.Dismiss();
                                                            try {
                                                                new MYAlert(EditAnotherPets.this).AlertOnly(getString(R.string.add_another_pets), new JSONObject(Result).getString("message"), new MYAlert.OnlyMessage() {
                                                                    @Override
                                                                    public void OnOk(boolean res) {
                                                                        onBackPressed();
                                                                    }
                                                                });
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }

                                                        @Override
                                                        public void OnError(String Error, String Response) {
                                                            Loger.MSG("Error", Error);
                                                            appLoader.Dismiss();
                                                        }

                                                        @Override
                                                        public void OnError(String Error) {
                                                            Loger.MSG("Error2", Error);
                                                            appLoader.Dismiss();
                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

        }
    }

    private void showPhotoDialog() {
        AlertDialog.Builder alertbuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View LayView = inflater.inflate(R.layout.alert_dialog_image_taker, null);

        ImageView img_gallery = (ImageView) LayView.findViewById(R.id.img_gallery);
        ImageView img_camera = (ImageView) LayView.findViewById(R.id.img_camera);

        SFNFTextView TXTTitle = (SFNFTextView) LayView.findViewById(R.id.Title);
        TXTTitle.setText(getResources().getString(R.string.add_image));

        img_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION1);
                } else {
                    openImageGallery();
                }
                Dialog.dismiss();
            }
        });

        img_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_WRITE_PERMISSION2);
                } else {
                    openCamera();
                }

                Dialog.dismiss();
            }
        });

        alertbuilder.setView(LayView);
        Dialog = alertbuilder.create();
        Dialog.show();
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


        Loger.MSG("@@ CAM SERVICEID-",getIntent().getStringExtra("SERVICEID")+"\n USERID-"+getIntent().getStringExtra("USERID"));
        if (photofile != null) {
            outState.putString(BITMAP_STORAGE_URL, photofile.getAbsolutePath().toString());
            Log.i("@@", "onSaveInstanceState : " + photofile.getAbsolutePath().toString());
        }
    }


    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        try {



            if (photofile == null) {

                photofile = new File(savedInstanceState.getString(BITMAP_STORAGE_URL));
                Glide.with(getApplicationContext()).load(photofile).into(img_pet);
            }
            Log.i("@@", "onRestoreInstanceState : " + photofile.getAbsolutePath().toString());
        }catch (NullPointerException e)
        {}

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CAMERA_CAPTURE && resultCode == RESULT_OK && data != null ) {

            if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M) {
                Uri selectedImageURI = data.getData();
                try {
                    photofile = new File(ImageFilePath.getPath(getApplicationContext(), selectedImageURI));
                    Glide.with(getApplicationContext()).load(photofile).into(img_pet);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                storeImage(photo);
            }

        } else if (requestCode ==PICK_IMAGE_REQUEST  && resultCode == RESULT_OK && photofile != null) {
            Uri selectedImageURI = data.getData();
            try {
                photofile = new File(ImageFilePath.getPath(getApplicationContext(), selectedImageURI));
                Glide.with(getApplicationContext()).load(photofile).into(img_pet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Image Error", Toast.LENGTH_SHORT).show();
        }
    }
    private void storeImage(Bitmap image) {
        File pictureFile =  new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "FREEWILDER");
        if (pictureFile == null) {
            Loger.MSG("@@",
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            Loger.MSG("@@","PIPATH"+pictureFile.getAbsolutePath());
            String uriSting = (pictureFile.getAbsolutePath()  + System.currentTimeMillis() + ".jpg");
            photofile=new File(uriSting);
            FileOutputStream fos = new FileOutputStream(uriSting);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
            Glide.with(getApplicationContext()).load(photofile).into(img_pet);
        } catch (FileNotFoundException e) {
            Loger.MSG("@@", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Loger.MSG("@@", "Error accessing file: " + e.getMessage());
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openImageGallery();
        }
        if (requestCode == REQUEST_WRITE_PERMISSION2 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        }
    }

    private void openImageGallery() {
        try {
            photofile = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);



        Dialog.dismiss();
    }

    private void openCamera() {
        try {
            photofile = createImageFile();
            Log.d("image path--", String.valueOf(photofile));

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePictureIntent, CAMERA_CAPTURE);

        } catch (Exception e) {
            Log.v("Catch Exception", e.toString());
        }
    }

    File createImageFile() throws IOException {

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());///new approach
        String imagefilename = "IMAGE_" + timestamp + "_";///new approach
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imagefilename, ".jpg", storageDirectory);/////new approach
        return image;
    }




    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }
}
