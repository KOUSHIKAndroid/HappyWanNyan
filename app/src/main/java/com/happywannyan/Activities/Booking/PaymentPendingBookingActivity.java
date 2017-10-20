package com.happywannyan.Activities.Booking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.happywannyan.Adapter.CardAdapter;
import com.happywannyan.Constant.AppConstant;
import com.happywannyan.POJO.SetGetAPIPostData;
import com.happywannyan.POJO.SetGetCards;
import com.happywannyan.POJO.SetGetStripData;
import com.happywannyan.R;
import com.happywannyan.SitterBooking.BookingFragmentFoure;
import com.happywannyan.SitterBooking.BookingOneActivity;
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

/**
 * Created by su on 10/20/17.
 */

public class PaymentPendingBookingActivity extends AppCompatActivity {


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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_pending_booking);
        appLoader = new AppLoader(this);

        REC_Card = (RecyclerView)findViewById(R.id.REC_Card);
        REC_Card.setLayoutManager(new LinearLayoutManager(PaymentPendingBookingActivity.this));

        setGetCardsArrayList = new ArrayList<>();

        findViewById(R.id.Card_request).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MYAlert(PaymentPendingBookingActivity.this).AlertOkCancel("",
//                        getActivity().getResources().getString(R.string.reservation_request),
                        getResources().getString(R.string.do_you_really_submit_this_payment), new MYAlert.OnOkCancel() {
                            @Override
                            public void OnOk() {


                            }
                            @Override
                            public void OnCancel() {

                            }
                        });
            }
        });

        findViewById(R.id.NEWCARD).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PaymentPendingBookingActivity.this, NewCardAddActivity.class);
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

        findViewById(R.id.IMG_icon_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        SetCardDetails();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == GET_NEW_CARD) {
            new AppConstant(PaymentPendingBookingActivity.this);

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

                Stripe stripe = new Stripe(PaymentPendingBookingActivity.this, AppConstant.STRIPE_PUBLISH_KEY);
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
                                new MYAlert(PaymentPendingBookingActivity.this).AlertOnly(getResources().getString(R.string.add_card_error), error.getLocalizedMessage(), new MYAlert.OnlyMessage() {
                                    @Override
                                    public void OnOk(boolean res) {

                                    }
                                });

                            }
                        }
                );

            } else {
                new MYAlert(PaymentPendingBookingActivity.this).AlertOnly(getResources().getString(R.string.add_card_error), getResources().getString(R.string.invalid_card_please_add), new MYAlert.OnlyMessage() {
                    @Override
                    public void OnOk(boolean res) {
                    }
                });
            }
        }
    }

    private void SetCardDetails() {
        appLoader.Show();
        new CustomJSONParser().APIForGetMethod(AppConstant.BASEURL + "app_users_accountinfo?lang_id=" + AppConstant.Language + "&user_id=" + AppConstant.UserId
                , new ArrayList<SetGetAPIPostData>(), new CustomJSONParser.JSONResponseInterface() {
                    @Override
                    public void OnSuccess(String Result) {
                        appLoader.Dismiss();
                        try {
                            JSONObject MainObject = new JSONObject(Result);
                            JSONArray arrayJson = MainObject.getJSONArray("user_stripe_data");
                            for (int i = 0; i < arrayJson.length(); i++) {
                                if (arrayJson.getJSONObject(i).getString("is_default").equalsIgnoreCase("1")) {
                                    cardFinalSelection = arrayJson.getJSONObject(i);
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

                        cardAdapter = new CardAdapter(PaymentPendingBookingActivity.this, setGetCardsArrayList, new BookingFragmentFoure.onClickItem() {
                            @Override
                            public void onSelectItemClick(int position, JSONObject data) {
                                cardFinalSelection = data;
                                Loger.MSG("SelectedData", "" + data);
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

                    }
                });
    }

    public interface onClickItem {
        void onSelectItemClick(int position, JSONObject data);
    }

}
