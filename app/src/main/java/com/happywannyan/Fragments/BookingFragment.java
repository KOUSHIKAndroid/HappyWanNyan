package com.happywannyan.Fragments;

import android.app.Activity;
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
import android.widget.Toast;

import com.happywannyan.Activities.BaseActivity;
import com.happywannyan.Activities.Booking.BookingDetailsActivity;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.TimeZone;

/**
 * Created by su on 5/30/17.
 */

public class BookingFragment extends Fragment {

    RecyclerView recyclerView;

    public static String TAGNAME = "";

    SFNFTextView tv_up_coming, tv_current, tv_pending, tv_past, tv_empty;
    View view_between_upcoming_current_booking, view_between_current_pending_booking, view_between_pending_past;

    ArrayList<SetGetAPIPostData> Params;
    AppLoader appLoader;
    ArrayList<JSONObject> AllBooking;

    BookingAdapter bookingAdapter;

    SwipeRefreshLayout swipeContainer;

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

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

        tv_up_coming = (SFNFTextView) view.findViewById(R.id.tv_up_coming);
        tv_current = (SFNFTextView) view.findViewById(R.id.tv_current);
        tv_pending = (SFNFTextView) view.findViewById(R.id.tv_pending);
        tv_past = (SFNFTextView) view.findViewById(R.id.tv_past);
        tv_empty = (SFNFTextView) view.findViewById(R.id.tv_empty);

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
        TimeZone tz = TimeZone.getDefault();
        setGetAPIPostData.setPARAMS("user_timezone");
        setGetAPIPostData.setValues(tz.getID());
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
                TAGNAME = tv_up_coming.getText().toString();
                loadBookingList("0");
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
                TAGNAME = tv_current.getText().toString();
                loadBookingList("0");
                seenStatus();

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
                TAGNAME = tv_pending.getText().toString();
                loadBookingList("0");
                seenStatus();
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
                TAGNAME = tv_past.getText().toString();
                type = "past_booking_list";
                loadBookingList("0");
            }
        });

//        swipeContainer.setColorSchemeColors(
//                Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);

        swipeContainer.setColorSchemeResources(
                R.color.colorRefreshProgress_1,
                R.color.colorRefreshProgress_2,
                R.color.colorRefreshProgress_3);


        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AllBooking.clear();
                loadBookingList("0");
                swipeContainer.setRefreshing(false);
            }
        });

        if (AppConstant.go_to.trim().equals("")) {
            TAGNAME = tv_up_coming.getText().toString();
            tv_up_coming.performClick();
        } else {
            TAGNAME = tv_pending.getText().toString();
            tv_pending.performClick();
            AppConstant.go_to = "";
        }
        seenStatus();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 222 && resultCode == Activity.RESULT_OK) {
            //AllBooking.clear();
            Loger.MSG("requestCode", "" + 222);
            AllBooking.clear();
            loadBookingList("0");
        }
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        Loger.MSG("Resume","Resume");
//        AllBooking.clear();
//        loadList("0");
//    }

    public void loadBookingList(final String start_from) {
        appLoader.Show();
        Params.get(0).setValues(start_from);

        new CustomJSONParser().APIForGetMethod(getActivity(), AppConstant.BASEURL + type + "?", Params, new CustomJSONParser.JSONResponseInterface() {
            @Override
            public void OnSuccess(String Result) {
                try {
                    swipeContainer.setVisibility(View.VISIBLE);
                    JSONObject jsonObject = new JSONObject(Result);
                    JSONArray all_booking = jsonObject.getJSONArray("booking_info_array");

//                        if (jsonObject.has("unseen_status")) {
//
//                            if (!jsonObject.getString("unseen_status").equals("0")) {
//                                if (type.equals("upcoming_booking_list")) {
//                                    tv_up_coming.setText(getResources().getString(R.string.up_coming_booking) + " (" + jsonObject.getString("unseen_status") + ")");
//                                    tv_up_coming.setBackgroundColor(getResources().getColor(R.color.colorPendingUpcoming));
//                                } else if (type.equals("pending_booking_list")) {
//                                    tv_pending.setText(getResources().getString(R.string.pending_booking) + " (" + jsonObject.getString("unseen_status") + ")");
//                                    tv_pending.setBackgroundColor(getResources().getColor(R.color.colorPendingUpcoming));
//                                }
//                            }else {
//                                if (type.equals("upcoming_booking_list")) {
//                                    tv_up_coming.setText(getResources().getString(R.string.up_coming_booking));
//                                    tv_up_coming.setBackgroundColor(getResources().getColor(R.color.colorWhite));
//                                } else if (type.equals("pending_booking_list")) {
//                                    tv_pending.setText(getResources().getString(R.string.pending_booking));
//                                    tv_pending.setBackgroundColor(getResources().getColor(R.color.colorWhite));
//                                }
//                            }
//                        }

                    int next_data = jsonObject.getInt("next_data");
                    Loger.MSG("next_data", "" + next_data);

                    for (int i = 0; i < all_booking.length(); i++) {
                        AllBooking.add(all_booking.getJSONObject(i));
                    }
                    Log.i("AllBookingSize", "" + AllBooking.size());

                    if (start_from.equals("0")) {

                        swipeContainer.setVisibility(View.VISIBLE);
                        tv_empty.setVisibility(View.GONE);

                        bookingAdapter = new BookingAdapter(getActivity(), BookingFragment.this, AllBooking, type);
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

                        swipeContainer.setVisibility(View.GONE);
                        tv_empty.setVisibility(View.VISIBLE);
                        recyclerView.setAdapter(null);

//                        new MYAlert(getActivity()).AlertOnly("" + TAGNAME, "" + getString(R.string.no_data_found), new MYAlert.OnlyMessage() {
//                            @Override
//                            public void OnOk(boolean res) {
//
//                            }
//                        });
                    } else {
                        new MYAlert(getActivity()).AlertOnly("" + TAGNAME, Error, new MYAlert.OnlyMessage() {
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
                if (Error.equalsIgnoreCase(getActivity().getResources().getString(R.string.please_check_your_internet_connection))) {
                    Toast.makeText(getActivity(), Error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void seenStatus() {

        new CustomJSONParser().APIForGetMethod(getActivity(), AppConstant.BASEURL + "app_booking_seen_status?user_id=" + AppConstant.UserId
                , new ArrayList<SetGetAPIPostData>(), new CustomJSONParser.JSONResponseInterface() {
                    @Override
                    public void OnSuccess(String Result) {
                        try {
                            JSONObject jsonObject = new JSONObject(Result);

                            if (jsonObject.getBoolean("pending_unseen_status")) {
                                tv_pending.setBackgroundColor(getResources().getColor(R.color.colorPendingUpcoming));
                            } else {
                                tv_pending.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                            }

                            if (jsonObject.getBoolean("upcoming_unseen_status")) {
                                tv_up_coming.setBackgroundColor(getResources().getColor(R.color.colorPendingUpcoming));
                            } else {
                                tv_up_coming.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void OnError(String Error, String Response) {
                    }

                    @Override
                    public void OnError(String Error) {
                        appLoader.Dismiss();
                        if (Error.equalsIgnoreCase(getResources().getString(R.string.please_check_your_internet_connection))) {
                            Toast.makeText(getActivity(), Error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
