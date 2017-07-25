package com.happywannyan.Adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.happywannyan.Activities.Booking.BookingDetails;
import com.happywannyan.Font.SFNFBoldTextView;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.Fragments.BookingFragment;
import com.happywannyan.POJO.SetGetUpComingBooking;
import com.happywannyan.R;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by su on 5/30/17.
 */

public class Booking_Adapter extends RecyclerView.Adapter<Booking_Adapter.MyViewHolder> {
    Context context;
    BookingFragment bookingFragment;
    public int nextData=1;
    int from=0;
    ArrayList<JSONObject> AllBooking;

    public Booking_Adapter(Context context, BookingFragment bookingFragment,ArrayList<JSONObject> AllBooking){
        this.context=context;
        this.bookingFragment=bookingFragment;
        this.AllBooking=AllBooking;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_up_coming_booking,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        try {

            final JSONObject object = AllBooking.get(position);

            Glide.with(context).load(object.getJSONObject("users_profile").getString("booked_user_image")).into(holder.img_view);

            holder.tv_title.setText(object.getJSONObject("users_profile").getString("custom_quotes"));
            holder.tv_name.setText(object.getJSONObject("users_profile").getString("booked_user_name"));

            holder.tv_start_date.setText(object.getJSONObject("booking_info").getString("booking_start_date"));
            holder.tv_end_date.setText(object.getJSONObject("booking_info").getString("booking_end_date"));
            holder.tv_booking_id.setText(object.getJSONObject("booking_info").getString("booking_id"));
            holder.tv_service_value.setText(object.getJSONObject("booking_info").getString("booking_service"));
            holder.tv_total_pets_value.setText(object.getJSONObject("booking_info").getString("booked_total_pet"));
            holder.tv_total_amount_value.setText(object.getJSONObject("booking_info").getString("booked_total_amount"));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context, holder.img_view, "cardimage");
                    Intent intent = new Intent(context, BookingDetails.class);
                    intent.putExtra("data",""+object);
                    context. startActivity(intent, options.toBundle());
//                    context.startActivity(new Intent(context, BookingDetails.class));
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position==AllBooking.size()-1 &&
                AllBooking.size()%10==0
                && AllBooking.size()>=10
                && nextData==1){

            ///////////lazy load here called///////
            from=from+10;
            //message_fragment.loadList(""+from);
            bookingFragment.loadList(String.valueOf(from));
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return AllBooking.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView img_view;
        SFNFTextView tv_title,tv_name;
        SFNFBoldTextView tv_start_date,tv_end_date,tv_booking_id,tv_service_value,tv_total_pets_value,tv_total_amount_value;
        public MyViewHolder(View itemView) {
            super(itemView);
            img_view= (AppCompatImageView) itemView.findViewById(R.id.img_view);

            tv_title= (SFNFTextView) itemView.findViewById(R.id.tv_title);
            tv_name= (SFNFTextView) itemView.findViewById(R.id.tv_name);

            tv_start_date= (SFNFBoldTextView) itemView.findViewById(R.id.tv_start_date);
            tv_end_date= (SFNFBoldTextView) itemView.findViewById(R.id.tv_end_date);
            tv_booking_id= (SFNFBoldTextView) itemView.findViewById(R.id.tv_booking_id);
            tv_service_value= (SFNFBoldTextView) itemView.findViewById(R.id.tv_service_value);
            tv_total_pets_value= (SFNFBoldTextView) itemView.findViewById(R.id.tv_total_pets_value);
            tv_total_amount_value= (SFNFBoldTextView) itemView.findViewById(R.id.tv_total_amount_value);
        }
    }
}
