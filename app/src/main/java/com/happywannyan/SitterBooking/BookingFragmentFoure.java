package com.happywannyan.SitterBooking;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happywannyan.Adapter.Adapter_Card;
import com.happywannyan.Constant.AppContsnat;
import com.happywannyan.OnFragmentInteractionListener;
import com.happywannyan.POJO.APIPOSTDATA;
import com.happywannyan.R;
import com.happywannyan.Utils.AppLoader;
import com.happywannyan.Utils.JSONPerser;
import com.stripe.android.model.Card;
import com.stripe.android.view.CardInputWidget;

import java.util.ArrayList;


public class BookingFragmentFoure extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView REC_Card;
    AppLoader Loader;

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
        Loader=new AppLoader(getActivity());
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
        CardInputWidget mCardInputWidget = (CardInputWidget) view.findViewById(R.id.card_input_widget);
        Card cardToSave = mCardInputWidget.getCard();
        if (cardToSave == null) {
//            mErrorDialogHandler.showError("Invalid Card Data");
        }
        REC_Card=(RecyclerView)view.findViewById(R.id.REC_Card);
        REC_Card.setLayoutManager(new LinearLayoutManager(getActivity()));
        SetCardDetails();


    }

    private void SetCardDetails() {
        Loader.Show();
        new JSONPerser().API_FOR_GET(AppContsnat.BASEURL + "app_users_accountinfo?lang_id=" + AppContsnat.Language + "&user_id=" + AppContsnat.UserId
                , new ArrayList<APIPOSTDATA>(), new JSONPerser.JSONRESPONSE() {
                    @Override
                    public void OnSuccess(String Result) {
                        Loader.Dismiss();
                        REC_Card.setAdapter(new Adapter_Card(getActivity(),Result));
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

    void test(){

    }

}
