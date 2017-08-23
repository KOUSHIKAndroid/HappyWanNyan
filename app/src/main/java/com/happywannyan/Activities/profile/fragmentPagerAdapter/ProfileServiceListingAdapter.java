package com.happywannyan.Activities.profile.fragmentPagerAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.happywannyan.Activities.profile.ProfileDetails;
import com.happywannyan.Font.SFNFBoldTextView;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.R;
import com.happywannyan.SitterBooking.BookingOne;
import com.happywannyan.Utils.Loger;
import com.happywannyan.Utils.MYAlert;

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

            Glide.with(mContext).load(object.getString("service_image")).into(holder.IMG_SERVICES);
            if(object.getString("description").toString().length()>70)
            {
                holder.TXT_ViewMore.setVisibility(View.VISIBLE);
                holder.Description.setMaxLines(2);
                Loger.MSG("@@ LENTH-",holder.Description.getText().length()+"");
            }else {
                holder.TXT_ViewMore.setVisibility(View.GONE);
                holder.Description.setTag("jjjjj");
            }



            holder.TXT_ViewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        new MYAlert(mContext).AlertOnly(mContext.getString(R.string.description), object.getString("description"), new MYAlert.OnlyMessage() {
                            @Override
                            public void OnOk(boolean res) {

                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

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
        SFNFTextView PricePer,TXT_ViewMore;
        RelativeLayout RL_Book;
        ImageView IMG_SERVICES;

        public ViewHolder(View itemView) {
            super(itemView);
            Title = (SFNFBoldTextView) itemView.findViewById(R.id.Title);
            Description = (SFNFTextView) itemView.findViewById(R.id.Description);
            TXT_ViewMore = (SFNFTextView) itemView.findViewById(R.id.TXT_ViewMore);
            PricePer = (SFNFTextView) itemView.findViewById(R.id.PricePer);
            RL_Book = (RelativeLayout) itemView.findViewById(R.id.RL_Book);
            IMG_SERVICES=(ImageView)itemView.findViewById(R.id.IMG_SERVICES);

        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

}
