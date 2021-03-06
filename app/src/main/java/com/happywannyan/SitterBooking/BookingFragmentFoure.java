package com.happywannyan.SitterBooking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.happywannyan.Adapter.CardAdapter;
import com.happywannyan.Constant.AppConstant;
import com.happywannyan.Font.SFNFBoldTextView;
import com.happywannyan.POJO.SetGetAPIPostData;
import com.happywannyan.POJO.SetGetCards;
import com.happywannyan.POJO.SetGetStripData;
import com.happywannyan.R;
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


public class BookingFragmentFoure extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ArrayList<SetGetCards> setGetCardsArrayList;

    final int GET_NEW_CARD = 2;
    String make_defaultValue = "0";

    CardAdapter cardAdapter;

    JSONObject cardFinalSelection;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView REC_Card;
    AppLoader appLoader;
    Card card;

    SFNFBoldTextView tv_total_amount;

    public BookingFragmentFoure() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookingFragmentFoure.
     */
    // TODO: Rename and change types and number of parameters
    public static BookingFragmentFoure newInstance(String param1, String param2) {
        BookingFragmentFoure fragment = new BookingFragmentFoure();
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
        appLoader = new AppLoader(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_booking_fragment_foure, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        REC_Card = (RecyclerView) view.findViewById(R.id.REC_Card);
        REC_Card.setLayoutManager(new LinearLayoutManager(getActivity()));

        tv_total_amount = (SFNFBoldTextView) view.findViewById(R.id.tv_total_amount);
        tv_total_amount.setText(((BookingOneActivity) getActivity()).totalAmount);

        setGetCardsArrayList = new ArrayList<>();

        view.findViewById(R.id.Card_request).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MYAlert(getActivity()).AlertOkCancel("",
                        getActivity().getResources().getString(R.string.this_is_just_a_reservation_request),
                        getActivity().getResources().getString(R.string.ok),
                        getActivity().getResources().getString(R.string.cancel),
                        new MYAlert.OnOkCancel() {
                            @Override
                            public void OnOk() {

                                for (int i = 0; i < ((BookingOneActivity) getActivity()).FirstPageData.size(); i++) {
                                    try {
                                        if (((BookingOneActivity) getActivity()).FirstPageData.get(i).getPARAMS().equalsIgnoreCase("user_card_id")) {
                                            ((BookingOneActivity) getActivity()).FirstPageData.get(i).setValues(cardFinalSelection.getString("id"));
                                            break;
                                        } else if (i == ((BookingOneActivity) getActivity()).FirstPageData.size() - 1) {
                                            SetGetAPIPostData setGetAPIPostData = new SetGetAPIPostData();
                                            setGetAPIPostData.setPARAMS("user_card_id");
                                            setGetAPIPostData.setValues(cardFinalSelection.getString("id"));
                                            ((BookingOneActivity) getActivity()).FirstPageData.add(setGetAPIPostData);
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                }

                                for (int i = 0; i < ((BookingOneActivity) getActivity()).FirstPageData.size(); i++) {
                                    if (((BookingOneActivity) getActivity()).FirstPageData.get(i).getPARAMS().equalsIgnoreCase("user_new_card")) {
                                        ((BookingOneActivity) getActivity()).FirstPageData.get(i).setValues("");
                                        break;
                                    } else if (i == ((BookingOneActivity) getActivity()).FirstPageData.size() - 1) {
                                        SetGetAPIPostData setGetAPIPostData = new SetGetAPIPostData();
                                        setGetAPIPostData.setPARAMS("user_new_card");
                                        setGetAPIPostData.setValues("");
                                        ((BookingOneActivity) getActivity()).FirstPageData.add(setGetAPIPostData);
                                    }
                                }

                                for (int i = 0; i < ((BookingOneActivity) getActivity()).FirstPageData.size(); i++) {
                                    try {
                                        if (((BookingOneActivity) getActivity()).FirstPageData.get(i).getPARAMS().equalsIgnoreCase("stripeToken")) {
                                            ((BookingOneActivity) getActivity()).FirstPageData.get(i).setValues("");
                                            break;
                                        } else if (i == ((BookingOneActivity) getActivity()).FirstPageData.size() - 1) {
                                            SetGetAPIPostData setGetAPIPostData = new SetGetAPIPostData();
                                            setGetAPIPostData.setPARAMS("stripeToken");
                                            setGetAPIPostData.setValues(cardFinalSelection.getString("stripe_id"));
                                            ((BookingOneActivity) getActivity()).FirstPageData.add(setGetAPIPostData);
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                }

                                for (int i = 0; i < ((BookingOneActivity) getActivity()).FirstPageData.size(); i++) {
                                    try {
                                        if (((BookingOneActivity) getActivity()).FirstPageData.get(i).getPARAMS().equalsIgnoreCase("name_on_card")) {
                                            ((BookingOneActivity) getActivity()).FirstPageData.get(i).setValues(cardFinalSelection.getString("name_on_card"));
                                            break;
                                        } else if (i == ((BookingOneActivity) getActivity()).FirstPageData.size() - 1) {
                                            SetGetAPIPostData setGetAPIPostData = new SetGetAPIPostData();
                                            setGetAPIPostData.setPARAMS("name_on_card");
                                            setGetAPIPostData.setValues(cardFinalSelection.getString("name_on_card"));
                                            ((BookingOneActivity) getActivity()).FirstPageData.add(setGetAPIPostData);
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

                                }

                                for (int i = 0; i < ((BookingOneActivity) getActivity()).FirstPageData.size(); i++) {
                                    try {
                                        if (((BookingOneActivity) getActivity()).FirstPageData.get(i).getPARAMS().equalsIgnoreCase("security_code")) {
                                            ((BookingOneActivity) getActivity()).FirstPageData.get(i).setValues(cardFinalSelection.getString("cvv_code"));
                                            break;
                                        } else if (i == ((BookingOneActivity) getActivity()).FirstPageData.size() - 1) {
                                            SetGetAPIPostData setGetAPIPostData = new SetGetAPIPostData();
                                            setGetAPIPostData.setPARAMS("security_code");
                                            setGetAPIPostData.setValues(cardFinalSelection.getString("cvv_code"));
                                            ((BookingOneActivity) getActivity()).FirstPageData.add(setGetAPIPostData);
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

                                }
                                //((BookingOneActivity) getActivity()).showConfirmReservationRequest();
                                //((BookingOneActivity) getActivity()).submitConfirmReservationRequest();
                                //((BookingOneActivity) getActivity()).submitConfirmReservationRequestByVolley();
                                ((BookingOneActivity) getActivity()).submitConfirmReservationRequestUsingHTTP();
                            }

                            @Override
                            public void OnCancel() {

                            }
                        });
            }
        });

        view.findViewById(R.id.NEWCARD).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), NewCardAddActivity.class);
                startActivityForResult(intent, GET_NEW_CARD);
//                new AddCreditCard(getActivity()).AddNewOnClick(new AddCreditCard.OnCradListener() {
//                    @Override
//                    public void OnAddComplete(String data) {
//
//                    }
//
//                    @Override
//                    public void OnCancel() {
//
//                    }
//                });
            }
        });
        getApiLiveSecreteKeyForStrip();
    }

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
                                new CustomJSONParser().GetStripeCustomerID(getActivity(), token.getId(), new CustomJSONParser.JSONResponseInterface() {
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


                                        new CustomJSONParser().APIForPostMethod(getActivity(), AppConstant.BASEURL + "add_save_card", Params, new CustomJSONParser.JSONResponseInterface() {
                                            @Override
                                            public void OnSuccess(String Result) {
                                                Loger.MSG("@@ CARD RESP-", Result);

                                                try {
                                                    Toast.makeText(getActivity(), new JSONObject(Result).getString("message"), Toast.LENGTH_SHORT).show();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }


                                                new CustomJSONParser().APIForGetMethod(getActivity(), AppConstant.BASEURL + "app_users_accountinfo?lang_id=" + AppConstant.Language + "&user_id=" + AppConstant.UserId
                                                        , new ArrayList<SetGetAPIPostData>(), new CustomJSONParser.JSONResponseInterface() {
                                                            @Override
                                                            public void OnSuccess(String Result) {
                                                                appLoader.Dismiss();
                                                                setGetCardsArrayList.clear();
                                                                try {
                                                                    JSONObject jsonObject = new JSONObject(Result);
                                                                    JSONArray jsonArrayUserStripeData = jsonObject.getJSONArray("user_stripe_data");

                                                                    ArrayList<SetGetStripData> stripDataArrayList = new ArrayList<SetGetStripData>();

                                                                    for (int i = 0; i < jsonArrayUserStripeData.length(); i++) {
                                                                        SetGetStripData setGetStripData = new SetGetStripData();
                                                                        if (jsonArrayUserStripeData.getJSONObject(i).getString("is_default").equals("0")) {
                                                                            setGetStripData.setCheck(false);
                                                                        } else {
                                                                            setGetStripData.setCheck(true);
                                                                        }
                                                                        setGetStripData.setJsonObjectUserStripeData(jsonArrayUserStripeData.getJSONObject(i));
                                                                        stripDataArrayList.add(setGetStripData);
                                                                    }

                                                                    SetGetCards setGetCards = new SetGetCards();
                                                                    setGetCards.setCommunication_messege_email(jsonObject.getString("communication_messege_email"));
                                                                    setGetCards.setUser_email("user_email");
                                                                    setGetCards.setJsonArrayCardDetails(jsonObject.getJSONArray("card_details"));
                                                                    setGetCards.setStripDataArrayList(stripDataArrayList);
                                                                    setGetCardsArrayList.add(setGetCards);

                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                cardAdapter.notifyDataSetChanged();
                                                            }

                                                            @Override
                                                            public void OnError(String Error, String Response) {
                                                                appLoader.Dismiss();
                                                            }

                                                            @Override
                                                            public void OnError(String Error) {
                                                                appLoader.Dismiss();
                                                                if (Error.equalsIgnoreCase(getActivity().getResources().getString(R.string.please_check_your_internet_connection))) {
                                                                    Toast.makeText(getActivity(), Error, Toast.LENGTH_SHORT).show();
                                                                }
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
                                new MYAlert(getActivity()).AlertOnly(getActivity().getResources().getString(R.string.add_card_error), error.getLocalizedMessage(), new MYAlert.OnlyMessage() {
                                    @Override
                                    public void OnOk(boolean res) {

                                    }
                                });

                            }
                        }
                );

            } else {
                new MYAlert(getActivity()).AlertOnly(getActivity().getResources().getString(R.string.add_card_error), getActivity().getResources().getString(R.string.invalid_card_please_add), new MYAlert.OnlyMessage() {
                    @Override
                    public void OnOk(boolean res) {
                    }
                });
            }
        }
    }

    private void getApiLiveSecreteKeyForStrip() {
        new CustomJSONParser().APIForGetMethod(getActivity(), AppConstant.BASEURL + "app_sitesetting", new ArrayList<SetGetAPIPostData>(), new CustomJSONParser.JSONResponseInterface() {
            @Override
            public void OnSuccess(String Result) {

                Loger.MSG("Result-->", Result);
                try {
                    JSONObject jsonObject = new JSONObject(Result);

                    if (jsonObject.getJSONArray("info_array").getJSONObject(0).getString("stripe_pay_type").equals("1")) {

                        AppConstant.STRIPE_SECRATE_KEY = jsonObject.getJSONArray("info_array").getJSONObject(0).getString("stripe_live_secret_key");
                        AppConstant.STRIPE_PUBLISH_KEY = jsonObject.getJSONArray("info_array").getJSONObject(0).getString("stripe_live_public_key");
                    } else {
                        AppConstant.STRIPE_SECRATE_KEY = jsonObject.getJSONArray("info_array").getJSONObject(0).getString("stripe_sandbox_secret_key");
                        AppConstant.STRIPE_PUBLISH_KEY = jsonObject.getJSONArray("info_array").getJSONObject(0).getString("stripe_sandbox_public_key");
                    }

//                    Loger.MSG("STRIPE_SECRATE_KEY",AppConstant.STRIPE_SECRATE_KEY);
//                    Loger.MSG("STRIPE_PUBLISH_KEY",AppConstant.STRIPE_PUBLISH_KEY);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                SetCardDetails();
            }

            @Override
            public void OnError(String Error, String Response) {
                Loger.MSG("Error-->", Response);
            }

            @Override
            public void OnError(String Error) {
                Loger.MSG("Error-->", Error);
            }
        });
    }

    private void SetCardDetails() {
        appLoader.Show();
        new CustomJSONParser().APIForGetMethod(getActivity(), AppConstant.BASEURL + "app_users_accountinfo?lang_id=" + AppConstant.Language + "&user_id=" + AppConstant.UserId
                , new ArrayList<SetGetAPIPostData>(), new CustomJSONParser.JSONResponseInterface() {
                    @Override
                    public void OnSuccess(String Result) {
                        appLoader.Dismiss();
                        try {
                            JSONObject MainObject = new JSONObject(Result);
                            JSONArray arrayJson = MainObject.getJSONArray("user_stripe_data");
                            cardFinalSelection = arrayJson.getJSONObject(0);
                            for (int i = 0; i < arrayJson.length(); i++) {
                                if (arrayJson.getJSONObject(i).getString("is_default").equalsIgnoreCase("1")) {
                                    cardFinalSelection = arrayJson.getJSONObject(i);
                                    Loger.MSG("@@SelectedData", "" + cardFinalSelection);
                                    break;
                                }
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        try {
                            JSONObject jsonObject = new JSONObject(Result);
                            JSONArray jsonArrayUserStripeData = jsonObject.getJSONArray("user_stripe_data");

                            ArrayList<SetGetStripData> stripDataArrayList = new ArrayList<SetGetStripData>();

                            for (int i = 0; i < jsonArrayUserStripeData.length(); i++) {
                                SetGetStripData setGetStripData = new SetGetStripData();
                                if (jsonArrayUserStripeData.getJSONObject(i).getString("is_default").equals("0")) {
                                    setGetStripData.setCheck(false);
                                } else {
                                    setGetStripData.setCheck(true);
                                }

                                setGetStripData.setJsonObjectUserStripeData(jsonArrayUserStripeData.getJSONObject(i));
                                stripDataArrayList.add(setGetStripData);
                            }

                            SetGetCards setGetCards = new SetGetCards();
                            setGetCards.setCommunication_messege_email(jsonObject.getString("communication_messege_email"));
                            setGetCards.setUser_email("user_email");
                            setGetCards.setJsonArrayCardDetails(jsonObject.getJSONArray("card_details"));
                            setGetCards.setStripDataArrayList(stripDataArrayList);

                            setGetCardsArrayList.add(setGetCards);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        cardAdapter = new CardAdapter(getActivity(), setGetCardsArrayList, new onClickItem() {
                            @Override
                            public void onSelectItemClick(int position, JSONObject data) {
                                cardFinalSelection = data;
                                Loger.MSG("@@SelectedData", "" + cardFinalSelection);
                            }
                        });
                        REC_Card.setAdapter(cardAdapter);
                    }

                    @Override
                    public void OnError(String Error, String Response) {
                        appLoader.Dismiss();

                    }

                    @Override
                    public void OnError(String Error) {
                        appLoader.Dismiss();
                        if (Error.equalsIgnoreCase(getActivity().getResources().getString(R.string.please_check_your_internet_connection))) {
                            Toast.makeText(getActivity(), Error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public interface onClickItem {
        void onSelectItemClick(int position, JSONObject data);
    }

}
