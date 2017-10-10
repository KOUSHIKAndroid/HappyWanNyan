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
import com.happywannyan.Constant.AppConstant;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.POJO.SetGetAPIPostData;
import com.happywannyan.R;
import com.happywannyan.Utils.AppLoader;
import com.happywannyan.Utils.CustomJSONParser;
import com.happywannyan.Utils.ImageFilePath;
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

public class AddAnotherPetsActivity extends AppCompatActivity implements View.OnClickListener {
    JSONArray other_info;
    AppLoader appLoader;
    JSONArray PetService;
    String PetTypeId = "";
    EditText TXTName;
    JSONArray SelectObject;
    RadioGroup Radio_Catspayed, Rad_catf;
    JSONArray Text, InputArea, Select, RadioArray;

    AlertDialog Dialog;
    ImageView img_pet;
    private static final int REQUEST_WRITE_PERMISSION1 = 3000;
    private static final int REQUEST_WRITE_PERMISSION2 = 4000;
    final String BITMAP_STORAGE_URL = "IMAGE_URL";
    private int PICK_IMAGE_REQUEST = 100;
    private int CAMERA_CAPTURE = 200;

    File photofile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_another_pets);
        appLoader = new AppLoader(this);
        appLoader.Show();
        TXTName = (EditText) findViewById(R.id.TXTName);
        Radio_Catspayed = (RadioGroup) findViewById(R.id.Radio_Catspayed);
        Rad_catf = (RadioGroup) findViewById(R.id.Rad_catf);
        img_pet = (ImageView) findViewById(R.id.img_pet);
        new AppConstant(this);
        new CustomJSONParser().APIForGetMethod(AppConstant.BASEURL + "parent_service?langid=en&user_id=" + AppConstant.UserId, new ArrayList<SetGetAPIPostData>(), new CustomJSONParser.JSONResponseInterface() {
            @Override
            public void OnSuccess(String Result) {
                try {
                    JSONObject object = new JSONObject(Result);
                    Loger.MSG("@@" + getClass().getName(), object.toString());
                    appLoader.Dismiss();
                    PetService = object.getJSONArray("allPetDetails");

                } catch (JSONException e) {
                    appLoader.Dismiss();
                }
            }

            @Override
            public void OnError(String Error, String Response) {
                appLoader.Dismiss();
            }

            @Override
            public void OnError(String Error) {
                appLoader.Dismiss();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        new AppConstant(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Hea:

                new MYAlert(AddAnotherPetsActivity.this).AlertTextLsit("" + getResources().getString(R.string.Chosepettype),
                        PetService, "name", new MYAlert.OnSingleListTextSelected() {
                            @Override
                            public void OnSelectedTEXT(JSONObject jsonObject) {
                                Loger.MSG("@@ Data", "" + jsonObject);
                                try {
                                    PetTypeId = jsonObject.getString("id");
                                    ((SFNFTextView) findViewById(R.id.Txt_type)).setText(jsonObject.getString("name"));
                                    ArrayList<SetGetAPIPostData> params = new ArrayList<>();
                                    SetGetAPIPostData setGetAPIPostData = new SetGetAPIPostData();
                                    setGetAPIPostData.setPARAMS("user_id");
                                    setGetAPIPostData.setValues(AppConstant.UserId);
                                    params.add(setGetAPIPostData);
                                    setGetAPIPostData = new SetGetAPIPostData();
                                    setGetAPIPostData.setPARAMS("lang_id");
                                    setGetAPIPostData.setValues(AppConstant.Language);
                                    params.add(setGetAPIPostData);
                                    setGetAPIPostData = new SetGetAPIPostData();
                                    setGetAPIPostData.setPARAMS("pet_type_id");
                                    setGetAPIPostData.setValues(PetTypeId);
                                    params.add(setGetAPIPostData);
                                    appLoader.Show();
                                    new CustomJSONParser().APIForGetMethod(AppConstant.BASEURL + "app_users_petinfo?", params, new CustomJSONParser.JSONResponseInterface() {
                                        @Override
                                        public void OnSuccess(String Result) {
                                            findViewById(R.id.Body).setVisibility(View.VISIBLE);
                                            SETDatField(Result);
                                            appLoader.Dismiss();
                                        }

                                        @Override
                                        public void OnError(String Error, String Response) {
                                            appLoader.Dismiss();
                                        }

                                        @Override
                                        public void OnError(String Error) {
                                            appLoader.Dismiss();
                                        }
                                    });

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });


                break;

            case R.id.IMG_icon_back:
                finish();
                break;
            case R.id.SE_Year:
                try {
                    new MYAlert(AddAnotherPetsActivity.this).AlertTextLsit(SelectObject.getJSONObject(0).getString("input_name"), SelectObject.getJSONObject(0).getJSONArray("input_option"), "option_name", new MYAlert.OnSingleListTextSelected() {
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
                    new MYAlert(AddAnotherPetsActivity.this).AlertTextLsit(SelectObject.getJSONObject(1).getString("input_name"), SelectObject.getJSONObject(1).getJSONArray("input_option"), "option_name", new MYAlert.OnSingleListTextSelected() {
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
                    new MYAlert(AddAnotherPetsActivity.this).AlertTextLsit(SelectObject.getJSONObject(2).getString("input_name"), SelectObject.getJSONObject(2).getJSONArray("input_option"), "option_name", new MYAlert.OnSingleListTextSelected() {
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
                    new MYAlert(AddAnotherPetsActivity.this).AlertTextLsit(SelectObject.getJSONObject(3).getString("input_name"), SelectObject.getJSONObject(3).getJSONArray("input_option"), "option_name", new MYAlert.OnSingleListTextSelected() {
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
                    new MYAlert(AddAnotherPetsActivity.this).AlertTextLsit(SelectObject.getJSONObject(4).getString("input_name"), SelectObject.getJSONObject(4).getJSONArray("input_option"), "option_name", new MYAlert.OnSingleListTextSelected() {
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

    private void SETDatField(String result) {
        Loger.MSG("@@" + getClass().getName(), result);
        try {
            JSONObject MainObject = new JSONObject(result);
            JSONObject info_array = MainObject.getJSONArray("info_array").getJSONObject(0);
            other_info = info_array.getJSONArray("other_info");
            SelectObject = new JSONArray();

            ((SFNFTextView) findViewById(R.id.TXT_petsize)).setText("");
            ((SFNFTextView) findViewById(R.id.TXT_petsize)).setTag(null);

            ((SFNFTextView) findViewById(R.id.TXT_petbreed)).setText("");
            ((SFNFTextView) findViewById(R.id.TXT_petbreed)).setTag(null);

            ((SFNFTextView) findViewById(R.id.TXT_gender)).setText("");
            ((SFNFTextView) findViewById(R.id.TXT_gender)).setTag(null);

            //((SFNFTextView) findViewById(R.id.TXT_Month)).setText("");
            //((SFNFTextView) findViewById(R.id.TXT_Month)).setTag(null);

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

                if(jsonObject.getString("input_field_type").equals("3") && jsonObject.getString("input_name").equalsIgnoreCase(getResources().getString(R.string.months))){

                    JSONObject jsonObjectMonth=new JSONObject();
                    jsonObjectMonth.put("option_id",jsonObject.getString("option_id"));
                    jsonObjectMonth.put("option_value","0");
                    jsonObjectMonth.put("option_name","");
                    jsonObjectMonth.put("option_status",0);

                    Loger.MSG("jsonObjectMonth",""+jsonObjectMonth);

                    ((SFNFTextView) findViewById(R.id.TXT_Month)).setText("");
                    ((SFNFTextView) findViewById(R.id.TXT_Month)).setTag(jsonObjectMonth);
                }

                if (jsonObject.getString("input_field_type").equals("3"))
                    SelectObject.put(jsonObject);


                if (jsonObject.getString("input_field_type").equals("1")) {
                    TXTName.setHint(jsonObject.getString("show_name"));
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
                    }
                }
            }


            ((SFNFTextView) findViewById(R.id.T4)).setText(SelectObject.getJSONObject(0).getString("input_name"));
            ((SFNFTextView) findViewById(R.id.T3)).setText(SelectObject.getJSONObject(1).getString("input_name"));
            ((SFNFTextView) findViewById(R.id.T5)).setText(SelectObject.getJSONObject(2).getString("input_name"));
            ((SFNFTextView) findViewById(R.id.T6)).setText(SelectObject.getJSONObject(3).getString("input_name"));
            if (SelectObject.length() > 3) {
                ((SFNFTextView) findViewById(R.id.A1)).setText(SelectObject.getJSONObject(4).getString("input_name"));
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

        ((SFNFTextView) findViewById(R.id.Txt_type)).setHintTextColor(ContextCompat.getColor(this, R.color.colorTextDarkGray));
        ((EditText) findViewById(R.id.TXTName)).setHintTextColor(ContextCompat.getColor(this, R.color.colorTextDarkGray));
        ((SFNFTextView) findViewById(R.id.TXT_Year)).setHintTextColor(ContextCompat.getColor(this, R.color.colorTextDarkGray));
        //((SFNFTextView) findViewById(R.id.TXT_Month)).setHintTextColor(ContextCompat.getColor(this, R.color.colorTextDarkGray));
        ((SFNFTextView) findViewById(R.id.TXT_gender)).setHintTextColor(ContextCompat.getColor(this, R.color.colorTextDarkGray));
        ((SFNFTextView) findViewById(R.id.TXT_petbreed)).setHintTextColor(ContextCompat.getColor(this, R.color.colorTextDarkGray));
        ((SFNFTextView) findViewById(R.id.TXT_petsize)).setHintTextColor(ContextCompat.getColor(this, R.color.colorTextDarkGray));
        ((EditText) findViewById(R.id.EditDescribe)).setHintTextColor(ContextCompat.getColor(this, R.color.colorTextDarkGray));


        if (((SFNFTextView) findViewById(R.id.Txt_type)).getText().toString().trim().equals("")) {
            ((SFNFTextView) findViewById(R.id.Txt_type)).setHintTextColor(ContextCompat.getColor(this, R.color.colorBtnRed));
            Log.i("Txt_type", "Txt_type");
        } else {

            if (photofile == null) {
                Toast.makeText(this,getResources().getString(R.string.select_pet_image), Toast.LENGTH_SHORT).show();
            } else {

                Log.i("TXTName", "TXTName");
                ((SFNFTextView) findViewById(R.id.Txt_type)).setHintTextColor(ContextCompat.getColor(this, R.color.colorTextDarkGray));
                if (((EditText) findViewById(R.id.TXTName)).getText().toString().trim().equals("")) {

                    ((EditText) findViewById(R.id.TXTName)).setHintTextColor(ContextCompat.getColor(this, R.color.colorBtnRed));
                    ((EditText) findViewById(R.id.TXTName)).requestFocus();
                } else {
                    Log.i("TXT_Year", "TXT_Year");
                    ((EditText) findViewById(R.id.TXTName)).setHintTextColor(ContextCompat.getColor(this, R.color.colorTextDarkGray));

                    if (((SFNFTextView) findViewById(R.id.TXT_Year)).getText().toString().trim().equals("")) {
                        ((SFNFTextView) findViewById(R.id.TXT_Year)).setHintTextColor(ContextCompat.getColor(this, R.color.colorBtnRed));
                    } else {
                        Log.i("TXT_Month", "TXT_Month");
                        //((SFNFTextView) findViewById(R.id.TXT_Year)).setHintTextColor(ContextCompat.getColor(this, R.color.colorTextDarkGray));
//                        if (((SFNFTextView) findViewById(R.id.TXT_Month)).getText().toString().trim().equals("")) {
//                            ((SFNFTextView) findViewById(R.id.TXT_Month)).setHintTextColor(ContextCompat.getColor(this, R.color.colorBtnRed));
//                        } else {
                            Log.i("TXT_gender", "TXT_gender");
                           // ((SFNFTextView) findViewById(R.id.TXT_Month)).setHintTextColor(ContextCompat.getColor(this, R.color.colorTextDarkGray));
                            if (((SFNFTextView) findViewById(R.id.TXT_gender)).getText().toString().trim().equals("")) {
                                ((SFNFTextView) findViewById(R.id.TXT_gender)).setHintTextColor(ContextCompat.getColor(this, R.color.colorBtnRed));
                            } else {
                                Log.i("TXT_breed", "TXT_breed");
                                ((SFNFTextView) findViewById(R.id.TXT_gender)).setHintTextColor(ContextCompat.getColor(this, R.color.colorTextDarkGray));
                                if (((SFNFTextView) findViewById(R.id.TXT_petbreed)).getText().toString().trim().equals("")) {
                                    ((SFNFTextView) findViewById(R.id.TXT_petbreed)).setHintTextColor(ContextCompat.getColor(this, R.color.colorBtnRed));
                                } else {
                                    Log.i("TXT_petsize", "TXT_petsize");
                                    ((SFNFTextView) findViewById(R.id.TXT_petbreed)).setHintTextColor(ContextCompat.getColor(this, R.color.colorTextDarkGray));
                                    if (((SFNFTextView) findViewById(R.id.TXT_petsize)).getText().toString().trim().equals("")
                                            && findViewById(R.id.RL_petSize).getVisibility() == View.VISIBLE) {
                                        ((SFNFTextView) findViewById(R.id.TXT_petsize)).setHintTextColor(ContextCompat.getColor(this, R.color.colorBtnRed));
                                    } else {
                                        ((SFNFTextView) findViewById(R.id.TXT_petsize)).setHintTextColor(ContextCompat.getColor(this, R.color.colorTextDarkGray));
                                        if (((EditText) findViewById(R.id.EditDescribe)).getText().toString().trim().equals("")) {
                                            ((EditText) findViewById(R.id.EditDescribe)).setHintTextColor(ContextCompat.getColor(this, R.color.colorBtnRed));
                                            ((EditText) findViewById(R.id.EditDescribe)).requestFocus();
                                        } else {
                                            ((EditText) findViewById(R.id.EditDescribe)).setHintTextColor(ContextCompat.getColor(this, R.color.colorTextDarkGray));
                                            InputArea = new JSONArray();
                                            RadioArray = new JSONArray();
                                            Text = new JSONArray();
                                            Select = new JSONArray();


                                            if (Radio_Catspayed.getCheckedRadioButtonId() == -1) {
                                                // No item selected
                                                Log.i("radiovalue", "No item selected");
                                                Toast.makeText(this, getString(R.string.pleaseselect) + " " + ((SFNFTextView) findViewById(R.id.B1)).getText(), Toast.LENGTH_SHORT).show();
                                            } else {
                                                String radiovalue1 = ((RadioButton) findViewById(Radio_Catspayed.getCheckedRadioButtonId())).getTag().toString();
                                                try {
                                                    RadioArray.put(new JSONObject(radiovalue1));
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                                if (findViewById(R.id.Radio2).getVisibility() == View.VISIBLE && Rad_catf.getCheckedRadioButtonId() == -1) {
                                                    Log.i("radiovalue", "No item selected");
                                                    Toast.makeText(this, getString(R.string.pleaseselect) + " " + ((SFNFTextView) findViewById(R.id.C1)).getText(), Toast.LENGTH_SHORT).show();
                                                } else {

                                                    if (findViewById(R.id.Radio2).getVisibility() == View.VISIBLE) {
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

                                                        if (findViewById(R.id.RL_petSize).getVisibility() == View.VISIBLE) {
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


                                                    ArrayList<SetGetAPIPostData> Params = new ArrayList<>();
                                                    SetGetAPIPostData setGetAPIPostData = new SetGetAPIPostData();
                                                    setGetAPIPostData.setPARAMS("user_id");
                                                    Loger.MSG("user_id", "" + AppConstant.UserId);
                                                    setGetAPIPostData.setValues(AppConstant.UserId);
                                                    Params.add(setGetAPIPostData);

                                                    setGetAPIPostData = new SetGetAPIPostData();
                                                    setGetAPIPostData.setPARAMS("langid");
                                                    setGetAPIPostData.setValues(AppConstant.Language);
                                                    Params.add(setGetAPIPostData);

                                                    setGetAPIPostData = new SetGetAPIPostData();
                                                    setGetAPIPostData.setPARAMS("pettypeid");
                                                    setGetAPIPostData.setValues(AppConstant.Language);
                                                    Params.add(setGetAPIPostData);

                                                    setGetAPIPostData = new SetGetAPIPostData();
                                                    setGetAPIPostData.setPARAMS("text");
                                                    setGetAPIPostData.setValues(Text.toString());
                                                    Params.add(setGetAPIPostData);

                                                    setGetAPIPostData = new SetGetAPIPostData();
                                                    setGetAPIPostData.setPARAMS("textarea");
                                                    setGetAPIPostData.setValues(InputArea.toString());
                                                    Params.add(setGetAPIPostData);

                                                    setGetAPIPostData = new SetGetAPIPostData();
                                                    setGetAPIPostData.setPARAMS("radio");
                                                    setGetAPIPostData.setValues(RadioArray.toString());
                                                    Params.add(setGetAPIPostData);

                                                    setGetAPIPostData = new SetGetAPIPostData();
                                                    setGetAPIPostData.setPARAMS("select");
                                                    setGetAPIPostData.setValues(Select.toString());
                                                    Params.add(setGetAPIPostData);
                                                    setGetAPIPostData = new SetGetAPIPostData();
                                                    setGetAPIPostData.setPARAMS("pettypeid");
                                                    setGetAPIPostData.setValues(PetTypeId);
                                                    Params.add(setGetAPIPostData);

                                                    ArrayList<File> PhotoFiles = new ArrayList<>();
                                                    PhotoFiles.add(photofile);
                                                    Loger.MSG("@@", " SIZE-" + PhotoFiles.size() + "");


                                                    appLoader.Show();

                                                    CustomJSONParser.ImageParam = "petimg";

                                                    new CustomJSONParser().APIForWithPhotoPostMethod(AppConstant.BASEURL + "app_users_addpetinfo?", Params, PhotoFiles, new CustomJSONParser.JSONResponseInterface() {
                                                        @Override
                                                        public void OnSuccess(String Result) {
                                                            appLoader.Dismiss();
                                                            try {
                                                                new MYAlert(AddAnotherPetsActivity.this).AlertOnly(getString(R.string.add_another_pets), new JSONObject(Result).getString("message"), new MYAlert.OnlyMessage() {
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
                        //}
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
                    requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION1);
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
                    requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_WRITE_PERMISSION2);
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


        Loger.MSG("@@ CAM SERVICEID-", getIntent().getStringExtra("SERVICEID") + "\n USERID-" + getIntent().getStringExtra("USERID"));
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
        } catch (NullPointerException e) {
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CAMERA_CAPTURE && resultCode == RESULT_OK && data != null) {

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                Uri selectedImageURI = data.getData();
                try {
                    photofile = new File(ImageFilePath.getPath(getApplicationContext(), selectedImageURI));
                    Glide.with(getApplicationContext()).load(photofile).into(img_pet);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                storeImage(photo);
            }

        } else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && photofile != null) {
            Uri selectedImageURI = data.getData();
            try {
                photofile = new File(ImageFilePath.getPath(getApplicationContext(), selectedImageURI));
                Glide.with(getApplicationContext()).load(photofile).into(img_pet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.image_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void storeImage(Bitmap image) {
        File pictureFile = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "FREEWILDER");
        if (pictureFile == null) {
            Loger.MSG("@@",
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            Loger.MSG("@@", "PIPATH" + pictureFile.getAbsolutePath());
            String uriSting = (pictureFile.getAbsolutePath() + System.currentTimeMillis() + ".jpg");
            photofile = new File(uriSting);
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
