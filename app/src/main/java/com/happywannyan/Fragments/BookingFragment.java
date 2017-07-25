package com.happywannyan.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happywannyan.Activities.BaseActivity;
import com.happywannyan.Activities.SearchResult;
import com.happywannyan.Adapter.Adapter_message;
import com.happywannyan.Adapter.Booking_Adapter;
import com.happywannyan.Constant.AppContsnat;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.POJO.APIPOSTDATA;
import com.happywannyan.POJO.SetGetUpComingBooking;
import com.happywannyan.R;
import com.happywannyan.Utils.AppLoader;
import com.happywannyan.Utils.JSONPerser;
import com.happywannyan.Utils.Loger;
import com.happywannyan.Utils.MYAlert;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by su on 5/30/17.
 */

public class BookingFragment extends Fragment{

    RecyclerView recyclerView;

    SFNFTextView tv_up_coming,tv_current,tv_pending,tv_past;
    View view_between_upcoming_current_booking,view_between_current_pending_booking,view_between_pending_past;

    ArrayList<APIPOSTDATA> Params ;
    AppLoader appLoader;
    ArrayList<JSONObject> AllBooking;

    Booking_Adapter bookingAdapter;

    String type;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_upcoming_booking,container,false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AllBooking = new ArrayList<>();
        Params = new ArrayList<>();
        appLoader=new AppLoader(getActivity());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new AppContsnat(getActivity());
        recyclerView= (RecyclerView) view.findViewById(R.id.rcv_upcoming_booking);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        tv_up_coming= (SFNFTextView) view.findViewById(R.id.tv_up_coming);
        tv_current= (SFNFTextView) view.findViewById(R.id.tv_current);
        tv_pending= (SFNFTextView)view.findViewById(R.id.tv_pending);
        tv_past= (SFNFTextView)view.findViewById(R.id.tv_past);

        view_between_upcoming_current_booking=view.findViewById(R.id.view_between_upcoming_current_booking);
        view_between_current_pending_booking=view.findViewById(R.id.view_between_current_pending_booking);
        view_between_pending_past=view.findViewById(R.id.view_between_pending_past);

        view.findViewById(R.id.IMG_icon_drwaer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) getActivity()).Menu_Drawer();
            }
        });

        APIPOSTDATA apipostdata = new APIPOSTDATA();
        apipostdata.setPARAMS("start_form");
        apipostdata.setValues("0");
        Params.add(apipostdata);

        apipostdata = new APIPOSTDATA();
        apipostdata.setPARAMS("user_id");
        apipostdata.setValues(AppContsnat.UserId);
        Params.add(apipostdata);

        apipostdata = new APIPOSTDATA();
        apipostdata.setPARAMS("lang_id");
        apipostdata.setValues(AppContsnat.Language);
        Params.add(apipostdata);

        apipostdata = new APIPOSTDATA();
        apipostdata.setPARAMS("per_page");
        apipostdata.setValues("10");
        Params.add(apipostdata);

        apipostdata = new APIPOSTDATA();
        apipostdata.setPARAMS("user_timezone");
        apipostdata.setValues("");
        Params.add(apipostdata);

        apipostdata = new APIPOSTDATA();
        apipostdata.setPARAMS("search_param");
        apipostdata.setValues("");
        Params.add(apipostdata);

        type="past_booking_list";




        tv_up_coming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_up_coming.setTextColor(ContextCompat.getColor(getActivity(), R.color.Black));
                tv_current.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_dark_gray));
                tv_pending.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_dark_gray));
                tv_past.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_dark_gray));

                view_between_upcoming_current_booking.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.Black));
                view_between_current_pending_booking.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.text_dark_gray));
                view_between_pending_past.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.text_dark_gray));

                AllBooking = new ArrayList<>();
                type="upcoming_booking_list";
                loadList("0");
            }
        });
        tv_current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tv_up_coming.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_dark_gray));
                tv_current.setTextColor(ContextCompat.getColor(getActivity(), R.color.Black));
                tv_pending.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_dark_gray));
                tv_past.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_dark_gray));

                view_between_upcoming_current_booking.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.Black));
                view_between_current_pending_booking.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.Black));
                view_between_pending_past.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.text_dark_gray));

                AllBooking = new ArrayList<>();
                type="current_booking_list";
                loadList("0");

            }
        });
        tv_pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_up_coming.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_dark_gray));
                tv_current.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_dark_gray));
                tv_pending.setTextColor(ContextCompat.getColor(getActivity(), R.color.Black));
                tv_past.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_dark_gray));

                view_between_upcoming_current_booking.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.text_dark_gray));
                view_between_current_pending_booking.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.Black));
                view_between_pending_past.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.Black));

                AllBooking = new ArrayList<>();
                type="pending_booking_list";
                loadList("0");
            }
        });
        tv_past.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tv_up_coming.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_dark_gray));
                tv_current.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_dark_gray));
                tv_pending.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_dark_gray));
                tv_past.setTextColor(ContextCompat.getColor(getActivity(), R.color.Black));

                view_between_upcoming_current_booking.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.text_dark_gray));
                view_between_current_pending_booking.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.text_dark_gray));
                view_between_pending_past.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.Black));

                AllBooking = new ArrayList<>();
                type="past_booking_list";
                loadList("0");
            }
        });
        tv_up_coming.performClick();

    }

    public void loadList(final String start_from){
        appLoader.Show();
        Params.get(0).setValues(start_from);

        new JSONPerser().API_FOR_GET(AppContsnat.BASEURL+type+"?", Params, new JSONPerser.JSONRESPONSE() {
            @Override
            public void OnSuccess(String Result) {
                try {
                    JSONObject jsonObject=new JSONObject(Result);
                    JSONArray all_booking=jsonObject.getJSONArray("booking_info_array");

                    int next_data=jsonObject.getInt("next_data");
                    Loger.MSG("next_data",""+next_data);

                    for(int i=0;i<all_booking.length();i++)
                    {
                        AllBooking.add(all_booking.getJSONObject(i));
                    }
                    Log.i("AllBookingSize",""+AllBooking.size());

                    if(start_from.equals("0")) {
                        bookingAdapter = new Booking_Adapter(getActivity(),BookingFragment.this, AllBooking);
                        recyclerView.setAdapter(bookingAdapter);
                    }
                    else
                    {
                        bookingAdapter.nextData=next_data;
                        bookingAdapter.notifyDataSetChanged();
                    }
                    appLoader.Dismiss();
                }catch (Exception e)
                {
                    e.printStackTrace();
                    appLoader.Dismiss();
                }
            }

            @Override
            public void OnError(String Error, String Response) {
                appLoader.Dismiss();
                try {

                    JSONObject jsonObject=    new JSONObject(Response);
                    if(jsonObject.getInt("next_data")==0 && jsonObject.getInt("start_form")==0){
                        recyclerView.setAdapter(null);
                        new MYAlert(getActivity()).AlertOnly(getResources().getString(R.string.app_name), Error, new MYAlert.OnlyMessage() {
                            @Override
                            public void OnOk(boolean res) {

                            }
                        });
                    }

                }catch (Exception e)
                {

                }
            }

            @Override
            public void OnError(String Error) {
                appLoader.Dismiss();
                recyclerView.setAdapter(null);
            }
        });
    }

}
