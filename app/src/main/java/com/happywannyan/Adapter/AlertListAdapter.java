package com.happywannyan.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.R;
import com.happywannyan.Utils.MYAlert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by su on 5/22/17.
 */

public class AlertListAdapter extends RecyclerView.Adapter<AlertListAdapter.MyViewHolder> {
    JSONArray MessageList;
    Context context;
    AlertDialog Dialog;
    MYAlert.OnSingleListTextSelected onSingleListTextSelected;
    String params;
    MYAlert myAlert;

    public AlertListAdapter(MYAlert myAlert, Context mContext, MYAlert.OnSingleListTextSelected onSingleListTextSelected, AlertDialog dialog, JSONArray listArray, String getPramsName) {
        this.context = mContext;
        this.MessageList = listArray;
        this.Dialog = dialog;
        this.myAlert = myAlert;
        this.onSingleListTextSelected = onSingleListTextSelected;
        this.params = getPramsName;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_text, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        try {
            JSONObject data2 = MessageList.getJSONObject(position);
            holder.Title.setText(data2.getString(params));
            holder.Title.setTag(data2);
            holder.Title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {
                        onSingleListTextSelected.OnSelectedTEXT(new JSONObject(view.getTag().toString()));
                        myAlert.dismissed();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {

        }
    }


    @Override
    public int getItemCount() {
        return MessageList.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        SFNFTextView Title;

        public MyViewHolder(View itemView) {
            super(itemView);

            Title = (SFNFTextView) itemView.findViewById(R.id.TXT_item);


        }
    }
}
