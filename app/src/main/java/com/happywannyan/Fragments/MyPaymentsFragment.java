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
import com.happywannyan.Adapter.AdapterPaymentList;
import com.happywannyan.Constant.AppConstant;
import com.happywannyan.POJO.APIPOSTDATA;
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

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyPaymentsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyPaymentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
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
    AdapterPaymentList adapterPaymentList;
    final int GET_NEW_CARD = 2;
    Card card;

    private OnFragmentInteractionListener mListener;

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
        recycler_view= (RecyclerView) view.findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        appLoader = new AppLoader(getActivity());

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




    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void LoadPaymentDetails(){
        appLoader.Show();
        new CustomJSONParser().APIForGetMethod(AppConstant.BASEURL + "app_users_accountinfo?lang_id=" + AppConstant.Language + "&user_id=" + AppConstant.UserId
                , new ArrayList<APIPOSTDATA>(), new CustomJSONParser.JSONResponseInterface() {
                    @Override
                    public void OnSuccess(String Result) {
                        Log.i("Result",""+Result);
                        appLoader.Dismiss();
                        adapterPaymentList=new AdapterPaymentList(getActivity(), Result, new BookingFragmentFoure.onClickItem() {
                            @Override
                            public void onSelectItemClick(int position, JSONObject data) {

                            }
                        });
                        recycler_view.setAdapter(adapterPaymentList);
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent dataIntent) throws {
//        super.onActivityResult(requestCode, resultCode, data);
mmmmmmmn    }
}
