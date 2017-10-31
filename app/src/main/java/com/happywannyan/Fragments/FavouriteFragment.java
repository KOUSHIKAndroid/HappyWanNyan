package com.happywannyan.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happywannyan.Activities.BaseActivity;
import com.happywannyan.Adapter.FavouriteSitterRecyclerAdapter;
import com.happywannyan.Constant.AppConstant;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.POJO.SetGetAPIPostData;
import com.happywannyan.POJO.SetGetFavourite;
import com.happywannyan.R;
import com.happywannyan.Utils.AppLoader;
import com.happywannyan.Utils.CustomJSONParser;
import com.happywannyan.Utils.MYAlert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FavouriteFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    AppLoader appLoader;

    SFNFTextView tv_empty;

    RecyclerView rcv_favourite;
    ArrayList<SetGetFavourite> favouriteArrayList;
    FavouriteSitterRecyclerAdapter favouriteSitterRecyclerAdapter;

    public FavouriteFragment() {
        // Required empty public constructor
    }

    public static FavouriteFragment newInstance(String param1, String param2) {
        FavouriteFragment fragment = new FavouriteFragment();
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
        new AppConstant(getActivity());
        appLoader = new AppLoader(getActivity());
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
        tv_empty= (SFNFTextView) view.findViewById(R.id.tv_empty);
        rcv_favourite = (RecyclerView) view.findViewById(R.id.recycler_view);
        rcv_favourite.setLayoutManager(new LinearLayoutManager(getActivity()));

        favouriteArrayList = new ArrayList<>();


        CallAPIFROMDATA(0);
    }

    public void CallAPIFROMDATA(final int startpoint) {
        ArrayList<SetGetAPIPostData> Params = new ArrayList<>();
        SetGetAPIPostData setGetAPIPostData = new SetGetAPIPostData();
        setGetAPIPostData.setPARAMS("user_id");
        setGetAPIPostData.setValues(AppConstant.UserId);
        Params.add(setGetAPIPostData);

        setGetAPIPostData = new SetGetAPIPostData();
        setGetAPIPostData.setPARAMS("start_form");
        setGetAPIPostData.setValues(startpoint + "");
        Params.add(setGetAPIPostData);

        setGetAPIPostData = new SetGetAPIPostData();
        setGetAPIPostData.setPARAMS("per_page");
        setGetAPIPostData.setValues("10");
        Params.add(setGetAPIPostData);
        appLoader.Show();
        new CustomJSONParser().APIForGetMethod(getActivity(),AppConstant.BASEURL + "users_favsetters_list?", Params, new CustomJSONParser.JSONResponseInterface() {
            @Override
            public void OnSuccess(String Result) {
                try {
                    JSONObject Object = new JSONObject(Result);
                    int next_data = Object.getInt("next_data");

                    JSONArray Array = Object.getJSONArray("info_array");

                    for (int i = 0; i < Array.length(); i++) {
                        SetGetFavourite setGetFavourite = new SetGetFavourite();
                        setGetFavourite.setTagName("view " + i);
                        setGetFavourite.setDataObject(Array.getJSONObject(i));
                        favouriteArrayList.add(setGetFavourite);
                    }
                    if (startpoint == 0) {

                        rcv_favourite.setVisibility(View.VISIBLE);
                        tv_empty.setVisibility(View.GONE);

                        favouriteSitterRecyclerAdapter = new FavouriteSitterRecyclerAdapter(getActivity(), favouriteArrayList,FavouriteFragment.this);
                        rcv_favourite.setAdapter(favouriteSitterRecyclerAdapter);
                    } else {
                        favouriteSitterRecyclerAdapter.nextData = next_data;
                        favouriteSitterRecyclerAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                appLoader.Dismiss();
            }

            @Override
            public void OnError(String Error, String Response) {
                appLoader.Dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(Response);
                    if (jsonObject.getInt("next_data") == 0 && jsonObject.getInt("start_form") == 0 && favouriteArrayList.size() == 0) {

                        rcv_favourite.setVisibility(View.GONE);
                        tv_empty.setVisibility(View.VISIBLE);
                        rcv_favourite.setAdapter(null);

//                        new MYAlert(getActivity()).AlertOnly("" + getActivity().getResources().getString(R.string.nav_favoritesitter), "" + getString(R.string.no_data_found), new MYAlert.OnlyMessage() {
//                            @Override
//                            public void OnOk(boolean res) {
//
//                            }
//                        });
                    }
                    else {
                        new MYAlert(getActivity()).AlertOnly("" + getActivity().getResources().getString(R.string.nav_favoritesitter),Error, new MYAlert.OnlyMessage() {
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
