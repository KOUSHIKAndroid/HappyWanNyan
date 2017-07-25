package com.happywannyan.SitterBooking;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.happywannyan.Font.SFNFBoldTextView;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.OnFragmentInteractionListener;
import com.happywannyan.R;

import org.json.JSONException;
import org.json.JSONObject;

public class BookingFrgamnetThree extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    JSONObject PageObject;
   LinearLayout LL_ForSingleDate,LL_DoubleDate;

    private OnFragmentInteractionListener mListener;

    public BookingFrgamnetThree() {
        // Required empty public constructor
    }

    public static BookingFrgamnetThree newInstance(String param1, String param2) {
        BookingFrgamnetThree fragment = new BookingFrgamnetThree();
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

        try {
            PageObject=new JSONObject(mParam1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_booking_frgamnet_three, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.Card_next).setOnClickListener(this);

        LL_ForSingleDate=(LinearLayout)view.findViewById(R.id.LL_ForSingleDate);
        LL_DoubleDate=(LinearLayout)view.findViewById(R.id.LL_DoubleDate);

        try {
            ((SFNFBoldTextView)view.findViewById(R.id.TXT_ServiceName)).setText(PageObject.getJSONObject("info_array").getString("service_name"));
            ((SFNFTextView)view.findViewById(R.id.TXT_Unit)).setText(PageObject.getJSONObject("info_array").getString("service_price"));
            ((SFNFTextView)view.findViewById(R.id.TXT_no_pets)).setText(PageObject.getJSONObject("info_array").getString("no_of_pet"));
            ((SFNFBoldTextView)view.findViewById(R.id.TXT_TotalPrice)).setText(PageObject.getJSONObject("info_array").getString("total_price"));
            ((SFNFTextView)view.findViewById(R.id.TXT_saftyPrice)).setText(PageObject.getJSONObject("info_array").getString("trust_safety_price"));
            ((SFNFTextView)view.findViewById(R.id.TXT_CancelPolicy)).setText(PageObject.getJSONObject("info_array").getString("cancel_policy"));

            if(((BookingOne)getActivity()).DoubleDate) {
                LL_DoubleDate.setVisibility(View.VISIBLE);
                LL_ForSingleDate.setVisibility(View.GONE);
                ((SFNFTextView) view.findViewById(R.id.TXT_StartDate)).setText(PageObject.getJSONObject("info_array").getString("start_date"));
                ((SFNFTextView) view.findViewById(R.id.TXT_EndDte)).setText(PageObject.getJSONObject("info_array").getString("end_date"));
                ((SFNFTextView) view.findViewById(R.id.TXT_No_OfNight)).setText(PageObject.getJSONObject("info_array").getString("no_of_nights"));
            }else {
                LL_ForSingleDate.setVisibility(View.VISIBLE);
                LL_DoubleDate.setVisibility(View.GONE);
                ((SFNFTextView) view.findViewById(R.id.TXT_DAte)).setText(PageObject.getJSONObject("info_array").getString("date"));
                if(PageObject.getJSONObject("info_array").has("no_of_times"))
                ((SFNFTextView) view.findViewById(R.id.TXT_ofTome)).setText(PageObject.getJSONObject("info_array").getString("no_of_times"));
                else
                    view.findViewById(R.id.RL_Times).setVisibility(View.GONE);

            }
        } catch (JSONException e) {
            e.printStackTrace()
            ;
        }


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri.getFragment());
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
        switch (view.getId()){
            case R.id.Card_next:
                mListener.onFragmentInteraction("foure");
                break;
        }
    }
}
