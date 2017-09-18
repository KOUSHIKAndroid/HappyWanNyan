package com.happywannyan.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.happywannyan.Constant.AppConstant;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.POJO.APIPOSTDATA;
import com.happywannyan.R;
import com.happywannyan.SitterBooking.BookingFragmentFoure;
import com.happywannyan.Utils.AppLoader;
import com.happywannyan.Utils.CustomJSONParser;
import com.happywannyan.Utils.Loger;
import com.happywannyan.Utils.MYAlert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by su on 9/7/17.
 */

public class AdapterPaymentList extends RecyclerView.Adapter<AdapterPaymentList.MyViewHolder> {
    Context mContext;
    JSONObject MainObject;
    JSONArray ARRY;
    AppLoader appLoader;
    BookingFragmentFoure.onClickItem onClickItem;
    JSONArray Crad;

    public AdapterPaymentList(Context context, String Response, BookingFragmentFoure.onClickItem onClickItem) {
        this.mContext = context;
        appLoader = new AppLoader(mContext);
        try {
            MainObject = new JSONObject(Response);
            ARRY = MainObject.getJSONArray("user_stripe_data");
            Crad = MainObject.getJSONArray("card_details");
            this.onClickItem = onClickItem;
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public AdapterPaymentList.MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_details_adapter, parent, false);
        return new AdapterPaymentList.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AdapterPaymentList.MyViewHolder holder, final int position) {
        try {
            final JSONObject OB = ARRY.getJSONObject(position);


            for (int j = 0; j < Crad.length(); j++) {
                JSONObject jj = Crad.getJSONObject(j);
                if (jj.getString("card_name").equals(OB.getString("card_brand"))) {
                    Glide.with(mContext).load(jj.getString("card_image")).into(holder.IMG_VISA);
                    break;
                }
            }
            holder.tv_name_on_card.setText(OB.getString("name_on_card"));
            holder.tv_card_number.setText("**** **** **** " + OB.getString("card_last_digits"));
            if (OB.getString("is_default").equalsIgnoreCase("1")) {
                holder.img_default.setImageResource(R.drawable.ic_check_checked);
                holder.tv_default.setText(mContext.getResources().getString(R.string.default2));
            } else {
                holder.img_default.setImageResource(R.drawable.ic_check_box_unchecked);
                holder.tv_default.setText(mContext.getResources().getString(R.string.make_default));
            }

            holder.LLDefault.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        selectionCard(OB.getString("id"), position, OB);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            holder.img_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new MYAlert(mContext).AlertOkCancel("",
                            mContext.getResources().getString(R.string.are_you_want_to_delete), new MYAlert.OnOkCancel() {
                                @Override
                                public void OnOk() {
                                    try {
                                        deleteCard(OB.getString("id"), position);
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

            holder.tv_card_month_date.setText(OB.getString("exp_month") + "/" + OB.getString("exp_year"));
            holder.tv_added_on.setText(OB.getString("added_on").split(" ")[0]);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return ARRY.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img_delete, IMG_VISA, img_default;
        LinearLayout LLDefault;
        SFNFTextView tv_name_on_card, tv_card_number, tv_card_month_date, tv_added_on,tv_default;


        public MyViewHolder(View itemView) {
            super(itemView);

            IMG_VISA = (ImageView) itemView.findViewById(R.id.IMG_VISA);
            img_delete = (ImageView) itemView.findViewById(R.id.img_delete);
            tv_name_on_card = (SFNFTextView) itemView.findViewById(R.id.tv_name_on_card);
            tv_card_number = (SFNFTextView) itemView.findViewById(R.id.tv_card_number);
            tv_card_month_date = (SFNFTextView) itemView.findViewById(R.id.tv_card_month_date);
            tv_added_on = (SFNFTextView) itemView.findViewById(R.id.tv_added_on);
            tv_default = (SFNFTextView) itemView.findViewById(R.id.tv_default);
            img_default = (ImageView) itemView.findViewById(R.id.img_default);
            LLDefault = (LinearLayout) itemView.findViewById(R.id.LLDefault);

        }
    }

    public void selectionCard(String default_card_id, final int position, final JSONObject OB) {
        appLoader.Show();
        new CustomJSONParser().APIForGetMethod(AppConstant.BASEURL + "make_card_default?user_id=" + AppConstant.UserId + "&default_card_id=" + default_card_id
                , new ArrayList<APIPOSTDATA>(), new CustomJSONParser.JSONResponseInterface() {
                    @Override
                    public void OnSuccess(String Result) {
                        appLoader.Dismiss();
                        Loger.MSG("Result-->", Result);
                        try {
                            JSONObject jsonObject = new JSONObject(Result);

                            Toast.makeText(mContext, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                            for (int c = 0; c < ARRY.length(); c++) {
                                JSONObject person = ARRY.getJSONObject(c);
                                person.put("is_default", "0");
                            }
                            JSONObject person = ARRY.getJSONObject(position);
                            person.put("is_default", "1");

                            notifyDataSetChanged();

                            onClickItem.onSelectItemClick(position, OB);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void OnError(String Error, String Response) {
                        appLoader.Dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(Response);
                            Toast.makeText(mContext, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void OnError(String Error) {
                        appLoader.Dismiss();
                    }
                });
    }

    public void deleteCard(String default_card_id, final int position) {
        appLoader.Show();
        new CustomJSONParser().APIForGetMethod(AppConstant.BASEURL + "app_delete_card?user_id=" + AppConstant.UserId + "&del_card_id=" + default_card_id
                , new ArrayList<APIPOSTDATA>(), new CustomJSONParser.JSONResponseInterface() {
                    @Override
                    public void OnSuccess(String Result) {
                        appLoader.Dismiss();
                        Loger.MSG("Result-->", Result);
                        try {
                            JSONObject jsonObject = new JSONObject(Result);
                            Toast.makeText(mContext, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            ARRY.remove(position);
                            notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void OnError(String Error, String Response) {
                        appLoader.Dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(Response);
                            Toast.makeText(mContext, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void OnError(String Error) {
                        appLoader.Dismiss();
                    }
                });
    }
}
