package com.happywannyan.SitterBooking;


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
import com.happywannyan.Constant.AppContsnat;
import com.happywannyan.OnFragmentInteractionListener;
import com.happywannyan.POJO.APIPOSTDATA;
import com.happywannyan.R;
import com.happywannyan.Utils.AppLoader;
import com.happywannyan.Utils.CustomJSONParser;
import com.happywannyan.Utils.Loger;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

public class BookingOne extends AppCompatActivity implements View.OnClickListener, OnFragmentInteractionListener {

    public String ServiceList;
    public String PRESelectService;

    AppLoader Apploaders;
    JSONObject ItemDetails;
    public ArrayList<String> MyPetList;
    public boolean DropDown = true;

    public boolean DoubleDate = true;

    public ArrayList<APIPOSTDATA> FirstPageData;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_one);
        findViewById(R.id.IMG_icon_back).setOnClickListener(this);

        Apploaders = new AppLoader(this);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        try {
            ServiceList = getIntent().getStringExtra("LIST");
            DropDown = getIntent().getBooleanExtra("Single", true);
            Loger.MSG("@@ LIST", " DATA-" + ServiceList);
            PRESelectService = getIntent().getStringExtra("SELECT");
            Loger.MSG("@@ SELECT", " DATA-" + PRESelectService);

            ItemDetails = new JSONObject(getIntent().getStringExtra("ItemDetails"));
            Loger.MSG("@@ ITEM", " DATA-" + ItemDetails);

        } catch (JSONException e) {
            Loger.Error("@@ Error", e.getMessage());
        }

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.Body, BookingFragmnetOne.newInstance(ServiceList, PRESelectService));
        fragmentTransaction.disallowAddToBackStack().commit();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.IMG_icon_back:
                onBackPressed();
                break;
        }
    }


    @Override
    public void onFragmentInteraction(String uri) {
        switch (uri) {

            case "Two":
                Apploaders.Show();

                try {
                    APIPOSTDATA apipostdata = new APIPOSTDATA();
                    apipostdata.setPARAMS("sitter_user_id");
                    apipostdata.setValues(ItemDetails.getString("sitter_user_id"));
                    FirstPageData.add(apipostdata);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                new CustomJSONParser().API_FOR_POST(AppContsnat.BASEURL + "before_booking_info", FirstPageData, new CustomJSONParser.JSONRESPONSE() {
                    @Override
                    public void OnSuccess(String Result) {
                        Apploaders.Dismiss();
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.add(R.id.Body, BookingFragmentTwo.newInstance(Result, null));
                        fragmentTransaction.addToBackStack(null).commit();

                    }

                    @Override
                    public void OnError(String Error, String Response) {
                        Apploaders.Dismiss();
                    }

                    @Override
                    public void OnError(String Error) {
                        Apploaders.Dismiss();
                    }
                });


                break;
            case "Three":
                Apploaders.Show();
                new CustomJSONParser().API_FOR_POST(AppContsnat.BASEURL + "before_booking_info", FirstPageData, new CustomJSONParser.JSONRESPONSE() {
                    @Override
                    public void OnSuccess(String Result) {

                        Apploaders.Dismiss();
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.add(R.id.Body, BookingFrgamnetThree.newInstance(Result, null));
                        fragmentTransaction.addToBackStack(null).commit();

                    }

                    @Override
                    public void OnError(String Error, String Response) {
                        Apploaders.Dismiss();
                    }

                    @Override
                    public void OnError(String Error) {
                        Apploaders.Dismiss();
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
        Apploaders.Show();
        new CustomJSONParser().API_FOR_POST(AppContsnat.BASEURL + "confirm_reservation_request", FirstPageData, new CustomJSONParser.JSONRESPONSE() {
            @Override
            public void OnSuccess(String Result) {
                Apploaders.Dismiss();
                Loger.MSG("Result-->", Result);
            }

            @Override
            public void OnError(String Error, String Response) {
                Apploaders.Dismiss();
            }

            @Override
            public void OnError(String Error) {
                Apploaders.Dismiss();
            }
        });
    }


    public void submitConfirmReservationRequestByVolley() {
        Apploaders.Show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppContsnat.BASEURL + "confirm_reservation_request",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                        Apploaders.Dismiss();
                        Loger.MSG("Result-->", result);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Apploaders.Dismiss();
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
        Apploaders.Show();
        new CustomJSONParser().postDataUsingHttp(AppContsnat.BASEURL + "confirm_reservation_request", FirstPageData, new CustomJSONParser.JSONRESPONSE() {
            @Override
            public void OnSuccess(String Result) {
                Apploaders.Dismiss();
                Loger.MSG("Result-->", Result);
                try {
                    JSONObject jsonObject=new JSONObject(Result);
                    if(jsonObject.getBoolean("response")){
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void OnError(String Error, String Response) {
                Apploaders.Dismiss();
            }

            @Override
            public void OnError(String Error) {
                Apploaders.Dismiss();
            }
        });
    }

}
