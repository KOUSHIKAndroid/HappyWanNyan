package com.happywannyan.SitterBooking;

import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.happywannyan.Constant.AppContsnat;
import com.happywannyan.OnFragmentInteractionListener;
import com.happywannyan.POJO.APIPOSTDATA;
import com.happywannyan.R;
import com.happywannyan.Utils.AppLoader;
import com.happywannyan.Utils.JSONPerser;
import com.happywannyan.Utils.Loger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BookingOne extends AppCompatActivity implements View.OnClickListener, OnFragmentInteractionListener {

    public String ServiceList;
    public String PRESelectService;

    AppLoader Apploaders;
    JSONObject ItemDetails;
   public ArrayList<String>MyPetList;
    public  boolean DropDown=true;

    public boolean DoubleDate=true;

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
            DropDown=getIntent().getBooleanExtra("Single",true);
            Loger.MSG("@@ LIST"," DATA-"+ServiceList);
            PRESelectService = getIntent().getStringExtra("SELECT");
            Loger.MSG("@@ SELECT"," DATA-"+PRESelectService);

                ItemDetails = new JSONObject(getIntent().getStringExtra("ItemDetails"));
                Loger.MSG("@@ ITEM", " DATA-" + ItemDetails);

        } catch (JSONException e) {
            Loger.Error("@@ Error",e.getMessage());
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


                new JSONPerser().API_FOR_POST(AppContsnat.BASEURL + "before_booking_info", FirstPageData, new JSONPerser.JSONRESPONSE() {
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
                new JSONPerser().API_FOR_POST(AppContsnat.BASEURL + "before_booking_info", FirstPageData, new JSONPerser.JSONRESPONSE() {
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
}
