package com.happywannyan.Activities.profile;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.happywannyan.Constant.AppConstant;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.R;
import com.happywannyan.Utils.AppDataHolder;
import com.happywannyan.Utils.AppLoader;
import com.happywannyan.Utils.CustomJSONParser;
import com.happywannyan.Utils.Loger;
import com.happywannyan.Utils.MYAlert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class ContactMsgActivity extends AppCompatActivity implements View.OnClickListener {

    Date checkIndate = null;
    Date checkOutdate = null;

    JSONArray JSONARRAY = new JSONArray("[{\"name\":\"I am Flexible\",\"value\":\"\"},{\"name\":\"12:00 AM\",\"value\":\"12:00 AM\"},{\"name\":\"12:30 AM\",\"value\":\"12:30 AM\"},{\"name\":\"01:00 AM\",\"value\":\"01:00 AM\"},{\"name\":\"01:30 AM\",\"value\":\"01:30 AM\"},{\"name\":\"02:00 AM\",\"value\":\"02:00 AM\"},{\"name\":\"02:30 AM\",\"value\":\"02:30 AM\"},{\"name\":\"03:00 AM\",\"value\":\"03:00 AM\"},{\"name\":\"03:30 AM\",\"value\":\"03:30 AM\"},{\"name\":\"04:00 AM\",\"value\":\"04:00 AM\"},{\"name\":\"04:30 AM\",\"value\":\"04:30 AM\"},{\"name\":\"05:00 AM\",\"value\":\"05:00 AM\"},{\"name\":\"05:30 AM\",\"value\":\"05:30 AM\"},{\"name\":\"06:00 AM\",\"value\":\"06:00 AM\"},{\"name\":\"06:30 AM\",\"value\":\"06:30 AM\"},{\"name\":\"07:00 AM\",\"value\":\"07:00 AM\"},{\"name\":\"07:30 AM\",\"value\":\"07:30 AM\"},{\"name\":\"08:00 AM\",\"value\":\"08:00 AM\"},{\"name\":\"08:30 AM\",\"value\":\"08:30 AM\"},{\"name\":\"09:00 AM\",\"value\":\"09:00 AM\"},{\"name\":\"09:30 AM\",\"value\":\"09:30 AM\"},{\"name\":\"10:00 AM\",\"value\":\"10:00 AM\"},{\"name\":\"10:30 AM\",\"value\":\"10:30 AM\"},{\"name\":\"11:00 AM\",\"value\":\"11:00 AM\"},{\"name\":\"11:30 AM\",\"value\":\"11:30 AM\"},{\"name\":\"12:00 PM\",\"value\":\"12:00 PM\"},{\"name\":\"12:30 PM\",\"value\":\"12:30 PM\"},{\"name\":\"01:00 PM\",\"value\":\"01:00 PM\"},{\"name\":\"01:30 PM\",\"value\":\"01:30 PM\"},{\"name\":\"02:00 PM\",\"value\":\"02:00 PM\"},{\"name\":\"02:30 PM\",\"value\":\"02:30 PM\"},{\"name\":\"03:00 PM\",\"value\":\"03:00 PM\"},{\"name\":\"03:30 PM\",\"value\":\"03:30 PM\"},{\"name\":\"04:00 PM\",\"value\":\"04:00 PM\"},{\"name\":\"04:30 PM\",\"value\":\"04:30 PM\"},{\"name\":\"05:00 PM\",\"value\":\"05:00 PM\"},{\"name\":\"05:30 PM\",\"value\":\"05:30 PM\"},{\"name\":\"06:00 PM\",\"value\":\"06:00 PM\"},{\"name\":\"06:30 PM\",\"value\":\"06:30 PM\"},{\"name\":\"07:00 PM\",\"value\":\"07:00 PM\"},{\"name\":\"07:30 PM\",\"value\":\"07:30 PM\"},{\"name\":\"08:00 PM\",\"value\":\"08:00 PM\"},{\"name\":\"08:30 PM\",\"value\":\"08:30 PM\"},{\"name\":\"09:00 PM\",\"value\":\"09:00 PM\"},{\"name\":\"09:30 PM\",\"value\":\"09:30 PM\"},{\"name\":\"10:00 PM\",\"value\":\"10:00 PM\"},{\"name\":\"10:30 PM\",\"value\":\"10:30 PM\"},{\"name\":\"11:00 PM\",\"value\":\"11:00 PM\"},{\"name\":\"11:30 PM\",\"value\":\"11:30 PM\"}]");
    EditText EDX_msg, EDX_first_name, EDX_last_name;
    String ID = "";
    AppLoader appLoader;
    CheckBox CHEK;

    public ContactMsgActivity() throws JSONException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_msg);

        setupParent(findViewById(R.id.contactmsglayout));

        EDX_msg = (EditText) findViewById(R.id.EDX_msg);
        EDX_first_name = (EditText) findViewById(R.id.EDX_first_name);
        EDX_last_name = (EditText) findViewById(R.id.EDX_last_name);

        CHEK = (CheckBox) findViewById(R.id.CHEK);
        ID = getIntent().getStringExtra("DATA");
        appLoader = new AppLoader(this);

        new AppConstant(ContactMsgActivity.this).getShareData(AppDataHolder.UserData, new AppDataHolder.AppSharePreferenceDataInterface() {
            @Override
            public void available(boolean available, JSONObject data) {
                try {
                    Loger.MSG("@@ DADAD", "" + data);

                    EDX_first_name.setText(data.getJSONObject("info_array").getString("firstname").trim());
                    EDX_last_name.setText(data.getJSONObject("info_array").getString("lastname").trim());

                    if (data.getJSONObject("info_array").getString("firstname").trim().equals("") && data.getJSONObject("info_array").getString("lastname").trim().equals("")) {
                        findViewById(R.id.Crad0).setVisibility(View.VISIBLE);
                    } else {
                        findViewById(R.id.Crad0).setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void notAvailable(String Error) {

            }
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.IMG_icon_drwaer:
                onBackPressed();
                break;
            case R.id.startdate:

                View dialoglayout1 = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_date_picker, null);
                final DatePicker dPicker1 = (DatePicker) dialoglayout1.findViewById(R.id.date_picker);

                Calendar cal1 = Calendar.getInstance();

                Loger.MSG("mYear", "" + cal1.get(Calendar.YEAR));
                Loger.MSG("mMonth", "" + cal1.get(Calendar.MONTH));
                Loger.MSG("mMonth", "" + cal1.get(Calendar.DAY_OF_MONTH));


                dPicker1.setMinDate(cal1.getTimeInMillis());

                if (checkIndate != null) {
                    cal1.setTime(checkIndate);
                    dPicker1.updateDate(cal1.get(Calendar.YEAR), cal1.get(Calendar.MONTH), cal1.get(Calendar.DAY_OF_MONTH));
                    dPicker1.setSelected(true);
                } else {
                    dPicker1.setSelected(true);
                }

                AlertDialog.Builder builder1 = new AlertDialog.Builder(ContactMsgActivity.this);
                builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            checkIndate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse("" + dPicker1.getYear() + "-" + (dPicker1.getMonth() + 1) + "-" + dPicker1.getDayOfMonth());
                            ((SFNFTextView) findViewById(R.id.startdate)).setText("" + dPicker1.getYear() + "-" + (dPicker1.getMonth() + 1) + "-" + dPicker1.getDayOfMonth());
                            ((SFNFTextView) findViewById(R.id.AlterDate)).setText("Set date");
                            checkOutdate = null;
                            Loger.MSG("date", "" + checkIndate);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                builder1.setView(dialoglayout1);
                builder1.show();

                break;
            case R.id.AlterDate:

                View dialoglayout2 = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_date_picker, null);
                final DatePicker dPicker2 = (DatePicker) dialoglayout2.findViewById(R.id.date_picker);

                Calendar cal2 = Calendar.getInstance();

                if (checkIndate == null) {
                    cal2.setTimeInMillis(System.currentTimeMillis() + (1000 * 60 * 60 * 24));
                    dPicker2.setMinDate(cal2.getTimeInMillis());
                    dPicker2.setSelected(true);
                    Loger.MSG("mYear", "" + cal2.get(Calendar.YEAR));
                    Loger.MSG("mMonth", "" + cal2.get(Calendar.MONTH));
                    Loger.MSG("mDay", "" + cal2.get(Calendar.DAY_OF_MONTH));
                } else {
                    try {
                        cal2.setTimeInMillis(checkIndate.getTime() + (1000 * 60 * 60 * 24));
                        dPicker2.setMinDate(cal2.getTimeInMillis());
                        dPicker2.setSelected(true);
                        Loger.MSG("mYear", "" + cal2.get(Calendar.YEAR));
                        Loger.MSG("mMonth", "" + cal2.get(Calendar.MONTH));
                        Loger.MSG("mDay", "" + cal2.get(Calendar.DAY_OF_MONTH));

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }


                AlertDialog.Builder builder2 = new AlertDialog.Builder(ContactMsgActivity.this);
                builder2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        ((SFNFTextView) findViewById(R.id.AlterDate)).setText("" + dPicker2.getYear() + "-" + (dPicker2.getMonth() + 1) + "-" + dPicker2.getDayOfMonth());
                    }
                });
                builder2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                builder2.setView(dialoglayout2);
                builder2.show();

                break;

            case R.id.RL_PickupTime:
                try {
                    new MYAlert(this).AlertTextLsit(getString(R.string.pickuptime), JSONARRAY, "name", new MYAlert.OnSingleListTextSelected() {
                        @Override
                        public void OnSelectedTEXT(JSONObject jsonObject) {
                            Log.d("@@@fhjhf", "--" + jsonObject);
                            try {
                                ((SFNFTextView) findViewById(R.id.TXT_PickupTime)).setText(jsonObject.getString("name"));
                                ((SFNFTextView) findViewById(R.id.TXT_PickupTime)).setTag(jsonObject.getString("name"));
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
                    new MYAlert(this).AlertTextLsit(getString(R.string.dropofftime), JSONARRAY, "name", new MYAlert.OnSingleListTextSelected() {
                        @Override
                        public void OnSelectedTEXT(JSONObject jsonObject) {
                            Log.d("@@@fhjhf", "--" + jsonObject);
                            try {
                                ((SFNFTextView) findViewById(R.id.TXT_DropTime)).setText(jsonObject.getString("name"));
                                ((SFNFTextView) findViewById(R.id.TXT_DropTime)).setTag(jsonObject.getString("name"));
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

                if (EDX_first_name.getText().toString().equals("")) {

                    EDX_first_name.setHintTextColor(Color.RED);
                    EDX_first_name.setHint(getString(R.string.please_enter_first_name));
                    EDX_first_name.requestFocus();

                } else {
                    if (EDX_last_name.getText().toString().equals("")) {

                        EDX_last_name.setHintTextColor(Color.RED);
                        EDX_last_name.setHint(getString(R.string.please_enter_last_name));
                        EDX_last_name.requestFocus();

                    } else {
                        if (EDX_msg.getText().toString().trim().length() > 0) {
                            if (!CHEK.isChecked()) {
                                String CHCH = "0";
                                if (((SFNFTextView) findViewById(R.id.startdate)).getText().toString().trim().equals("")) {
                                    Toast.makeText(ContactMsgActivity.this, getResources().getString(R.string.please_select_start_date), Toast.LENGTH_SHORT).show();
                                } else {

                                    if (((SFNFTextView) findViewById(R.id.AlterDate)).getText().toString().trim().equals("")) {
                                        Toast.makeText(ContactMsgActivity.this, getResources().getString(R.string.please_select_end_date), Toast.LENGTH_SHORT).show();
                                    } else {
                                        appLoader.Show();

                                        HashMap<String, String> Params = new HashMap<>();
                                        Params.put("user_id", AppConstant.UserId);
                                        Params.put("lang_id", AppConstant.Language);
                                        Params.put("sitter_id", ID);
                                        TimeZone tz = TimeZone.getDefault();
                                        Params.put("user_timezone", tz.getID());


                                        try {
                                            Params.put("firstname", EDX_first_name.getText().toString().trim());
                                            Params.put("lastname", EDX_last_name.getText().toString().trim());
                                            Params.put("add_message", URLEncoder.encode("" + EDX_msg.getText().toString().trim(), "UTF-8"));
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }

                                        Params.put("start_date", ((SFNFTextView) findViewById(R.id.startdate)).getText().toString());
                                        Params.put("end_date", ((SFNFTextView) findViewById(R.id.AlterDate)).getText().toString());
                                        Params.put("drop_off", ((SFNFTextView) findViewById(R.id.TXT_DropTime)).getText().toString());
                                        Params.put("pick_up", ((SFNFTextView) findViewById(R.id.TXT_PickupTime)).getText().toString());
                                        Params.put("dont_date", CHCH);
                                        new CustomJSONParser().APIForPostMethod2(ContactMsgActivity.this, AppConstant.BASEURL + "contact_sitter", Params, new CustomJSONParser.JSONResponseInterface() {
                                            @Override
                                            public void OnSuccess(String Result) {
                                                appLoader.Dismiss();

                                                /////////////////////update Share Preference (Login credential)////////////////////////////////////
                                                new AppConstant(ContactMsgActivity.this).upDateShareDATA(AppDataHolder.UserData, "", EDX_first_name.getText().toString().trim(), EDX_last_name.getText().toString().trim());
                                                ///////////////////////////////////////////////////////END//////////////////////////////////
                                                new MYAlert(ContactMsgActivity.this).AlertOnly(getString(R.string.contact), getResources().getString(R.string.message_has_been_sent_successfully), new MYAlert.OnlyMessage() {
                                                    @Override
                                                    public void OnOk(boolean res) {
                                                        finish();
                                                    }
                                                });

                                            }

                                            @Override
                                            public void OnError(String Error, String Response) {
                                                appLoader.Dismiss();
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
                                                appLoader.Dismiss();
                                                if (Error.equalsIgnoreCase(getResources().getString(R.string.please_check_your_internet_connection))) {
                                                    Toast.makeText(ContactMsgActivity.this, Error, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }
                            } else {
                                String CHCH = "1";
                                appLoader.Show();
                                HashMap<String, String> Params = new HashMap<>();
                                Params.put("user_id", AppConstant.UserId);
                                Params.put("lang_id", AppConstant.Language);
                                Params.put("sitter_id", ID);
                                TimeZone tz = TimeZone.getDefault();
                                Params.put("user_timezone", tz.getID());

                                try {
                                    Params.put("firstname", EDX_first_name.getText().toString().trim());
                                    Params.put("lastname", EDX_last_name.getText().toString().trim());
                                    Params.put("add_message", URLEncoder.encode("" + EDX_msg.getText().toString().trim(), "UTF-8"));
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }

                                Params.put("start_date", "");
                                Params.put("end_date", "");
                                Params.put("drop_off", ((SFNFTextView) findViewById(R.id.TXT_DropTime)).getText().toString());
                                Params.put("pick_up", ((SFNFTextView) findViewById(R.id.TXT_PickupTime)).getText().toString());
                                Params.put("dont_date", CHCH);
                                new CustomJSONParser().APIForPostMethod2(ContactMsgActivity.this, AppConstant.BASEURL + "contact_sitter", Params, new CustomJSONParser.JSONResponseInterface() {
                                    @Override
                                    public void OnSuccess(String Result) {
                                        appLoader.Dismiss();

                                        /////////////////////update Share Preference (Login credential)////////////////////////////////////
                                        new AppConstant(ContactMsgActivity.this).upDateShareDATA(AppDataHolder.UserData, "", EDX_first_name.getText().toString().trim(), EDX_last_name.getText().toString().trim());
                                        ///////////////////////////////////////////////////////END//////////////////////////////////
                                        new MYAlert(ContactMsgActivity.this).AlertOnly(getString(R.string.contact), getResources().getString(R.string.message_has_been_sent_successfully), new MYAlert.OnlyMessage() {
                                            @Override
                                            public void OnOk(boolean res) {
                                                finish();
                                            }
                                        });

                                    }

                                    @Override
                                    public void OnError(String Error, String Response) {
                                        appLoader.Dismiss();
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
                                        appLoader.Dismiss();
                                        if (Error.equalsIgnoreCase(getResources().getString(R.string.please_check_your_internet_connection))) {
                                            Toast.makeText(ContactMsgActivity.this, Error, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        } else {
                            EDX_msg.setHintTextColor(Color.RED);
                            EDX_msg.setHint(getString(R.string.please_enter_message));
                            EDX_msg.requestFocus();
                        }
                    }
                }
                break;
        }
    }

    private void setupParent(View view)
    {
        if(!(view instanceof EditText))
        {
            view.setOnTouchListener(new View.OnTouchListener()
            {
                public boolean onTouch(View v, MotionEvent event)
                {
                    hideSoftKeyboard();
                    return false;
                }
            });
        }
        if (view instanceof ViewGroup)
        {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++)
            {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupParent(innerView);
            }
        }
    }

    private void hideSoftKeyboard()
    {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (getCurrentFocus()!=null)
        {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

}
