package com.happywannyan.Activities.profile.fragmentPagerAdapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.happywannyan.R;

import org.json.JSONArray;
import org.json.JSONException;


/**
 * Created by bodhidipta on 22/05/17.
 */

public class ProfileImageListingAdapter extends RecyclerView.Adapter<ProfileImageListingAdapter.ViewHolder> {

    private Context mContext = null;
JSONArray array;
    public ProfileImageListingAdapter(Context mContext, JSONArray images) {
        this.mContext = mContext;
        this.array=images;
    }

    @Override
    public int getItemCount() {
        return array.length();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.profile_image_item_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            Glide.with(mContext).load(array.getString(position)).into(holder.imageView);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.IMG);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

}