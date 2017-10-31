package com.happywannyan.Activities.Booking;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.happywannyan.Activities.AddAnotherPetsActivity;
import com.happywannyan.Activities.ResetPasswordActivity;
import com.happywannyan.Activities.profile.ContactMsgActivity;
import com.happywannyan.Adapter.AdapterPendingBookingPetService;
import com.happywannyan.Constant.AppConstant;
import com.happywannyan.Font.SFNFBoldTextView;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.POJO.SetGetAPIPostData;
import com.happywannyan.POJO.SetGetPendingBooking;
import com.happywannyan.R;
import com.happywannyan.Utils.AppLoader;
import com.happywannyan.Utils.CustomJSONParser;
import com.happywannyan.Utils.Loger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.TimeZone;

/**
 * Created by su on 10/20/17.
 */

public class AcceptBookingActivity extends AppCompatActivity {
    RecyclerView rcv_pending_ped_list_service;
    ArrayList<SetGetPendingBooking> pendingBookingArrayList;
    AppLoader appLoader;
    String search_id = "",pet_id="";
    AdapterPendingBookingPetService adapterPendingBookingPetService;
    LinearLayout LL_FOOTER1;
    ArrayList<SetGetAPIPostData> params;
    EditText EDX_coupon_code;

    public boolean atLeastOnceCheck=false;

    public ArrayList<SetGetAPIPostData> postParamCoupon;

    String sitter_users_id,booking_id,booked_total_amount;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending);
        rcv_pending_ped_list_service = (RecyclerView) findViewById(R.id.rcv_pending_ped_list_service);
        rcv_pending_ped_list_service.setLayoutManager(new LinearLayoutManager(AcceptBookingActivity.this));


        postParamCoupon = new ArrayList<>();


        findViewById(R.id.tv_coupon_code).setVisibility(View.GONE);
        findViewById(R.id.img_clear).setVisibility(View.GONE);
        EDX_coupon_code = (EditText)findViewById(R.id.EDX_coupon_code);



        EDX_coupon_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (!charSequence.toString().toString().equals("")) {
                    findViewById(R.id.tv_coupon_code).setVisibility(View.VISIBLE);
                    findViewById(R.id.img_clear).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.tv_coupon_code).setVisibility(View.GONE);
                    findViewById(R.id.img_clear).setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        findViewById(R.id.img_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EDX_coupon_code.setText("");
            }
        });



        findViewById(R.id.tv_coupon_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //input_layout_coupon_code.setErrorEnabled(false);
                //input_layout_coupon_code.setError(null);

                ((SFNFTextView) findViewById(R.id.Tv_coupon_code_valid_check)).setVisibility(View.GONE);

                if (EDX_coupon_code.getText().toString().trim().equals("")) {
                    //input_layout_coupon_code.setErrorEnabled(true);
                    //input_layout_coupon_code.setError("Field can't be empty");
                    ((SFNFTextView)findViewById(R.id.Tv_coupon_code_valid_check)).setVisibility(View.VISIBLE);
                    ((SFNFTextView)findViewById(R.id.Tv_coupon_code_valid_check)).setText(getResources().getString(R.string.filed_can_not_be_empty));
                    ((SFNFTextView)findViewById(R.id.Tv_coupon_code_valid_check)).setTextColor(Color.RED);
                } else {
                    if (EDX_coupon_code.getText().toString().trim().length() < 6) {
                        //input_layout_coupon_code.setErrorEnabled(true);
                        //input_layout_coupon_code.setError("Field must be greater then 5");
                        ((SFNFTextView) findViewById(R.id.Tv_coupon_code_valid_check)).setVisibility(View.VISIBLE);
                        ((SFNFTextView) findViewById(R.id.Tv_coupon_code_valid_check)).setText(getResources().getString(R.string.field_length_must_be_greater_than_five));
                        ((SFNFTextView) findViewById(R.id.Tv_coupon_code_valid_check)).setTextColor(Color.RED);
                    } else {
                        SetGetAPIPostData setGetAPIPostData = new SetGetAPIPostData();
                        setGetAPIPostData.setPARAMS("user_id");
                        setGetAPIPostData.setValues("" + AppConstant.UserId);
                        postParamCoupon.add(setGetAPIPostData);

                        setGetAPIPostData = new SetGetAPIPostData();
                        setGetAPIPostData.setPARAMS("coupon_no");
                        setGetAPIPostData.setValues(EDX_coupon_code.getText().toString().trim());
                        postParamCoupon.add(setGetAPIPostData);

                        appLoader.Show();

                        new CustomJSONParser().APIForPostMethod(AcceptBookingActivity.this,AppConstant.BASEURL + "Api_coupon_exits", postParamCoupon, new CustomJSONParser.JSONResponseInterface() {
                            @Override
                            public void OnSuccess(String Result) {
                                appLoader.Dismiss();
                                Loger.MSG("Result", Result);

                                try {
                                    JSONObject jsonObject = new JSONObject(Result);
                                    ((SFNFTextView) findViewById(R.id.Tv_coupon_code_valid_check)).setVisibility(View.VISIBLE);
                                    ((SFNFTextView) findViewById(R.id.Tv_coupon_code_valid_check)).setText(jsonObject.getString("message"));
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        ((SFNFTextView) findViewById(R.id.Tv_coupon_code_valid_check)).setTextColor(getResources().getColor(R.color.colorGreen, null));
                                    } else {
                                        ((SFNFTextView) findViewById(R.id.Tv_coupon_code_valid_check)).setTextColor(getResources().getColor(R.color.colorGreen));
                                    }

//                                    coupon_id = jsonObject.getJSONObject("info_array").getString("id");
//                                    coupon_amount = jsonObject.getJSONObject("info_array").getString("amount");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void OnError(String Error, String Response) {
                                appLoader.Dismiss();
                                try {
                                    JSONObject jsonObject = new JSONObject(Response);
                                    ((SFNFTextView) findViewById(R.id.Tv_coupon_code_valid_check)).setVisibility(View.VISIBLE);
                                    ((SFNFTextView)findViewById(R.id.Tv_coupon_code_valid_check)).setText(jsonObject.getString("message"));
                                    ((SFNFTextView)findViewById(R.id.Tv_coupon_code_valid_check)).setTextColor(Color.RED);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void OnError(String Error) {
                                appLoader.Dismiss();
                                if (Error.equalsIgnoreCase(getResources().getString(R.string.please_check_your_internet_connection))){
                                    Toast.makeText(AcceptBookingActivity.this,Error,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                }
            }
        });





        LL_FOOTER1 = (LinearLayout) findViewById(R.id.LL_FOOTER1);

        View ButtomView = getLayoutInflater().inflate(R.layout.footer_card_button, null);
        ((CardView) ButtomView.findViewById(R.id.Card_AddRevw)).setCardBackgroundColor(Color.parseColor("#bf3e49"));
        ((SFNFBoldTextView) ButtomView.findViewById(R.id.TXT_ButtonName)).setText(getResources().getString(R.string.next));
        LL_FOOTER1.addView(ButtomView);
        ButtomView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atLeastOnceCheck=false;
                for (int i=0;i<pendingBookingArrayList.size();i++){
                    if (pendingBookingArrayList.get(i).isChecked()){

                        pet_id=pendingBookingArrayList.get(i).getPet_id()+",";
                        atLeastOnceCheck=true;
                    }
                }

                if (atLeastOnceCheck){
                    pet_id=pet_id.substring(0,pet_id.length()-1);
                    Intent intent=new Intent(AcceptBookingActivity.this,PaymentPendingBookingActivity.class);
                    intent.putExtra("sitter_users_id",sitter_users_id);
                    intent.putExtra("booking_id",booking_id);
                    intent.putExtra("booked_total_amount",booked_total_amount);
                    intent.putExtra("pet_id",pet_id);
                    startActivity(intent);
                }else {
                    Toast.makeText(AcceptBookingActivity.this,getResources().getString(R.string.check_at_least_one_pet), Toast.LENGTH_SHORT).show();
                }
            }
        });

        pendingBookingArrayList = new ArrayList<>();
        appLoader = new AppLoader(this);

        findViewById(R.id.IMG_icon_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        search_id = getIntent().getExtras().getString("search_id");

        params = new ArrayList<>();

        SetGetAPIPostData setGetAPIPostData = new SetGetAPIPostData();
        setGetAPIPostData.setPARAMS("user_id");
        setGetAPIPostData.setValues(AppConstant.UserId);
        params.add(setGetAPIPostData);

        setGetAPIPostData = new SetGetAPIPostData();
        setGetAPIPostData.setPARAMS("lang_id");
        setGetAPIPostData.setValues(AppConstant.Language);
        params.add(setGetAPIPostData);

        setGetAPIPostData = new SetGetAPIPostData();
        setGetAPIPostData.setPARAMS("start_form");
        setGetAPIPostData.setValues("0");
        params.add(setGetAPIPostData);

        setGetAPIPostData = new SetGetAPIPostData();
        setGetAPIPostData.setPARAMS("per_page");
        setGetAPIPostData.setValues("10");
        params.add(setGetAPIPostData);


        setGetAPIPostData = new SetGetAPIPostData();
        TimeZone tz = TimeZone.getDefault();
        setGetAPIPostData.setPARAMS("user_timezone");
        setGetAPIPostData.setValues(tz.getID());
        params.add(setGetAPIPostData);

        setGetAPIPostData = new SetGetAPIPostData();
        setGetAPIPostData.setPARAMS("search_id");
        setGetAPIPostData.setValues(search_id);
        params.add(setGetAPIPostData);

        loadAcceptBookingDetails();

        findViewById(R.id.tv_add_pet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(AcceptBookingActivity.this, AddAnotherPetsActivity.class), 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
                loadAcceptBookingDetails();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    public void loadAcceptBookingDetails(){
        appLoader.Show();
        new CustomJSONParser().APIForGetMethod(AcceptBookingActivity.this,AppConstant.BASEURL + "pending_booking_list?", params, new CustomJSONParser.JSONResponseInterface() {

            @Override
            public void OnSuccess(String Result) {
                appLoader.Dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(Result);

                    JSONArray booking_info_array=jsonObject.getJSONArray("booking_info_array");
                    JSONObject booking_info = booking_info_array.getJSONObject(0).getJSONObject("booking_info");
                    JSONObject confirmAccept=booking_info.getJSONObject("confirm_accept");
                    JSONArray petInfoSectionArray=confirmAccept.getJSONArray("pet_info_section");

                    sitter_users_id=booking_info.getString("sitter_users_id");
                    booking_id=booking_info.getString("id");
                    booked_total_amount=booking_info.getString("booked_total_amount");

                    if(petInfoSectionArray.length()>0) {
                        rcv_pending_ped_list_service.setVisibility(View.VISIBLE);
                        findViewById(R.id.tv_add_pet).setVisibility(View.GONE);
                        for (int i = 0; i < petInfoSectionArray.length(); i++) {
                            SetGetPendingBooking setGetPendingBooking = new SetGetPendingBooking();
                            setGetPendingBooking.setChecked(false);
                            setGetPendingBooking.setPet_id(petInfoSectionArray.getJSONObject(i).getString("pet_id"));
                            setGetPendingBooking.setPet_details(petInfoSectionArray.getJSONObject(i).getString("pet_details"));
                            pendingBookingArrayList.add(setGetPendingBooking);
                        }
                        adapterPendingBookingPetService = new AdapterPendingBookingPetService(AcceptBookingActivity.this, pendingBookingArrayList);
                        rcv_pending_ped_list_service.setAdapter(adapterPendingBookingPetService);
                    }else {
                        rcv_pending_ped_list_service.setVisibility(View.GONE);
                        findViewById(R.id.tv_add_pet).setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void OnError(String Error, String Response) {
                appLoader.Dismiss();
            }

            @Override
            public void OnError(String Error) {
                appLoader.Dismiss();
                if (Error.equalsIgnoreCase(getResources().getString(R.string.please_check_your_internet_connection))){
                    Toast.makeText(AcceptBookingActivity.this,Error,Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
