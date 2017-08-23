package com.happywannyan.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by apple on 10/08/17.
 */

public class Adapter_Card extends RecyclerView.Adapter<Adapter_Card.CardViewHolder> {
    Context mContext;
    JSONObject MainObject;
    JSONArray ARRY;


    public Adapter_Card(Context context,String Response) {
        this.mContext=context;
        try {
            MainObject=new JSONObject(Response);
            ARRY=MainObject.getJSONArray("user_stripe_data");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.card_layout,parent,false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        for(int i=0;i<ARRY.length();i++){
            try {
                JSONObject OB=ARRY.getJSONObject(i);
                JSONArray Crad=MainObject.getJSONArray("card_details");

                for(int j=0;j<Crad.length();j++)
                {
                    JSONObject jj=Crad.getJSONObject(j);
                    if(jj.getString("card_name").equals(OB.getString("card_brand")))
                    {
                        Glide.with(mContext).load(jj.getString("card_image")).into(holder.IMG_VISA);
                        break;
                    }
                }
                holder.TXT_CardHolderName.setText(OB.getString("name_on_card"));
                holder.TXT_CradName.setText("**** **** **** "+OB.getString("card_last_digits"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public int getItemCount() {
        return ARRY.length();
    }

    class CardViewHolder extends RecyclerView.ViewHolder{
        ImageView IMG_VISA;
        SFNFTextView TXT_CardHolderName,TXT_CradName;
        public CardViewHolder(View itemView) {
            super(itemView);
            IMG_VISA=(ImageView)itemView.findViewById(R.id.IMG_VISA);
            TXT_CardHolderName=(SFNFTextView)itemView.findViewById(R.id.TXT_CardHolderName);
            TXT_CradName=(SFNFTextView)itemView.findViewById(R.id.TXT_CradName);

        }
    }
}
