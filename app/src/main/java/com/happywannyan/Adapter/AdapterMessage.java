package com.happywannyan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.happywannyan.Activities.BaseActivity;
import com.happywannyan.Activities.MessageDetailsPageActivity;
import com.happywannyan.Constant.AppContsnat;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.Fragments.MessageFragment;
import com.happywannyan.POJO.MessageDataType;
import com.happywannyan.R;
import com.happywannyan.Utils.ColorCircleDrawable;
import com.happywannyan.Utils.helper.ObservableHorizontalScrollView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by su on 5/22/17.
 */

public class AdapterMessage extends RecyclerView.Adapter<AdapterMessage.MyViewHolder> {
    ArrayList<MessageDataType> MessageList;
    Context context;
    int from = 0;
    int Screenwidth = 0;
    int PrevPoint = -1;
    public int nextData = 1;
    MessageFragment message_fragment;

    public AdapterMessage(Context context, MessageFragment message_fragment, ArrayList<MessageDataType> MessageList) {
        this.context = context;
        this.message_fragment = message_fragment;
        this.MessageList = MessageList;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        message_fragment.getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        Screenwidth = displayMetrics.widthPixels;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {

//holder.MAINCONTENT.setLayoutParams(new ViewGroup.LayoutParams(Screenwidth,ViewGroup.LayoutParams.WRAP_CONTENT));
            holder.MAINCONTENT.setLayoutParams(new LinearLayout.LayoutParams(Screenwidth, ViewGroup.LayoutParams.WRAP_CONTENT));


            final JSONObject object = MessageList.get(position).getJsonObject();
            holder.tv_name.setText(object.getString("usersname").trim());
            holder.tv_details.setText(object.getString("message_info").trim());
            holder.left_red_view.setText(object.getString("time_difference").trim());
            Glide.with(context).load(object.getString("usersimage").trim()).into(holder.img_view);

//            if (object.has("booking_id") && object.getString("booking_id").length() > 0) {
//                String BookingID = "<html><b>" + context.getString(R.string.BookingId) + "</b> " + object.getString("booking_id") + "</html>";
//                holder.tv_booking_id.setText(Html.fromHtml(BookingID));
//                holder.tv_booking_id.setVisibility(View.VISIBLE);
//            } else {
//                holder.tv_booking_id.setVisibility(View.GONE);
//            }

            if (object.has("unread_msg_count") && object.getInt("unread_msg_count") > 0 && object.getString("color_code").trim().equals("#ac2925")) {
                //holder.TXT_MSG_STATUS.setBackgroundColor(Color.parseColor(object.getString("color_code").trim()));
                holder.TXT_MSG_STATUS.setText("" + object.getInt("unread_msg_count"));
                holder.TXT_MSG_STATUS.setVisibility(View.VISIBLE);

//                ShapeDrawable footerBackground = new ShapeDrawable();
//
//// The corners are ordered top-left, top-right, bottom-right,
//// bottom-left. For each corner, the array contains 2 values, [X_radius,
//// Y_radius]
//                float[] radii = new float[8];
//                radii[0] = 8;
//                radii[1] = 8;
//
//                radii[2] = 8;
//                radii[3] = 8;
//
//                footerBackground.setShape(new RoundRectShape(radii, null, null));
//                footerBackground.getPaint().setColor(Color.parseColor(object.getString("color_code").trim()));
//                holder.TXT_MSG_STATUS.setBackgroundDrawable(footerBackground);



//                GradientDrawable shape = new GradientDrawable();
////                shape.setShape(GradientDrawable.OVAL);
//                shape.setCornerRadii(new float[] { 25, 25, 25, 25, 25, 25, 25, 25 });
//                shape.setColor(Color.parseColor(object.getString("color_code").trim()));
//                shape.setStroke(3, Color.parseColor(object.getString("color_code").trim()));
//                holder.LL_STATUS.setBackground(shape);

                holder.TXT_MSG_STATUS.setBackground(new ColorCircleDrawable(Color.parseColor(object.getString("color_code").trim())));

            } else {
                holder.TXT_MSG_STATUS.setVisibility(View.GONE);
            }


//            if (MessageList.get(position).isScrooll()) {
//                holder.H_SCROLL.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
//            } else {
//                holder.H_SCROLL.smoothScrollTo((0), holder.H_SCROLL.getScrollY());
//            }
//
//            holder.H_SCROLL.setOnScrollListener(new ObservableHorizontalScrollView.OnScrollListener() {
//                @Override
//                public void onScrollChanged(ObservableHorizontalScrollView scrollView, int x, int y, int oldX, int oldY) {
//
//
//                }
//
//                @Override
//                public void onEndScroll(ObservableHorizontalScrollView scrollView) {
//                    if (PrevPoint >= 0) {
//                        MessageList.get(PrevPoint).setScrooll(false);
//                        MessageList.get(position).setScrooll(true);
//                        PrevPoint = position;
//                    } else
//                    {
//                        MessageList.get(position).setScrooll(true);
//                        PrevPoint = position;
//                    }
//
//                    notifyDataSetChanged();
//
//                }
//            });


            holder.MAINCONTENT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        if (object.getInt("unread_msg_count")>0) {
                            if (AppContsnat.message_count>object.getInt("unread_msg_count")) {
                                AppContsnat.message_count = AppContsnat.message_count - object.getInt("unread_msg_count");
                            }
                        }
                        MessageFragment.MESSAGECODE = object.getString("message_type_code").trim();
                        MessageFragment.TAGNAME = object.getString("message_type").trim();
                        Intent intent = new Intent(context, MessageDetailsPageActivity.class);
                        intent.putExtra("message_id", object.getString("parent_id").trim());
                        if (!AppContsnat.UserId.equals(object.getString("receiver_id").trim()))
                            intent.putExtra("receiver_id", object.getString("receiver_id").trim());
                        else
                            intent.putExtra("receiver_id", object.getString("sender_id").trim());
                        intent.putExtra("usersname", object.getString("usersname").trim());
                        intent.putExtra("usersimage", object.getString("usersimage").trim());
                        if (context instanceof BaseActivity) {
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
        ImageView img_view, IMG_DELETE;
        SFNFTextView tv_name, tv_details, left_red_view, TXT_MSG_STATUS;
        View Item;
        LinearLayout MAINCONTENT;
        ObservableHorizontalScrollView H_SCROLL;

        public MyViewHolder(View itemView) {
            super(itemView);
            H_SCROLL = (ObservableHorizontalScrollView) itemView.findViewById(R.id.H_SCROLL);
            IMG_DELETE = (ImageView) itemView.findViewById(R.id.IMG_DELETE);
            tv_name = (SFNFTextView) itemView.findViewById(R.id.tv_name);
            tv_details = (SFNFTextView) itemView.findViewById(R.id.tv_details);
            left_red_view = (SFNFTextView) itemView.findViewById(R.id.tv_days);
            TXT_MSG_STATUS = (SFNFTextView) itemView.findViewById(R.id.TXT_MSG_STATUS);
            img_view = (ImageView) itemView.findViewById(R.id.img_view);
            MAINCONTENT = (LinearLayout) itemView.findViewById(R.id.MAINCONTENT);
            Item = itemView;

        }
    }
}
