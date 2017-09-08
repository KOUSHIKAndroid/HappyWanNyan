package com.happywannyan.Activities.profile;

import android.app.DialogFragment;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.happywannyan.Constant.AppContsnat;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.R;
import com.happywannyan.Utils.AppCalender;
import com.happywannyan.Utils.AppLoader;
import com.happywannyan.Utils.CustomJSONParser;
import com.happywannyan.Utils.Loger;
import com.happywannyan.Utils.MYAlert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

public class ContactMsgActivity extends AppCompatActivity implements View.OnClickListener, AppCalender.OnDateSelect {


JSONArray JSONARRY=new JSONArray("[{\"name\":\"I am Flexible\",\"value\":\"\"},{\"name\":\"12:00 AM\",\"value\":\"12:00 AM\"},{\"name\":\"12:30 AM\",\"value\":\"12:30 AM\"},{\"name\":\"01:00 AM\",\"value\":\"01:00 AM\"},{\"name\":\"01:30 AM\",\"value\":\"01:30 AM\"},{\"name\":\"02:00 AM\",\"value\":\"02:00 AM\"},{\"name\":\"02:30 AM\",\"value\":\"02:30 AM\"},{\"name\":\"03:00 AM\",\"value\":\"03:00 AM\"},{\"name\":\"03:30 AM\",\"value\":\"03:30 AM\"},{\"name\":\"04:00 AM\",\"value\":\"04:00 AM\"},{\"name\":\"04:30 AM\",\"value\":\"04:30 AM\"},{\"name\":\"05:00 AM\",\"value\":\"05:00 AM\"},{\"name\":\"05:30 AM\",\"value\":\"05:30 AM\"},{\"name\":\"06:00 AM\",\"value\":\"06:00 AM\"},{\"name\":\"06:30 AM\",\"value\":\"06:30 AM\"},{\"name\":\"07:00 AM\",\"value\":\"07:00 AM\"},{\"name\":\"07:30 AM\",\"value\":\"07:30 AM\"},{\"name\":\"08:00 AM\",\"value\":\"08:00 AM\"},{\"name\":\"08:30 AM\",\"value\":\"08:30 AM\"},{\"name\":\"09:00 AM\",\"value\":\"09:00 AM\"},{\"name\":\"09:30 AM\",\"value\":\"09:30 AM\"},{\"name\":\"10:00 AM\",\"value\":\"10:00 AM\"},{\"name\":\"10:30 AM\",\"value\":\"10:30 AM\"},{\"name\":\"11:00 AM\",\"value\":\"11:00 AM\"},{\"name\":\"11:30 AM\",\"value\":\"11:30 AM\"},{\"name\":\"12:00 PM\",\"value\":\"12:00 PM\"},{\"name\":\"12:30 PM\",\"value\":\"12:30 PM\"},{\"name\":\"01:00 PM\",\"value\":\"01:00 PM\"},{\"name\":\"01:30 PM\",\"value\":\"01:30 PM\"},{\"name\":\"02:00 PM\",\"value\":\"02:00 PM\"},{\"name\":\"02:30 PM\",\"value\":\"02:30 PM\"},{\"name\":\"03:00 PM\",\"value\":\"03:00 PM\"},{\"name\":\"03:30 PM\",\"value\":\"03:30 PM\"},{\"name\":\"04:00 PM\",\"value\":\"04:00 PM\"},{\"name\":\"04:30 PM\",\"value\":\"04:30 PM\"},{\"name\":\"05:00 PM\",\"value\":\"05:00 PM\"},{\"name\":\"05:30 PM\",\"value\":\"05:30 PM\"},{\"name\":\"06:00 PM\",\"value\":\"06:00 PM\"},{\"name\":\"06:30 PM\",\"value\":\"06:30 PM\"},{\"name\":\"07:00 PM\",\"value\":\"07:00 PM\"},{\"name\":\"07:30 PM\",\"value\":\"07:30 PM\"},{\"name\":\"08:00 PM\",\"value\":\"08:00 PM\"},{\"name\":\"08:30 PM\",\"value\":\"08:30 PM\"},{\"name\":\"09:00 PM\",\"value\":\"09:00 PM\"},{\"name\":\"09:30 PM\",\"value\":\"09:30 PM\"},{\"name\":\"10:00 PM\",\"value\":\"10:00 PM\"},{\"name\":\"10:30 PM\",\"value\":\"10:30 PM\"},{\"name\":\"11:00 PM\",\"value\":\"11:00 PM\"},{\"name\":\"11:30 PM\",\"value\":\"11:30 PM\"}]");

    EditText EDX_msg;
    String ID="";

    AppLoader Loader;
    CheckBox CHEK;

    public ContactMsgActivity() throws JSONException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_msg);
        EDX_msg=(EditText)findViewById(R.id.EDX_msg);
        CHEK=(CheckBox)findViewById(R.id.CHEK);
        ID=getIntent().getStringExtra("DATA");
        Loader=new AppLoader(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.IMG_icon_drwaer:
                onBackPressed();
                break;
            case R.id.startdate:
                DialogFragment newFragment = AppCalender.newInstance(R.id.startdate);
                newFragment.show(getFragmentManager(), "datePicker");
                break;
            case R.id.AlterDate:
                 newFragment = AppCalender.newInstance(R.id.AlterDate);
                newFragment.show(getFragmentManager(), "datePicker");
                break;

            case R.id.RL_PickupTime:
                try {
                    new MYAlert(this).AlertTextLsit(getString(R.string.pickuptime), JSONARRY, "name", new MYAlert.OnSignleListTextSelected() {
                        @Override
                        public void OnSelectedTEXT(JSONObject jsonObject) {
                            Log.d("@@@fhjhf","--"+jsonObject);
                            try {
                                ((SFNFTextView)findViewById(R.id.TXT_PickupTime)).setText(jsonObject.getString("name"));
                                ((SFNFTextView)findViewById(R.id.TXT_PickupTime)).setTag(jsonObject.getString("name"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.RL_DropOffTime:
                try {
                    new MYAlert(this).AlertTextLsit(getString(R.string.dropofftime), JSONARRY, "name", new MYAlert.OnSignleListTextSelected() {
                        @Override
                        public void OnSelectedTEXT(JSONObject jsonObject) {
                            Log.d("@@@fhjhf","--"+jsonObject);
                            try {
                                ((SFNFTextView)findViewById(R.id.TXT_DropTime)).setText(jsonObject.getString("name"));
                                ((SFNFTextView)findViewById(R.id.TXT_DropTime)).setTag(jsonObject.getString("name"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.Card_next:

                if(EDX_msg.getText().toString().trim().length()>0) {
                    Loader.Show();
                    String CHCH="0";
                    if(CHEK.isChecked())
                        CHCH="1";


                    HashMap<String, String> Params = new HashMap<>();
                    Params.put("user_id", AppContsnat.UserId);
                    Params.put("lang_id", AppContsnat.Language);
                    Params.put("sitter_id", ID);
                    TimeZone tz = TimeZone.getDefault();
                    Params.put("user_timezone", tz.getID());
                    Params.put("add_message", EDX_msg.getText().toString());
                    Params.put("start_date", ((SFNFTextView) findViewById(R.id.startdate)).getText().toString());
                    Params.put("end_date", ((SFNFTextView) findViewById(R.id.AlterDate)).getText().toString());
                    Params.put("drop_off", ((SFNFTextView) findViewById(R.id.TXT_DropTime)).getText().toString());
                    Params.put("pick_up", ((SFNFTextView) findViewById(R.id.TXT_PickupTime)).getText().toString());
                    Params.put("dont_date", CHCH);
                    new CustomJSONParser().API_FOR_POST_2(AppContsnat.BASEURL + "contact_sitter", Params, new CustomJSONParser.JSONRESPONSE() {
                        @Override
                        public void OnSuccess(String Result) {
                            Loader.Dismiss();
                            try {
                                new MYAlert(ContactMsgActivity.this).AlertOnly(getString(R.string.contact), new JSONObject(Result).getString("message"), new MYAlert.OnlyMessage() {
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
                            Loader.Dismiss();
                            try {
                                new MYAlert(ContactMsgActivity.this).AlertOnly(getString(R.string.contact), new JSONObject(Response).getString("message"), new MYAlert.OnlyMessage() {
                                    @Override
                                    public void OnOk(boolean res) {

                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void OnError(String Error) {
                            Loader.Dismiss();
                        }
                    });
                }else {
                    EDX_msg.setHintTextColor(Color.RED);
                    EDX_msg.setHint(getString(R.string.please_enter_message));
                    EDX_msg.requestFocus();
                }


                break;


        }
    }

    @Override
    public void Ondate(Calendar date, int viewid) {
        switch (viewid) {
            case R.id.startdate:
                Loger.MSG("@@ Date", date.getTime().toString());
                ((SFNFTextView) findViewById(R.id.startdate)).setText(date.get(Calendar.YEAR) + "-" + date.get(Calendar.MONTH) + "-" + date.get(Calendar.DAY_OF_MONTH));
                break;
            case R.id.AlterDate:
                Loger.MSG("@@ Date", date.getTime().toString());
                ((SFNFTextView) findViewById(R.id.AlterDate)).setText(date.get(Calendar.YEAR) + "-" + date.get(Calendar.MONTH) + "-" + date.get(Calendar.DAY_OF_MONTH));
                break;

        }
    }
}
