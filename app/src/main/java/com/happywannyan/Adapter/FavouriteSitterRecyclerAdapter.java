package com.happywannyan.Adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.happywannyan.Activities.profile.ContactMsgActivity;
import com.happywannyan.Activities.profile.MeetUpWannyanActivity;
import com.happywannyan.Activities.profile.ProfileDetailsActivity;
import com.happywannyan.Constant.AppConstant;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.POJO.SetGetAPIPostData;
import com.happywannyan.POJO.SetGetFavourite;
import com.happywannyan.R;
import com.happywannyan.Utils.AppLoader;
import com.happywannyan.Utils.CustomJSONParser;
import com.happywannyan.Utils.Loger;
import com.happywannyan.Utils.MYAlert;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by su on 6/26/17.
 */

public class FavouriteSitterRecyclerAdapter extends RecyclerView.Adapter<FavouriteSitterRecyclerAdapter.MyViewHolder> {
    DisplayMetrics displayMetrics;
    Context context;
    ArrayList<SetGetFavourite> favouriteArrayList;
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();
    AppLoader appLoader;

    public FavouriteSitterRecyclerAdapter(Context context, ArrayList<SetGetFavourite> favouriteArrayList) {
        this.context = context;
        this.favouriteArrayList = favouriteArrayList;
        displayMetrics = context.getResources().getDisplayMetrics();
        appLoader = new AppLoader(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.adapter_favourite, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final String data = favouriteArrayList.get(position).getTagName();
        // Use ViewBindHelper to restore and save the open/close state of the SwipeRevealView
        // put an unique string id as value, can be any string which uniquely define the data
        binderHelper.bind(holder.swipe_layout, data);


        holder.RL_Main.getLayoutParams().width = displayMetrics.widthPixels;
        holder.LLDelete.getLayoutParams().width = (displayMetrics.widthPixels) / 3;
        // holder.LLDelete.getLayoutParams().height = (displayMetrics.widthPixels) / 3;

        final JSONObject object = favouriteArrayList.get(position).getDataObject();

        try {
            if(!object.getString("image").trim().equals("")) {
                Glide.with(context).load(object.getString("image").trim()).into(holder.img_view);
            }
            holder.tv_title.setText(object.getString("full_name"));
            holder.tv_address.setText(object.getString("location"));
//            holder.tv_reserve_or_not_reserve.setText(favouriteArrayList.get(position).getReservation());
//            holder.tv_meet_up.setText(favouriteArrayList.get(position).getMeet_up());
//            holder.tv_contact.setText(favouriteArrayList.get(position).getContact());

            holder.RL_Main.setOnClickListener(new View.OnClickListener() {
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
                    Intent inten = new Intent(context, MeetUpWannyanActivity.class);
                    try {
                        inten.putExtra("DATA", object.getString("id"));
                        context.startActivity(inten);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

            holder.tv_contact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent inten = new Intent(context, ContactMsgActivity.class);
                    try {
                        inten.putExtra("DATA", object.getString("id"));
                        context.startActivity(inten);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            holder.LLDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new MYAlert(context).AlertOkCancel("",
                            context.getResources().getString(R.string.are_you_sure_you_want_to_delete), new MYAlert.OnOkCancel() {
                                @Override
                                public void OnOk() {
                                    //onBackPressed();
                                    try {
                                        deleteYourPet(object.getString("id"), position);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void OnCancel() {

                                }
                            });
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return favouriteArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img_view;
        SFNFTextView tv_title, tv_reserve_or_not_reserve, tv_address, tv_meet_up, tv_contact;
        LinearLayout LLDelete;
        RelativeLayout RL_Main;
        View itemView;
        SwipeRevealLayout swipe_layout;

        public MyViewHolder(View itemView) {
            super(itemView);

            img_view = (ImageView) itemView.findViewById(R.id.img_view);

            tv_title = (SFNFTextView) itemView.findViewById(R.id.tv_title);
            tv_reserve_or_not_reserve = (SFNFTextView) itemView.findViewById(R.id.tv_reserve_or_not_reserve);
            tv_address = (SFNFTextView) itemView.findViewById(R.id.tv_address);
            tv_meet_up = (SFNFTextView) itemView.findViewById(R.id.tv_meet_up);
            tv_contact = (SFNFTextView) itemView.findViewById(R.id.tv_contact);
            swipe_layout = (SwipeRevealLayout) itemView.findViewById(R.id.swipe_layout);
            RL_Main = (RelativeLayout) itemView.findViewById(R.id.RL_Main);
            LLDelete = (LinearLayout) itemView.findViewById(R.id.LLDelete);

        }
    }


    public void deleteYourPet(String delSetterId, final int position) {
        appLoader.Show();

        ArrayList<SetGetAPIPostData> params = new ArrayList<>();

        SetGetAPIPostData setGetAPIPostData = new SetGetAPIPostData();
        setGetAPIPostData.setPARAMS("user_id");
        setGetAPIPostData.setValues(AppConstant.UserId);
        params.add(setGetAPIPostData);

        setGetAPIPostData = new SetGetAPIPostData();
        setGetAPIPostData.setPARAMS("lang_id");
        setGetAPIPostData.setValues(AppConstant.Language);
        params.add(setGetAPIPostData);

        setGetAPIPostData = new SetGetAPIPostData();
        setGetAPIPostData.setPARAMS("fav_status");
        setGetAPIPostData.setValues("0");
        params.add(setGetAPIPostData);

        setGetAPIPostData = new SetGetAPIPostData();
        setGetAPIPostData.setPARAMS("sitter_user_id");
        setGetAPIPostData.setValues(delSetterId);
        params.add(setGetAPIPostData);

        /////////delete here and api fire////////////////

        new CustomJSONParser().APIForGetMethod(AppConstant.BASEURL + "app_favourite_sitters?", params, new CustomJSONParser.JSONResponseInterface() {
            @Override
            public void OnSuccess(String Result) {
                appLoader.Dismiss();

                try {

                    JSONObject jObject = new JSONObject(Result);
                    Loger.MSG("DeleteJSONObject", "" + jObject);

                    if (jObject.getBoolean("response")) {
                        favouriteArrayList.remove(position);
                        //notifyDataSetChanged();
                        notifyItemRemoved(position);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void OnError(String Error, String Response) {
                appLoader.Dismiss();
            }

            @Override
            public void OnError(String Error) {
                appLoader.Dismiss();
            }
        });

    }

}
