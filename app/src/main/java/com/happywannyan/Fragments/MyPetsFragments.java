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

import com.happywannyan.Activities.AddAnotherPetsActivity;
import com.happywannyan.Activities.BaseActivity;
import com.happywannyan.Adapter.YourPetsAdapter;
import com.happywannyan.Constant.AppConstant;
import com.happywannyan.POJO.SetGetAPIPostData;
import com.happywannyan.POJO.SetGetYourPets;
import com.happywannyan.R;
import com.happywannyan.Utils.AppLoader;
import com.happywannyan.Utils.CustomJSONParser;
import com.happywannyan.Utils.Loger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyPetsFragments extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    AppLoader appLoader;
    RecyclerView recyclerView;

    YourPetsAdapter yourPets_adapter;
    ArrayList<SetGetYourPets> ListPets;

    private OnFragmentInteractionListener mListener;

    public MyPetsFragments() {
        // Required empty public constructor
    }

    public static MyPetsFragments newInstance(String param1, String param2) {
        MyPetsFragments fragment = new MyPetsFragments();
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
        new AppConstant(getActivity());
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

        //yourPets_adapter = new YourPetsAdapter(getActivity(), ListPets);
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
                startActivity(new Intent(getActivity(), AddAnotherPetsActivity.class));
            }
        });


    }

    private void GET_PETDATA(final int i) {
        appLoader.Show();
        ArrayList<SetGetAPIPostData> params = new ArrayList<>();
        SetGetAPIPostData setGetAPIPostData = new SetGetAPIPostData();
        setGetAPIPostData.setPARAMS("user_id");
        setGetAPIPostData.setValues(AppConstant.UserId);
        params.add(setGetAPIPostData);
        setGetAPIPostData = new SetGetAPIPostData();
        setGetAPIPostData.setPARAMS("lang_id");
        setGetAPIPostData.setValues(AppConstant.Language);
        params.add(setGetAPIPostData);
        setGetAPIPostData = new SetGetAPIPostData();
        setGetAPIPostData.setPARAMS("start_form");
        setGetAPIPostData.setValues("" + i);
        params.add(setGetAPIPostData);
        setGetAPIPostData = new SetGetAPIPostData();
        setGetAPIPostData.setPARAMS("per_page");
        setGetAPIPostData.setValues("10");
        params.add(setGetAPIPostData);
        new CustomJSONParser().APIForGetMethod(AppConstant.BASEURL + "app_users_petinfo?", params, new CustomJSONParser.JSONResponseInterface() {
            @Override
            public void OnSuccess(String Result) {
                appLoader.Dismiss();

                try {

                    JSONObject jObject = new JSONObject(Result);

                    JSONArray infoarry = jObject.getJSONArray("info_array");
                    for (int i = 0; i < infoarry.length(); i++) {
                        JSONObject jsonObject = infoarry.getJSONObject(i);
                        SetGetYourPets setGetYourPets = new SetGetYourPets();
                        setGetYourPets.setEdit_id(jsonObject.getString("edit_id"));
                        setGetYourPets.setPet_type_id(jsonObject.getString("pet_type_id"));
                        setGetYourPets.setPet_image(jsonObject.getString("pet_image"));
                        setGetYourPets.setPet_name(jsonObject.getString("pet_name"));
                        setGetYourPets.setOtherinfo(jsonObject);
                        ListPets.add(setGetYourPets);
                    }
                    if (i == 0) {
                        yourPets_adapter = new YourPetsAdapter(getActivity(), ListPets);
                        recyclerView.setAdapter(yourPets_adapter);
                    } else {
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
        yourPets_adapter = new YourPetsAdapter(getActivity(), ListPets);
        recyclerView.setAdapter(yourPets_adapter);
    }
}
