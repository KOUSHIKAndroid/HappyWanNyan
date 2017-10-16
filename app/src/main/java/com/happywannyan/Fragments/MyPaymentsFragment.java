package com.happywannyan.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happywannyan.Activities.BaseActivity;
import com.happywannyan.Adapter.PaymentListAdapter;
import com.happywannyan.Constant.AppConstant;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.POJO.SetGetAPIPostData;
import com.happywannyan.R;
import com.happywannyan.SitterBooking.BookingFragmentFoure;
import com.happywannyan.SitterBooking.NewCardAddActivity;
import com.happywannyan.Utils.AppLoader;
import com.happywannyan.Utils.CustomJSONParser;
import com.happywannyan.Utils.Loger;
import com.happywannyan.Utils.MYAlert;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class MyPaymentsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView recycler_view;
    AppLoader appLoader;
    PaymentListAdapter paymentListAdapter;
    final int GET_NEW_CARD = 2;
    Card card;
    String make_defaultValue = "0";

    SFNFTextView tv_empty;

    public MyPaymentsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyPaymentsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyPaymentsFragment newInstance(String param1, String param2) {
        MyPaymentsFragment fragment = new MyPaymentsFragment();
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
        return inflater.inflate(R.layout.fragment_my_payments, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recycler_view = (RecyclerView) view.findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        appLoader = new AppLoader(getActivity());

        tv_empty= (SFNFTextView) view.findViewById(R.id.tv_empty);

        view.findViewById(R.id.IMG_icon_drwaer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) getActivity()).Menu_Drawer();
            }
        });
        view.findViewById(R.id.NEWCARD).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewCardAddActivity.class);
                startActivityForResult(intent, GET_NEW_CARD);
            }
        });

        LoadPaymentDetails();
    }

    public void LoadPaymentDetails() {
        appLoader.Show();
        new CustomJSONParser().APIForGetMethod(AppConstant.BASEURL + "app_users_accountinfo?lang_id=" + AppConstant.Language + "&user_id=" + AppConstant.UserId
                , new ArrayList<SetGetAPIPostData>(), new CustomJSONParser.JSONResponseInterface() {
                    @Override
                    public void OnSuccess(String Result) {
                        Log.i("Result", "" + Result);
                        appLoader.Dismiss();

                        recycler_view.setVisibility(View.VISIBLE);
                        tv_empty.setVisibility(View.GONE);

                        paymentListAdapter = new PaymentListAdapter(getActivity(), Result, new BookingFragmentFoure.onClickItem() {
                            @Override
                            public void onSelectItemClick(int position, JSONObject data) {

                            }
                        });
                        recycler_view.setAdapter(paymentListAdapter);
                    }

                    @Override
                    public void OnError(String Error, String Response) {
                        try {
                            JSONObject MainObject = new JSONObject(Response);
                            JSONArray ARRY = MainObject.getJSONArray("user_stripe_data");

                            if (ARRY.length()==0){
                                recycler_view.setVisibility(View.GONE);
                                tv_empty.setVisibility(View.VISIBLE);
                                new MYAlert(getActivity()).AlertOnly("" + getActivity().getResources().getString(R.string.nav_payment), "" + getString(R.string.no_data_found), new MYAlert.OnlyMessage() {
                                    @Override
                                    public void OnOk(boolean res) {

                                    }
                                });
                            }

                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                        appLoader.Dismiss();
                    }

                    @Override
                    public void OnError(String Error) {
                        appLoader.Dismiss();
                    }
                });
    }

    //    public interface onClickItem {
//        void onSelectItemClick(int position, JSONObject data);
//    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == GET_NEW_CARD) {
            new AppConstant(getActivity());

            final String cardHolderName = data.getStringExtra("cardHolderName");
            final String cardNumber = data.getStringExtra("cardNumber");
            String expiry = data.getStringExtra("expiry");
            String cvv = data.getStringExtra("cvv");
            make_defaultValue = data.getStringExtra("make_default");

            final int year = Integer.parseInt(
//                    "20" +
                    expiry.split("/")[1]);
            final int month = Integer.parseInt(expiry.split("/")[0]);

            Loger.MSG("@@ Expiry-", "CardHolderName-" + cardHolderName);
            Loger.MSG("@@ Expiry-", "CardNumber-" + cardNumber);
            Loger.MSG("@@ Expiry-", "Year-" + year);
            Loger.MSG("@@ Expiry-", "Month-" + month);
            Loger.MSG("@@ Expiry-", "cvv-" + cvv);
            Loger.MSG("@@ Expiry-", "make_defaultValue-" + make_defaultValue);

            card = new Card(cardNumber, month, year, cvv);
            if (card.validateCard()) {
                Loger.MSG("@@ Card ID->", card.getId() + "");
                Loger.MSG("@@ Card Number->", card.getNumber() + "");
                card.setName(cardHolderName);

                Stripe stripe = new Stripe(getActivity(), AppConstant.STRIPE_PUBLISH_KEY);
                appLoader.Show();

                stripe.createToken(
                        card,
                        new TokenCallback() {
                            public void onSuccess(final Token token) {
                                new CustomJSONParser().GetStripeCustomerID(token.getId(), new CustomJSONParser.JSONResponseInterface() {
                                    @Override
                                    public void OnSuccess(String Result) {
                                        Loger.MSG("@@ TokenSuccess", Result);

                                        //String CustomerID = ""+token.getId();

                                        String CustomerID = "";

                                        try {
                                            JSONObject JSONObjectTokenSuccessData = new JSONObject(Result);
                                            JSONArray jsonArray = JSONObjectTokenSuccessData.getJSONArray("data");
                                            CustomerID = jsonArray.getJSONObject(0).getString("id");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

//                                        try {
//                                            CustomerID=new JSONObject(Result).getString("id");
//                                        } catch (JSONException e) {
//                                            e.printStackTrace();
//                                        }

                                        Loger.MSG("@@ TOKEN finger-", token.getCard().getFingerprint() + "");
                                        Loger.MSG("@@ TOKEN Brand-", token.getCard().getBrand() + "");
                                        Loger.MSG("@@ TOKEN customerId-", token.getCard().getCustomerId() + "");
                                        Loger.MSG("@@ TOKEN customerLast4Digits-", token.getCard().getLast4() + "");
                                        Loger.MSG("@@ TOKEN ID-", token.getCard().getId() + "");

                                        ArrayList<SetGetAPIPostData> Params = new ArrayList<>();

                                        SetGetAPIPostData setGetAPIPostData = new SetGetAPIPostData();
                                        setGetAPIPostData.setPARAMS("card_brand");
                                        setGetAPIPostData.setValues(token.getCard().getBrand() + "");
                                        Params.add(setGetAPIPostData);

                                        setGetAPIPostData = new SetGetAPIPostData();
                                        setGetAPIPostData.setPARAMS("name_on_card");
                                        setGetAPIPostData.setValues(cardHolderName);
                                        Params.add(setGetAPIPostData);

                                        setGetAPIPostData = new SetGetAPIPostData();
                                        setGetAPIPostData.setPARAMS("card_id");
                                        setGetAPIPostData.setValues(token.getCard().getId() + "");
                                        Params.add(setGetAPIPostData);

                                        setGetAPIPostData = new SetGetAPIPostData();
                                        setGetAPIPostData.setPARAMS("address_line2");
                                        setGetAPIPostData.setValues(token.getCard().getAddressLine2() + "");
                                        Params.add(setGetAPIPostData);

                                        setGetAPIPostData = new SetGetAPIPostData();
                                        setGetAPIPostData.setPARAMS("address_city");
                                        setGetAPIPostData.setValues(token.getCard().getAddressCity() + "");
                                        Params.add(setGetAPIPostData);

                                        setGetAPIPostData = new SetGetAPIPostData();
                                        setGetAPIPostData.setPARAMS("user_id");
                                        setGetAPIPostData.setValues(AppConstant.UserId + "");
                                        Params.add(setGetAPIPostData);

                                        setGetAPIPostData = new SetGetAPIPostData();
                                        setGetAPIPostData.setPARAMS("address_zip");
                                        setGetAPIPostData.setValues(token.getCard().getAddressZip() + "");
                                        Params.add(setGetAPIPostData);

                                        setGetAPIPostData = new SetGetAPIPostData();
                                        setGetAPIPostData.setPARAMS("new_card");
                                        setGetAPIPostData.setValues("1");
                                        Params.add(setGetAPIPostData);

                                        setGetAPIPostData = new SetGetAPIPostData();
                                        setGetAPIPostData.setPARAMS("address_line1");
                                        setGetAPIPostData.setValues(token.getCard().getAddressLine1() + "");
                                        Params.add(setGetAPIPostData);

                                        setGetAPIPostData = new SetGetAPIPostData();
                                        setGetAPIPostData.setPARAMS("exp_month");
                                        setGetAPIPostData.setValues(month + "");
                                        Params.add(setGetAPIPostData);

                                        setGetAPIPostData = new SetGetAPIPostData();
                                        setGetAPIPostData.setPARAMS("address_state");
                                        setGetAPIPostData.setValues(token.getCard().getAddressState() + "");
                                        Params.add(setGetAPIPostData);

                                        setGetAPIPostData = new SetGetAPIPostData();
                                        setGetAPIPostData.setPARAMS("stripe_id");
                                        setGetAPIPostData.setValues(CustomerID);
                                        Params.add(setGetAPIPostData);

                                        setGetAPIPostData = new SetGetAPIPostData();
                                        setGetAPIPostData.setPARAMS("card_last_digits");
                                        setGetAPIPostData.setValues(token.getCard().getLast4() + "");
                                        Params.add(setGetAPIPostData);

                                        setGetAPIPostData = new SetGetAPIPostData();
                                        setGetAPIPostData.setPARAMS("card_status");
                                        setGetAPIPostData.setValues("");
                                        Params.add(setGetAPIPostData);

                                        setGetAPIPostData = new SetGetAPIPostData();
                                        setGetAPIPostData.setPARAMS("cvv_code");
                                        setGetAPIPostData.setValues(card.getCVC() + "");
                                        Params.add(setGetAPIPostData);

                                        setGetAPIPostData = new SetGetAPIPostData();
                                        setGetAPIPostData.setPARAMS("make_default");
                                        setGetAPIPostData.setValues(make_defaultValue);
                                        Params.add(setGetAPIPostData);

                                        setGetAPIPostData = new SetGetAPIPostData();
                                        setGetAPIPostData.setPARAMS("address_country");
                                        setGetAPIPostData.setValues(token.getCard().getAddressCountry() + "");
                                        Params.add(setGetAPIPostData);

                                        setGetAPIPostData = new SetGetAPIPostData();
                                        setGetAPIPostData.setPARAMS("exp_year");
                                        setGetAPIPostData.setValues(year + "");
                                        Params.add(setGetAPIPostData);

                                        setGetAPIPostData = new SetGetAPIPostData();
                                        setGetAPIPostData.setPARAMS("description");
                                        setGetAPIPostData.setValues("");
                                        Params.add(setGetAPIPostData);


                                        new CustomJSONParser().APIForPostMethod(AppConstant.BASEURL + "add_save_card", Params, new CustomJSONParser.JSONResponseInterface() {
                                            @Override
                                            public void OnSuccess(String Result) {
                                                Loger.MSG("@@ CARD RESP-", Result);
                                                new CustomJSONParser().APIForGetMethod(AppConstant.BASEURL + "app_users_accountinfo?lang_id=" + AppConstant.Language + "&user_id=" + AppConstant.UserId
                                                        , new ArrayList<SetGetAPIPostData>(), new CustomJSONParser.JSONResponseInterface() {
                                                            @Override
                                                            public void OnSuccess(String Result) {
                                                                appLoader.Dismiss();

                                                                paymentListAdapter = new PaymentListAdapter(getActivity(), Result, new BookingFragmentFoure.onClickItem() {
                                                                    @Override
                                                                    public void onSelectItemClick(int position, JSONObject data) {

                                                                    }
                                                                });
                                                                recycler_view.setAdapter(paymentListAdapter);
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

                                            @Override
                                            public void OnError(String Error, String Response) {
                                                Loger.MSG("@@ CARD Error ->", Response);
                                                appLoader.Dismiss();
                                            }

                                            @Override
                                            public void OnError(String Error) {
                                                appLoader.Dismiss();
                                            }
                                        });
                                    }

                                    @Override
                                    public void OnError(String Error, String Response) {
                                        Loger.MSG("@@ CARD Error ->", Error);
                                        Loger.MSG("@@ CARD ErrorResponse ->", Response);
                                        appLoader.Dismiss();
                                    }

                                    @Override
                                    public void OnError(String Error) {
                                        Loger.MSG("@@ CARD Error ->", Error);
                                        appLoader.Dismiss();
                                    }
                                });
                            }

                            public void onError(Exception error) {
                                appLoader.Dismiss();
                                new MYAlert(getActivity()).AlertOnly(getResources().getString(R.string.add_card_error), error.getLocalizedMessage(), new MYAlert.OnlyMessage() {
                                    @Override
                                    public void OnOk(boolean res) {

                                    }
                                });

                            }
                        }
                );

            } else {
                new MYAlert(getActivity()).AlertOnly(getResources().getString(R.string.add_card_error),getResources().getString(R.string.invalid_card_please_add), new MYAlert.OnlyMessage() {
                    @Override
                    public void OnOk(boolean res) {
                    }
                });
            }
        }
    }
}
