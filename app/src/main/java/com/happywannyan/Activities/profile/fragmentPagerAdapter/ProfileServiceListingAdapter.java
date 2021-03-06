package com.happywannyan.Activities.profile.fragmentPagerAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.happywannyan.Activities.LoginChooserActivity;
import com.happywannyan.Activities.profile.MeetUpWannyanActivity;
import com.happywannyan.Activities.profile.ProfileDetailsActivity;
import com.happywannyan.Constant.AppConstant;
import com.happywannyan.Font.SFNFBoldTextView;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.R;
import com.happywannyan.SitterBooking.BookingOneActivity;
import com.happywannyan.Utils.AppDataHolder;
import com.happywannyan.Utils.Loger;
import com.happywannyan.Utils.MYAlert;
import com.happywannyan.Utils.helper.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by bodhidipta on 22/05/17.
 */

public class ProfileServiceListingAdapter extends RecyclerView.Adapter<ProfileServiceListingAdapter.ViewHolder> {

    private Context mContext = null;
    JSONArray ServiceArray;
    int block_user_status;
    String SitterId;

    public ProfileServiceListingAdapter(Context mContext,String SitterId,JSONArray serviceArray, int block_user_status) {
        this.mContext = mContext;
        this.SitterId=SitterId;
        this.ServiceArray = serviceArray;
        this.block_user_status = block_user_status;
    }

    @Override
    public int getItemCount() {
        return ServiceArray.length();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.profile_service_listing_item_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {

            if (position==ServiceArray.length()-1){
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, 0, new Utils(mContext).dpToPx(65));
                holder.LL_Main.setLayoutParams(params);
            }


            final JSONObject object = ServiceArray.getJSONObject(position);
            holder.Title.setText(object.getString("service_name"));
            holder.Description.setText(object.getString("description"));
            holder.PricePer.setText(object.getString("service_price") + "/" + object.getString("unit_name"));

            if(!object.getString("service_image").trim().equals("")) {
                Glide.with(mContext).load(object.getString("service_image").trim()).into(holder.IMG_SERVICES);
            }


            if (object.getString("description").toString().length() > 70) {
                holder.TXT_ViewMore.setVisibility(View.VISIBLE);
                holder.Description.setMaxLines(2);
                Loger.MSG("@@ LENTH-", holder.Description.getText().length() + "");
            } else {
                holder.TXT_ViewMore.setVisibility(View.GONE);
                holder.Description.setTag("jjjjj");
            }


            holder.TXT_ViewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        new MYAlert(mContext).AlertOnly(object.getString("service_name"), object.getString("description"), new MYAlert.OnlyMessage() {
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

                    new AppConstant((ProfileDetailsActivity)mContext).getShareData(AppDataHolder.UserData, new AppDataHolder.AppSharePreferenceDataInterface() {
                        @Override
                        public void available(boolean available, JSONObject data) {
                            ///////////If login then work/////////////////////////////////////////////////////
                            if (block_user_status == 1) {
                                new MYAlert(mContext).AlertOnly(mContext.getResources().getString(R.string.booknow), mContext.getResources().getString(R.string.unable_to_make_book), new MYAlert.OnlyMessage() {
                                    @Override
                                    public void OnOk(boolean res) {

                                    }
                                });
                            } else {
                                Loger.MSG("object-->",""+object);
                                Intent intent = new Intent(mContext, BookingOneActivity.class);
                                intent.putExtra("LIST", "");
                                intent.putExtra("ItemDetails", "" + ((ProfileDetailsActivity) mContext).PrevJSONObject);
                                intent.putExtra("Single", true);
                                intent.putExtra("SELECT", "" + object);
                                intent.putExtra("SitterId", "" + SitterId);
                                mContext.startActivity(intent);
                            }
                            ///////////End/////////////////////////////////////////////////////
                        }

                        @Override
                        public void notAvailable(String Error) {

                            new MYAlert(mContext).AlertOkCancel("", mContext.getResources().getString(R.string.please_login), mContext.getResources().getString(R.string.ok), mContext.getResources().getString(R.string.cancel), new MYAlert.OnOkCancel() {
                                @Override
                                public void OnOk() {
                                    Intent intent=new Intent(mContext, LoginChooserActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    ((ProfileDetailsActivity)mContext).startActivity(intent);
                                    ((ProfileDetailsActivity)mContext).finish();
                                }

                                @Override
                                public void OnCancel() {

                                }
                            });
                        }
                    });
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        SFNFBoldTextView Title;
        SFNFTextView Description;
        SFNFTextView PricePer, TXT_ViewMore;
        RelativeLayout RL_Book;
        LinearLayout LL_Main;
        ImageView IMG_SERVICES;

        public ViewHolder(View itemView) {
            super(itemView);

            Title = (SFNFBoldTextView) itemView.findViewById(R.id.Title);
            Description = (SFNFTextView) itemView.findViewById(R.id.Description);
            TXT_ViewMore = (SFNFTextView) itemView.findViewById(R.id.TXT_ViewMore);
            PricePer = (SFNFTextView) itemView.findViewById(R.id.PricePer);
            RL_Book = (RelativeLayout) itemView.findViewById(R.id.RL_Book);
            LL_Main = (LinearLayout) itemView.findViewById(R.id.LL_Main);
            IMG_SERVICES = (ImageView) itemView.findViewById(R.id.IMG_SERVICES);

        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

}
