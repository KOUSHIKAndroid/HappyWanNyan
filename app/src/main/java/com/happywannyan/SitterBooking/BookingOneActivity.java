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
                        getResources().getString(R.string.do_you_want_to_cancel_booking), new MYAlert.OnOkCancel() {
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

                new CustomJSONParser().APIForPostMethod(AppConstant.BASEURL + "before_booking_info", FirstPageData, new CustomJSONParser.JSONResponseInterface() {
                    @Override
                    public void OnSuccess(String Result) {
                        appLoader.Dismiss();
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.add(R.id.Body, BookingFragmentTwo.newInstance(Result, null));
                        fragmentTransaction.addToBackStack(null).commit();
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


                break;
            case "Three":
                appLoader.Show();
                new CustomJSONParser().APIForPostMethod(AppConstant.BASEURL + "before_booking_info", FirstPageData, new CustomJSONParser.JSONResponseInterface() {
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

//    public void showConfirmReservationRequest() {
//        for (int i = 0; i < FirstPageData.size(); i++) {
//            Loger.MSG(FirstPageData.get(i).getPARAMS(), "-->" + FirstPageData.get(i).getValues());
//        }
//    }

    public void submitConfirmReservationRequest() {
        appLoader.Show();
        new CustomJSONParser().APIForPostMethod(AppConstant.BASEURL + "confirm_reservation_request", FirstPageData, new CustomJSONParser.JSONResponseInterface() {
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
        new CustomJSONParser().postDataUsingHttp(AppConstant.BASEURL + "confirm_reservation_request", FirstPageData, new CustomJSONParser.JSONResponseInterface() {
            @Override
            public void OnSuccess(String Result) {
                appLoader.Dismiss();
                Loger.MSG("Result-->", Result);
                try {
                    JSONObject jsonObject = new JSONObject(Result);
                    if (jsonObject.getBoolean("response")) {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                        //finish();
                        Intent intent = new Intent(BookingOneActivity.this, BaseActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("go_to", "pending_message");
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
            }

            @Override
            public void OnError(String Error) {
                appLoader.Dismiss();
            }
        });
    }
}
