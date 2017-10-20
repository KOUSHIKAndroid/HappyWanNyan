package com.happywannyan.Activities.Booking;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.happywannyan.Adapter.AdapterPendingBookingPetService;
import com.happywannyan.Constant.AppConstant;
import com.happywannyan.Font.SFNFBoldTextView;
import com.happywannyan.POJO.SetGetAPIPostData;
import com.happywannyan.POJO.SetGetPendingBooking;
import com.happywannyan.R;
import com.happywannyan.Utils.AppLoader;
import com.happywannyan.Utils.CustomJSONParser;

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
    String search_id = "";
    AdapterPendingBookingPetService adapterPendingBookingPetService;
    LinearLayout LL_FOOTER1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending);
        rcv_pending_ped_list_service = (RecyclerView) findViewById(R.id.rcv_pending_ped_list_service);
        rcv_pending_ped_list_service.setLayoutManager(new LinearLayoutManager(AcceptBookingActivity.this));

        LL_FOOTER1 = (LinearLayout) findViewById(R.id.LL_FOOTER1);

        View ButtomView = getLayoutInflater().inflate(R.layout.footer_card_button, null);
        ((CardView) ButtomView.findViewById(R.id.Card_AddRevw)).setCardBackgroundColor(Color.parseColor("#bf3e49"));
        ((SFNFBoldTextView) ButtomView.findViewById(R.id.TXT_ButtonName)).setText(getResources().getString(R.string.next));
        LL_FOOTER1.addView(ButtomView);
        ButtomView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AcceptBookingActivity.this,"next",Toast.LENGTH_SHORT).show();
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

        appLoader.Show();
        new CustomJSONParser().APIForGetMethod(AppConstant.BASEURL + "pending_booking_list?", params, new CustomJSONParser.JSONResponseInterface() {

            @Override
            public void OnSuccess(String Result) {
                appLoader.Dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(Result);

                    JSONArray booking_info_array=jsonObject.getJSONArray("booking_info_array");
                    JSONObject booking_info = booking_info_array.getJSONObject(0).getJSONObject("booking_info");
                    JSONObject confirmAccept=booking_info.getJSONObject("confirm_accept");
                    JSONArray petInfoSectionArray=confirmAccept.getJSONArray("pet_info_section");

                    for (int i=0;i<petInfoSectionArray.length();i++){
                        SetGetPendingBooking setGetPendingBooking=new SetGetPendingBooking();
                        setGetPendingBooking.setChecked(false);
                        setGetPendingBooking.setPet_id(petInfoSectionArray.getJSONObject(i).getString("pet_id"));
                        setGetPendingBooking.setPet_details(petInfoSectionArray.getJSONObject(i).getString("pet_details"));
                        pendingBookingArrayList.add(setGetPendingBooking);
                    }

                    adapterPendingBookingPetService = new AdapterPendingBookingPetService(AcceptBookingActivity.this, pendingBookingArrayList);
                    rcv_pending_ped_list_service.setAdapter(adapterPendingBookingPetService);
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
            }
        });

    }
}
