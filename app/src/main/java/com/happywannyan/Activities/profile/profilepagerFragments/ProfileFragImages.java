package com.happywannyan.Activities.profile.profilepagerFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happywannyan.Activities.profile.ProfileDetails;
import com.happywannyan.Activities.profile.fragmentPagerAdapter.ProfileImageListingAdapter;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by bodhidipta on 22/05/17.
 */

public class ProfileFragImages  extends Fragment {

    JSONArray Images;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_fragment_listing_only, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            Images=new JSONObject(((ProfileDetails)getActivity()).JSONRESPONSE).getJSONObject("info_array").getJSONArray("imagelist");
            if(Images.length()>0){
                view.findViewById(R.id.No_Review).setVisibility(View.GONE);
                RecyclerView list = (RecyclerView) view.findViewById(R.id.service_recycler);
                list.setLayoutManager(new GridLayoutManager(getActivity(), 4));
                list.setAdapter(new ProfileImageListingAdapter(getActivity(),Images));
            }else {
                ((SFNFTextView)view.findViewById(R.id.No_Review)).setText(getString(R.string.no_ImagesFound));
                view.findViewById(R.id.No_Review).setVisibility(View.VISIBLE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }



    }
}