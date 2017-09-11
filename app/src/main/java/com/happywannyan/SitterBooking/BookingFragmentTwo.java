package com.happywannyan.SitterBooking;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.happywannyan.Constant.AppContsnat;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.OnFragmentInteractionListener;
import com.happywannyan.POJO.APIPOSTDATA;
import com.happywannyan.R;
import com.happywannyan.Utils.AppDataHolder;
import com.happywannyan.Utils.MYAlert;
import com.happywannyan.Utils.MethodsUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class BookingFragmentTwo extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    LinearLayout LL_MYPETS;
    SFNFTextView TXT_PickupTime, TXT_dropTime;

    EditText EDX_Fname, EDX_Lname;

    private OnFragmentInteractionListener mListener;

    public BookingFragmentTwo() {
        // Required empty public constructor
    }


    public static BookingFragmentTwo newInstance(String param1, String param2) {
        BookingFragmentTwo fragment = new BookingFragmentTwo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new AppContsnat(getActivity());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            Log.d("@@ PARA<MS", mParam1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EDX_Fname = (EditText) view.findViewById(R.id.EDX_Fname);
        EDX_Lname = (EditText) view.findViewById(R.id.EDX_Lname);

        new MethodsUtils().setupParent(view.findViewById(R.id.RLParent),getActivity());

        view.findViewById(R.id.Card_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean atLeastOneCheck = false;
                EDX_Fname.setError(null);
                EDX_Lname.setError(null);

                ((BookingOneActivity) getActivity()).MyPetList = new ArrayList<String>();

                for (int k = 0; k < LL_MYPETS.getChildCount(); k++) {
                    if (LL_MYPETS.getChildAt(k) instanceof CheckBox && ((CheckBox) ((CheckBox) LL_MYPETS.getChildAt(k))).isChecked()) {
                        ((BookingOneActivity) getActivity()).MyPetList.add(((CheckBox) LL_MYPETS.getChildAt(k)).getTag() + "");
                        atLeastOneCheck = true;
                    }
                }

                if (EDX_Fname.getText().toString().trim().equals("")) {
                    EDX_Fname.setError("Please enter first name");
                } else {
                    if (EDX_Lname.getText().toString().trim().equals("")) {
                        EDX_Lname.setError("Please enter last name");
                    } else {
                        if (atLeastOneCheck) {

                            for (int i = 0; i < ((BookingOneActivity) getActivity()).FirstPageData.size(); i++) {
                                if (((BookingOneActivity) getActivity()).FirstPageData.get(i) .getPARAMS().equalsIgnoreCase("pick_up")) {
                                    ((BookingOneActivity) getActivity()).FirstPageData.get(i).setValues(TXT_PickupTime.getTag().toString());
                                    break;
                                }
                                else if(i==((BookingOneActivity) getActivity()).FirstPageData.size()-1){
                                    APIPOSTDATA apipostdata = new APIPOSTDATA();
                                    apipostdata.setPARAMS("pick_up");
                                    apipostdata.setValues(TXT_PickupTime.getTag().toString());
                                    ((BookingOneActivity) getActivity()).FirstPageData.add(apipostdata);
                                }
                            }

                            for (int i = 0; i < ((BookingOneActivity) getActivity()).FirstPageData.size(); i++) {
                                if (((BookingOneActivity) getActivity()).FirstPageData.get(i) .getPARAMS().equalsIgnoreCase("drop_off")) {
                                    ((BookingOneActivity) getActivity()).FirstPageData.get(i).setValues(TXT_dropTime.getTag().toString());
                                    break;
                                }
                                else if(i==((BookingOneActivity) getActivity()).FirstPageData.size()-1){
                                    APIPOSTDATA apipostdata = new APIPOSTDATA();
                                    apipostdata.setPARAMS("drop_off");
                                    apipostdata.setValues(TXT_dropTime.getTag().toString());
                                    ((BookingOneActivity) getActivity()).FirstPageData.add(apipostdata);
                                }
                            }

                            for (int i = 0; i < ((BookingOneActivity) getActivity()).FirstPageData.size(); i++) {
                                if (((BookingOneActivity) getActivity()).FirstPageData.get(i) .getPARAMS().equalsIgnoreCase("pet_id")) {
                                    String petIdString="";
                                    for (int j = 0; j<((BookingOneActivity) getActivity()).MyPetList.size(); j++){
                                        petIdString=petIdString+((BookingOneActivity) getActivity()).MyPetList.get(j)+",";
                                    }
                                    ((BookingOneActivity) getActivity()).FirstPageData.get(i).setValues(petIdString.substring(0,petIdString.length()-1));
                                    break;
                                }
                                else if(i==((BookingOneActivity) getActivity()).FirstPageData.size()-1){
                                    APIPOSTDATA apipostdata = new APIPOSTDATA();
                                    apipostdata.setPARAMS("pet_id");
                                    String petIdString="";
                                    for (int j = 0; j<((BookingOneActivity) getActivity()).MyPetList.size(); j++){
                                        petIdString=petIdString+((BookingOneActivity) getActivity()).MyPetList.get(j)+",";
                                    }
                                    apipostdata.setValues(petIdString.substring(0,petIdString.length()-1));
                                    ((BookingOneActivity) getActivity()).FirstPageData.add(apipostdata);
                                }
                            }


                            for (int i = 0; i < ((BookingOneActivity) getActivity()).FirstPageData.size(); i++) {
                                if (((BookingOneActivity) getActivity()).FirstPageData.get(i) .getPARAMS().equalsIgnoreCase("total_pets")) {
                                    ((BookingOneActivity) getActivity()).FirstPageData.get(i).setValues(((BookingOneActivity) getActivity()).MyPetList.size() + "");
                                    break;
                                }
                                else if(i==((BookingOneActivity) getActivity()).FirstPageData.size()-1){
                                    APIPOSTDATA apipostdata = new APIPOSTDATA();
                                    apipostdata.setPARAMS("total_pets");
                                    apipostdata.setValues(((BookingOneActivity) getActivity()).MyPetList.size() + "");
                                    ((BookingOneActivity) getActivity()).FirstPageData.add(apipostdata);
                                }
                            }
                            //((BookingOneActivity)getActivity()).showConfirmReservationRequest();

                            mListener.onFragmentInteraction("Three");

                        } else {
                            Toast.makeText(getActivity(), "Check at least one pet", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        view.findViewById(R.id.RL_DropOffTime).setOnClickListener(this);
        view.findViewById(R.id.RL_PickupTime).setOnClickListener(this);

        TXT_PickupTime = (SFNFTextView) view.findViewById(R.id.TXT_PickupTime);
        TXT_PickupTime.setTag("");

        TXT_dropTime = (SFNFTextView) view.findViewById(R.id.TXT_dropTime);
        TXT_dropTime.setTag("");

        new AppContsnat(getActivity()).GET_SHAREDATA(AppDataHolder.UserData, new AppDataHolder.App_sharePrefData() {
            @Override
            public void Avialable(boolean avilavle, JSONObject data) {
                try {
                    EDX_Fname.setText(data.getJSONObject("info_array").getString("firstname"));
                    EDX_Lname.setText(data.getJSONObject("info_array").getString("lastname"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void NotAvilable(String Error) {


            }
        });

        LL_MYPETS = (LinearLayout) view.findViewById(R.id.LL_MYPETS);


        SetPetList();

    }

    private void SetPetList() {
        try {
            JSONObject MainJ = new JSONObject(mParam1).getJSONObject("info_array");
            JSONArray Array = MainJ.getJSONArray("pet_section");
            for (int i = 0; i < Array.length(); i++) {
                CheckBox chk = new CheckBox(getActivity());
                chk.setGravity(View.LAYOUT_DIRECTION_LTR);
                chk.setText(Array.getJSONObject(i).getString("name"));
                chk.setTag(Array.getJSONObject(i).getString("id"));
                LL_MYPETS.addView(chk);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_booking_fragment_two, container, false);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.RL_PickupTime:
                JSONObject MainJ = null;
                try {
                    MainJ = new JSONObject(mParam1).getJSONObject("info_array");
                    JSONArray Array = MainJ.getJSONArray("perf_start_droptime");
                    new MYAlert(getActivity()).AlertTextLsit(getString(R.string.pickuptime), Array, "name", new MYAlert.OnSignleListTextSelected() {
                        @Override
                        public void OnSelectedTEXT(JSONObject jsonObject) {
                            Log.d("@@@fhjhf", "--" + jsonObject);
                            try {
                                TXT_PickupTime.setText(jsonObject.getString("name"));
                                TXT_PickupTime.setTag(jsonObject.getString("value"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.RL_DropOffTime:
                try {
                    MainJ = new JSONObject(mParam1).getJSONObject("info_array");
                    JSONArray Array = MainJ.getJSONArray("perf_end_droptime");
                    new MYAlert(getActivity()).AlertTextLsit(getString(R.string.dropofftime), Array, "name", new MYAlert.OnSignleListTextSelected() {
                        @Override
                        public void OnSelectedTEXT(JSONObject jsonObject) {
                            Log.d("@@@fhjhf", "--" + jsonObject);
                            try {
                                TXT_dropTime.setText(jsonObject.getString("name"));
                                TXT_dropTime.setTag(jsonObject.getString("value"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
