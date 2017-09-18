package com.happywannyan.Activities.profile;

import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.happywannyan.Constant.AppConstant;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.POJO.SetGetAPIPostData;
import com.happywannyan.R;
import com.happywannyan.Utils.AppCalender;
import com.happywannyan.Utils.AppLoader;
import com.happywannyan.Utils.CustomJSONParser;
import com.happywannyan.Utils.Loger;
import com.happywannyan.Utils.MYAlert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class MeetUpWannyanActivity extends AppCompatActivity implements View.OnClickListener, AppCalender.OnDateSelect {
    AppLoader appLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetup_wannyan);
        appLoader = new AppLoader(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.EDX_no_of_pets:
                JSONArray array = new JSONArray();
                try {
                    array.put(new JSONObject().put("name", "0"));
                    array.put(new JSONObject().put("name", "1"));
                    array.put(new JSONObject().put("name", "2"));
                    array.put(new JSONObject().put("name", "3"));
                    array.put(new JSONObject().put("name", "4"));
                    array.put(new JSONObject().put("name", "5"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Loger.MSG("@@" + getClass().getName(), array.toString());
                new MYAlert(this).AlertTextLsit(getString(R.string.numberofpets), array, "name", new MYAlert.OnSingleListTextSelected() {
                    @Override
                    public void OnSelectedTEXT(JSONObject jsonObject) {
                        try {
                            ((SFNFTextView) findViewById(R.id.EDX_no_of_pets)).setText(jsonObject.getString("name"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;

            case R.id.startdate:
                DialogFragment newFragment = AppCalender.newInstance(R.id.startdate);
                newFragment.show(getFragmentManager(), "datePicker");
                break;
            case R.id.AlterDate:
                newFragment = AppCalender.newInstance(R.id.AlterDate);
                newFragment.show(getFragmentManager(), "datePicker");
                break;
            case R.id.IMG_icon_drwaer:
                onBackPressed();
                break;
            case R.id.reservation:
                ArrayList<SetGetAPIPostData> setGetAPIPostDatas = new ArrayList<>();
                SetGetAPIPostData setGetAPIPostData = new SetGetAPIPostData();
                setGetAPIPostData.setPARAMS("user_id");
                setGetAPIPostData.setValues(AppConstant.UserId);
                setGetAPIPostDatas.add(setGetAPIPostData);
                setGetAPIPostData = new SetGetAPIPostData();
                setGetAPIPostData.setPARAMS("sitter_id");
                setGetAPIPostData.setValues(getIntent().getStringExtra("DATA"));
                setGetAPIPostDatas.add(setGetAPIPostData);

                setGetAPIPostData = new SetGetAPIPostData();
                setGetAPIPostData.setPARAMS("lang_id");
                setGetAPIPostData.setValues(AppConstant.Language);
                setGetAPIPostDatas.add(setGetAPIPostData);
                setGetAPIPostData = new SetGetAPIPostData();
                setGetAPIPostData.setPARAMS("user_timezone");
                setGetAPIPostData.setValues(Calendar.getInstance().getTimeZone().getID());
                setGetAPIPostDatas.add(setGetAPIPostData);
                setGetAPIPostData = new SetGetAPIPostData();
                setGetAPIPostData.setPARAMS("meet_date");
                setGetAPIPostData.setValues(((SFNFTextView) findViewById(R.id.startdate)).toString());
                setGetAPIPostDatas.add(setGetAPIPostData);
                setGetAPIPostData = new SetGetAPIPostData();
                setGetAPIPostData.setPARAMS("alt_date");
                setGetAPIPostData.setValues(((SFNFTextView) findViewById(R.id.AlterDate)).toString());
                setGetAPIPostDatas.add(setGetAPIPostData);
                setGetAPIPostData = new SetGetAPIPostData();
                setGetAPIPostData.setPARAMS("message");
                setGetAPIPostData.setValues(((EditText) findViewById(R.id.EDX_msg)).toString());
                setGetAPIPostDatas.add(setGetAPIPostData);
                setGetAPIPostData = new SetGetAPIPostData();
                setGetAPIPostData.setPARAMS("with_pet_status");
                if (((CheckBox) findViewById(R.id.Check)).isChecked())
                    setGetAPIPostData.setValues("1");
                else
                    setGetAPIPostData.setValues("0");
                setGetAPIPostDatas.add(setGetAPIPostData);
                setGetAPIPostData = new SetGetAPIPostData();
                setGetAPIPostData.setPARAMS("No_of_pet");
                setGetAPIPostData.setValues(((SFNFTextView) findViewById(R.id.EDX_no_of_pets)).toString());
                setGetAPIPostDatas.add(setGetAPIPostData);

                if (((SFNFTextView) findViewById(R.id.startdate)).getText().toString().trim().equals("")) {
                    ((SFNFTextView) findViewById(R.id.startdate)).setHintTextColor(Color.RED);
                    ((SFNFTextView) findViewById(R.id.startdate)).setHint(getString(R.string.pleaseselect));
                } else if (((SFNFTextView) findViewById(R.id.AlterDate)).getText().toString().trim().equals("")) {
                    ((SFNFTextView) findViewById(R.id.AlterDate)).setHint(getString(R.string.pleaseselect));
                    ((SFNFTextView) findViewById(R.id.AlterDate)).setHintTextColor(Color.RED);
                } else if (((EditText) findViewById(R.id.EDX_msg)).getText().toString().trim().equals("")) {
                    ((EditText) findViewById(R.id.EDX_msg)).setHint(getString(R.string.please_enter_message));
                    ((EditText) findViewById(R.id.EDX_msg)).setHintTextColor(Color.RED);
                    ((EditText) findViewById(R.id.EDX_msg)).requestFocus();
                } else {


                    appLoader.Show();
                    new CustomJSONParser().APIForPostMethod(AppConstant.BASEURL + "meetandgreet_request", setGetAPIPostDatas, new CustomJSONParser.JSONResponseInterface() {
                        @Override
                        public void OnSuccess(String Result) {
                            appLoader.Dismiss();
                            Loger.MSG("@@ MSG", Result);
                            try {
                                new MYAlert(MeetUpWannyanActivity.this).AlertOnly(getString(R.string.sucess), new JSONObject(Result).getString("message"), new MYAlert.OnlyMessage() {
                                    @Override
                                    public void OnOk(boolean res) {

                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void OnError(String Error, String Response) {
                            Loger.MSG("@@ Err", Response);
                            appLoader.Dismiss();
                        }

                        @Override
                        public void OnError(String Error) {
                            Loger.MSG("@@ Err", Error);
                            appLoader.Dismiss();
                        }
                    });
                }

        }
    }


    @Override
    public void Ondate(Calendar date, int viewid) {
        switch (viewid) {
            case R.id.startdate:
                Loger.MSG("@@ Date", date.getTime().toString());
                ((SFNFTextView) findViewById(R.id.startdate)).setText(date.get(Calendar.YEAR) + "/" + date.get(Calendar.MONTH) + "/" + date.get(Calendar.DAY_OF_MONTH));
                break;
            case R.id.AlterDate:
                Loger.MSG("@@ Date", date.getTime().toString());
                ((SFNFTextView) findViewById(R.id.AlterDate)).setText(date.get(Calendar.YEAR) + "/" + date.get(Calendar.MONTH) + "/" + date.get(Calendar.DAY_OF_MONTH));
                break;
        }
    }
}
