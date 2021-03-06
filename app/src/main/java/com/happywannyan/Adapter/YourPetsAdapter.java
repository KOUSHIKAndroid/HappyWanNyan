package com.happywannyan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.happywannyan.Activities.EditAnotherPetsActivity;
import com.happywannyan.Activities.profile.MeetUpWannyanActivity;
import com.happywannyan.Constant.AppConstant;
import com.happywannyan.Font.SFNFBoldTextView;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.Fragments.MyPaymentsFragment;
import com.happywannyan.Fragments.MyPetsFragments;
import com.happywannyan.POJO.SetGetAPIPostData;
import com.happywannyan.POJO.SetGetYourPets;
import com.happywannyan.R;
import com.happywannyan.Utils.AppLoader;
import com.happywannyan.Utils.CustomJSONParser;
import com.happywannyan.Utils.Loger;
import com.happywannyan.Utils.MYAlert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by su on 5/26/17.
 */

public class YourPetsAdapter extends RecyclerView.Adapter<YourPetsAdapter.MyViewHolder> {
    ArrayList<SetGetYourPets> setGetYourPetsArrayList;
    Context context;
    AppLoader appLoader;
    MyPetsFragments myPetsFragments;

    public YourPetsAdapter(Context context, ArrayList<SetGetYourPets> setGetYourPetsArrayList,MyPetsFragments myPetsFragments) {
        this.context = context;
        this.setGetYourPetsArrayList = setGetYourPetsArrayList;
        this.myPetsFragments=myPetsFragments;
        appLoader = new AppLoader(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_your_pets, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final SetGetYourPets data = setGetYourPetsArrayList.get(position);


        holder.tv_name.setText(data.getPet_name());
        try {
            JSONArray SelectData = new JSONArray();
            JSONArray RADIO = new JSONArray();
            JSONArray ObjectData = data.getOtherinfo().getJSONArray("other_info");
            for (int i = 0; i < ObjectData.length(); i++) {
                if (ObjectData.getJSONObject(i).getString("input_field_type").equals("3"))
                    SelectData.put(ObjectData.getJSONObject(i));
                if (ObjectData.getJSONObject(i).getString("input_field_type").equals("5"))
                    RADIO.put(ObjectData.getJSONObject(i));
            }

            holder.tv_year.setText(SelectData.getJSONObject(0).getString("show_name"));
            holder.tv_year_lbl.setText(SelectData.getJSONObject(0).getString("input_name"));
            holder.tv_month.setText(SelectData.getJSONObject(1).getString("show_name"));
            holder.tv_month_lbl.setText(SelectData.getJSONObject(1).getString("input_name"));
            holder.tv_gender.setText(SelectData.getJSONObject(2).getString("show_name"));
            holder.tv_gender_lbl.setText(SelectData.getJSONObject(2).getString("input_name"));
            holder.tv_size.setText(SelectData.getJSONObject(3).getString("show_name"));
            holder.tv_size_lbl.setText(SelectData.getJSONObject(3).getString("input_name"));
            if (SelectData != null && SelectData.length() > 4) {
                holder.tv_breed_value.setText(SelectData.getJSONObject(4).getString("show_name"));
                holder.tv_breed_lbl.setText(SelectData.getJSONObject(4).getString("input_name"));
            } else {
                holder.tv_breed_value.setVisibility(View.GONE);
                holder.tv_breed_lbl.setVisibility(View.GONE);
            }

            holder.tv_type.setText(ObjectData.getJSONObject(0).getString("show_name"));
            if (RADIO != null && RADIO.getJSONObject(0) != null) {
                holder.tv_spayed_or_neutered_value.setText(RADIO.getJSONObject(0).getString("show_name"));
                holder.tv_spayed_or_neutered.setText(RADIO.getJSONObject(0).getString("input_name"));
            } else {
                holder.tv_spayed_or_neutered_value.setVisibility(View.GONE);
                holder.tv_spayed_or_neutered.setVisibility(View.GONE);
            }
            if (RADIO != null && RADIO.length() > 1 && RADIO.getJSONObject(1) != null) {
                holder.tv_friendly_value.setText(RADIO.getJSONObject(1).getString("show_name"));
                holder.tv_friendly.setText(RADIO.getJSONObject(1).getString("input_name"));
            } else {
                holder.tv_friendly_value.setVisibility(View.GONE);
                holder.tv_friendly.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        if(!data.getPet_image().trim().equals("")) {
            Glide.with(context).load(data.getPet_image().trim()).into(holder.img_view);
        }

        holder.img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditAnotherPetsActivity.class);
                Loger.MSG("@@@", data.getOtherinfo().toString());
                intent.putExtra("Data", data.getOtherinfo().toString());
                context.startActivity(intent);
            }
        });

        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new MYAlert(context).AlertOkCancel(context.getResources().getString(R.string.delete),
                        context.getResources().getString(R.string.are_you_sure_you_want_to_delete_this_information),
                        context.getResources().getString(R.string.delete),
                        context.getResources().getString(R.string.cancel),
                         new MYAlert.OnOkCancel() {
                            @Override
                            public void OnOk() {
                                Loger.MSG("ok_alert", "ok");
                                Loger.MSG("getEdit_id", "" + data.getEdit_id());
                                Loger.MSG("getPet_type_id", "" + data.getPet_type_id());
                                Loger.MSG("Otherinfo", "" + data.getOtherinfo());
                                Loger.MSG("Pet_name", "" + data.getPet_name());

                                deleteYourPet(data.getEdit_id(), position);
                            }

                            @Override
                            public void OnCancel() {

                            }
                        });

            }
        });

        if(position==setGetYourPetsArrayList.size()-1){
           // myPetsFragments.GET_PETDATA(setGetYourPetsArrayList.size());
        }
    }

    @Override
    public int getItemCount() {
        return setGetYourPetsArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img_view, img_edit, img_delete;
        SFNFTextView tv_name, tv_type, tv_year_lbl, tv_month_lbl, tv_gender_lbl, tv_size_lbl, tv_breed_lbl, tv_spayed_or_neutered, tv_friendly;
        SFNFBoldTextView tv_year, tv_month, tv_gender, tv_size, tv_breed_value, tv_spayed_or_neutered_value, tv_friendly_value;


        public MyViewHolder(View itemView) {
            super(itemView);
            img_view = (ImageView) itemView.findViewById(R.id.img_view);
            img_edit = (ImageView) itemView.findViewById(R.id.img_edit);
            img_delete = (ImageView) itemView.findViewById(R.id.img_delete);

            tv_name = (SFNFTextView) itemView.findViewById(R.id.tv_name);
            tv_spayed_or_neutered = (SFNFTextView) itemView.findViewById(R.id.tv_spayed_or_neutered);
            tv_friendly = (SFNFTextView) itemView.findViewById(R.id.tv_friendly);
            tv_year_lbl = (SFNFTextView) itemView.findViewById(R.id.tv_year_lbl);
            tv_month_lbl = (SFNFTextView) itemView.findViewById(R.id.tv_month_lbl);
            tv_gender_lbl = (SFNFTextView) itemView.findViewById(R.id.tv_gender_lbl);
            tv_size_lbl = (SFNFTextView) itemView.findViewById(R.id.tv_size_lbl);
            tv_breed_lbl = (SFNFTextView) itemView.findViewById(R.id.tv_breed_lbl);

            tv_type = (SFNFTextView) itemView.findViewById(R.id.tv_type);
            tv_year = (SFNFBoldTextView) itemView.findViewById(R.id.tv_year);
            tv_month = (SFNFBoldTextView) itemView.findViewById(R.id.tv_month);
            tv_gender = (SFNFBoldTextView) itemView.findViewById(R.id.tv_gender);
            tv_size = (SFNFBoldTextView) itemView.findViewById(R.id.tv_size);
            tv_breed_value = (SFNFBoldTextView) itemView.findViewById(R.id.tv_breed_value);

            tv_spayed_or_neutered_value = (SFNFBoldTextView) itemView.findViewById(R.id.tv_spayed_or_neutered_value);
            tv_friendly_value = (SFNFBoldTextView) itemView.findViewById(R.id.tv_friendly_value);
        }
    }

    public void deleteYourPet(String delId, final int position) {
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
        setGetAPIPostData.setPARAMS("DelId");
        setGetAPIPostData.setValues(delId);
        params.add(setGetAPIPostData);

        /////////delete here and api fire////////////////

        new CustomJSONParser().APIForGetMethod(context,AppConstant.BASEURL + "app_users_deletePetprofile?", params, new CustomJSONParser.JSONResponseInterface() {
            @Override
            public void OnSuccess(String Result) {
                appLoader.Dismiss();

                try {

                    JSONObject jObject = new JSONObject(Result);
                    Loger.MSG("DeletejObject", "" + jObject);

                    if (jObject.getBoolean("response")) {
                        setGetYourPetsArrayList.remove(position);
                        //notifyDataSetChanged();
                        notifyItemRemoved(position);

                        if (setGetYourPetsArrayList.size()==0){
                            myPetsFragments.tv_empty.setVisibility(View.VISIBLE);
                            myPetsFragments.recyclerView.setVisibility(View.VISIBLE);
                        }
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
                if (Error.equalsIgnoreCase(context.getResources().getString(R.string.please_check_your_internet_connection))){
                    Toast.makeText(context,Error,Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
