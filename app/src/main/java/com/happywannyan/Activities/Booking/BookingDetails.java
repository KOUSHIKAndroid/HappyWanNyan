package com.happywannyan.Activities.Booking;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.happywannyan.Activities.MessageDetailsPage;
import com.happywannyan.Constant.AppContsnat;
import com.happywannyan.Font.SFNFBoldTextView;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.POJO.APIPOSTDATA;
import com.happywannyan.R;
import com.happywannyan.Utils.AppLoader;
import com.happywannyan.Utils.JSONPerser;
import com.happywannyan.Utils.Loger;
import com.happywannyan.Utils.MYAlert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimeZone;

public class BookingDetails extends AppCompatActivity {

    JSONObject jsonObject;
    JSONArray PetInfo;
    LinearLayout LLPetInfo, LL_FOOTER1, LL_FOOTER2;
    AppLoader Loader;

    AlertDialog Dialog;
    TimeZone Tz;

    MYAlert MYALERT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);
        Tz=TimeZone.getDefault();
        MYALERT=new MYAlert(this);
        findViewById(R.id.IMG_icon_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        try {
            Loader=new AppLoader(this);
            jsonObject = new JSONObject(getIntent().getStringExtra("data"));
            PetInfo = jsonObject.getJSONArray("pet_details");
            Loger.MSG("@@ Booking Details", jsonObject.toString());
            ImageView profimage = (ImageView) findViewById(R.id.img_view);

            ImageView backimage = (ImageView) findViewById(R.id.IMG_icon_back);
            LLPetInfo = (LinearLayout) findViewById(R.id.LLPetInfo);
            LL_FOOTER1 = (LinearLayout) findViewById(R.id.LL_FOOTER1);
            LL_FOOTER2 = (LinearLayout) findViewById(R.id.LL_FOOTER2);
            Glide.with(this).load(jsonObject.getJSONObject("users_profile").getString("booked_user_image")).into(profimage);
            ((SFNFTextView) findViewById(R.id.tv_name)).setText(jsonObject.getJSONObject("users_profile").getString("custom_quotes"));
            ((SFNFTextView) findViewById(R.id.tv_type)).setText(jsonObject.getJSONObject("users_profile").getString("booked_user_name"));

            ((SFNFTextView) findViewById(R.id.TXT_total_no_pet)).setText(jsonObject.getJSONObject("booking_info").getString("booked_total_pet"));

            ((SFNFBoldTextView) findViewById(R.id.StartDate)).setText(jsonObject.getJSONObject("booking_info").getString("booking_start_date"));
            ((SFNFBoldTextView) findViewById(R.id.Enddate)).setText(jsonObject.getJSONObject("booking_info").getString("booking_end_date"));
            ((SFNFBoldTextView) findViewById(R.id.BookingId)).setText(jsonObject.getJSONObject("booking_info").getString("booking_id"));
            ((SFNFBoldTextView) findViewById(R.id.tv_service_value)).setText(jsonObject.getJSONObject("booking_info").getString("booking_service"));





            if (jsonObject.getJSONObject("booking_info").has("accept_button") && !jsonObject.getJSONObject("booking_info").getString("accept_button").trim().equals("")) {

                View ButtomView = getLayoutInflater().inflate(R.layout.footer_card_button, null);
                ((CardView)ButtomView.findViewById(R.id.Card_AddRevw)).setCardBackgroundColor(Color.parseColor("#bf3e49"));
                ((SFNFBoldTextView) ButtomView.findViewById(R.id.TXT_ButtonName)).setText(jsonObject.getJSONObject("booking_info").getString("accept_button"));
                LL_FOOTER1.addView(ButtomView);
                ButtomView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AcceptButton();
                    }
                });
                

                
            }

            if (jsonObject.getJSONObject("booking_info").has("deny_button") && !jsonObject.getJSONObject("booking_info").getString("deny_button").trim().equals("")) {
                View ButtomView = getLayoutInflater().inflate(R.layout.footer_card_button, null);
                ((SFNFBoldTextView) ButtomView.findViewById(R.id.TXT_ButtonName)).setText(jsonObject.getJSONObject("booking_info").getString("deny_button"));
                if (LL_FOOTER1.getChildCount() <2)
                    LL_FOOTER1.addView(ButtomView);
                else
                    LL_FOOTER2.addView(ButtomView);

                ButtomView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Deny_Button(jsonObject.getJSONObject("booking_info").getString("booking_id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }


            if (jsonObject.getJSONObject("booking_info").has("send_msg_status") && !jsonObject.getJSONObject("booking_info").getString("send_msg_status").trim().equals("")) {
                View ButtomView = getLayoutInflater().inflate(R.layout.footer_card_button, null);
                ((CardView)ButtomView.findViewById(R.id.Card_AddRevw)).setCardBackgroundColor(Color.parseColor("#bf3e49"));

                ((SFNFBoldTextView) ButtomView.findViewById(R.id.TXT_ButtonName)).setText(jsonObject.getJSONObject("booking_info").getString("send_msg_status"));
                if (LL_FOOTER1.getChildCount() < 2)
                    LL_FOOTER1.addView(ButtomView);
                else
                    LL_FOOTER2.addView(ButtomView);

                ButtomView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if(jsonObject.getJSONObject("booking_info").has("send_msg_show") && jsonObject.getJSONObject("booking_info").getInt("send_msg_show")==0)
                                Send_Message(jsonObject.getJSONObject("booking_info").getString("send_msg_status"),getString(R.string.please_enter_message),getString(R.string.submit)
                                ,jsonObject.getJSONObject("booking_info").getString("id"),jsonObject.getJSONObject("booking_info").getString("booking_type"));
                            else
                                GotoMessage(jsonObject.getJSONObject("booking_info").getString("message_id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }


            if (jsonObject.getJSONObject("booking_info").has("delete_button") && !jsonObject.getJSONObject("booking_info").getString("delete_button").trim().equals("")) {
                View ButtomView = getLayoutInflater().inflate(R.layout.footer_card_button, null);
                ((SFNFBoldTextView) ButtomView.findViewById(R.id.TXT_ButtonName)).setText(jsonObject.getJSONObject("booking_info").getString("delete_button"));
                if (LL_FOOTER1.getChildCount() <2)
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


            if (jsonObject.getJSONObject("booking_info").has("review_status") && !jsonObject.getJSONObject("booking_info").getString("review_status").trim().equals("")) {
                View ButtomView = getLayoutInflater().inflate(R.layout.footer_card_button, null);
                ((SFNFBoldTextView) ButtomView.findViewById(R.id.TXT_ButtonName)).setText(jsonObject.getJSONObject("booking_info").getString("review_status"));
                if (LL_FOOTER1.getChildCount() <2)
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

            if (jsonObject.getJSONObject("booking_info").has("cancel_status") && !jsonObject.getJSONObject("booking_info").getString("cancel_status").trim().equals("")) {
                View ButtomView = getLayoutInflater().inflate(R.layout.footer_card_button, null);
                ((CardView)ButtomView.findViewById(R.id.Card_AddRevw)).setCardBackgroundColor(Color.parseColor("#bf3e49"));
                ((SFNFBoldTextView) ButtomView.findViewById(R.id.TXT_ButtonName)).setText(jsonObject.getJSONObject("booking_info").getString("cancel_status"));
                if (LL_FOOTER1.getChildCount() < 2)
                    LL_FOOTER1.addView(ButtomView);
                else
                    LL_FOOTER2.addView(ButtomView);

                ButtomView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if(jsonObject.getJSONObject("booking_info").getInt("refund_status")==0)
                            CancelStatusWork(jsonObject.getJSONObject("booking_info").getString("booking_id"),jsonObject.getJSONObject("booking_info").getString("booking_type"));
                            if(jsonObject.getJSONObject("booking_info").getInt("refund_status")==1)
                            {
                                CancelWith_Reasons(jsonObject);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

            if (jsonObject.getJSONObject("booking_info").has("cancel_status_button") && !jsonObject.getJSONObject("booking_info").getString("cancel_status_button").trim().equals("")) {
                View ButtomView = getLayoutInflater().inflate(R.layout.footer_card_button, null);
                ((SFNFBoldTextView) ButtomView.findViewById(R.id.TXT_ButtonName)).setText(jsonObject.getJSONObject("booking_info").getString("cancel_status_button"));
                if (LL_FOOTER1.getChildCount() <2)
                    LL_FOOTER1.addView(ButtomView);
                else
                    LL_FOOTER2.addView(ButtomView);


                ((CardView)ButtomView.findViewById(R.id.Card_AddRevw)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CancelStatusButtonWork();
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
                    ((SFNFTextView) PetView.findViewById(R.id.tv_pet_name_value)).setText(PetObj.getJSONArray("pet_info").getJSONObject(0).getString("value"));
                    ((SFNFTextView) PetView.findViewById(R.id.tv_name_pet)).setText(PetObj.getJSONArray("pet_info").getJSONObject(0).getString("name"));

                    ((SFNFBoldTextView) PetView.findViewById(R.id.tv_year)).setText(PetObj.getJSONArray("pet_info").getJSONObject(1).getString("value"));
                    ((SFNFTextView) PetView.findViewById(R.id.year_lbl)).setText(PetObj.getJSONArray("pet_info").getJSONObject(1).getString("name"));

                    ((SFNFBoldTextView) PetView.findViewById(R.id.tv_month)).setText(PetObj.getJSONArray("pet_info").getJSONObject(2).getString("value"));
                    ((SFNFTextView) PetView.findViewById(R.id.tv_month_lbl)).setText(PetObj.getJSONArray("pet_info").getJSONObject(2).getString("name"));

                    ((SFNFBoldTextView) PetView.findViewById(R.id.tv_gender)).setText(PetObj.getJSONArray("pet_info").getJSONObject(3).getString("value"));
                    ((SFNFTextView) PetView.findViewById(R.id.tv_gender_lbl)).setText(PetObj.getJSONArray("pet_info").getJSONObject(3).getString("name"));

                    ((SFNFBoldTextView) PetView.findViewById(R.id.tv_breed)).setText(PetObj.getJSONArray("pet_info").getJSONObject(4).getString("value"));
                    ((SFNFTextView) PetView.findViewById(R.id.tv_breed_lbl)).setText(PetObj.getJSONArray("pet_info").getJSONObject(4).getString("name"));

                    ((SFNFBoldTextView) PetView.findViewById(R.id.tv_size_value)).setText(PetObj.getJSONArray("pet_info").getJSONObject(5).getString("value"));
                    ((SFNFTextView) PetView.findViewById(R.id.tv_size_name)).setText(PetObj.getJSONArray("pet_info").getJSONObject(5).getString("name"));

                    ((SFNFTextView) PetView.findViewById(R.id.tv_describe_value)).setText(PetObj.getJSONArray("pet_info").getJSONObject(6).getString("value"));
                    ((SFNFTextView) PetView.findViewById(R.id.tv_describe_name)).setText(PetObj.getJSONArray("pet_info").getJSONObject(6).getString("name"));

                    ((SFNFTextView) PetView.findViewById(R.id.tv_answer_one)).setText(PetObj.getJSONArray("pet_info").getJSONObject(7).getString("value"));
                    ((SFNFTextView) PetView.findViewById(R.id.tv_question_one)).setText(PetObj.getJSONArray("pet_info").getJSONObject(7).getString("name"));


                    ((SFNFTextView) PetView.findViewById(R.id.tv_question_two)).setText(PetObj.getJSONArray("pet_info").getJSONObject(3).getString("name"));
                    ((SFNFTextView) PetView.findViewById(R.id.tv_answer_two)).setText(PetObj.getJSONArray("pet_info").getJSONObject(3).getString("value"));


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

    private void CancelWith_Reasons(JSONObject jsonObject) {

        Intent intent=new Intent(this,CancelBookingWithReasons.class);
        startActivity(intent);

    }

    private void GotoMessage(String MsgId) {

        Intent intent=new Intent(this, MessageDetailsPage.class);
        intent.putExtra("receiver_id",AppContsnat.UserId);
        intent.putExtra("message_id",MsgId);
        startActivity(intent);


    }

    private void AcceptButton() {
    }

    private void Deny_Button(String BookingID) {
        Loader.Show();

        String URL= AppContsnat.BASEURL+"booking_deny_confirm?user_id="+AppContsnat.UserId+"&booking_id="+BookingID;
        new JSONPerser().API_FOR_GET(URL, new ArrayList<APIPOSTDATA>(), new JSONPerser.JSONRESPONSE() {
            @Override
            public void OnSuccess(String Result) {

                String Message="";
                try {
                    Message=new JSONObject(Result).getString("message");
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
                Loader.Dismiss();
            }

            @Override
            public void OnError(String Error) {
                Loader.Dismiss();
            }
        });


    }

    private void Send_Message(String Title, String Hint, String ButtonName, final String BookingID, final String BookingType) {
        new MYAlert(this).AlertBoxMessageSend(Title, Hint, ButtonName, new MYAlert.OnEditTexSubmit() {
            @Override
            public void OnEditSubmit(String Messge) {

                Loader.Show();
                HashMap<String,String> Params=new HashMap<String, String>();
//                try {
//                    Params.put("booking_id",jsonObject.getJSONObject("booking_info").getString("id"));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                Params.put("booking_id",BookingID);
                Params.put("message",Messge);
                Params.put("user_id",AppContsnat.UserId);
                Params.put("lang_id",AppContsnat.Language);
                Params.put("user_timezone",Tz.getID());

                new JSONPerser().API_FOR_POST_2(AppContsnat.BASEURL + "start_message_api", Params, new JSONPerser.JSONRESPONSE() {
                    @Override
                    public void OnSuccess(String Result) {
                        Loader.Dismiss();
                        String Message="";
                        try {
                            Message=new JSONObject(Result).getString("message");
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
                        Loader.Dismiss();
                        MYALERT.AlertForAPIRESPONSE(getString(R.string.Error), Response, new MYAlert.OnlyMessage() {
                            @Override
                            public void OnOk(boolean res) {

                            }
                        });

                    }

                    @Override
                    public void OnError(String Error) {
                        Loader.Dismiss();
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
    }

    private void CancelStatusWork(final String BookingID,final String BookingType) {

        new MYAlert(BookingDetails.this).AlertOkCancel(getString(R.string.cancel), getString(R.string.do_you_want_to_cancel_booking), new MYAlert.OnOkCancel() {
            @Override
            public void OnOk() {
                Loader.Show();
                HashMap<String,String> Params=new HashMap<String, String>();
                Params.put("booking_id",BookingID);
                Params.put("booking_type",BookingType);
                Params.put("user_id",AppContsnat.UserId);
                Params.put("lang_id",AppContsnat.Language);
                Params.put("user_timezone",Tz.getID());

                new JSONPerser().API_FOR_POST_2(AppContsnat.BASEURL + "cancel_reservation_request", Params, new JSONPerser.JSONRESPONSE() {
                    @Override
                    public void OnSuccess(String Result) {
                        Loader.Dismiss();
                        String Message="";
                        try {
                            Message=new JSONObject(Result).getString("message");
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
                        Loader.Dismiss();
                        MYALERT.AlertForAPIRESPONSE(getString(R.string.Error), Response, new MYAlert.OnlyMessage() {
                            @Override
                            public void OnOk(boolean res) {

                            }
                        });

                    }

                    @Override
                    public void OnError(String Error) {
                        Loader.Dismiss();
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

    private void CancelStatusButtonWork() {
    }
}
