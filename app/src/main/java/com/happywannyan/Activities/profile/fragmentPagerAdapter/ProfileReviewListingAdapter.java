package com.happywannyan.Activities.profile.fragmentPagerAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.bumptech.glide.Glide;
import com.happywannyan.Font.SFNFBoldTextView;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by bodhidipta on 22/05/17.
 */

public class ProfileReviewListingAdapter extends RecyclerView.Adapter<ProfileReviewListingAdapter.ViewHolder> {

    private Context mContext = null;
JSONArray JSONArry;
    public ProfileReviewListingAdapter(Context mContext,JSONArray jsonArray) {
        this.mContext = mContext;
        this.JSONArry=jsonArray;
    }

    @Override
    public int getItemCount() {
        return JSONArry.length();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.profile_review_list_item_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            JSONObject jsonObject=JSONArry.getJSONObject(position);
            holder.UserName.setText(jsonObject.getString("review_user_name"));
            holder.Description.setText(jsonObject.getString("review_message"));
//            holder.Date.setText(jsonObject.getString(""));
            Glide.with(mContext).load(jsonObject.getString("review_user_img")).into(holder.IMG_Profile);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    class ViewHolder extends RecyclerView.ViewHolder {
        SFNFBoldTextView UserName;
        RatingBar Rating;
        ImageView IMG_Profile;
        SFNFTextView Date;
        SFNFTextView Description;
        public ViewHolder(View itemView) {
            super(itemView);
            UserName=(SFNFBoldTextView)itemView.findViewById(R.id.UserName);
            Rating=(RatingBar)itemView.findViewById(R.id.Rating);
            Date=(SFNFTextView)itemView.findViewById(R.id.Date);
            Description=(SFNFTextView)itemView.findViewById(R.id.Description);
            IMG_Profile=(ImageView)itemView.findViewById(R.id.IMG_Profile);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

}