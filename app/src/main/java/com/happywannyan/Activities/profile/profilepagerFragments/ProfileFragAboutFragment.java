package com.happywannyan.Activities.profile.profilepagerFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.happywannyan.Activities.profile.ProfileDetailsActivity;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by bodhidipta on 22/05/17.
 */

public class ProfileFragAboutFragment extends Fragment {
    JSONObject jsonObject;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            jsonObject=new JSONObject(((ProfileDetailsActivity)getActivity()).JSONRESPONSESTRING);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_fragment_about, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        try {
            LinearLayout LLSkils=(LinearLayout)view.findViewById(R.id.LLSkils);
            LinearLayout LLSpclAcco=(LinearLayout)view.findViewById(R.id.LLSpclAcco);
            LinearLayout LLHightLisgt=(LinearLayout)view.findViewById(R.id.LLHightLisgt);
            JSONArray Skills=jsonObject.getJSONObject("info_array").getJSONArray("special_skills");
            JSONArray SPCLAcco=jsonObject.getJSONObject("info_array").getJSONArray("spe_accommodation");
            JSONArray hightlight_feature=jsonObject.getJSONObject("info_array").getJSONArray("pet_requirment");

            for(int i=0;i<Skills.length();i++){
                View item=LayoutInflater.from(getActivity()).inflate(R.layout.profile_about_items,null);
                SFNFTextView textView=(SFNFTextView)item.findViewById(R.id.Text);
                textView.setText(Skills.getString(i));
                textView.setTextColor(ResourcesCompat.getColor(getActivity().getResources(), R.color.colorBtnRed, null));
                LLSkils.addView(item);
            }

            for(int i=0;i<SPCLAcco.length();i++){
                View item=LayoutInflater.from(getActivity()).inflate(R.layout.profile_about_items,null);
                SFNFTextView textView=(SFNFTextView)item.findViewById(R.id.Text);
                textView.setText(SPCLAcco.getString(i));
                LLSpclAcco.addView(item);
            }

            for(int i=0;i<hightlight_feature.length();i++){
                SFNFTextView Tittle=new SFNFTextView(getActivity());
                Tittle.setText(hightlight_feature.getJSONObject(i).getString("heading_name"));
                LLHightLisgt.addView(Tittle);


                LinearLayout Horizental=new LinearLayout(getActivity());
                Horizental.setOrientation(LinearLayout.HORIZONTAL);
                Horizental.setGravity(Gravity.BOTTOM);
                JSONArray TemArr=hightlight_feature.getJSONObject(i).getJSONArray("pet_info");
                int size=30;

                for(int j=0;j<TemArr.length();j++) {
                    size=size+40;
                    View item = LayoutInflater.from(getActivity()).inflate(R.layout.profile_about_items_petsize, null);
                    ImageView imageView=(ImageView)item.findViewById(R.id.IMAGE);
                    Glide.with(getActivity()).load(TemArr.getJSONObject(j).getString("pet_image")).override(size,size).into(imageView);
                    SFNFTextView textView = (SFNFTextView) item.findViewById(R.id.TXT_SIZE);
                    textView.setText(TemArr.getJSONObject(j).getString("pet_req_name"));
                    Horizental.addView(item);
                }
                LLHightLisgt.addView(Horizental);
            }
            ((SFNFTextView)view.findViewById(R.id.Description)).setText(jsonObject.getJSONObject("info_array").getJSONObject("about_info").getString("description"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
