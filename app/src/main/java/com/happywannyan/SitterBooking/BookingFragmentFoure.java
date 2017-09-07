package com.happywannyan.SitterBooking;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cooltechworks.creditcarddesign.CardEditActivity;
import com.cooltechworks.creditcarddesign.CreditCardUtils;
import com.happywannyan.Adapter.Adapter_Card;
import com.happywannyan.Constant.AppContsnat;
import com.happywannyan.OnFragmentInteractionListener;
import com.happywannyan.POJO.APIPOSTDATA;
import com.happywannyan.POJO.SetGetCards;
import com.happywannyan.R;
import com.happywannyan.Utils.AppLoader;
import com.happywannyan.Utils.JSONPerser;
import com.happywannyan.Utils.Loger;
import com.happywannyan.Utils.MYAlert;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;


public class BookingFragmentFoure extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ArrayList<SetGetCards> setGetCardsArrayList;

    JSONObject cardFinalSelection;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView REC_Card;
    AppLoader Loader;
    Card card;

    private OnFragmentInteractionListener mListener;

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
        Loader = new AppLoader(getActivity());
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

        setGetCardsArrayList = new ArrayList<>();

        view.findViewById(R.id.Card_request).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MYAlert(getActivity()).AlertOkCancel(getActivity().getResources().getString(R.string.reservation_request),
                        getActivity().getResources().getString(R.string.this_is_just_a_reservation_request), new MYAlert.OnOkCancel() {
                            @Override
                            public void OnOk() {

                                for (int i = 0; i < ((BookingOne) getActivity()).FirstPageData.size(); i++) {
                                    try {
                                        if (((BookingOne) getActivity()).FirstPageData.get(i).getPARAMS().equalsIgnoreCase("user_card_id")) {
                                            ((BookingOne) getActivity()).FirstPageData.get(i).setValues(cardFinalSelection.getString("id"));
                                            break;
                                        } else if (i == ((BookingOne) getActivity()).FirstPageData.size() - 1) {
                                            APIPOSTDATA apipostdata = new APIPOSTDATA();
                                            apipostdata.setPARAMS("user_card_id");
                                            apipostdata.setValues(cardFinalSelection.getString("id"));
                                            ((BookingOne) getActivity()).FirstPageData.add(apipostdata);
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                }

                                for (int i = 0; i < ((BookingOne) getActivity()).FirstPageData.size(); i++) {
                                    if (((BookingOne) getActivity()).FirstPageData.get(i).getPARAMS().equalsIgnoreCase("user_new_card")) {
                                        ((BookingOne) getActivity()).FirstPageData.get(i).setValues("");
                                        break;
                                    } else if (i == ((BookingOne) getActivity()).FirstPageData.size() - 1) {
                                        APIPOSTDATA apipostdata = new APIPOSTDATA();
                                        apipostdata.setPARAMS("user_new_card");
                                        apipostdata.setValues("");
                                        ((BookingOne) getActivity()).FirstPageData.add(apipostdata);
                                    }
                                }

                                for (int i = 0; i < ((BookingOne) getActivity()).FirstPageData.size(); i++) {
                                    if (((BookingOne) getActivity()).FirstPageData.get(i).getPARAMS().equalsIgnoreCase("stripeToken")) {
                                        ((BookingOne) getActivity()).FirstPageData.get(i).setValues("");
                                        break;
                                    } else if (i == ((BookingOne) getActivity()).FirstPageData.size() - 1) {
                                        APIPOSTDATA apipostdata = new APIPOSTDATA();
                                        apipostdata.setPARAMS("stripeToken");
                                        apipostdata.setValues("");
                                        ((BookingOne) getActivity()).FirstPageData.add(apipostdata);
                                    }
                                }

                                for (int i = 0; i < ((BookingOne) getActivity()).FirstPageData.size(); i++) {
                                    try {
                                        if (((BookingOne) getActivity()).FirstPageData.get(i).getPARAMS().equalsIgnoreCase("name_on_card")) {
                                            ((BookingOne) getActivity()).FirstPageData.get(i).setValues(cardFinalSelection.getString("name_on_card"));
                                            break;
                                        } else if (i == ((BookingOne) getActivity()).FirstPageData.size() - 1) {
                                            APIPOSTDATA apipostdata = new APIPOSTDATA();
                                            apipostdata.setPARAMS("name_on_card");
                                            apipostdata.setValues(cardFinalSelection.getString("name_on_card"));
                                            ((BookingOne) getActivity()).FirstPageData.add(apipostdata);
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

                                }

                                for (int i = 0; i < ((BookingOne) getActivity()).FirstPageData.size(); i++) {
                                    try {
                                        if (((BookingOne) getActivity()).FirstPageData.get(i).getPARAMS().equalsIgnoreCase("security_code")) {
                                            ((BookingOne) getActivity()).FirstPageData.get(i).setValues(cardFinalSelection.getString("cvv_code"));
                                            break;
                                        } else if (i == ((BookingOne) getActivity()).FirstPageData.size() - 1) {
                                            APIPOSTDATA apipostdata = new APIPOSTDATA();
                                            apipostdata.setPARAMS("security_code");
                                            apipostdata.setValues(cardFinalSelection.getString("cvv_code"));
                                            ((BookingOne) getActivity()).FirstPageData.add(apipostdata);
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

                                }
                                ((BookingOne) getActivity()).showConfirmReservationRequest();
                                ((BookingOne) getActivity()).submitConfirmReservationRequest();
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

                final int GET_NEW_CARD = 2;

                Intent intent = new Intent(getActivity(), CardEditActivity.class);
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

        SetCardDetails();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 2) {
            new AppContsnat(getActivity());

            final String cardHolderName = data.getStringExtra(CreditCardUtils.EXTRA_CARD_HOLDER_NAME);
            final String cardNumber = data.getStringExtra(CreditCardUtils.EXTRA_CARD_NUMBER);
            String expiry = data.getStringExtra(CreditCardUtils.EXTRA_CARD_EXPIRY);
            String cvv = data.getStringExtra(CreditCardUtils.EXTRA_CARD_CVV);
            Loger.MSG("@@ Exipry-", cardNumber);

            final int year = Integer.parseInt("20" + expiry.split("/")[1]);
            final int month = Integer.parseInt(expiry.split("/")[0]);
            Loger.MSG("@@ Exipry-", " YESR-" + year);
            Loger.MSG("@@ Exipry-", " MOnth-" + month);


            card = new Card(cardNumber, month, year, cvv);
            if (card.validateCard()) {
                Loger.MSG("@@ Crad ID-", card.getId() + "");
                Loger.MSG("@@ Crad ID-", card.getNumber() + "");
                Loger.MSG("@@ Crad-", card.getNumber() + "");
                card.setName(cardHolderName);


                Stripe stripe = new Stripe(getActivity(), AppContsnat.STRIPE_PUBLISH_KEY);
                Loader.Show();

                stripe.createToken(
                        card,
                        new TokenCallback() {
                            public void onSuccess(final Token token) {


                                new JSONPerser().GET_STRIPE_CUSTIMERID(token.getId(), new JSONPerser.JSONRESPONSE() {
                                    @Override
                                    public void OnSuccess(String Result) {
                                        Loger.MSG("@@ CUUU", Result);

                                        String CustomerID = "";
//                                        try {
//                                            CustomerID=new JSONObject(Result).getString("id");
//                                        } catch (JSONException e) {
//                                            e.printStackTrace();
//                                        }


                                        Loger.MSG("@@ TOKEN finger-", token.getCard().getFingerprint() + "");
                                        Loger.MSG("@@ TOKEN Brand-", token.getCard().getBrand() + "");
                                        Loger.MSG("@@ TOKEN customerId-", token.getCard().getCustomerId() + "");
                                        Loger.MSG("@@ TOKEN customerId-", token.getCard().getLast4() + "");
                                        Loger.MSG("@@ TOKEN ID-", token.getCard().getId() + "");
                                        HashMap<String, String> Params = new HashMap<String, String>();
                                        Params.put("user_id", AppContsnat.UserId);
                                        Params.put("stripe_id", CustomerID + "");
                                        Params.put("card_id", token.getCard().getId() + "");
                                        Params.put("name_on_card", cardHolderName);
                                        Params.put("name_on_card", cardHolderName);
                                        Params.put("exp_month", month + "");
                                        Params.put("exp_year", year + "");
                                        Params.put("card_last_digits", token.getCard().getLast4() + "");
                                        Params.put("cvv_code", card.getCVC() + "");
                                        Params.put("new_card", "1");
                                        Params.put("make_default", "1");
                                        new JSONPerser().API_FOR_POST_2(AppContsnat.BASEURL + "add_save_card", Params, new JSONPerser.JSONRESPONSE() {
                                            @Override
                                            public void OnSuccess(String Result) {
                                                Loger.MSG("@@ CRAD RESP-", Result);
                                                new JSONPerser().API_FOR_GET(AppContsnat.BASEURL + "app_users_accountinfo?lang_id=" + AppContsnat.Language + "&user_id=" + AppContsnat.UserId
                                                        , new ArrayList<APIPOSTDATA>(), new JSONPerser.JSONRESPONSE() {
                                                            @Override
                                                            public void OnSuccess(String Result) {
                                                                Loader.Dismiss();
                                                                REC_Card.setAdapter(new Adapter_Card(getActivity(), Result, new onClickItem() {
                                                                    @Override
                                                                    public void onSelectItemClick(int position, JSONObject data) {
                                                                        cardFinalSelection = data;
                                                                        Loger.MSG("SelectedData",""+data);
                                                                    }
                                                                }));
                                                            }

                                                            @Override
                                                            public void OnError(String Error, String Response) {
                                                                Loader.Dismiss();

                                                            }

                                                            @Override
                                                            public void OnError(String Error) {
                                                                Loader.Dismiss();

                                                            }
                                                        });
                                            }

                                            @Override
                                            public void OnError(String Error, String Response) {
                                                Loger.MSG("@@ CRAD Err'-", Response);
                                                Loader.Dismiss();

                                            }

                                            @Override
                                            public void OnError(String Error) {
                                                Loader.Dismiss();

                                            }
                                        });


                                    }

                                    @Override
                                    public void OnError(String Error, String Response) {

                                    }

                                    @Override
                                    public void OnError(String Error) {

                                    }
                                });


                            }

                            public void onError(Exception error) {
                                Loader.Dismiss();
                                new MYAlert(getActivity()).AlertOnly("Add Card Error", error.getLocalizedMessage(), new MYAlert.OnlyMessage() {
                                    @Override
                                    public void OnOk(boolean res) {

                                    }
                                });

                            }
                        }
                );

            } else {
                new MYAlert(getActivity()).AlertOnly("Add Card Error", "Invalid card please add a correct card", new MYAlert.OnlyMessage() {
                    @Override
                    public void OnOk(boolean res) {

                    }
                });
            }
        }

    }

    private void SetCardDetails() {
        Loader.Show();
        new JSONPerser().API_FOR_GET(AppContsnat.BASEURL + "app_users_accountinfo?lang_id=" + AppContsnat.Language + "&user_id=" + AppContsnat.UserId
                , new ArrayList<APIPOSTDATA>(), new JSONPerser.JSONRESPONSE() {
                    @Override
                    public void OnSuccess(String Result) {
                        Loader.Dismiss();
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

                        REC_Card.setAdapter(new Adapter_Card(getActivity(), Result, new onClickItem() {
                            @Override
                            public void onSelectItemClick(int position, JSONObject data) {
                                cardFinalSelection = data;
                                Loger.MSG("SelectedData",""+data);
                            }
                        }));
                    }

                    @Override
                    public void OnError(String Error, String Response) {
                        Loader.Dismiss();

                    }

                    @Override
                    public void OnError(String Error) {
                        Loader.Dismiss();

                    }
                });

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri.toString());
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

    void test() {

    }

    public interface onClickItem {
        void onSelectItemClick(int position, JSONObject data);
    }

}
