package com.happywannyan.Adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.happywannyan.Activities.profile.ContactMsgActivity;
import com.happywannyan.Activities.profile.MeetUpWannyanActivity;
import com.happywannyan.Activities.profile.ProfileDetailsActivity;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.POJO.SetGetFavourite;
import com.happywannyan.R;
import com.happywannyan.Utils.Loger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by su on 6/26/17.
 */

public class FavouriteRecyclerAdapter extends RecyclerView.Adapter<FavouriteRecyclerAdapter.MyViewHolder> {
    DisplayMetrics displayMetrics;
    Context context;
    ArrayList<SetGetFavourite> favouriteArrayList;
    public FavouriteRecyclerAdapter(Context context, ArrayList<SetGetFavourite> favouriteArrayList){
        this.context=context;
        this.favouriteArrayList=favouriteArrayList;
        displayMetrics = context.getResources().getDisplayMetrics();
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.adapter_favourite,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        if(!favouriteArrayList.get(position).isCheckRightValue()){
            //holder.horizontalScrollView.invalidate();
            holder.horizontalScrollView.fullScroll(HorizontalScrollView.FOCUS_LEFT);
        }
        else {
            holder.horizontalScrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
        }


        holder.LLMain.getLayoutParams().width =displayMetrics.widthPixels;
        holder.LLDelete.getLayoutParams().width =(displayMetrics.widthPixels)/3;

        final JSONObject object= favouriteArrayList.get(position).getDataObject();

        try {
            Glide.with(context).load(object.getString("image")).into(holder.img_view);
            holder.tv_title.setText(object.getString("full_name"));
            holder.tv_address.setText(object.getString("location"));
//            holder.tv_reserve_or_not_reserve.setText(favouriteArrayList.get(position).getReservation());
//            holder.tv_meet_up.setText(favouriteArrayList.get(position).getMeet_up());
//            holder.tv_contact.setText(favouriteArrayList.get(position).getContact());

            holder.LLMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context, holder.img_view, "cardimage");
                    Intent intent = new Intent(context, ProfileDetailsActivity.class);
                    intent.putExtra("data", "" + object);
                    context.startActivity(intent, options.toBundle());
                }
            });

            holder.tv_reserve_or_not_reserve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context, holder.img_view, "cardimage");
                    Intent intent = new Intent(context, ProfileDetailsActivity.class);
                    intent.putExtra("data", "" + object);
                    context.startActivity(intent, options.toBundle());
                }
            });

            holder.tv_meet_up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent inten=new Intent(context,MeetUpWannyanActivity.class);
                    try {
                        inten.putExtra("DATA",object.getString("id"));
                        context.startActivity(inten);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

            holder.tv_contact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent inten=new Intent(context,ContactMsgActivity.class);
                    try {
                        inten.putExtra("DATA",object.getString("id"));
                        context.startActivity(inten);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }





        holder.horizontalScrollView.setOnTouchListener(new View.OnTouchListener() {
            float x1=0,x2=0;
            float MIN_DISTANCE=0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        x2 = event.getX();
                        float deltaX = x2 - x1;

                        if (Math.abs(deltaX) > MIN_DISTANCE)
                        {
                            // Left to Right swipe action
                            if (x2 > x1)
                            {
                                Toast.makeText(context, "Left to Right swipe [Next]", Toast.LENGTH_SHORT).show ();

                                Log.i("position","position:"+position);
                                Log.i("start","start");

                                if(favouriteArrayList.get(position).isCheckRightValue()){
                                favouriteArrayList.get(position).setCheckRightValue(false);
                                }

                                notifyDataSetChanged();


                                for (int i =0;i<favouriteArrayList.size();i++)
                                {
                                    Log.i("isopen","["+i+"]"+favouriteArrayList.get(i).isCheckRightValue());
                                }
                            }

                            // Right to left swipe action
                            else
                            {
                                Toast.makeText(context, "Right to Left swipe [Previous]", Toast.LENGTH_SHORT).show ();

                                Log.i("position","position:"+position);
                                Log.i("start","start");

                                if(!favouriteArrayList.get(position).isCheckRightValue()){

                                    favouriteArrayList.get(position).setCheckRightValue(true);
                                }


                                for (int i =0;i<favouriteArrayList.size();i++)
                                {
                                    if (i!=position)
                                    {
                                        if (favouriteArrayList.get(i).isCheckRightValue())
                                        {
                                            favouriteArrayList.get(i).setCheckRightValue(false);

                                            notifyItemChanged(i);

                                            break;
                                        }
                                    }
                                }

                                for (int i =0;i<favouriteArrayList.size();i++)
                                {
                                    Log.i("isopen","["+i+"]"+favouriteArrayList.get(i).isCheckRightValue());
                                }
                            }

                        }
                        else
                        {
                            // consider as something else - a screen tap for example
                        }
                        break;
                }
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return favouriteArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img_view;
        SFNFTextView tv_title,tv_reserve_or_not_reserve,tv_address,tv_meet_up,tv_contact;
        LinearLayout LLMain,LLDelete;
        HorizontalScrollView horizontalScrollView;
        View itemView;
        public MyViewHolder(View itemView) {
            super(itemView);

            img_view= (ImageView) itemView.findViewById(R.id.img_view);

            tv_title= (SFNFTextView) itemView.findViewById(R.id.tv_title);
            tv_reserve_or_not_reserve= (SFNFTextView) itemView.findViewById(R.id.tv_reserve_or_not_reserve);
            tv_address= (SFNFTextView) itemView.findViewById(R.id.tv_address);
            tv_meet_up= (SFNFTextView) itemView.findViewById(R.id.tv_meet_up);
            tv_contact= (SFNFTextView) itemView.findViewById(R.id.tv_contact);


            LLMain= (LinearLayout) itemView.findViewById(R.id.LLMain);
            LLDelete= (LinearLayout) itemView.findViewById(R.id.LLDelete);

            horizontalScrollView= (HorizontalScrollView) itemView.findViewById(R.id.scrollview);
            this.itemView=itemView;
        }
    }
}
