package com.happywannyan.SitterBooking;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.happywannyan.Activities.CalenderActivity;
import com.happywannyan.Constant.AppContsnat;
import com.happywannyan.Font.SFNFBoldTextView;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.OnFragmentInteractionListener;
import com.happywannyan.POJO.APIPOSTDATA;
import com.happywannyan.R;
import com.happywannyan.Utils.Loger;
import com.happywannyan.Utils.MYAlert;
import com.happywannyan.Utils.MethodsUtils;
import com.happywannyan.Utils.helper.DatePickerFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BookingFragmnetOne extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    SFNFTextView TXT_ServiceName, TXT_StartDate, TXT_EndDte, TXT_ExtarItem, TXT_SingleDate, tv_times_visit;
    SFNFBoldTextView TXT_Price;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    JSONArray ExtraPopup;

    LinearLayout LL_S_F;
    RelativeLayout RL_SingleDate, RL_ExtraDropDown;
    private OnFragmentInteractionListener mListener;
    private static final int CALL_CALENDER = 12;
    String no_of_visit = "0", no_of_times = "0";

    public BookingFragmnetOne() {
    }

    public static BookingFragmnetOne newInstance(String param1, String param2) {
        BookingFragmnetOne fragment = new BookingFragmnetOne();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_booking_fragmnet_one, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.Card_next).setOnClickListener(this);
        view.findViewById(R.id.RL_ChoseCat).setOnClickListener(this);
        view.findViewById(R.id.RL_SingleDate).setOnClickListener(this);
        view.findViewById(R.id.LL_S_F).setOnClickListener(this);
        view.findViewById(R.id.RL_ExtraDropDown).setOnClickListener(this);
        TXT_ServiceName = (SFNFTextView) view.findViewById(R.id.TXT_ServiceName);
        TXT_SingleDate = (SFNFTextView) view.findViewById(R.id.TXT_SingleDate);
        TXT_StartDate = (SFNFTextView) view.findViewById(R.id.TXT_StartDate);
        TXT_EndDte = (SFNFTextView) view.findViewById(R.id.TXT_EndDte);
        tv_times_visit = (SFNFTextView) view.findViewById(R.id.tv_times_visit);
        TXT_ExtarItem = (SFNFTextView) view.findViewById(R.id.TXT_ExtarItem);
        TXT_Price = (SFNFBoldTextView) view.findViewById(R.id.TXT_Price);
        LL_S_F = (LinearLayout) view.findViewById(R.id.LL_S_F);
        RL_SingleDate = (RelativeLayout) view.findViewById(R.id.RL_SingleDate);
        RL_ExtraDropDown = (RelativeLayout) view.findViewById(R.id.RL_ExtraDropDown);
        RL_SingleDate.setVisibility(View.GONE);
        RL_ExtraDropDown.setVisibility(View.GONE);
        LL_S_F.setVisibility(View.GONE);


        if (!mParam2.equals("NA")) {
            try {
                JSONObject jsonObject = new JSONObject(mParam2);
                Loger.MSG("jsonObject", "" + jsonObject);
                TXT_ServiceName.setText(jsonObject.getString("service_name"));
                TXT_ServiceName.setTag(jsonObject);
                RL_SingleDate.setVisibility(View.GONE);
                RL_ExtraDropDown.setVisibility(View.GONE);
                LL_S_F.setVisibility(View.GONE);
                TXT_Price.setText(jsonObject.getString("service_price") + " / " + jsonObject.getString("unit_name"));
                if (jsonObject.getString("date_field").equals("double")) {
                    LL_S_F.setVisibility(View.VISIBLE);
                    ((BookingOne) getActivity()).DoubleDate = true;
                } else {
                    ((BookingOne) getActivity()).DoubleDate = false;
                    RL_SingleDate.setVisibility(View.VISIBLE);
                }

                if (jsonObject.getString("no_of_times").equals("1") ||
                        jsonObject.getString("no_of_visit").equals("1")) {
                    if (jsonObject.getString("no_of_times").equals("1")) {
                        RL_ExtraDropDown.setVisibility(View.VISIBLE);
                        tv_times_visit.setText(getString(R.string.how_many_times));
                        no_of_times = "1";
                        no_of_visit = "0";
                        ExtraPopup = jsonObject.getJSONArray("no_of_times_dropdown");
                        TXT_ExtarItem.setText(ExtraPopup.getJSONObject(0).getString("name"));
                        TXT_ExtarItem.setTag(ExtraPopup.getJSONObject(0).getString("value"));
                    }

                    if (jsonObject.getString("no_of_visit").equals("1")) {
                        RL_ExtraDropDown.setVisibility(View.VISIBLE);
                        tv_times_visit.setText(getString(R.string.how_many_visits));
                        no_of_times = "0";
                        no_of_visit = "1";
                        ExtraPopup = jsonObject.getJSONArray("no_of_visit_dropdown");
                        TXT_ExtarItem.setText(ExtraPopup.getJSONObject(0).getString("name"));
                        TXT_ExtarItem.setTag(ExtraPopup.getJSONObject(0).getString("value"));
                    }
                } else {
                    RL_ExtraDropDown.setVisibility(View.GONE);
                    ExtraPopup = null;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri.getFragment());
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CALL_CALENDER:
                String StartDate = data.getStringExtra("startdate");
                String EndDate = data.getStringExtra("enddate");
                TXT_StartDate.setText(StartDate);
                TXT_EndDte.setText(EndDate);
                TXT_SingleDate.setText("");

                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Card_next:
                try {
                    ((BookingOne) getActivity()).FirstPageData = new ArrayList<>();

                    APIPOSTDATA apipostdata = new APIPOSTDATA();
                    apipostdata.setPARAMS("langid");
                    apipostdata.setValues(AppContsnat.Language);
                    ((BookingOne) getActivity()).FirstPageData.add(apipostdata);


                    java.util.TimeZone tz= java.util.TimeZone.getDefault();
                    apipostdata = new APIPOSTDATA();
                    apipostdata.setPARAMS("user_timezone");
                    apipostdata.setValues(tz.getID());
                    ((BookingOne) getActivity()).FirstPageData.add(apipostdata);

                    apipostdata = new APIPOSTDATA();
                    apipostdata.setPARAMS("user_id");
                    apipostdata.setValues(AppContsnat.UserId);
                    ((BookingOne) getActivity()).FirstPageData.add(apipostdata);


                    apipostdata = new APIPOSTDATA();
                    apipostdata.setPARAMS("service_id");
                    apipostdata.setValues(new JSONObject(TXT_ServiceName.getTag() + "").getString("service_id"));
                    ((BookingOne) getActivity()).FirstPageData.add(apipostdata);

                    if (TXT_SingleDate.getText().equals("")) {

                        apipostdata = new APIPOSTDATA();
                        apipostdata.setPARAMS("start_date");
                        apipostdata.setValues(TXT_StartDate.getText().toString());
                        ((BookingOne) getActivity()).FirstPageData.add(apipostdata);

                        apipostdata = new APIPOSTDATA();
                        apipostdata.setPARAMS("end_date");
                        apipostdata.setValues(TXT_EndDte.getText().toString());
                        ((BookingOne) getActivity()).FirstPageData.add(apipostdata);

                    } else {

                        apipostdata = new APIPOSTDATA();
                        apipostdata.setPARAMS("start_date");
                        apipostdata.setValues(TXT_SingleDate.getText().toString());
                        ((BookingOne) getActivity()).FirstPageData.add(apipostdata);

                        apipostdata = new APIPOSTDATA();
                        apipostdata.setPARAMS("end_date");
                        apipostdata.setValues("");
                        ((BookingOne) getActivity()).FirstPageData.add(apipostdata);
                    }

                    apipostdata = new APIPOSTDATA();
                    if (no_of_visit.equals("1")) {
                        apipostdata.setPARAMS("no_of_visit");
                        apipostdata.setValues(TXT_ExtarItem.getTag() + "");
                    } else {
                        apipostdata.setPARAMS("no_of_visit");
                        apipostdata.setValues("");
                    }
                    ((BookingOne) getActivity()).FirstPageData.add(apipostdata);


                    apipostdata = new APIPOSTDATA();
                    if (no_of_times.equals("1")) {
                        apipostdata.setPARAMS("no_times");
                        apipostdata.setValues(TXT_ExtarItem.getTag() + "");
                    } else {
                        apipostdata.setPARAMS("no_times");
                        apipostdata.setValues("");
                    }
                    ((BookingOne) getActivity()).FirstPageData.add(apipostdata);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (TXT_ServiceName.getText().length() > 0) {
                    if (LL_S_F.getVisibility() == View.VISIBLE && TXT_StartDate.getText().length() > 0) {
                        ((BookingOne)getActivity()).showConfirmReservationRequest();
                        mListener.onFragmentInteraction("Two");

                    } else {
                        TXT_StartDate.setHintTextColor(Color.RED);
                    }

                    if (LL_S_F.getVisibility() == View.GONE) {
                        if (TXT_SingleDate.getText().length() > 0) {
                            mListener.onFragmentInteraction("Two");
                        } else {
                            TXT_SingleDate.setHintTextColor(Color.RED);
                        }
                    }

                } else {
                    TXT_ServiceName.setHintTextColor(Color.RED);
                }


                break;
            case R.id.RL_ChoseCat:
                if (!((BookingOne) getActivity()).DropDown)
                    try {
                        new MYAlert(getActivity()).AlertTextLsit("" + getString(R.string.ChooseService), new JSONArray(mParam1), "service_name", new MYAlert.OnSignleListTextSelected() {
                            @Override
                            public void OnSelectedTEXT(JSONObject jsonObject) {
                                try {
                                    TXT_ServiceName.setText(jsonObject.getString("service_name"));
                                    TXT_ServiceName.setTag(jsonObject);
                                    RL_SingleDate.setVisibility(View.GONE);
                                    RL_ExtraDropDown.setVisibility(View.GONE);
                                    LL_S_F.setVisibility(View.GONE);
                                    TXT_Price.setText(jsonObject.getString("service_price") + " / " + jsonObject.getString("unit_name"));
                                    if (jsonObject.getString("date_field").equals("double")) {
                                        LL_S_F.setVisibility(View.VISIBLE);
                                        ((BookingOne) getActivity()).DoubleDate = true;
                                    } else {
                                        ((BookingOne) getActivity()).DoubleDate = false;
                                        RL_SingleDate.setVisibility(View.VISIBLE);
                                    }

                                    if (jsonObject.getString("no_of_times").equals("1") ||
                                            jsonObject.getString("no_of_visit").equals("1")) {
                                        if (jsonObject.getString("no_of_times").equals("1")) {
                                            RL_ExtraDropDown.setVisibility(View.VISIBLE);
                                            tv_times_visit.setText(getString(R.string.how_many_times));
                                            no_of_times = "1";
                                            no_of_visit = "0";
                                            ExtraPopup = jsonObject.getJSONArray("no_of_times_dropdown");
                                            TXT_ExtarItem.setText(ExtraPopup.getJSONObject(0).getString("name"));
                                            TXT_ExtarItem.setTag(ExtraPopup.getJSONObject(0).getString("value"));
                                        }

                                        if (jsonObject.getString("no_of_visit").equals("1")) {
                                            RL_ExtraDropDown.setVisibility(View.VISIBLE);
                                            tv_times_visit.setText(getString(R.string.how_many_visits));
                                            no_of_times = "0";
                                            no_of_visit = "1";
                                            ExtraPopup = jsonObject.getJSONArray("no_of_visit_dropdown");
                                            TXT_ExtarItem.setText(ExtraPopup.getJSONObject(0).getString("name"));
                                            TXT_ExtarItem.setTag(ExtraPopup.getJSONObject(0).getString("value"));
                                        }
                                    } else {
                                        RL_ExtraDropDown.setVisibility(View.GONE);
                                        ExtraPopup = null;
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                break;
            case R.id.LL_S_F:
                startActivityForResult(new Intent(getActivity(), CalenderActivity.class), CALL_CALENDER);
                break;

            case R.id.RL_ExtraDropDown:
                Loger.MSG("ExtraPopup", "" + ExtraPopup);
                new MYAlert(getActivity()).AlertTextLsit("", ExtraPopup, "name", new MYAlert.OnSignleListTextSelected() {
                    @Override
                    public void OnSelectedTEXT(JSONObject jsonObject) {
                        try {

                            TXT_ExtarItem.setText(jsonObject.getString("name"));
                            TXT_ExtarItem.setTag(jsonObject.getString("value"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;

            case R.id.RL_SingleDate:

                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.DatePickerFragment(new DatePickerFragment.DateSelect() {
                    @Override
                    public void OnDateSelected(int year, int month, int day) {
                        Log.d("@@ DATE", " DAte-" + year + "/" + month + "/" + day);
                        TXT_SingleDate.setText(day + "-" + month + "-" + year);
                        TXT_StartDate.setText("");
                        TXT_EndDte.setText("");
                    }
                });
                datePickerFragment.show(getActivity().getFragmentManager(), "Date");
                break;

        }

    }
}
