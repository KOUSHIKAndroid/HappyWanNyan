package com.happywannyan.Activities.Booking;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.happywannyan.Constant.AppConstant;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.R;
import com.happywannyan.Utils.AppLoader;
import com.happywannyan.Utils.CustomJSONParser;
import com.happywannyan.Utils.Loger;
import com.happywannyan.Utils.MYAlert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.TimeZone;

public class CancelBookingWithReasonsActivity extends AppCompatActivity implements View.OnClickListener {
    JSONObject DATAObject;
    JSONArray cancel_reason_type;
    String CancelID = "";
    MYAlert myAlert;
    AppLoader appLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_booking_with_reasons);
        myAlert = new MYAlert(this);
        appLoader = new AppLoader(this);
        DATAObject = (BookingDetailsActivity.jsonObjectPrevious);
        Loger.MSG("#@@@@", "--- " + DATAObject.toString());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.RL_Cancel_couse:
                try {

                    if (DATAObject.getJSONObject("booking_info").has("cancel_reason_type") && DATAObject.getJSONObject("booking_info").getJSONArray("cancel_reason_type").length() > 0) {
                        cancel_reason_type = DATAObject.getJSONObject("booking_info").getJSONArray("cancel_reason_type");
                        myAlert.AlertTextLsit(getString(R.string.select_cancel_reason), cancel_reason_type, "cancel_reason", new MYAlert.OnSingleListTextSelected() {
                            @Override
                            public void OnSelectedTEXT(JSONObject jsonObject) {
                                try {
                                    (((SFNFTextView) findViewById(R.id.TXT_ResonText))).setText(jsonObject.getString("cancel_reason"));
                                    CancelID = jsonObject.getString("id");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Loger.Error("@@", e.getMessage());
                }


                break;
            case R.id.BTN_Confirm:
                if (CancelID.equals("")) {
                    ((SFNFTextView) findViewById(R.id.TXT_ResonText)).setHintTextColor(Color.RED);
                } else {
                    if (((EditText) findViewById(R.id.EDX_Cancel_Message)).getText().toString().trim().equals("")) {
                        ((EditText) findViewById(R.id.EDX_Cancel_Message)).setHint(getString(R.string.please_enter_message));
                        ((EditText) findViewById(R.id.EDX_Cancel_Message)).requestFocus();
                    } else {
                        ConfirmCancel();
                    }
                }
                break;
            case R.id.IMG_icon_back:
                onBackPressed();
                break;


        }

    }

    private void ConfirmCancel() {
        appLoader.Show();
        HashMap<String, String> Params = new HashMap<>();
        try {
            Params.put("booking_id", DATAObject.getJSONObject("booking_info").getString("id"));
            Params.put("booking_type", DATAObject.getJSONObject("booking_info").getString("booking_type"));
            Params.put("user_id", AppConstant.UserId);
            Params.put("user_timezone", TimeZone.getDefault().getID());
            Params.put("lang_id", AppConstant.Language);
            Params.put("cancel_option", CancelID);
            Params.put("message_cancel", (((EditText) findViewById(R.id.EDX_Cancel_Message)).getText().toString()));
            Params.put("booking_refund", 1 + "");

            new CustomJSONParser().APIForPostMethod2(CancelBookingWithReasonsActivity.this,AppConstant.BASEURL + "cancel_reservation_request", Params, new CustomJSONParser.JSONResponseInterface() {
                @Override
                public void OnSuccess(String Result) {
                    appLoader.Dismiss();
                    try {
                        myAlert.AlertForAPIRESPONSE(getString(R.string.sucess), new JSONObject(Result).getString("message"), new MYAlert.OnlyMessage() {
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
                    appLoader.Dismiss();
                    try {
                        myAlert.AlertForAPIRESPONSE(getString(R.string.Error), new JSONObject(Response).getString("message"), new MYAlert.OnlyMessage() {
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
                    myAlert.AlertForAPIRESPONSE(getString(R.string.Error), Error, new MYAlert.OnlyMessage() {
                        @Override
                        public void OnOk(boolean res) {

                        }
                    });
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
