package com.happywannyan.SitterBooking;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.happywannyan.Constant.AppConstant;
import com.happywannyan.Font.SFNFBoldTextView;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.OnFragmentInteractionListener;
import com.happywannyan.POJO.APIPOSTDATA;
import com.happywannyan.R;
import com.happywannyan.Utils.AppLoader;
import com.happywannyan.Utils.CustomJSONParser;
import com.happywannyan.Utils.Loger;
import com.happywannyan.Utils.MethodsUtils;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class BookingFragmentThree extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    String coupon_id="",coupon_amount="";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    JSONObject PageObject;
    LinearLayout LL_ForSingleDate,LL_DoubleDate;
    EditText EDX_coupon_code;
    public ArrayList<APIPOSTDATA> postParamCoupon;
    AppLoader appLoader;
    //TextInputLayout input_layout_coupon_code;
    private OnFragmentInteractionListener mListener;

    public BookingFragmentThree() {
        // Required empty public constructor
    }

    public static BookingFragmentThree newInstance(String param1, String param2) {
        BookingFragmentThree fragment = new BookingFragmentThree();
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
    public void onViewCreated(final View viewMain, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(viewMain, savedInstanceState);
        viewMain.findViewById(R.id.Card_next).setOnClickListener(this);

        LL_ForSingleDate=(LinearLayout)viewMain.findViewById(R.id.LL_ForSingleDate);
        LL_DoubleDate=(LinearLayout)viewMain.findViewById(R.id.LL_DoubleDate);

        appLoader = new AppLoader(getActivity());
        postParamCoupon=new ArrayList<>();

        //input_layout_coupon_code= (TextInputLayout) viewMain.findViewById(R.id.input_layout_coupon_code);
        viewMain.findViewById(R.id.tv_coupon_code).setVisibility(View.GONE);
        viewMain.findViewById(R.id.img_clear).setVisibility(View.GONE);
        EDX_coupon_code= (EditText) viewMain.findViewById(R.id.EDX_coupon_code);

        new MethodsUtils().setupParent(viewMain.findViewById(R.id.ScrollViewMain),getActivity());

        try {
            ((SFNFBoldTextView)viewMain.findViewById(R.id.TXT_ServiceName)).setText(PageObject.getJSONObject("info_array").getString("service_name"));
            ((SFNFTextView)viewMain.findViewById(R.id.TXT_Unit)).setText(PageObject.getJSONObject("info_array").getString("service_price"));
            ((SFNFTextView)viewMain.findViewById(R.id.TXT_no_pets)).setText(PageObject.getJSONObject("info_array").getString("no_of_pet"));
            ((SFNFBoldTextView) viewMain.findViewById(R.id.TXT_TotalPrice)).setText(PageObject.getJSONObject("info_array").getString("total_price"));
            ((SFNFTextView)viewMain.findViewById(R.id.TXT_saftyPrice)).setText(PageObject.getJSONObject("info_array").getString("trust_safety_price"));
            ((SFNFTextView)viewMain.findViewById(R.id.TXT_CancelPolicy)).setText(PageObject.getJSONObject("info_array").getString("cancel_policy"));

            Loger.MSG("DoubleDate",""+((BookingOneActivity)getActivity()).DoubleDate);
            if(((BookingOneActivity)getActivity()).DoubleDate) {
                LL_DoubleDate.setVisibility(View.VISIBLE);
                LL_ForSingleDate.setVisibility(View.GONE);
                ((SFNFTextView) viewMain.findViewById(R.id.TXT_StartDate)).setText(PageObject.getJSONObject("info_array").getString("start_date"));
                ((SFNFTextView) viewMain.findViewById(R.id.TXT_EndDte)).setText(PageObject.getJSONObject("info_array").getString("end_date"));
                ((SFNFTextView) viewMain.findViewById(R.id.TXT_No_OfNight)).setText(PageObject.getJSONObject("info_array").getString("no_of_nights"));
            }else {
                LL_ForSingleDate.setVisibility(View.VISIBLE);
                LL_DoubleDate.setVisibility(View.GONE);
                ((SFNFTextView) viewMain.findViewById(R.id.TXT_Date)).setText(PageObject.getJSONObject("info_array").getString("date"));
                if(PageObject.getJSONObject("info_array").has("no_of_times"))
                ((SFNFTextView) viewMain.findViewById(R.id.TXT_ofDays)).setText(PageObject.getJSONObject("info_array").getString("no_of_days"));
                else
                    viewMain.findViewById(R.id.RL_Times).setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        viewMain.findViewById(R.id.img_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EDX_coupon_code.setText("");
            }
        });

        EDX_coupon_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(!charSequence.toString().toString().equals("")){
                    viewMain.findViewById(R.id.tv_coupon_code).setVisibility(View.VISIBLE);
                    viewMain.findViewById(R.id.img_clear).setVisibility(View.VISIBLE);
                }else {
                    viewMain.findViewById(R.id.tv_coupon_code).setVisibility(View.GONE);
                    viewMain.findViewById(R.id.img_clear).setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        viewMain.findViewById(R.id.tv_coupon_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //input_layout_coupon_code.setErrorEnabled(false);
                //input_layout_coupon_code.setError(null);

                ((SFNFTextView) viewMain.findViewById(R.id.Tv_coupon_code_valid_check)).setVisibility(View.GONE);

                if (EDX_coupon_code.getText().toString().trim().equals("")){
                    //input_layout_coupon_code.setErrorEnabled(true);
                    //input_layout_coupon_code.setError("Field can't be empty");
                    ((SFNFTextView) viewMain.findViewById(R.id.Tv_coupon_code_valid_check)).setVisibility(View.VISIBLE);
                    ((SFNFTextView) viewMain.findViewById(R.id.Tv_coupon_code_valid_check)).setText("Field can't be empty");
                    ((SFNFTextView) viewMain.findViewById(R.id.Tv_coupon_code_valid_check)).setTextColor(Color.RED);
                }else {
                    if (EDX_coupon_code.getText().toString().trim().length()<6){
                        //input_layout_coupon_code.setErrorEnabled(true);
                        //input_layout_coupon_code.setError("Field must be greater then 5");
                        ((SFNFTextView) viewMain.findViewById(R.id.Tv_coupon_code_valid_check)).setVisibility(View.VISIBLE);
                        ((SFNFTextView) viewMain.findViewById(R.id.Tv_coupon_code_valid_check)).setText("Field must be greater then 5");
                        ((SFNFTextView) viewMain.findViewById(R.id.Tv_coupon_code_valid_check)).setTextColor(Color.RED);
                    }else {
                        APIPOSTDATA apipostdata = new APIPOSTDATA();
                        apipostdata.setPARAMS("user_id");
                        apipostdata.setValues("" + AppConstant.UserId);
                        postParamCoupon.add(apipostdata);

                        apipostdata = new APIPOSTDATA();
                        apipostdata.setPARAMS("coupon_no");
                        apipostdata.setValues(EDX_coupon_code.getText().toString().trim());
                        postParamCoupon.add(apipostdata);

                        appLoader.Show();

                        new CustomJSONParser().APIForPostMethod(AppConstant.BASEURL + "Api_coupon_exits", postParamCoupon, new CustomJSONParser.JSONResponseInterface() {
                            @Override
                            public void OnSuccess(String Result) {
                                appLoader.Dismiss();
                                Loger.MSG("Result",Result);

                                try {
                                    JSONObject jsonObject=new JSONObject(Result);
                                    ((SFNFTextView) viewMain.findViewById(R.id.Tv_coupon_code_valid_check)).setVisibility(View.VISIBLE);
                                    ((SFNFTextView) viewMain.findViewById(R.id.Tv_coupon_code_valid_check)).setText(jsonObject.getString("message"));
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        ((SFNFTextView) viewMain.findViewById(R.id.Tv_coupon_code_valid_check)).setTextColor(getResources().getColor(R.color.colorGreen, null));
                                    }
                                    else {
                                        ((SFNFTextView) viewMain.findViewById(R.id.Tv_coupon_code_valid_check)).setTextColor(getResources().getColor(R.color.colorGreen));
                                    }
                                    ((LinearLayout) viewMain.findViewById(R.id.LL_subtotal_discount)).setVisibility(View.VISIBLE);
                                    ((SFNFBoldTextView) viewMain.findViewById(R.id.TXT_SubTotalPrice)).setText(PageObject.getJSONObject("info_array").getString("total_price"));
                                    ((SFNFBoldTextView) viewMain.findViewById(R.id.TXT_DiscountPrice)).setText("-"+jsonObject.getJSONObject("info_array").getString("amount"));

                                    coupon_id=jsonObject.getJSONObject("info_array").getString("id");
                                    coupon_amount=jsonObject.getJSONObject("info_array").getString("amount");


                                    if (Double.parseDouble(PageObject.getJSONObject("info_array").getString("total_price"))
                                            >Double.parseDouble(jsonObject.getJSONObject("info_array").getString("amount")))
                                    {
                                        ((SFNFBoldTextView) viewMain.findViewById(R.id.TXT_TotalPrice)).setText(""+(Double.parseDouble(PageObject.getJSONObject("info_array").getString("total_price"))-Double.parseDouble(jsonObject.getJSONObject("info_array").getString("amount"))));
                                    }
                                    else {
                                        ((SFNFBoldTextView) viewMain.findViewById(R.id.TXT_TotalPrice)).setText(PageObject.getJSONObject("info_array").getString("trust_safety_price"));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }

                            @Override
                            public void OnError(String Error, String Response) {
                                appLoader.Dismiss();
                                try {
                                    ((LinearLayout) viewMain.findViewById(R.id.LL_subtotal_discount)).setVisibility(View.GONE);
                                    ((SFNFBoldTextView) viewMain.findViewById(R.id.TXT_TotalPrice)).setText(PageObject.getJSONObject("info_array").getString("total_price"));
                                    JSONObject jsonObject=new JSONObject(Response);
                                    ((SFNFTextView) viewMain.findViewById(R.id.Tv_coupon_code_valid_check)).setVisibility(View.VISIBLE);
                                    ((SFNFTextView) viewMain.findViewById(R.id.Tv_coupon_code_valid_check)).setText(jsonObject.getString("message"));
                                    ((SFNFTextView) viewMain.findViewById(R.id.Tv_coupon_code_valid_check)).setTextColor(Color.RED);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void OnError(String Error) {
                                appLoader.Dismiss();
                            }
                        });

                    }
                }
            }
        });

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

                for (int i = 0; i < ((BookingOneActivity) getActivity()).FirstPageData.size(); i++) {
                    if (((BookingOneActivity) getActivity()).FirstPageData.get(i) .getPARAMS().equalsIgnoreCase("coupon_id")) {
                        ((BookingOneActivity) getActivity()).FirstPageData.get(i).setValues(coupon_id);
                        break;
                    }
                    else if(i==((BookingOneActivity) getActivity()).FirstPageData.size()-1){
                        APIPOSTDATA apipostdata = new APIPOSTDATA();
                        apipostdata.setPARAMS("coupon_id");
                        apipostdata.setValues(coupon_id);
                        ((BookingOneActivity) getActivity()).FirstPageData.add(apipostdata);
                    }
                }

                for (int i = 0; i < ((BookingOneActivity) getActivity()).FirstPageData.size(); i++) {
                    if (((BookingOneActivity) getActivity()).FirstPageData.get(i) .getPARAMS().equalsIgnoreCase("coupon_amount")) {
                        ((BookingOneActivity) getActivity()).FirstPageData.get(i).setValues(coupon_amount);
                        break;
                    }
                    else if(i==((BookingOneActivity) getActivity()).FirstPageData.size()-1){
                        APIPOSTDATA apipostdata = new APIPOSTDATA();
                        apipostdata.setPARAMS("coupon_amount");
                        apipostdata.setValues(coupon_amount);
                        ((BookingOneActivity) getActivity()).FirstPageData.add(apipostdata);
                    }
                }

                for (int i = 0; i < ((BookingOneActivity) getActivity()).FirstPageData.size(); i++) {
                    if (((BookingOneActivity) getActivity()).FirstPageData.get(i) .getPARAMS().equalsIgnoreCase("add_message")) {
                        ((BookingOneActivity) getActivity()).FirstPageData.get(i).setValues("");
                        break;
                    }
                    else if(i==((BookingOneActivity) getActivity()).FirstPageData.size()-1){
                        APIPOSTDATA apipostdata = new APIPOSTDATA();
                        apipostdata.setPARAMS("add_message");
                        apipostdata.setValues("");
                        ((BookingOneActivity) getActivity()).FirstPageData.add(apipostdata);
                    }
                }

                //((BookingOneActivity)getActivity()).showConfirmReservationRequest();

                mListener.onFragmentInteraction("four");
                break;
        }
    }
}