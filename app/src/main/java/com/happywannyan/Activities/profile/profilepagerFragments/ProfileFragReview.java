package com.happywannyan.Activities.profile.profilepagerFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happywannyan.Activities.profile.ProfileDetails;
import com.happywannyan.Activities.profile.fragmentPagerAdapter.ProfileReviewListingAdapter;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by bodhidipta on 22/05/17.
 */

public class ProfileFragReview  extends Fragment {

    JSONArray Reviews;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_fragment_listing_only, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            Reviews= new JSONObject(((ProfileDetails)getActivity()).JSONRESPONSE).getJSONObject("info_array").getJSONArray("review_list");
            if(Reviews.length()>0)
            {
                view.findViewById(R.id.No_Review).setVisibility(View.GONE);
                RecyclerView list = (RecyclerView) view.findViewById(R.id.service_recycler);
                list.setLayoutManager(new LinearLayoutManager(getActivity()));
                list.setAdapter(new ProfileReviewListingAdapter(getActivity(),Reviews));
            }else {
                ((SFNFTextView)view.findViewById(R.id.No_Review)).setText(getString(R.string.no_reviewFound));
                view.findViewById(R.id.No_Review).setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
