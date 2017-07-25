package com.happywannyan.Fragments;

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

import com.happywannyan.Activities.BaseActivity;
import com.happywannyan.Adapter.FavouriteRecyclerAdapter;
import com.happywannyan.Constant.AppContsnat;
import com.happywannyan.POJO.APIPOSTDATA;
import com.happywannyan.POJO.SetGetFavourite;
import com.happywannyan.R;
import com.happywannyan.Utils.AppLoader;
import com.happywannyan.Utils.JSONPerser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Favourite extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    AppLoader Loader;

    RecyclerView rcv_favourite;
    ArrayList<SetGetFavourite> favouriteArrayList;
    FavouriteRecyclerAdapter favouriteRecyclerAdapter;

    public Favourite() {
        // Required empty public constructor
    }

    public static Favourite newInstance(String param1, String param2) {
        Favourite fragment = new Favourite();
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
        new AppContsnat(getActivity());
        Loader = new AppLoader(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourite, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.IMG_icon_drwaer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BaseActivity) getActivity()).Menu_Drawer();
            }
        });

        rcv_favourite = (RecyclerView) view.findViewById(R.id.recycler_view);
        rcv_favourite.setLayoutManager(new LinearLayoutManager(getActivity()));

        favouriteArrayList = new ArrayList<>();


        CallAPIFROMDATA(0);


    }

    private void CallAPIFROMDATA(final int startpoint) {
        ArrayList<APIPOSTDATA> Params = new ArrayList<>();
        APIPOSTDATA apipostdata = new APIPOSTDATA();
        apipostdata.setPARAMS("user_id");
        apipostdata.setValues(AppContsnat.UserId);
        Params.add(apipostdata);

        apipostdata = new APIPOSTDATA();
        apipostdata.setPARAMS("start_form");
        apipostdata.setValues(startpoint + "");
        Params.add(apipostdata);

        apipostdata = new APIPOSTDATA();
        apipostdata.setPARAMS("per_page");
        apipostdata.setValues("10");
        Params.add(apipostdata);
        Loader.Show();
        new JSONPerser().API_FOR_GET(AppContsnat.BASEURL + "users_favsetters_list?", Params, new JSONPerser.JSONRESPONSE() {
            @Override
            public void OnSuccess(String Result) {
                try {
                    JSONObject Object = new JSONObject(Result);
                    JSONArray Array = Object.getJSONArray("info_array");

                    for (int i = 0; i < Array.length(); i++) {

                        SetGetFavourite setGetFavourite = new SetGetFavourite();
                        setGetFavourite.setCheckRightValue(false);
                        setGetFavourite.setDataObject(Array.getJSONObject(i));
                        favouriteArrayList.add(setGetFavourite);

                    }
                    if (startpoint == 0) {
                        favouriteRecyclerAdapter = new FavouriteRecyclerAdapter(getActivity(), favouriteArrayList);
                        rcv_favourite.setAdapter(favouriteRecyclerAdapter);
                    } else
                        favouriteRecyclerAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Loader.Dismiss();
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
}
