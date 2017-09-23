package com.happywannyan.Activities.Booking;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.happywannyan.Activities.MessageDetailsPageActivity;
import com.happywannyan.Constant.AppConstant;
import com.happywannyan.Font.SFNFBoldTextView;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.POJO.SetGetAPIPostData;
import com.happywannyan.R;
import com.happywannyan.Utils.AppLoader;
import com.happywannyan.Utils.CustomJSONParser;
import com.happywannyan.Utils.Loger;
import com.happywannyan.Utils.MYAlert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimeZone;

public class BookingDetailsActivity extends AppCompatActivity {

    public static JSONObject jsonObjectPrevious;
    JSONArray PetInfo;
    LinearLayout LLPetInfo, LL_FOOTER1, LL_FOOTER2;
    AppLoader appLoader;
    int block_user_status = 0;
    TimeZone Tz;
    MYAlert MYALERT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);
        Tz = TimeZone.getDefault();
        MYALERT = new MYAlert(this);
        findViewById(R.id.IMG_icon_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Loger.MSG("## KOUSHIK", "dfghjkl---------");
        try {
            appLoader = new AppLoader(this);
            jsonObjectPrevious = new JSONObject(getIntent().getStringExtra("data"));
            PetInfo = jsonObjectPrevious.getJSONArray("pet_details");
            ImageView profimage = (ImageView) findViewById(R.id.img_view);

            ImageView backimage = (ImageView) findViewById(R.id.IMG_icon_back);
            LLPetInfo = (LinearLayout) findViewById(R.id.LLPetInfo);
            LL_FOOTER1 = (LinearLayout) findViewById(R.id.LL_FOOTER1);
            LL_FOOTER2 = (LinearLayout) findViewById(R.id.LL_FOOTER2);

            block_user_status = jsonObjectPrevious.getJSONObject("booking_info").getInt("block_user_status");

            Glide.with(this).load(jsonObjectPrevious.getJSONObject("users_profile").getString("booked_user_image")).into(profimage);
            ((SFNFTextView) findViewById(R.id.tv_name)).setText(jsonObjectPrevious.getJSONObject("users_profile").getString("who_booked"));
            ((SFNFTextView) findViewById(R.id.tv_type)).setText(jsonObjectPrevious.getJSONObject("users_profile").getString("booked_user_name"));

            ((SFNFTextView) findViewById(R.id.TXT_total_no_pet)).setText(jsonObjectPrevious.getJSONObject("booking_info").getString("booked_total_pet"));

            ((SFNFBoldTextView) findViewById(R.id.StartDate)).setText(jsonObjectPrevious.getJSONObject("booking_info").getString("booking_start_date"));
            ((SFNFBoldTextView) findViewById(R.id.Enddate)).setText(jsonObjectPrevious.getJSONObject("booking_info").getString("booking_end_date"));
            ((SFNFBoldTextView) findViewById(R.id.BookingId)).setText(jsonObjectPrevious.getJSONObject("booking_info").getString("booking_id"));
            ((SFNFBoldTextView) findViewById(R.id.tv_service_value)).setText(jsonObjectPrevious.getJSONObject("booking_info").getString("booking_service"));


            if (jsonObjectPrevious.getJSONObject("booking_info").has("accept_button") && !jsonObjectPrevious.getJSONObject("booking_info").getString("accept_button").trim().equals("")) {

                View ButtomView = getLayoutInflater().inflate(R.layout.footer_card_button, null);
                ((CardView) ButtomView.findViewById(R.id.Card_AddRevw)).setCardBackgroundColor(Color.parseColor("#bf3e49"));
                ((SFNFBoldTextView) ButtomView.findViewById(R.id.TXT_ButtonName)).setText(jsonObjectPrevious.getJSONObject("booking_info").getString("accept_button"));
                LL_FOOTER1.addView(ButtomView);
                ButtomView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AcceptButton();
                    }
                });
            }

            if (jsonObjectPrevious.getJSONObject("booking_info").has("deny_button") && !jsonObjectPrevious.getJSONObject("booking_info").getString("deny_button").trim().equals("")) {
                View ButtomView = getLayoutInflater().inflate(R.layout.footer_card_button, null);
                ((SFNFBoldTextView) ButtomView.findViewById(R.id.TXT_ButtonName)).setText(jsonObjectPrevious.getJSONObject("booking_info").getString("deny_button"));
                if (LL_FOOTER1.getChildCount() < 2)
                    LL_FOOTER1.addView(ButtomView);
                else
                    LL_FOOTER2.addView(ButtomView);

                ButtomView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Deny_Button(jsonObjectPrevious.getJSONObject("booking_info").getString("id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }


            if (jsonObjectPrevious.getJSONObject("booking_info").has("send_msg_status") && !jsonObjectPrevious.getJSONObject("booking_info").getString("send_msg_status").trim().equals("")) {
                View ButtomView = getLayoutInflater().inflate(R.layout.footer_card_button, null);
                ((CardView) ButtomView.findViewById(R.id.Card_AddRevw)).setCardBackgroundColor(Color.parseColor("#bf3e49"));
                ((SFNFBoldTextView) ButtomView.findViewById(R.id.TXT_ButtonName)).setText(jsonObjectPrevious.getJSONObject("booking_info").getString("send_msg_status"));
                if (LL_FOOTER1.getChildCount() < 2)
                    LL_FOOTER1.addView(ButtomView);
                else
                    LL_FOOTER2.addView(ButtomView);

                ButtomView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (jsonObjectPrevious.getJSONObject("booking_info").has("send_msg_show") && jsonObjectPrevious.getJSONObject("booking_info").getInt("send_msg_show") == 0) {
                                if (block_user_status == 0) {
                                    Send_Message(jsonObjectPrevious.getJSONObject("booking_info").getString("send_msg_status"), getString(R.string.please_enter_message), getString(R.string.submit)
                                            , jsonObjectPrevious.getJSONObject("booking_info").getString("id"), jsonObjectPrevious.getJSONObject("booking_info").getString("booking_type"));
                                } else {
                                    MYALERT.AlertOnly(getResources().getString(R.string.message), getResources().getString(R.string.unable_to_send_message), new MYAlert.OnlyMessage() {
                                        @Override
                                        public void OnOk(boolean res) {

                                        }
                                    });
                                }
                            } else {
                                GotoMessage(jsonObjectPrevious.getJSONObject("booking_info").getString("message_id"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }


            if (jsonObjectPrevious.getJSONObject("booking_info").has("delete_button") && !jsonObjectPrevious.getJSONObject("booking_info").getString("delete_button").trim().equals("")) {
                View ButtomView = getLayoutInflater().inflate(R.layout.footer_card_button, null);
                ((SFNFBoldTextView) ButtomView.findViewById(R.id.TXT_ButtonName)).setText(jsonObjectPrevious.getJSONObject("booking_info").getString("delete_button"));
                if (LL_FOOTER1.getChildCount() < 2)
                    LL_FOOTER1.addView(ButtomView);
                else
                    LL_FOOTER2.addView(ButtomView);
                ButtomView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Delete_Button();
                    }
                });
            }


            if (jsonObjectPrevious.getJSONObject("booking_info").has("review_status") && !jsonObjectPrevious.getJSONObject("booking_info").getString("review_status").trim().equals("")) {
                View ButtomView = getLayoutInflater().inflate(R.layout.footer_card_button, null);
                ((SFNFBoldTextView) ButtomView.findViewById(R.id.TXT_ButtonName)).setText(jsonObjectPrevious.getJSONObject("booking_info").getString("review_status"));
                if (LL_FOOTER1.getChildCount() < 2)
                    LL_FOOTER1.addView(ButtomView);
                else
                    LL_FOOTER2.addView(ButtomView);
                ButtomView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Review_Status();
                    }
                });

            }

            if (jsonObjectPrevious.getJSONObject("booking_info").has("cancel_status") && !jsonObjectPrevious.getJSONObject("booking_info").getString("cancel_status").trim().equals("")) {
                View ButtomView = getLayoutInflater().inflate(R.layout.footer_card_button, null);
                ((CardView) ButtomView.findViewById(R.id.Card_AddRevw)).setCardBackgroundColor(Color.parseColor("#bf3e49"));
                ((SFNFBoldTextView) ButtomView.findViewById(R.id.TXT_ButtonName)).setText(jsonObjectPrevious.getJSONObject("booking_info").getString("cancel_status"));
                if (LL_FOOTER1.getChildCount() < 2)
                    LL_FOOTER1.addView(ButtomView);
                else
                    LL_FOOTER2.addView(ButtomView);

                ButtomView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Loger.MSG("refund_status-->", "" + jsonObjectPrevious.getJSONObject("booking_info").getInt("refund_status"));
                            if (jsonObjectPrevious.getJSONObject("booking_info").getInt("refund_status") == 0)
                                CancelStatusWork(jsonObjectPrevious.getJSONObject("booking_info").getString("booking_id"), jsonObjectPrevious.getJSONObject("booking_info").getString("booking_type"), getString(R.string.do_you_want_to_cancel_booking));
                            if (jsonObjectPrevious.getJSONObject("booking_info").getInt("refund_status") == 1) {
                                CancelWith_Reasons();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            if (jsonObjectPrevious.getJSONObject("booking_info").has("cancel_status_button") && !jsonObjectPrevious.getJSONObject("booking_info").getString("cancel_status_button").trim().equals("")) {
                View ButtomView = getLayoutInflater().inflate(R.layout.footer_card_button, null);
                ((SFNFBoldTextView) ButtomView.findViewById(R.id.TXT_ButtonName)).setText(jsonObjectPrevious.getJSONObject("booking_info").getString("cancel_status_button"));
                if (LL_FOOTER1.getChildCount() < 2)
                    LL_FOOTER1.addView(ButtomView);
                else
                    LL_FOOTER2.addView(ButtomView);


                ((CardView) ButtomView.findViewById(R.id.Card_AddRevw)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Loger.MSG("cancel_status_button-->", "Cancel");
                        try {
                            CancelStatusWork(jsonObjectPrevious.getJSONObject("booking_info").getString("id"), jsonObjectPrevious.getJSONObject("booking_info").getString("booking_type"), getString(R.string.are_you_sure_you_want_to_cancel_reservation_request));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }


            if (PetInfo.length() <= 0) {
                View nodata = getLayoutInflater().inflate(R.layout.no_data_layout, null);
                SFNFTextView nodatText = (SFNFTextView) nodata.findViewById(R.id.TXT_Nodata);
                nodatText.setText(getString(R.string.no_pet_data_found));
                findViewById(R.id.TXTeading).setVisibility(View.GONE);
                LLPetInfo.addView(nodata);
            } else {
                for (int i = 0; i < PetInfo.length(); i++) {
                    View PetView = getLayoutInflater().inflate(R.layout.booking_aditional_pet, null);
                    JSONObject PetObj = PetInfo.getJSONObject(i);

                    ImageView petimage = (ImageView) PetView.findViewById(R.id.img_view_pets);
                    Glide.with(this).load(PetObj.getString("pet_image")).into(petimage);

                    if (PetObj.getJSONArray("pet_info").length() > 0) {

                        ((SFNFTextView) PetView.findViewById(R.id.tv_describe_name)).setText(PetObj.getJSONArray("pet_info").getJSONObject(0).getString("name"));
                        ((SFNFTextView) PetView.findViewById(R.id.tv_describe_value)).setText(PetObj.getJSONArray("pet_info").getJSONObject(0).getString("value"));

                    }
                    if (PetObj.getJSONArray("pet_info").length() > 1) {
                        ((SFNFTextView) PetView.findViewById(R.id.tv_name_pet)).setText(PetObj.getJSONArray("pet_info").getJSONObject(1).getString("name"));
                        ((SFNFTextView) PetView.findViewById(R.id.tv_pet_name_value)).setText(PetObj.getJSONArray("pet_info").getJSONObject(1).getString("value"));
                    }

                    if (PetObj.getJSONArray("pet_info").length() > 2) {
                        ((SFNFTextView) PetView.findViewById(R.id.tv_question_one)).setText(PetObj.getJSONArray("pet_info").getJSONObject(2).getString("name"));
                        ((SFNFTextView) PetView.findViewById(R.id.tv_answer_one)).setText(PetObj.getJSONArray("pet_info").getJSONObject(2).getString("value"));
                    }

                    if (PetObj.getJSONArray("pet_info").length() > 3) {
                        ((SFNFTextView) PetView.findViewById(R.id.tv_question_two)).setText(PetObj.getJSONArray("pet_info").getJSONObject(3).getString("name"));
                        ((SFNFTextView) PetView.findViewById(R.id.tv_answer_two)).setText(PetObj.getJSONArray("pet_info").getJSONObject(3).getString("value"));
                    }

                    if (PetObj.getJSONArray("pet_info").length() > 4) {
                        ((SFNFTextView) PetView.findViewById(R.id.tv_month_lbl)).setText(PetObj.getJSONArray("pet_info").getJSONObject(4).getString("name"));
                        ((SFNFBoldTextView) PetView.findViewById(R.id.tv_month)).setText(PetObj.getJSONArray("pet_info").getJSONObject(4).getString("value"));
                    }

                    if (PetObj.getJSONArray("pet_info").length() > 5) {
                        ((SFNFTextView) PetView.findViewById(R.id.tv_breed_lbl)).setText(PetObj.getJSONArray("pet_info").getJSONObject(5).getString("name"));
                        ((SFNFBoldTextView) PetView.findViewById(R.id.tv_breed)).setText(PetObj.getJSONArray("pet_info").getJSONObject(5).getString("value"));
                    }

                    if (PetObj.getJSONArray("pet_info").length() > 6) {
                        ((SFNFTextView) PetView.findViewById(R.id.tv_gender_lbl)).setText(PetObj.getJSONArray("pet_info").getJSONObject(6).getString("name"));
                        ((SFNFBoldTextView) PetView.findViewById(R.id.tv_gender)).setText(PetObj.getJSONArray("pet_info").getJSONObject(6).getString("value"));
                    }

                    if (PetObj.getJSONArray("pet_info").length() > 7) {
                        ((SFNFTextView) PetView.findViewById(R.id.year_lbl)).setText(PetObj.getJSONArray("pet_info").getJSONObject(7).getString("name"));
                        ((SFNFBoldTextView) PetView.findViewById(R.id.tv_year)).setText(PetObj.getJSONArray("pet_info").getJSONObject(7).getString("value"));
                    }

                    if (PetObj.getJSONArray("pet_info").length() > 8) {
                        ((SFNFTextView) PetView.findViewById(R.id.tv_size_name)).setText(PetObj.getJSONArray("pet_info").getJSONObject(8).getString("name"));
                        ((SFNFBoldTextView) PetView.findViewById(R.id.tv_size_value)).setText(PetObj.getJSONArray("pet_info").getJSONObject(8).getString("value"));
                    }
                    LLPetInfo.addView(PetView);
                }
            }

            backimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void CancelWith_Reasons() {
        Loger.MSG("@@BBB", jsonObjectPrevious + " 33");

        Intent intent = new Intent(this, CancelBookingWithReasonsActivity.class);
        intent.putExtra("OBJECTDATA", jsonObjectPrevious + "");
        startActivity(intent);

    }

    private void GotoMessage(String MsgId) {
        Intent intent = new Intent(this, MessageDetailsPageActivity.class);
        intent.putExtra("receiver_id", AppConstant.UserId);
        intent.putExtra("message_id", MsgId);
        startActivity(intent);
    }

    private void AcceptButton() {
        try {
            if (jsonObjectPrevious.getJSONObject("booking_info").getString("accept_type_booking").equals("N")) {
//                Normal Confimation Accpet
                MYALERT.AlertAccept_Cancel(getString(R.string.accept), getString(R.string.confirm_somple_msg), new MYAlert.OnOkCancel() {
                    @Override
                    public void OnOk() {
                        appLoader.Show();
                        HashMap<String, String> Params = new HashMap<String, String>();
                        Params.put("user_id", AppConstant.UserId);
                        try {
                            Params.put("booking_id", jsonObjectPrevious.getJSONObject("booking_info").getString("id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Params.put("langid", AppConstant.Language);
                        Params.put("user_timezone", TimeZone.getDefault().getID());

                        new CustomJSONParser().APIForPostMethod2(AppConstant.BASEURL + "normal_accept_booking", Params, new CustomJSONParser.JSONResponseInterface() {
                            @Override
                            public void OnSuccess(String Result) {
                                appLoader.Dismiss();
                                try {
                                    MYALERT.AlertForAPIRESPONSE(getString(R.string.accept), new JSONObject(Result).getString("message"), new MYAlert.OnlyMessage() {
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
                                    MYALERT.AlertForAPIRESPONSE(getString(R.string.accept), new JSONObject(Response).getString("message"), new MYAlert.OnlyMessage() {
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
                                MYALERT.AlertForAPIRESPONSE(getString(R.string.Error), Error, new MYAlert.OnlyMessage() {
                                    @Override
                                    public void OnOk(boolean res) {

                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void OnCancel() {

                    }
                });


            } else if (jsonObjectPrevious.getJSONObject("booking_info").getString("accept_type_booking").equals("P")) {
//                Open Popup With pet_info_section and  users_payment_section from JSON
                startActivity(new Intent(this, AcceptBookingActivity.class));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void Deny_Button(String BookingID) {
        appLoader.Show();

        String URL = AppConstant.BASEURL + "booking_deny_confirm?user_id=" + AppConstant.UserId + "&booking_id=" + BookingID;
        new CustomJSONParser().APIForGetMethod(URL, new ArrayList<SetGetAPIPostData>(), new CustomJSONParser.JSONResponseInterface() {
            @Override
            public void OnSuccess(String Result) {

                String Message = "";
                try {
                    Message = new JSONObject(Result).getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                MYALERT.AlertForAPIRESPONSE(getString(R.string.Deny), Message, new MYAlert.OnlyMessage() {
                    @Override
                    public void OnOk(boolean res) {

                    }
                });

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

    private void Send_Message(String Title, String Hint, String ButtonName, final String BookingID, final String BookingType) {
        new MYAlert(this).AlertBoxMessageSend(Title, Hint, ButtonName, new MYAlert.OnEditTexSubmit() {
            @Override
            public void OnEditSubmit(String Messge) {

                appLoader.Show();
                HashMap<String, String> Params = new HashMap<String, String>();
//                try {
//                    Params.put("booking_id",jsonObject.getJSONObject("booking_info").getString("id"));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                Params.put("booking_id", BookingID);
                Params.put("message", Messge);
                Params.put("user_id", AppConstant.UserId);
                Params.put("lang_id", AppConstant.Language);
                Params.put("user_timezone", Tz.getID());

                new CustomJSONParser().APIForPostMethod2(AppConstant.BASEURL + "start_message_api", Params, new CustomJSONParser.JSONResponseInterface() {
                    @Override
                    public void OnSuccess(String Result) {
                        appLoader.Dismiss();
                        String Message = "";
                        try {
                            Message = new JSONObject(Result).getString("message");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        MYALERT.AlertForAPIRESPONSE(getString(R.string.sucess), Message, new MYAlert.OnlyMessage() {
                            @Override
                            public void OnOk(boolean res) {

                            }
                        });

                    }

                    @Override
                    public void OnError(String Error, String Response) {
                        appLoader.Dismiss();
                        MYALERT.AlertForAPIRESPONSE(getString(R.string.Error), Response, new MYAlert.OnlyMessage() {
                            @Override
                            public void OnOk(boolean res) {

                            }
                        });

                    }

                    @Override
                    public void OnError(String Error) {
                        appLoader.Dismiss();
                        MYALERT.AlertForAPIRESPONSE(getString(R.string.Error), Error, new MYAlert.OnlyMessage() {
                            @Override
                            public void OnOk(boolean res) {

                            }
                        });

                    }
                });

            }

            @Override
            public void OnCancel(boolean cancel) {

            }
        });

    }

    private void Delete_Button() {
    }

    private void Review_Status() {
        try {
            Intent intent = new Intent(this, AddReviewActivity.class);
            Loger.MSG("During_Intent_B_ID","-->"+jsonObjectPrevious.getJSONObject("booking_info").getString("id"));
            intent.putExtra("B_ID", jsonObjectPrevious.getJSONObject("booking_info").getString("id"));
            startActivityForResult(intent,1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void CancelStatusWork(final String BookingID, final String BookingType, String dialogMSG) {

        new MYAlert(BookingDetailsActivity.this).AlertOkCancel(getString(R.string.cancel), dialogMSG, new MYAlert.OnOkCancel() {
            @Override
            public void OnOk() {
                appLoader.Show();
                HashMap<String, String> Params = new HashMap<String, String>();
                Params.put("booking_id", BookingID);
                Params.put("booking_type", BookingType);
                Params.put("user_id", AppConstant.UserId);
                Params.put("lang_id", AppConstant.Language);
                Params.put("user_timezone", Tz.getID());

                new CustomJSONParser().APIForPostMethod2(AppConstant.BASEURL + "cancel_reservation_request", Params, new CustomJSONParser.JSONResponseInterface() {
                    @Override
                    public void OnSuccess(String Result) {
                        appLoader.Dismiss();
                        String Message = "";
                        try {
                            Message = new JSONObject(Result).getString("message");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        MYALERT.AlertForAPIRESPONSE(getString(R.string.sucess), Message, new MYAlert.OnlyMessage() {
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
                            MYALERT.AlertForAPIRESPONSE(getString(R.string.Error), new JSONObject(Response).getString("message"), new MYAlert.OnlyMessage() {
                                @Override
                                public void OnOk(boolean res) {

                                }
                            });
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void OnError(String Error) {
                        appLoader.Dismiss();
                        MYALERT.AlertForAPIRESPONSE(getString(R.string.Error), Error, new MYAlert.OnlyMessage() {
                            @Override
                            public void OnOk(boolean res) {

                            }
                        });
                    }
                });
            }

            @Override
            public void OnCancel() {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                String Result = data.getStringExtra("Result");
                try {
                    Toast.makeText(BookingDetailsActivity.this,""+new  JSONObject(Result).getString("message"),Toast.LENGTH_SHORT).show();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
                finish();
            }
        }
    }
}
