package com.happywannyan.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happywannyan.Activities.BaseActivity;
import com.happywannyan.Adapter.BookingAdapter;
import com.happywannyan.Constant.AppConstant;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.POJO.SetGetAPIPostData;
import com.happywannyan.R;
import com.happywannyan.Utils.AppLoader;
import com.happywannyan.Utils.CustomJSONParser;
import com.happywannyan.Utils.Loger;
import com.happywannyan.Utils.MYAlert;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by su on 5/30/17.
 */

public class BookingFragment extends Fragment {

    RecyclerView recyclerView;

    SFNFTextView tv_up_coming, tv_current, tv_pending, tv_past;
    View view_between_upcoming_current_booking, view_between_current_pending_booking, view_between_pending_past;

    ArrayList<SetGetAPIPostData> Params;
    AppLoader appLoader;
    ArrayList<JSONObject> AllBooking;

    BookingAdapter bookingAdapter;

    String type;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_upcoming_booking, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AllBooking = new ArrayList<>();
        Params = new ArrayList<>();
        appLoader = new AppLoader(getActivity());
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new AppConstant(getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.rcv_upcoming_booking);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        tv_up_coming = (SFNFTextView) view.findViewById(R.id.tv_up_coming);
        tv_current = (SFNFTextView) view.findViewById(R.id.tv_current);
        tv_pending = (SFNFTextView) view.findViewById(R.id.tv_pending);
        tv_past = (SFNFTextView) view.findViewById(R.id.tv_past);

        view_between_upcoming_current_booking = view.findViewById(R.id.view_between_upcoming_current_booking);
        view_between_current_pending_booking = view.findViewById(R.id.view_between_current_pending_booking);
        view_between_pending_past = view.findViewById(R.id.view_between_pending_past);

        view.findViewById(R.id.IMG_icon_drwaer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) getActivity()).Menu_Drawer();
            }
        });

        SetGetAPIPostData setGetAPIPostData = new SetGetAPIPostData();
        setGetAPIPostData.setPARAMS("start_form");
        setGetAPIPostData.setValues("0");
        Params.add(setGetAPIPostData);

        setGetAPIPostData = new SetGetAPIPostData();
        setGetAPIPostData.setPARAMS("user_id");
        setGetAPIPostData.setValues(AppConstant.UserId);
        Params.add(setGetAPIPostData);

        setGetAPIPostData = new SetGetAPIPostData();
        setGetAPIPostData.setPARAMS("lang_id");
        setGetAPIPostData.setValues(AppConstant.Language);
        Params.add(setGetAPIPostData);

        setGetAPIPostData = new SetGetAPIPostData();
        setGetAPIPostData.setPARAMS("per_page");
        setGetAPIPostData.setValues("10");
        Params.add(setGetAPIPostData);

        setGetAPIPostData = new SetGetAPIPostData();
        setGetAPIPostData.setPARAMS("user_timezone");
        setGetAPIPostData.setValues("");
        Params.add(setGetAPIPostData);

        setGetAPIPostData = new SetGetAPIPostData();
        setGetAPIPostData.setPARAMS("search_param");
        setGetAPIPostData.setValues("");
        Params.add(setGetAPIPostData);

        type = "past_booking_list";


        tv_up_coming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_up_coming.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));
                tv_current.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorTextDarkGray));
                tv_pending.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorTextDarkGray));
                tv_past.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorTextDarkGray));

                view_between_upcoming_current_booking.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));
                view_between_current_pending_booking.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorTextDarkGray));
                view_between_pending_past.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorTextDarkGray));

                AllBooking.clear();
                type = "upcoming_booking_list";
                loadList("0");
            }
        });
        tv_current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tv_up_coming.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorTextDarkGray));
                tv_current.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));
                tv_pending.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorTextDarkGray));
                tv_past.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorTextDarkGray));

                view_between_upcoming_current_booking.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));
                view_between_current_pending_booking.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));
                view_between_pending_past.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorTextDarkGray));

                AllBooking.clear();
                type = "current_booking_list";
                loadList("0");

            }
        });
        tv_pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_up_coming.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorTextDarkGray));
                tv_current.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorTextDarkGray));
                tv_pending.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));
                tv_past.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorTextDarkGray));

                view_between_upcoming_current_booking.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorTextDarkGray));
                view_between_current_pending_booking.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));
                view_between_pending_past.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));

                AllBooking.clear();
                type = "pending_booking_list";
                loadList("0");
            }
        });
        tv_past.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tv_up_coming.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorTextDarkGray));
                tv_current.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorTextDarkGray));
                tv_pending.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorTextDarkGray));
                tv_past.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));

                view_between_upcoming_current_booking.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorTextDarkGray));
                view_between_current_pending_booking.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorTextDarkGray));
                view_between_pending_past.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));

                AllBooking.clear();
                type = "past_booking_list";
                loadList("0");
            }
        });

        if (AppConstant.go_to.trim().equals("")) {
            tv_up_coming.performClick();
        } else {
            tv_pending.performClick();
            AppConstant.go_to = "";
        }

//        ((SwipeRefreshLayout)view.findViewById(R.id.swipeContainer)).setColorSchemeColors(
//                Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);

        ((SwipeRefreshLayout) view.findViewById(R.id.swipeContainer)).setColorSchemeResources(
                R.color.colorRefreshProgress_1,
                R.color.colorRefreshProgress_2,
                R.color.colorRefreshProgress_3);


        ((SwipeRefreshLayout) view.findViewById(R.id.swipeContainer)).setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AllBooking.clear();
                loadList("0");
                ((SwipeRefreshLayout) view.findViewById(R.id.swipeContainer)).setRefreshing(false);
            }
        });

        loadList("0");
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 222) {
            //AllBooking.clear();
            Loger.MSG("requestCode",""+222);
            AllBooking.clear();
            loadList("0");
        }
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        Loger.MSG("Resume","Resume");
//        AllBooking.clear();
//        loadList("0");
//    }

    public void loadList(final String start_from) {
        appLoader.Show();
        Params.get(0).setValues(start_from);

        new CustomJSONParser().APIForGetMethod(AppConstant.BASEURL + type + "?", Params, new CustomJSONParser.JSONResponseInterface() {
            @Override
            public void OnSuccess(String Result) {
                try {
                    JSONObject jsonObject = new JSONObject(Result);
                    JSONArray all_booking = jsonObject.getJSONArray("booking_info_array");

                    int next_data = jsonObject.getInt("next_data");
                    Loger.MSG("next_data", "" + next_data);

                    for (int i = 0; i < all_booking.length(); i++) {
                        AllBooking.add(all_booking.getJSONObject(i));
                    }
                    Log.i("AllBookingSize", "" + AllBooking.size());

                    if (start_from.equals("0")) {
                        bookingAdapter = new BookingAdapter(getActivity(), BookingFragment.this, AllBooking);
                        recyclerView.setAdapter(bookingAdapter);
                    } else {
                        bookingAdapter.nextData = next_data;
                        bookingAdapter.notifyDataSetChanged();
                    }
                    appLoader.Dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    appLoader.Dismiss();
                }
            }

            @Override
            public void OnError(String Error, String Response) {
                appLoader.Dismiss();
                try {

                    JSONObject jsonObject = new JSONObject(Response);
                    if (jsonObject.getInt("next_data") == 0 && jsonObject.getInt("start_form") == 0 && AllBooking.size() == 0) {
                        recyclerView.setAdapter(null);
                        new MYAlert(getActivity()).AlertOnly(getResources().getString(R.string.app_name), Error, new MYAlert.OnlyMessage() {
                            @Override
                            public void OnOk(boolean res) {

                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
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
