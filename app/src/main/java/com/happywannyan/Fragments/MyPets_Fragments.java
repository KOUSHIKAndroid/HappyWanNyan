package com.happywannyan.Fragments;

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

import com.happywannyan.Activities.AddAnotherPets;
import com.happywannyan.Activities.BaseActivity;
import com.happywannyan.Adapter.YourPets_Adapter;
import com.happywannyan.Constant.AppContsnat;
import com.happywannyan.POJO.APIPOSTDATA;
import com.happywannyan.POJO.YourPets;
import com.happywannyan.R;
import com.happywannyan.Utils.AppLoader;
import com.happywannyan.Utils.JSONPerser;
import com.happywannyan.Utils.Loger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyPets_Fragments extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    AppLoader appLoader;
    RecyclerView recyclerView;

    YourPets_Adapter yourPets_adapter;
    ArrayList<YourPets> ListPets;

    private OnFragmentInteractionListener mListener;

    public MyPets_Fragments() {
        // Required empty public constructor
    }

    public static MyPets_Fragments newInstance(String param1, String param2) {
        MyPets_Fragments fragment = new MyPets_Fragments();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appLoader = new AppLoader(getActivity());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        new AppContsnat(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_pets, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ListPets = new ArrayList<>();

        //GET_PETDATA(0);

        //yourPets_adapter = new YourPets_Adapter(getActivity(), ListPets);
        //recyclerView.setAdapter(yourPets_adapter);


        view.findViewById(R.id.IMG_icon_drwaer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) getActivity()).Menu_Drawer();
            }
        });

        view.findViewById(R.id.fab_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddAnotherPets.class));
            }
        });


    }

    private void GET_PETDATA(final int i) {
        appLoader.Show();
        ArrayList<APIPOSTDATA> params = new ArrayList<>();
        APIPOSTDATA apipostdata = new APIPOSTDATA();
        apipostdata.setPARAMS("user_id");
        apipostdata.setValues(AppContsnat.UserId);
        params.add(apipostdata);
        apipostdata = new APIPOSTDATA();
        apipostdata.setPARAMS("lang_id");
        apipostdata.setValues(AppContsnat.Language);
        params.add(apipostdata);
        apipostdata = new APIPOSTDATA();
        apipostdata.setPARAMS("start_form");
        apipostdata.setValues("" + i);
        params.add(apipostdata);
        apipostdata = new APIPOSTDATA();
        apipostdata.setPARAMS("per_page");
        apipostdata.setValues("10");
        params.add(apipostdata);
        new JSONPerser().API_FOR_GET(AppContsnat.BASEURL + "app_users_petinfo?", params, new JSONPerser.JSONRESPONSE() {
            @Override
            public void OnSuccess(String Result) {
                appLoader.Dismiss();

                try {

                    JSONObject jObject = new JSONObject(Result);

                    JSONArray infoarry = jObject.getJSONArray("info_array");
                    for (int i = 0; i < infoarry.length(); i++) {
                        JSONObject jsonObject = infoarry.getJSONObject(i);
                        YourPets yourPets = new YourPets();
                        yourPets.setEdit_id(jsonObject.getString("edit_id"));
                        yourPets.setPet_type_id(jsonObject.getString("pet_type_id"));
                        yourPets.setPet_image(jsonObject.getString("pet_image"));
                        yourPets.setPet_name(jsonObject.getString("pet_name"));
                        yourPets.setOtherinfo(jsonObject);
                        ListPets.add(yourPets);
                    }
                    if(i==0) {
                        yourPets_adapter = new YourPets_Adapter(getActivity(), ListPets);
                        recyclerView.setAdapter(yourPets_adapter);
                    }else {
                        yourPets_adapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                }


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

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


    @Override
    public void onResume() {
        super.onResume();
        Loger.MSG("DEBUG", "onResume of HomeFragment");
        ListPets = new ArrayList<>();
        GET_PETDATA(0);
        yourPets_adapter = new YourPets_Adapter(getActivity(), ListPets);
        recyclerView.setAdapter(yourPets_adapter);
    }
}
