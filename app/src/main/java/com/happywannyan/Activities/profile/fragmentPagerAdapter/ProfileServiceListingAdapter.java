package com.happywannyan.Activities.profile.fragmentPagerAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.happywannyan.Activities.profile.ProfileDetails;
import com.happywannyan.Font.SFNFBoldTextView;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.R;
import com.happywannyan.SitterBooking.BookingOne;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by bodhidipta on 22/05/17.
 */

public class ProfileServiceListingAdapter extends RecyclerView.Adapter<ProfileServiceListingAdapter.ViewHolder> {

    private Context mContext = null;
    JSONArray ServiceArry;

    public ProfileServiceListingAdapter(Context mContext, JSONArray serviceArry) {
        this.mContext = mContext;
        this.ServiceArry = serviceArry;
    }

    @Override
    public int getItemCount() {
        return ServiceArry.length();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.profile_service_listing_item_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            final JSONObject object = ServiceArry.getJSONObject(position);
            holder.Title.setText(object.getString("service_name"));
            holder.Description.setText(object.getString("description"));
            holder.PricePer.setText(object.getString("service_price") + "/" + object.getString("unit_name"));
            holder.RL_Book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, BookingOne.class);
                    intent.putExtra("LIST", "");
                    intent.putExtra("ItemDetails",""+((ProfileDetails)mContext).PrevJSON);
                    intent.putExtra("Single",true);
                    intent.putExtra("SELECT", "" + object);
                    mContext.startActivity(intent);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        SFNFBoldTextView Title;
        SFNFTextView Description;
        SFNFTextView PricePer;
        RelativeLayout RL_Book;

        public ViewHolder(View itemView) {
            super(itemView);
            Title = (SFNFBoldTextView) itemView.findViewById(R.id.Title);
            Description = (SFNFTextView) itemView.findViewById(R.id.Description);
            PricePer = (SFNFTextView) itemView.findViewById(R.id.PricePer);
            RL_Book = (RelativeLayout) itemView.findViewById(R.id.RL_Book);

        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

}
