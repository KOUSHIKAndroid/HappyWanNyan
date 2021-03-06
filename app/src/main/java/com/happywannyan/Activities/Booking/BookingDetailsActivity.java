package com.happywannyan.Activities.Booking;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.happywannyan.Activities.MessageDetailsPageActivity;
import com.happywannyan.Adapter.CardAdapter;
import com.happywannyan.Constant.AppConstant;
import com.happywannyan.Font.SFNFBoldTextView;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.POJO.SetGetAPIPostData;
import com.happywannyan.POJO.SetGetCards;
import com.happywannyan.POJO.SetGetStripData;
import com.happywannyan.R;
import com.happywannyan.SitterBooking.BookingFragmentFoure;
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

            if (!jsonObjectPrevious.getJSONObject("users_profile").getString("booked_user_image").trim().equals("")) {
                Glide.with(this).load(jsonObjectPrevious.getJSONObject("users_profile").getString("booked_user_image").trim()).into(profimage);
            }
            ((SFNFTextView) findViewById(R.id.tv_name)).setText(jsonObjectPrevious.getJSONObject("users_profile").getString("who_booked"));
            ((SFNFTextView) findViewById(R.id.tv_type)).setText(jsonObjectPrevious.getJSONObject("users_profile").getString("booked_user_name"));

            ((SFNFTextView) findViewById(R.id.TXT_total_no_pet)).setText(jsonObjectPrevious.getJSONObject("booking_info").getString("booked_total_pet"));

            ((SFNFBoldTextView) findViewById(R.id.StartDate)).setText(jsonObjectPrevious.getJSONObject("booking_info").getString("booking_start_date"));
            if (jsonObjectPrevious.getJSONObject("booking_info").getString("booking_end_date").trim().equals("")) {
                ((SFNFBoldTextView) findViewById(R.id.Enddate)).setText(jsonObjectPrevious.getJSONObject("booking_info").getString("booking_start_date"));
            } else {
                ((SFNFBoldTextView) findViewById(R.id.Enddate)).setText(jsonObjectPrevious.getJSONObject("booking_info").getString("booking_end_date"));
            }


            Loger.MSG("jsonObjectPrevious-->", "" + jsonObjectPrevious);

            if (jsonObjectPrevious.getJSONObject("booking_info").getString("booking_id").equalsIgnoreCase("")) {
                ((SFNFTextView) findViewById(R.id.tv_booking_id_name)).setText("");
                if (jsonObjectPrevious.getJSONObject("booking_info").getString("booking_type").equalsIgnoreCase("PE")) {

                    if (jsonObjectPrevious.getJSONObject("booking_info").getString("accept_type_booking").equalsIgnoreCase("P")
                            || jsonObjectPrevious.getJSONObject("booking_info").getString("accept_type_booking").equalsIgnoreCase("N")) {

                        ((SFNFBoldTextView) findViewById(R.id.BookingId)).setText(getResources().getString(R.string.pending_booking) + ">>");
                        ((SFNFBoldTextView) findViewById(R.id.BookingId)).setTypeface(null, Typeface.BOLD);
                        ((SFNFBoldTextView) findViewById(R.id.BookingId)).setTextColor(Color.parseColor("#FFFFFF"));
                    } else {
                        ((SFNFBoldTextView) findViewById(R.id.BookingId)).setText(getResources().getString(R.string.pending_booking));
                        ((SFNFBoldTextView) findViewById(R.id.BookingId)).setTypeface(null, Typeface.BOLD);
                        ((SFNFBoldTextView) findViewById(R.id.BookingId)).setTextColor(Color.parseColor("#FFFFFF"));
                    }
                } else {
                    ((SFNFBoldTextView) findViewById(R.id.BookingId)).setText(getResources().getString(R.string.not_a_booking));
                    ((SFNFBoldTextView) findViewById(R.id.BookingId)).setTypeface(null, Typeface.BOLD);
                    ((SFNFBoldTextView) findViewById(R.id.BookingId)).setTextColor(Color.parseColor("#FFFFFF"));
                }
            } else {
                ((SFNFTextView) findViewById(R.id.tv_booking_id_name)).setText(getResources().getString(R.string.up_coming_booking_id));
                ((SFNFBoldTextView) findViewById(R.id.BookingId)).setText(jsonObjectPrevious.getJSONObject("booking_info").getString("booking_id"));
                ((SFNFBoldTextView) findViewById(R.id.BookingId)).setTypeface(null, Typeface.BOLD);
                ((SFNFBoldTextView) findViewById(R.id.BookingId)).setTextColor(Color.parseColor("#FFFFFF"));
            }

            ((SFNFBoldTextView) findViewById(R.id.tv_service_value)).setText(jsonObjectPrevious.getJSONObject("booking_info").getString("booking_service"));

            if (!jsonObjectPrevious.getJSONObject("booking_info").getString("no_of_visit").equals("")) {
                ((SFNFTextView) findViewById(R.id.tv_visits_or_times)).setText(getResources().getString(R.string.number_of_visits));
                ((SFNFBoldTextView) findViewById(R.id.tv_visits_or_times_value)).setText(jsonObjectPrevious.getJSONObject("booking_info").getString("no_of_visit"));

            } else if (!jsonObjectPrevious.getJSONObject("booking_info").getString("no_of_times").equals("")) {
                ((SFNFTextView) findViewById(R.id.tv_visits_or_times)).setText(getResources().getString(R.string.number_of_times));
                ((SFNFBoldTextView) findViewById(R.id.tv_visits_or_times_value)).setText(jsonObjectPrevious.getJSONObject("booking_info").getString("no_of_times"));
            } else {
                ((SFNFTextView) findViewById(R.id.tv_visits_or_times)).setText("");
                ((SFNFBoldTextView) findViewById(R.id.tv_visits_or_times_value)).setText("");
            }


//            if (!jsonObjectPrevious.getJSONObject("booking_info").getString("trust_safety_fee").equals("")){
//                findViewById(R.id.LLSafetyAndCoupon).setVisibility(View.VISIBLE);
//                ((SFNFBoldTextView) findViewById(R.id.tv_trust_and_safety_value)).setText(jsonObjectPrevious.getJSONObject("booking_info").getString("trust_safety_fee"));
//            }else {
//                findViewById(R.id.LLSafetyAndCoupon).setVisibility(View.GONE);
//            }

            ((SFNFBoldTextView) findViewById(R.id.tv_trust_and_safety_value)).setText(jsonObjectPrevious.getJSONObject("booking_info").getString("trust_safety_fee"));


            if (AppConstant.UserId.equals(jsonObjectPrevious.getJSONObject("booking_info").getString("buyer_id").trim())) {

                if (jsonObjectPrevious.getJSONObject("booking_info").getString("coupon_amount").equals("")) {

                    findViewById(R.id.ExtraLL_Subtotal).setVisibility(View.GONE);
                    findViewById(R.id.ExtraLL_Coupon).setVisibility(View.GONE);

                    ((SFNFTextView) findViewById(R.id.tv_total_amount)).setText(jsonObjectPrevious.getJSONObject("booking_info").getString("booked_total_amount"));
                } else {
                    findViewById(R.id.ExtraLL_Subtotal).setVisibility(View.VISIBLE);
                    findViewById(R.id.ExtraLL_Coupon).setVisibility(View.VISIBLE);

                    ((SFNFTextView) findViewById(R.id.tv_total_amount)).setText(jsonObjectPrevious.getJSONObject("booking_info").getString("sub_amount"));
                    ((SFNFTextView) findViewById(R.id.tv_subtotal)).setText(jsonObjectPrevious.getJSONObject("booking_info").getString("booked_total_amount"));
                    ((SFNFTextView) findViewById(R.id.tv_coupon)).setText(jsonObjectPrevious.getJSONObject("booking_info").getString("coupon_amount"));
                }
            } else {

                findViewById(R.id.ExtraLL_Subtotal).setVisibility(View.VISIBLE);
                findViewById(R.id.ExtraLL_Coupon).setVisibility(View.GONE);
                ((SFNFTextView) findViewById(R.id.tv_total_amount)).setText(jsonObjectPrevious.getJSONObject("booking_info").getString("booked_total_amount"));
                ((SFNFTextView) findViewById(R.id.tv_subtotal)).setText(jsonObjectPrevious.getJSONObject("booking_info").getString("sub_amount"));
            }


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
                            DenyButton(jsonObjectPrevious.getJSONObject("booking_info").getString("id"));
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

                            if (AppConstant.UserId.equals(jsonObjectPrevious.getJSONObject("booking_info").getString("sitter_users_id"))) {
                                if (jsonObjectPrevious.getJSONObject("booking_info").getString("booking_type").equals("U")) {
                                    seenStatusDecrease("U");
                                } else if (jsonObjectPrevious.getJSONObject("booking_info").getString("booking_type").equals("PE")) {
                                    seenStatusDecrease("PE");
                                }
                            }

                            if (jsonObjectPrevious.getJSONObject("booking_info").has("send_msg_show") && jsonObjectPrevious.getJSONObject("booking_info").getInt("send_msg_show") == 0) {
                                if (block_user_status == 0) {
                                    SendMessage(jsonObjectPrevious.getJSONObject("booking_info").getString("send_msg_status"), getString(R.string.please_enter_message), getString(R.string.submit)
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
                        DeleteBookingButton();
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
                        try {
                            if (jsonObjectPrevious.getJSONObject("booking_info").has("review_details")) {
                                showReviewStatus();
                            } else {
                                AddReviewStatus();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
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
                            if (jsonObjectPrevious.getJSONObject("booking_info").getInt("refund_status") == 0) {
                                Loger.MSG("bookingId", jsonObjectPrevious.getJSONObject("booking_info").getString("id"));
                                CancelStatusWork("PE", jsonObjectPrevious.getJSONObject("booking_info").getString("id"), jsonObjectPrevious.getJSONObject("booking_info").getString("booking_type"), getString(R.string.do_you_want_to_cancel_booking));
                            } else if (jsonObjectPrevious.getJSONObject("booking_info").getInt("refund_status") == 1) {
                                CancelBookingWithReasons();
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
                            Loger.MSG("bookingId", jsonObjectPrevious.getJSONObject("booking_info").getString("id"));
                            CancelStatusWork("U", jsonObjectPrevious.getJSONObject("booking_info").getString("id"), jsonObjectPrevious.getJSONObject("booking_info").getString("booking_type"), getString(R.string.are_you_sure_you_want_to_cancel_reservation_request));
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

                    if (!PetObj.getString("pet_image").trim().equals("")) {
                        Glide.with(this).load(PetObj.getString("pet_image").trim()).into(petimage);
                    }

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

    private void CancelBookingWithReasons() {
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

            try {
                if (AppConstant.UserId.equals(jsonObjectPrevious.getJSONObject("booking_info").getString("sitter_users_id"))) {
                    seenStatusDecrease("PEA");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (jsonObjectPrevious.getJSONObject("booking_info").getString("accept_type_booking").equals("N")) {
//                Normal Confimation Accpet
                MYALERT.AlertAccept_Cancel(getString(R.string.accept), getString(R.string.confirm_somple_msg), new MYAlert.OnOkCancel() {
                    @Override
                    public void OnOk() {
                        appLoader.Show();

                        ArrayList<SetGetAPIPostData> Params = new ArrayList<>();
                        SetGetAPIPostData setGetAPIPostData = new SetGetAPIPostData();
                        setGetAPIPostData.setPARAMS("user_id");
                        setGetAPIPostData.setValues(AppConstant.UserId);
                        Params.add(setGetAPIPostData);

                        try {
                            setGetAPIPostData = new SetGetAPIPostData();
                            setGetAPIPostData.setPARAMS("booking_id");
                            setGetAPIPostData.setValues(jsonObjectPrevious.getJSONObject("booking_info").getString("id"));
                            Params.add(setGetAPIPostData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        setGetAPIPostData = new SetGetAPIPostData();
                        setGetAPIPostData.setPARAMS("langid");
                        setGetAPIPostData.setValues(AppConstant.Language);
                        Params.add(setGetAPIPostData);

                        setGetAPIPostData = new SetGetAPIPostData();
                        setGetAPIPostData.setPARAMS("user_timezone");
                        setGetAPIPostData.setValues(TimeZone.getDefault().getID());
                        Params.add(setGetAPIPostData);


                        new CustomJSONParser().postDataUsingHttp(BookingDetailsActivity.this, AppConstant.BASEURL + "normal_accept_booking", Params, new CustomJSONParser.JSONResponseInterface() {
                            @Override
                            public void OnSuccess(String Result) {
                                appLoader.Dismiss();
                                try {
                                    MYALERT.AlertForAPIRESPONSE(getString(R.string.accept), new JSONObject(Result).getString("message"), new MYAlert.OnlyMessage() {
                                        @Override
                                        public void OnOk(boolean res) {

                                            Intent resultIntent = new Intent();
                                            resultIntent.putExtra("value", "done");
                                            setResult(Activity.RESULT_OK, resultIntent);
                                            finish();

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

                                if (Error.equalsIgnoreCase(getResources().getString(R.string.please_check_your_internet_connection))) {
                                    Toast.makeText(BookingDetailsActivity.this, Error, Toast.LENGTH_SHORT).show();
                                }

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

                Intent intent = new Intent(this, AcceptBookingActivity.class);
                intent.putExtra("search_id", jsonObjectPrevious.getJSONObject("booking_info").getString("id"));
                startActivity(intent);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void DenyButton(String BookingID) {
        appLoader.Show();

        try {
            if (AppConstant.UserId.equals(jsonObjectPrevious.getJSONObject("booking_info").getString("sitter_users_id"))) {
                seenStatusDecrease("PE");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = AppConstant.BASEURL + "booking_deny_confirm?user_id=" + AppConstant.UserId + "&booking_id=" + BookingID + "&lang_id=" + AppConstant.Language;
        new CustomJSONParser().APIForGetMethod(BookingDetailsActivity.this, URL, new ArrayList<SetGetAPIPostData>(), new CustomJSONParser.JSONResponseInterface() {
            @Override
            public void OnSuccess(String Result) {
                appLoader.Dismiss();
                String Message = "";
                try {
                    Message = new JSONObject(Result).getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                MYALERT.AlertForAPIRESPONSE(getString(R.string.Deny), Message, new MYAlert.OnlyMessage() {
                    @Override
                    public void OnOk(boolean res) {
                        try {
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("value", "done");
                            setResult(Activity.RESULT_OK, resultIntent);
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void OnError(String Error, String Response) {
                appLoader.Dismiss();
                Loger.Error("Error-->", Error);
                Loger.Error("Response-->", Response);
                try {
                    MYALERT.AlertForAPIRESPONSE(getString(R.string.deny), new JSONObject(Response).getString("message"), new MYAlert.OnlyMessage() {
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
                    Toast.makeText(BookingDetailsActivity.this, Error, Toast.LENGTH_SHORT).show();
                }
                Loger.Error("Error-->", Error);

                MYALERT.AlertForAPIRESPONSE(getString(R.string.Error), Error, new MYAlert.OnlyMessage() {
                    @Override
                    public void OnOk(boolean res) {

                    }
                });
            }
        });


    }

    private void SendMessage(String Title, String Hint, String ButtonName, final String BookingID, final String BookingType) {
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

                new CustomJSONParser().APIForPostMethod2(BookingDetailsActivity.this, AppConstant.BASEURL + "start_message_api", Params, new CustomJSONParser.JSONResponseInterface() {
                    @Override
                    public void OnSuccess(String Result) {
                        appLoader.Dismiss();

                        MYALERT.AlertForAPIRESPONSE(getString(R.string.sucess), getResources().getString(R.string.booking_message_send_successfully), new MYAlert.OnlyMessage() {
                            @Override
                            public void OnOk(boolean res) {

                                Intent resultIntent = new Intent();
                                resultIntent.putExtra("value", "done");
                                setResult(Activity.RESULT_OK, resultIntent);
                                finish();
                            }
                        });
                    }

                    @Override
                    public void OnError(String Error, String Response) {
                        appLoader.Dismiss();

                        MYALERT.AlertForAPIRESPONSE(getString(R.string.Error), getResources().getString(R.string.some_thing_wrong), new MYAlert.OnlyMessage() {
                            @Override
                            public void OnOk(boolean res) {

                            }
                        });
                    }

                    @Override
                    public void OnError(String Error) {
                        appLoader.Dismiss();
                        if (Error.equalsIgnoreCase(getResources().getString(R.string.please_check_your_internet_connection))) {
                            Toast.makeText(BookingDetailsActivity.this, Error, Toast.LENGTH_SHORT).show();
                        }
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

    private void DeleteBookingButton() {
        new MYAlert(BookingDetailsActivity.this).AlertOkCancel("", getString(R.string.are_you_sure_you_want_to_delete),
                getResources().getString(R.string.ok),
                getResources().getString(R.string.cancel),
                new MYAlert.OnOkCancel() {
                    @Override
                    public void OnOk() {
                        appLoader.Show();
                        String URL = null;
                        try {
                            URL = AppConstant.BASEURL + "delete_booking_api?user_id=" + AppConstant.UserId + "&booking_id=" + jsonObjectPrevious.getJSONObject("booking_info").getString("id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        new CustomJSONParser().APIForGetMethod(BookingDetailsActivity.this, URL, new ArrayList<SetGetAPIPostData>(), new CustomJSONParser.JSONResponseInterface() {
                            @Override
                            public void OnSuccess(String Result) {
                                appLoader.Dismiss();
                                Loger.MSG("Result", Result);
                                try {
                                    if (new JSONObject(Result).getBoolean("response")) {
                                        Toast.makeText(BookingDetailsActivity.this,
//                                                new JSONObject(Result).getString("messege")
                                                getResources().getString(R.string.booking_delete_successfully)
                                                , Toast.LENGTH_SHORT).show();

                                        Intent resultIntent = new Intent();
                                        resultIntent.putExtra("value", "done");
                                        setResult(Activity.RESULT_OK, resultIntent);
                                        finish();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void OnError(String Error, String Response) {
                                appLoader.Dismiss();
//                                try {

                                MYALERT.AlertForAPIRESPONSE(getString(R.string.delete),
//                                            new JSONObject(Response).getString("message")
                                        getResources().getString(R.string.already_booking_deleted)
                                        , new MYAlert.OnlyMessage() {
                                            @Override
                                            public void OnOk(boolean res) {

                                            }
                                        });
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
                            }

                            @Override
                            public void OnError(String Error) {
                                appLoader.Dismiss();
                                if (Error.equalsIgnoreCase(getResources().getString(R.string.please_check_your_internet_connection))) {
                                    Toast.makeText(BookingDetailsActivity.this, Error, Toast.LENGTH_SHORT).show();
                                }
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

    private void AddReviewStatus() {
        try {
            Intent intent = new Intent(this, AddReviewActivity.class);
            Loger.MSG("During_Intent_B_ID", "-->" + jsonObjectPrevious.getJSONObject("booking_info").getString("id"));
            intent.putExtra("B_ID", jsonObjectPrevious.getJSONObject("booking_info").getString("id"));
            startActivityForResult(intent, 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void CancelStatusWork(final String forPeOrU, final String BookingID, final String BookingType, String dialogMSG) {

        new MYAlert(BookingDetailsActivity.this).AlertOkCancel(getString(R.string.cancel), dialogMSG,
                getResources().getString(R.string.ok),
                getResources().getString(R.string.cancel),
                new MYAlert.OnOkCancel() {
                    @Override
                    public void OnOk() {
                        appLoader.Show();
                        HashMap<String, String> Params = new HashMap<String, String>();
                        Params.put("booking_id", BookingID);
                        Params.put("booking_type", BookingType);
                        Params.put("user_id", AppConstant.UserId);
                        Params.put("lang_id", AppConstant.Language);
                        Params.put("user_timezone", Tz.getID());

                        new CustomJSONParser().APIForPostMethod2(BookingDetailsActivity.this, AppConstant.BASEURL + "cancel_reservation_request", Params, new CustomJSONParser.JSONResponseInterface() {
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

                                        Intent resultIntent = new Intent();
                                        resultIntent.putExtra("value", "done");
                                        setResult(Activity.RESULT_OK, resultIntent);
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
                                if (Error.equalsIgnoreCase(getResources().getString(R.string.please_check_your_internet_connection))) {
                                    Toast.makeText(BookingDetailsActivity.this, Error, Toast.LENGTH_SHORT).show();
                                }
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
            if (resultCode == RESULT_OK) {
                String Result = data.getStringExtra("Result");
                try {
                    Toast.makeText(BookingDetailsActivity.this, "" + new JSONObject(Result).getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                Intent resultIntent = new Intent();
                resultIntent.putExtra("value", "done");
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        }
    }

    private void showReviewStatus() {
        // custom dialog
        final Dialog dialog = new Dialog(BookingDetailsActivity.this);
        dialog.setContentView(R.layout.dailog_custom_review);
        dialog.setTitle("");

        RatingBar rtb_review = (RatingBar) dialog.findViewById(R.id.rtb_review);
        ImageView img_review = (ImageView) dialog.findViewById(R.id.img_review);
        SFNFTextView tv_review_desc = (SFNFTextView) dialog.findViewById(R.id.tv_review_desc);


        try {
            rtb_review.setRating(Float.parseFloat(jsonObjectPrevious.getJSONObject("booking_info").getJSONObject("review_details").getString("type_overall")));
            tv_review_desc.setText(jsonObjectPrevious.getJSONObject("booking_info").getJSONObject("review_details").getString("review_desc"));
            if (!jsonObjectPrevious.getJSONObject("booking_info").getJSONObject("review_details").getString("rvw_msg_attachment").trim().equals("")) {
                img_review.setVisibility(View.VISIBLE);
                Glide.with(this).load(jsonObjectPrevious.getJSONObject("booking_info").getJSONObject("review_details").getString("rvw_msg_attachment").trim()).into(img_review);
            } else {
                img_review.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        dialog.show();
    }

    private void seenStatusDecrease(String booking_type) {
        ArrayList<SetGetAPIPostData> setGetAPIPostDataArrayList = new ArrayList<>();
        SetGetAPIPostData setGetAPIPostData = new SetGetAPIPostData();

        try {

            setGetAPIPostData.setPARAMS("booking_id");
            setGetAPIPostData.setValues(jsonObjectPrevious.getJSONObject("booking_info").getString("id"));
            setGetAPIPostDataArrayList.add(setGetAPIPostData);


            setGetAPIPostData = new SetGetAPIPostData();
            setGetAPIPostData.setPARAMS("sitter_id");
            setGetAPIPostData.setValues(jsonObjectPrevious.getJSONObject("booking_info").getString("sitter_users_id"));
            setGetAPIPostDataArrayList.add(setGetAPIPostData);


            setGetAPIPostData = new SetGetAPIPostData();
            setGetAPIPostData.setPARAMS("booking_type");
            setGetAPIPostData.setValues(booking_type);
            setGetAPIPostDataArrayList.add(setGetAPIPostData);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new CustomJSONParser().APIForGetMethod(BookingDetailsActivity.this, AppConstant.BASEURL + "app_booking_seen_status?user_id=" + AppConstant.UserId
                , setGetAPIPostDataArrayList, new CustomJSONParser.JSONResponseInterface() {
                    @Override
                    public void OnSuccess(String Result) {
                    }

                    @Override
                    public void OnError(String Error, String Response) {
                    }

                    @Override
                    public void OnError(String Error) {
                        appLoader.Dismiss();
                        if (Error.equalsIgnoreCase(getResources().getString(R.string.please_check_your_internet_connection))) {
                            Toast.makeText(BookingDetailsActivity.this, Error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
