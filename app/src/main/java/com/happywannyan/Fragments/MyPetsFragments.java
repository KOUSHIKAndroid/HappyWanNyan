package com.happywannyan.Fragments;

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

import com.happywannyan.Activities.AddAnotherPetsActivity;
import com.happywannyan.Activities.BaseActivity;
import com.happywannyan.Adapter.YourPetsAdapter;
import com.happywannyan.Constant.AppConstant;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.POJO.SetGetAPIPostData;
import com.happywannyan.POJO.SetGetYourPets;
import com.happywannyan.R;
import com.happywannyan.Utils.AppLoader;
import com.happywannyan.Utils.CustomJSONParser;
import com.happywannyan.Utils.Loger;
import com.happywannyan.Utils.MYAlert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link MyPetsFragments#newInstance} factory method to
 * create an instance of this fragment.
 */
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
    SFNFTextView tv_empty;
    YourPetsAdapter yourPets_adapter;
    ArrayList<SetGetYourPets> ListPets;

    public MyPetsFragments() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyPetsFragments.
     */
    // TODO: Rename and change types and number of parameters
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        appLoader = new AppLoader(getActivity());
        new AppConstant(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_pets_fragments, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        tv_empty= (SFNFTextView) view.findViewById(R.id.tv_empty);
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

    @Override
    public void onResume() {
        super.onResume();
        Loger.MSG("DEBUG", "onResume of HomeFragment");
        ListPets = new ArrayList<>();
        GET_PETDATA(0);
        yourPets_adapter = new YourPetsAdapter(getActivity(), ListPets,MyPetsFragments.this);
        recyclerView.setAdapter(yourPets_adapter);
    }

    public void GET_PETDATA(final int from) {
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
        setGetAPIPostData.setValues("" + from);
        params.add(setGetAPIPostData);
        setGetAPIPostData = new SetGetAPIPostData();
        setGetAPIPostData.setPARAMS("per_page");
        setGetAPIPostData.setValues("100");
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
                    if (from == 0) {

                        recyclerView.setVisibility(View.VISIBLE);
                        tv_empty.setVisibility(View.GONE);

                        yourPets_adapter = new YourPetsAdapter(getActivity(), ListPets,MyPetsFragments.this);
                        recyclerView.setAdapter(yourPets_adapter);
                    } else {
                        yourPets_adapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void OnError(String Error, String Response) {
                appLoader.Dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(Response);
                    if (jsonObject.getInt("next_data") == 0 && jsonObject.getInt("start_form") == 0 && ListPets.size() == 0) {

                        recyclerView.setVisibility(View.GONE);
                        tv_empty.setVisibility(View.VISIBLE);
                        tv_empty.setVisibility(View.VISIBLE);
                        recyclerView.setAdapter(null);

                        new MYAlert(getActivity()).AlertOnly("" + getActivity().getResources().getString(R.string.nav_yourpet), "" + getString(R.string.no_data_found), new MYAlert.OnlyMessage() {
                            @Override
                            public void OnOk(boolean res) {

                            }
                        });
                    }
                    else {
                        new MYAlert(getActivity()).AlertOnly("" + getActivity().getResources().getString(R.string.nav_yourpet),Error, new MYAlert.OnlyMessage() {
                            @Override
                            public void OnOk(boolean res) {

                            }
                        });
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }

            @Override
            public void OnError(String Error) {
                appLoader.Dismiss();
            }
        });
    }

}
