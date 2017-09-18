package com.happywannyan.Activities.profile.profilepagerFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happywannyan.Activities.profile.ProfileDetailsActivity;
import com.happywannyan.Activities.profile.fragmentPagerAdapter.ProfileServiceListingAdapter;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.R;
import com.happywannyan.Utils.Loger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by bodhidipta on 22/05/17.
 */

public class ProfileFragServiceFragment extends Fragment {

    JSONArray ServiceArray;
    int block_user_status = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_fragment_listing_only, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView list = (RecyclerView) view.findViewById(R.id.service_recycler);

        try {
            ServiceArray = new JSONObject(((ProfileDetailsActivity) getActivity()).JSONRESPONSESTRING).getJSONObject("info_array").getJSONArray("servicelist");
            final JSONObject BasicInfo = new JSONObject(((ProfileDetailsActivity) getActivity()).JSONRESPONSESTRING).getJSONObject("info_array").getJSONObject("basic_info");
            block_user_status = BasicInfo.getInt("block_user_status");
            Loger.MSG("ServiceArray", "-->" + ServiceArray.toString());

            if (ServiceArray.length() > 0) {
                view.findViewById(R.id.No_Review).setVisibility(View.GONE);
                list.setLayoutManager(new LinearLayoutManager(getActivity()));
                list.setAdapter(new ProfileServiceListingAdapter(getActivity(), ServiceArray, block_user_status));
            } else {
                ((SFNFTextView) view.findViewById(R.id.No_Review)).setText(getString(R.string.no_ServicesFound));
                view.findViewById(R.id.No_Review).setVisibility(View.VISIBLE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
