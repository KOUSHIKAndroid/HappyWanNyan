package com.happywannyan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.happywannyan.Constant.AppConstant;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.R;
import com.happywannyan.Utils.Loger;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * Created by apple on 20/07/17.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    Context mContext;
    JSONArray Array;
    String USerTime = "", SenderTime = "";


    public MessageAdapter(Context mContext, JSONArray array) {
        this.mContext = mContext;
        Array = array;
        Loger.MSG("Array_length", "" + Array.length());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.message_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            JSONObject ItemObject = Array.getJSONObject(position);
            holder.TXT_date.setText(ItemObject.getString("date"));
            JSONArray DATAARY = ItemObject.getJSONArray("info");

            for (int i = 0; i < DATAARY.length(); i++) {

                final JSONObject MsgItem = DATAARY.getJSONObject(i);
                Loger.MSG("MsgItem", "" + MsgItem);

                if (MsgItem.has("sender_id") && MsgItem.getString("sender_id").equals(AppConstant.UserId)) {

                    View UserItem = LayoutInflater.from(mContext).inflate(R.layout.msg_item_user, null);

                    SFNFTextView TXT_User_Time = (SFNFTextView) UserItem.findViewById(R.id.TXT_User_Time);
                    SFNFTextView TXT_User_Message = (SFNFTextView) UserItem.findViewById(R.id.TXT_User_Message);
                    ImageView IMG_User_Attach = (ImageView) UserItem.findViewById(R.id.IMG_User_Attach);
                    ImageView IMG_User_MAP = (ImageView) UserItem.findViewById(R.id.IMG_User_MAP);


                    if (!USerTime.equals(MsgItem.getString("postedon"))) {
                        String[] words = MsgItem.getString("postedon").split("\\s");
                        TXT_User_Time.setText(words[words.length - 2] + " " + words[words.length - 1]);
                        USerTime = MsgItem.getString("postedon");
                    } else {
                        TXT_User_Time.setVisibility(View.GONE);
                    }

                    if (MsgItem.getString("msg_lat").length() > 0 && MsgItem.getString("msg_long").length() > 0) {

                        if (!MsgItem.getString("url_location").equals("")) {
                            Picasso.with(mContext).load(MsgItem.getString("url_location")).into(IMG_User_MAP);
                        }
//                        Glide.with(mContext).load(MsgItem.getString("url_location")).override(600, 600).diskCacheStrategy(DiskCacheStrategy.ALL).into(IMG_User_MAP);
//                        Glide.with(mContext).load("https://maps.googleapis.com/maps/api/staticmap?center=22.585072500000003,88.49047265625003&zoom=13&size=200x200&markers=%@&key=AIzaSyDAS-0Wh-K3QII2h7DgO8bd-f1dSy4lW3M").override(600, 600).diskCacheStrategy(DiskCacheStrategy.ALL).into(IMG_User_MAP);
                        IMG_User_MAP.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String uri = "";
                                try {
                                    uri = String.format(Locale.ENGLISH, "geo:%f,%f", Float.parseFloat(MsgItem.getString("msg_lat")), Float.parseFloat(MsgItem.getString("msg_long")));
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                    intent.setPackage("com.google.android.apps.maps");
                                    mContext.startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    } else {
                        IMG_User_MAP.setVisibility(View.GONE);
                    }


                    if (MsgItem.getString("message_info").equals("")) {

                        if (MsgItem.getString("message_attachment").endsWith(".doc") || MsgItem.getString("message_attachment").endsWith(".pdf")) {
                            TXT_User_Message.setVisibility(View.VISIBLE);
                            TXT_User_Message.setText(MsgItem.getString("message_attachment"));
                        } else {
                            TXT_User_Message.setVisibility(View.GONE);
                            if (!MsgItem.getString("message_attachment").trim().equals("")) {
                                Glide.with(mContext).load(MsgItem.getString("message_attachment").trim()).override(600, 600).diskCacheStrategy(DiskCacheStrategy.ALL).into(IMG_User_Attach);
                            }
                        }

                    } else {
                        IMG_User_Attach.setVisibility(View.GONE);
                        TXT_User_Message.setText(MsgItem.getString("message_info"));
                    }


                    holder.LLMain.addView(UserItem);


                } else {
                    View SenderItem = LayoutInflater.from(mContext).inflate(R.layout.msg_item_sender, null);

                    SFNFTextView TXT_Sender_message = (SFNFTextView) SenderItem.findViewById(R.id.TXT_Sender_message);
                    SFNFTextView TXT_sender_name_time = (SFNFTextView) SenderItem.findViewById(R.id.TXT_sender_name_time);
                    ImageView IMG_Sender_Attach = (ImageView) SenderItem.findViewById(R.id.IMG_Sender_Attach);
                    ImageView IMG_SenderUser = (ImageView) SenderItem.findViewById(R.id.IMG_SenderUser);
                    ImageView IMG_Sender_Map = (ImageView) SenderItem.findViewById(R.id.IMG_Sender_Map);
                    RelativeLayout RL_Sender = (RelativeLayout) SenderItem.findViewById(R.id.RL_Sender);

                    Loger.MSG("@@ ITEM ", i + " - " + MsgItem);


                    if (MsgItem.getString("msg_lat").length() > 0 && MsgItem.getString("msg_long").length() > 0) {
                        Picasso.with(mContext).load(MsgItem.getString("url_location")).into(IMG_Sender_Map);

//                        Glide.with(mContext).load(MsgItem.getString("url_location")).override(600, 600).diskCacheStrategy(DiskCacheStrategy.ALL).into(IMG_Sender_Map);
                        IMG_Sender_Map.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String uri = "";
                                try {
                                    uri = String.format(Locale.ENGLISH, "geo:%f,%f", MsgItem.getString("msg_lat"), MsgItem.getString("msg_long"));
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                    mContext.startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    } else {
                        IMG_Sender_Map.setVisibility(View.GONE);
                    }


                    if (!SenderTime.equals(MsgItem.getString("postedon"))) {
                        String[] words = MsgItem.getString("postedon").split("\\s");
                        TXT_sender_name_time.setText(words[words.length - 2] + " " + words[words.length - 1]);
                        SenderTime = MsgItem.getString("postedon");
                        if (!MsgItem.getString("usersimage").trim().equals("")) {
                            Glide.with(mContext).load(MsgItem.getString("usersimage").trim()).override(600, 600).diskCacheStrategy(DiskCacheStrategy.ALL).into(IMG_SenderUser);
                        }
                    } else {
                        RL_Sender.setVisibility(View.GONE);
                        TXT_sender_name_time.setVisibility(View.GONE);
                    }


                    if (MsgItem.getString("message_info").equals("")) {

                        if (MsgItem.getString("message_attachment").endsWith(".doc") || MsgItem.getString("message_attachment").endsWith(".pdf")) {
                            TXT_Sender_message.setVisibility(View.VISIBLE);
                            TXT_Sender_message.setText(MsgItem.getString("message_attachment"));
                        } else {
                            TXT_Sender_message.setVisibility(View.GONE);
                            if (!MsgItem.getString("message_attachment").trim().equals("")) {
                                Glide.with(mContext).load(MsgItem.getString("message_attachment").trim()).override(600, 600).diskCacheStrategy(DiskCacheStrategy.ALL).into(IMG_Sender_Attach);
                            }
                        }

                    } else {
                        IMG_Sender_Attach.setVisibility(View.GONE);
                        TXT_Sender_message.setText(MsgItem.getString("message_info"));
                    }


                    holder.LLMain.addView(SenderItem);
                }
                Loger.MSG("info_size", "" + DATAARY.length());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return Array.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SFNFTextView TXT_date;
        RelativeLayout RL_Date;
        LinearLayout LLMain;

        public ViewHolder(View itemView) {
            super(itemView);
            TXT_date = (SFNFTextView) itemView.findViewById(R.id.TXT_date);

            RL_Date = (RelativeLayout) itemView.findViewById(R.id.RL_Date);
            LLMain = (LinearLayout) itemView.findViewById(R.id.LLMain);
        }
    }
}
