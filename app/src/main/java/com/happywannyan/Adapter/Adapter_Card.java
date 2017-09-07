package com.happywannyan.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.happywannyan.Constant.AppContsnat;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.POJO.APIPOSTDATA;
import com.happywannyan.R;
import com.happywannyan.SitterBooking.BookingFragmentFoure;
import com.happywannyan.Utils.AppLoader;
import com.happywannyan.Utils.JSONPerser;
import com.happywannyan.Utils.Loger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by apple on 10/08/17.
 */

public class Adapter_Card extends RecyclerView.Adapter<Adapter_Card.CardViewHolder> {
    Context mContext;
    JSONObject MainObject;
    JSONArray ARRY;
    AppLoader Loader;
    BookingFragmentFoure.onClickItem onClickItem;
    JSONArray Crad;

    public Adapter_Card(Context context, String Response, BookingFragmentFoure.onClickItem onClickItem) {
        this.mContext = context;
        Loader = new AppLoader(mContext);
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
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.card_layout, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, final int position) {
//        for(int i=0;i<ARRY.length();i++){
        try {
            final JSONObject OB = ARRY.getJSONObject(position);


            for (int j = 0; j < Crad.length(); j++) {
                JSONObject jj = Crad.getJSONObject(j);
                if (jj.getString("card_name").equals(OB.getString("card_brand"))) {
                    Glide.with(mContext).load(jj.getString("card_image")).into(holder.IMG_VISA);
                    break;
                }
            }
            holder.TXT_CardHolderName.setText(OB.getString("name_on_card"));
            holder.TXT_CradName.setText("**** **** **** " + OB.getString("card_last_digits"));
            if (OB.getString("is_default").equalsIgnoreCase("1")) {
                holder.img_selection.setImageResource(R.drawable.ic_circle_field);
            } else {
                holder.img_selection.setImageResource(R.drawable.ic_circle);
            }

            holder.totalView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {
                        selectionCard(OB.getString("id"), position, OB);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
//        }

    }

    @Override
    public int getItemCount() {
        return ARRY.length();
    }

    class CardViewHolder extends RecyclerView.ViewHolder {
        ImageView IMG_VISA, img_selection;
        SFNFTextView TXT_CardHolderName, TXT_CradName;

        View totalView;

        public CardViewHolder(View itemView) {
            super(itemView);
            IMG_VISA = (ImageView) itemView.findViewById(R.id.IMG_VISA);
            img_selection = (ImageView) itemView.findViewById(R.id.img_selection);
            TXT_CardHolderName = (SFNFTextView) itemView.findViewById(R.id.TXT_CardHolderName);
            TXT_CradName = (SFNFTextView) itemView.findViewById(R.id.TXT_CradName);
            totalView = itemView;
        }
    }

    public void selectionCard(String default_card_id, final int position, final JSONObject OB) {
        Loader.Show();
        new JSONPerser().API_FOR_GET(AppContsnat.BASEURL + "make_card_default?user_id=" + AppContsnat.UserId + "&default_card_id=" + default_card_id
                , new ArrayList<APIPOSTDATA>(), new JSONPerser.JSONRESPONSE() {
                    @Override
                    public void OnSuccess(String Result) {
                        Loader.Dismiss();
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
                        Loader.Dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(Response);
                            Toast.makeText(mContext, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void OnError(String Error) {
                        Loader.Dismiss();

                    }
                });
    }
}
