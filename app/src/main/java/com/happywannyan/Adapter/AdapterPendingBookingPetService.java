package com.happywannyan.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.happywannyan.POJO.SetGetPendingBooking;
import com.happywannyan.R;


import java.util.ArrayList;

/**
 * Created by su on 10/20/17.
 */

public class AdapterPendingBookingPetService extends RecyclerView.Adapter<AdapterPendingBookingPetService.MyViewHolder> {
    Context context;
    ArrayList<SetGetPendingBooking> pet_details;

    public AdapterPendingBookingPetService(Context context, ArrayList<SetGetPendingBooking> pet_details){
        this.context=context;
        this.pet_details=pet_details;
    }
    @Override
    public AdapterPendingBookingPetService.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_pending_booking, viewGroup, false);
        return new AdapterPendingBookingPetService.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AdapterPendingBookingPetService.MyViewHolder myViewHolder, final int position) {

        myViewHolder.checkbox.setText(pet_details.get(position).getPet_details());

        myViewHolder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pet_details.get(position).isChecked())
                {
                    pet_details.get(position).setChecked(false);
                }else {
                    pet_details.get(position).setChecked(true);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return pet_details.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkbox;
        public MyViewHolder(View itemView) {
            super(itemView);
            checkbox= (CheckBox) itemView.findViewById(R.id.checkbox);
        }
    }
}
