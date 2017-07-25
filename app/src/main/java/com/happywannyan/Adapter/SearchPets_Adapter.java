package com.happywannyan.Adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import com.bumptech.glide.Glide;
import com.happywannyan.Activities.Booking.BookingDetails;
import com.happywannyan.Font.SFNFBoldTextView;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.Activities.profile.ProfileDetails;
import com.happywannyan.POJO.SearchData;
import com.happywannyan.R;
import com.happywannyan.Utils.provider.RatingColor;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by su on 5/27/17.
 */

public class SearchPets_Adapter extends RecyclerView.Adapter<SearchPets_Adapter.MyViewHolder> {

    Context context;
    ArrayList<SearchData> searchPetArrayList;

    public SearchPets_Adapter(Context context, ArrayList<SearchData> searchPetArrayList) {
        this.context = context;
        this.searchPetArrayList = searchPetArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_search_pet, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final SearchData searchData = searchPetArrayList.get(position);


        try {
            Glide.with(context).load(searchData.getSearcItem().getString("photo_url")).into(holder.img_view);
            holder.tv_title.setText(searchData.getSearcItem().getString("nickname"));


//        holder.tv_days.setText(searchPetArrayList.get(position).getDays());
            holder.tv_name.setText(searchData.getSearcItem().getString("business_name"));
            holder.tv_service_.setText(searchData.getSearcItem().getString("service_name_all"));
            holder.tv_details.setText(searchData.getSearcItem().getString("Profile_summary"));
            holder.tv_address.setText(searchData.getSearcItem().getString("whole_address"));
            holder.tv_Price.setText(Html.fromHtml(searchData.getSearcItem().getString("currency")) + " " + searchData.getSearcItem().getString("price_one"));
            holder.tv_review.setText(searchData.getSearcItem().getString("num_rvw") + " " + context.getResources().getString(R.string.review));

            holder.rating_bar.setRating(Float.parseFloat(searchData.getSearcItem().getString("ave_rating")));
            holder.rating_bar.setIsIndicator(true);
            LayerDrawable stars = (LayerDrawable) holder.rating_bar.getProgressDrawable();
            RatingColor.SETRatingColor(stars);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context, holder.img_view, "cardimage");
                    Intent intent = new Intent(context, ProfileDetails.class);
                    intent.putExtra("data", "" + searchData.getSearcItem());
                    context.startActivity(intent, options.toBundle());
//                 context.startActivity(new Intent(context, ProfileDetails.class));
                }
            });

        } catch (JSONException e) {

        } catch (Exception e) {

        }


    }

    @Override
    public int getItemCount() {
        return searchPetArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView img_view;
        SFNFTextView tv_title, tv_name, tv_details, tv_address, tv_time, tv_review;
        SFNFBoldTextView tv_Price, tv_service_;
        RatingBar rating_bar;
        View itemView;

        public MyViewHolder(View itemView) {
            super(itemView);
            img_view = (AppCompatImageView) itemView.findViewById(R.id.img_view);
            tv_title = (SFNFTextView) itemView.findViewById(R.id.tv_title);
            tv_name = (SFNFTextView) itemView.findViewById(R.id.tv_name);
            tv_details = (SFNFTextView) itemView.findViewById(R.id.tv_details);
            tv_address = (SFNFTextView) itemView.findViewById(R.id.tv_address);
            tv_time = (SFNFTextView) itemView.findViewById(R.id.tv_time);
            rating_bar = (RatingBar) itemView.findViewById(R.id.rating_bar);
            tv_review = (SFNFTextView) itemView.findViewById(R.id.tv_review);
            tv_Price = (SFNFBoldTextView) itemView.findViewById(R.id.tv_Price);
            tv_service_ = (SFNFBoldTextView) itemView.findViewById(R.id.tv_service_);
            this.itemView = itemView;

            rating_bar = (AppCompatRatingBar) itemView.findViewById(R.id.rating_bar);

        }
    }
}
