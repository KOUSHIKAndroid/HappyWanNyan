package com.happywannyan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.happywannyan.Activities.BaseActivity;
import com.happywannyan.Activities.MessageDetailsPage;
import com.happywannyan.Constant.AppContsnat;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.Fragments.Message_Fragment;
import com.happywannyan.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by su on 5/22/17.
 */

public class Adapter_message extends RecyclerView.Adapter<Adapter_message.MyViewHolder> {
    ArrayList<JSONObject> MessageList;
    Context context;
    int from = 0;
    public int nextData = 1;
    Message_Fragment message_fragment;

    public Adapter_message(Context context, Message_Fragment message_fragment, ArrayList<JSONObject> MessageList) {
        this.context = context;
        this.message_fragment = message_fragment;
        this.MessageList = MessageList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        try {
            JSONObject object = MessageList.get(position);
            holder.Title.setText(MessageList.get(position).getString("message_type"));
            holder.tv_name.setText(MessageList.get(position).getString("usersname"));
            holder.tv_details.setText(MessageList.get(position).getString("message_info"));
            holder.left_red_view.setText(MessageList.get(position).getString("time_difference") + " ago");
            Glide.with(context).load(object.getString("usersimage")).into(holder.img_view);

            if (object.has("booking_id") && object.getString("booking_id").length() > 0) {
                String BookingID = "<html><b>" + context.getString(R.string.BookingId) + "</b> " + object.getString("booking_id") + "</html>";
                holder.tv_booking_id.setText(Html.fromHtml(BookingID));
                holder.tv_booking_id.setVisibility(View.VISIBLE);
            } else {
                holder.tv_booking_id.setVisibility(View.GONE);
            }

            if (object.has("message_status") && !object.getString("message_status").equals("")) {
                holder.CARD_STATUS.setCardBackgroundColor(Color.parseColor(object.getString("color_code")));
                holder.TXT_MSG_STATUS.setText(object.getString("message_status"));
                holder.CARD_STATUS.setVisibility(View.VISIBLE);
            }else {
                holder.CARD_STATUS.setVisibility(View.GONE);
            }


            holder.Item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        Message_Fragment.MESSAGECODE = MessageList.get(position).getString("message_type_code");
                        Message_Fragment.TAGNAME = MessageList.get(position).getString("message_type");
                        Intent intent = new Intent(context, MessageDetailsPage.class);
                        intent.putExtra("message_id", MessageList.get(position).getString("message_id"));
                        if (!AppContsnat.UserId.equals(MessageList.get(position).getString("receiver_id")))
                            intent.putExtra("receiver_id", MessageList.get(position).getString("receiver_id"));
                        else
                            intent.putExtra("receiver_id", MessageList.get(position).getString("sender_id"));
                        intent.putExtra("usersname", MessageList.get(position).getString("usersname"));
                        intent.putExtra("usersimage", MessageList.get(position).getString("usersimage"));
                        if(context instanceof BaseActivity)
                        {
                            (message_fragment).CallDetailsPage(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });

            if (position == MessageList.size() - 1 &&
                    MessageList.size() % 10 == 0
                    && MessageList.size() >= 10
                    && nextData == 1) {

                ///////////lazy load here called///////
                from = from + 10;
                //message_fragment.loadList(""+from);
                message_fragment.loadList(String.valueOf(from));
            }

        } catch (JSONException e) {

        }
    }


    @Override
    public int getItemCount() {
        return MessageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img_view, img_walking;

        SFNFTextView Title, tv_name, tv_details, left_red_view, tv_booking_id, TXT_MSG_STATUS;
        View Item;
        CardView CARD_STATUS;

        public MyViewHolder(View itemView) {
            super(itemView);

            Title = (SFNFTextView) itemView.findViewById(R.id.tv_title);
            tv_booking_id = (SFNFTextView) itemView.findViewById(R.id.tv_booking_id);
            tv_name = (SFNFTextView) itemView.findViewById(R.id.tv_name);
            tv_details = (SFNFTextView) itemView.findViewById(R.id.tv_details);
            left_red_view = (SFNFTextView) itemView.findViewById(R.id.tv_days);
            TXT_MSG_STATUS = (SFNFTextView) itemView.findViewById(R.id.TXT_MSG_STATUS);
            img_view = (ImageView) itemView.findViewById(R.id.img_view);
            CARD_STATUS = (CardView) itemView.findViewById(R.id.CARD_STATUS);
            Item = itemView;

        }
    }
}
