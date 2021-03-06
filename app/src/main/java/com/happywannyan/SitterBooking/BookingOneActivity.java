package com.happywannyan.SitterBooking;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.happywannyan.Activities.BaseActivity;
import com.happywannyan.Activities.profile.MeetUpWannyanActivity;
import com.happywannyan.Constant.AppConstant;
import com.happywannyan.OnFragmentInteractionListener;
import com.happywannyan.POJO.SetGetAPIPostData;
import com.happywannyan.R;
import com.happywannyan.Utils.AppLoader;
import com.happywannyan.Utils.CustomJSONParser;
import com.happywannyan.Utils.Loger;
import com.happywannyan.Utils.MYAlert;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class BookingOneActivity extends AppCompatActivity implements View.OnClickListener, OnFragmentInteractionListener {

    public String ServiceList;
    public String PRESelectService;

    public String totalAmount="";

    AppLoader appLoader;
    String SitterId;
    public ArrayList<String> MyPetList;
    public boolean DropDown = true;
    public boolean DoubleDate = true;

    public ArrayList<SetGetAPIPostData> FirstPageData;

    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_one);
        findViewById(R.id.IMG_icon_back).setOnClickListener(this);
        findViewById(R.id.PAGE_Cancel).setOnClickListener(this);

        appLoader = new AppLoader(this);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        ServiceList = getIntent().getStringExtra("LIST");
        DropDown = getIntent().getBooleanExtra("Single", true);
        Loger.MSG("@@ LIST", " DATA-" + ServiceList);
        PRESelectService = getIntent().getStringExtra("SELECT");
        Loger.MSG("@@ SELECT", " DATA-" + PRESelectService);

        SitterId = getIntent().getStringExtra("SitterId");
        Loger.MSG("@@ ITEM", " SitterId-" + SitterId);


        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.Body, BookingFragmentOne.newInstance(ServiceList, PRESelectService));
        fragmentTransaction.disallowAddToBackStack().commit();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.IMG_icon_back:
                onBackPressed();
                break;
            case R.id.PAGE_Cancel:
                new MYAlert(BookingOneActivity.this).AlertOkCancel("",
                        getResources().getString(R.string.do_you_want_to_cancel_booking),
                        getResources().getString(R.string.ok),
                        getResources().getString(R.string.cancel),
                        new MYAlert.OnOkCancel() {
                            @Override
                            public void OnOk() {
                                //onBackPressed();
                                finish();
                            }

                            @Override
                            public void OnCancel() {

                            }
                        });
                break;
        }
    }


    @Override
    public void onFragmentInteraction(String uri) {
        switch (uri) {

            case "Two":
                appLoader.Show();

                SetGetAPIPostData setGetAPIPostData = new SetGetAPIPostData();
                setGetAPIPostData.setPARAMS("sitter_user_id");
                setGetAPIPostData.setValues(SitterId);
                FirstPageData.add(setGetAPIPostData);




                new CustomJSONParser().APIForPostMethod(BookingOneActivity.this,AppConstant.BASEURL + "before_booking_info", FirstPageData, new CustomJSONParser.JSONResponseInterface() {
                    @Override
                    public void OnSuccess(String Result) {
                        appLoader.Dismiss();

                        try {
                            SetGetAPIPostData setGetAPIPostData = new SetGetAPIPostData();
                            setGetAPIPostData.setPARAMS("no_of_days");

                            if (new JSONObject(Result).getJSONObject("info_array").has("no_of_days")) {
                                setGetAPIPostData.setValues(new JSONObject(Result).getJSONObject("info_array").getString("no_of_days"));
                            }
                            else {
                                setGetAPIPostData.setValues(new JSONObject(Result).getJSONObject("info_array").getString("no_of_nights"));
                            }

                            FirstPageData.add(setGetAPIPostData);

                            fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.add(R.id.Body, BookingFragmentTwo.newInstance(Result, null));
                            fragmentTransaction.addToBackStack(null).commit();

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
                            Toast.makeText(BookingOneActivity.this,Error,Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                break;
            case "Three":
                appLoader.Show();
                new CustomJSONParser().APIForPostMethod(BookingOneActivity.this,AppConstant.BASEURL + "before_booking_info", FirstPageData, new CustomJSONParser.JSONResponseInterface() {
                    @Override
                    public void OnSuccess(String Result) {
                        appLoader.Dismiss();
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.add(R.id.Body, BookingFragmentThree.newInstance(Result, null));
                        fragmentTransaction.addToBackStack(null).commit();
                    }

                    @Override
                    public void OnError(String Error, String Response) {
                        appLoader.Dismiss();
                    }

                    @Override
                    public void OnError(String Error) {
                        appLoader.Dismiss();
                        if (Error.equalsIgnoreCase(getResources().getString(R.string.please_check_your_internet_connection))){
                            Toast.makeText(BookingOneActivity.this,Error,Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                break;
            case "four":
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.Body, BookingFragmentFoure.newInstance(null, null));
                fragmentTransaction.addToBackStack(null).commit();
                break;

        }
    }

    public void showConfirmReservationRequest() {
        for (int i = 0; i < FirstPageData.size(); i++) {
            Loger.MSG(FirstPageData.get(i).getPARAMS(), "-->" + FirstPageData.get(i).getValues());
        }
    }

    public void submitConfirmReservationRequest() {
        appLoader.Show();
        new CustomJSONParser().APIForPostMethod(BookingOneActivity.this,AppConstant.BASEURL + "confirm_reservation_request", FirstPageData, new CustomJSONParser.JSONResponseInterface() {
            @Override
            public void OnSuccess(String Result) {
                appLoader.Dismiss();
                Loger.MSG("Result-->", Result);
            }

            @Override
            public void OnError(String Error, String Response) {
                appLoader.Dismiss();
            }

            @Override
            public void OnError(String Error) {
                appLoader.Dismiss();
                if (Error.equalsIgnoreCase(getResources().getString(R.string.please_check_your_internet_connection))){
                    Toast.makeText(BookingOneActivity.this,Error,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void submitConfirmReservationRequestByVolley() {
        appLoader.Show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstant.BASEURL + "confirm_reservation_request",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                        appLoader.Dismiss();
                        Loger.MSG("Result-->", result);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        appLoader.Dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters
                for (int i = 0; i < FirstPageData.size(); i++) {
                    params.put(FirstPageData.get(i).getPARAMS(), FirstPageData.get(i).getValues());
                }

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }


    public void submitConfirmReservationRequestUsingHTTP() {
        appLoader.Show();
        ArrayList<SetGetAPIPostData> FinalPageData=new ArrayList<>();
        for (int j=0;j<FirstPageData.size();j++){

            SetGetAPIPostData setGetAPIPostData=new SetGetAPIPostData();
            setGetAPIPostData.setPARAMS(FirstPageData.get(j).getPARAMS());
            setGetAPIPostData.setValues(FirstPageData.get(j).getValues());
            FinalPageData.add(setGetAPIPostData);
        }

        for (int i = 0; i < FinalPageData.size(); i++) {
            if (FinalPageData.get(i).getPARAMS().equalsIgnoreCase("no_of_pet")) {
                FinalPageData.get(i).setPARAMS("total_pets");
            }
            else if(FinalPageData.get(i).getPARAMS().equalsIgnoreCase("no_of_times"))
            {
                FinalPageData.get(i).setPARAMS("no_times");
            }
            else if(FinalPageData.get(i).getPARAMS().equalsIgnoreCase("no_of_visit"))
            {
                FinalPageData.get(i).setPARAMS("no_of_visits");
            }
            else if(FinalPageData.get(i).getPARAMS().equalsIgnoreCase("no_of_days"))
            {
                FinalPageData.get(i).setPARAMS("total_days");
            }
        }


        for (int i = 0; i < FinalPageData.size(); i++) {
            Loger.MSG(FinalPageData.get(i).getPARAMS(), "-->" + FinalPageData.get(i).getValues());
        }




        new CustomJSONParser().postDataUsingHttp(BookingOneActivity.this,AppConstant.BASEURL + "confirm_reservation_request", FinalPageData, new CustomJSONParser.JSONResponseInterface() {
            @Override
            public void OnSuccess(String Result) {
                appLoader.Dismiss();
                Loger.MSG("Result-->", Result);
                try {
                    JSONObject jsonObject = new JSONObject(Result);
                    if (jsonObject.getBoolean("response")) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.reservation_request_sent), Toast.LENGTH_LONG).show();
                        //finish();
                        Intent intent = new Intent(BookingOneActivity.this, BaseActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        AppConstant.go_to="pending_message";
                        AppConstant.messageAndBookingConditionCheck=true;
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void OnError(String Error, String Response) {
                appLoader.Dismiss();
//                try {
                    Toast.makeText(BookingOneActivity.this,
getResources().getString(R.string.some_thing_wrong)
//                            new JSONObject(Response).getString("message")
                            ,Toast.LENGTH_SHORT).show();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }

            @Override
            public void OnError(String Error) {
                appLoader.Dismiss();
                if (Error.equalsIgnoreCase(getResources().getString(R.string.please_check_your_internet_connection))){
                    Toast.makeText(BookingOneActivity.this,Error,Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(BookingOneActivity.this,Error,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
