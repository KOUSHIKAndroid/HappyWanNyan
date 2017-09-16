package com.happywannyan.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.POJO.SetGetCards;
import com.happywannyan.R;
import com.happywannyan.SitterBooking.BookingFragmentFoure;
import com.happywannyan.Utils.AppLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by apple on 10/08/17.
 */

public class AdapterCard extends RecyclerView.Adapter<AdapterCard.CardViewHolder> {
    Context mContext;
    AppLoader appLoader;
    BookingFragmentFoure.onClickItem onClickItem;
    ArrayList<SetGetCards> setGetCardsArrayList;

    public AdapterCard(Context context, ArrayList<SetGetCards> setGetCardsArrayList, BookingFragmentFoure.onClickItem onClickItem) {
        this.mContext = context;
        appLoader = new AppLoader(mContext);
        this.setGetCardsArrayList = setGetCardsArrayList;
        this.onClickItem = onClickItem;
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
            final JSONObject OB = setGetCardsArrayList.get(0).getStripDataArrayList().get(position).getJsonObjectUserStripeData();

            for (int j = 0; j < setGetCardsArrayList.get(0).getJsonArrayCardDetails().length(); j++) {
                JSONObject jj = setGetCardsArrayList.get(0).getJsonArrayCardDetails().getJSONObject(j);
                if (jj.getString("card_name").equals(OB.getString("card_brand"))) {
                    Glide.with(mContext).load(jj.getString("card_image")).into(holder.IMG_VISA);
                    break;
                }
            }
            holder.TXT_CardHolderName.setText(OB.getString("name_on_card"));
            holder.TXT_CradName.setText("**** **** **** " + OB.getString("card_last_digits"));
            if (OB.getString("is_default").equalsIgnoreCase("1")) {
                holder.tv_default_status.setVisibility(View.VISIBLE);
            } else {
                holder.tv_default_status.setVisibility(View.GONE);
            }
            if (setGetCardsArrayList.get(0).getStripDataArrayList().get(position).isCheck()) {
                holder.img_selection.setImageResource(R.drawable.ic_check_checked);
            } else {
                holder.img_selection.setImageResource(R.drawable.ic_check_box_unchecked);
            }

            holder.totalView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int p = 0; p < setGetCardsArrayList.get(0).getStripDataArrayList().size(); p++) {
                        setGetCardsArrayList.get(0).getStripDataArrayList().get(p).setCheck(false);
                    }
                    onClickItem.onSelectItemClick(position, setGetCardsArrayList.get(0).getStripDataArrayList().get(position).getJsonObjectUserStripeData());
                    setGetCardsArrayList.get(0).getStripDataArrayList().get(position).setCheck(true);
                    notifyDataSetChanged();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
//        }

    }

    @Override
    public int getItemCount() {
        return setGetCardsArrayList.get(0).getStripDataArrayList().size();
    }

    class CardViewHolder extends RecyclerView.ViewHolder {
        ImageView IMG_VISA, img_selection;
        SFNFTextView TXT_CardHolderName, TXT_CradName, tv_default_status;

        View totalView;

        public CardViewHolder(View itemView) {
            super(itemView);
            IMG_VISA = (ImageView) itemView.findViewById(R.id.IMG_VISA);
            img_selection = (ImageView) itemView.findViewById(R.id.img_selection);
            TXT_CardHolderName = (SFNFTextView) itemView.findViewById(R.id.TXT_CardHolderName);
            TXT_CradName = (SFNFTextView) itemView.findViewById(R.id.TXT_CradName);
            tv_default_status = (SFNFTextView) itemView.findViewById(R.id.tv_default_status);
            totalView = itemView;
        }
    }
}
