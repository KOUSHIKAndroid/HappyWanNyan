package com.happywannyan.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.happywannyan.Adapter.Adapter_AlertList;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.R;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by apple on 22/05/17.
 */

public class MYAlert {
    Context mContext;
    AlertDialog Dialog;

    public void dismised() {
        Dialog.dismiss();
    }

    public interface OnlyMessage {
        public void OnOk(boolean res);
    }
    public interface  OnOkCancel{
        void OnOk();
        void  OnCancel();
    }

    public interface OnSignleListTextSelected {
        public void OnSelectedTEXT(JSONObject jsonObject);
    }

    public interface OnEditTexSubmit {
        void OnEditSubmit(String Messge);

        void OnCancel(boolean cancel);
    }

    public MYAlert(Context mContext) {
        this.mContext = mContext;
    }

    public void AlertOnly(String Title, String Message, final OnlyMessage onlyMessage) {
        AlertDialog.Builder alertbuilder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View LayView = inflater.inflate(R.layout.alert_dialog_msg, null);
        SFNFTextView TXTMessage = (SFNFTextView) LayView.findViewById(R.id.Message);
        TXTMessage.setText(Message);
        SFNFTextView TXTTitle = (SFNFTextView) LayView.findViewById(R.id.Title);
        TXTTitle.setText(Title);
        Button BTN_OK = (Button) LayView.findViewById(R.id.BTN_OK);
        BTN_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onlyMessage.OnOk(true);
                Dialog.dismiss();
            }
        });
        alertbuilder.setView(LayView);
        Dialog = alertbuilder.create();
        Dialog.show();

    }

    public void AlertOkCancel(String Title, String Message, final OnOkCancel onlyMessage) {
        AlertDialog.Builder alertbuilder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View LayView = inflater.inflate(R.layout.alert_dialog_ok_cancel, null);
        SFNFTextView TXTMessage = (SFNFTextView) LayView.findViewById(R.id.Message);
        TXTMessage.setText(Message);
        SFNFTextView TXTTitle = (SFNFTextView) LayView.findViewById(R.id.Title);
        TXTTitle.setText(Title);
        Button BTN_OK = (Button) LayView.findViewById(R.id.BTN_OK);
        Button BTN_CANCEL = (Button) LayView.findViewById(R.id.BTN_CANCEL);

        BTN_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onlyMessage.OnOk();
                Dialog.dismiss();
            }
        });

        BTN_CANCEL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onlyMessage.OnCancel();
                Dialog.dismiss();
            }
        });

        alertbuilder.setView(LayView);
        Dialog = alertbuilder.create();
        Dialog.show();
    }


    public void AlertTextLsit(String Title, JSONArray ListArray, String GetPramsName, final OnSignleListTextSelected onSignleListTextSelected) {
        AlertDialog.Builder alertbuilder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View LayView = inflater.inflate(R.layout.alert_text_list, null);

        SFNFTextView TXTTitle = (SFNFTextView) LayView.findViewById(R.id.Title);
        TXTTitle.setText(Title);
        RecyclerView ListLay = (RecyclerView) LayView.findViewById(R.id.recycler_view);
        ListLay.setLayoutManager(new LinearLayoutManager(mContext));
        ListLay.setAdapter(new Adapter_AlertList(this, mContext, onSignleListTextSelected, Dialog, ListArray, GetPramsName));

        LayView.findViewById(R.id.IMG_Back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onlyMessage.OnOk(true);
                Dialog.dismiss();
            }
        });
        alertbuilder.setView(LayView);
        Dialog = alertbuilder.create();
        Dialog.show();
    }


    public void AlertBoxMessageSend(String Titile,String EditTextName, String ButtonName, final OnEditTexSubmit onEditTexSubmit) {
        AlertDialog.Builder alertbuilder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View LayView = inflater.inflate(R.layout.edit_msg_layout, null);

        SFNFTextView TXTTitle = (SFNFTextView) LayView.findViewById(R.id.Title);
        TXTTitle.setText(Titile);
        Button BTN_OK = (Button) LayView.findViewById(R.id.BTN_OK);
        final EditText EDX_message = (EditText) LayView.findViewById(R.id.EDX_message);
        EDX_message.setHint(EditTextName);
        ImageView IMG_Back = (ImageView) LayView.findViewById(R.id.IMG_Back);
        IMG_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditTexSubmit.OnCancel(true);
                Dialog.dismiss();
            }
        });
        BTN_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(EDX_message.getText().toString().trim().length()>0)
                {
                    onEditTexSubmit.OnEditSubmit(EDX_message.getText().toString());
                    Dialog.dismiss();
                }else {
                    EDX_message.setHintTextColor(Color.RED);
                    EDX_message.requestFocus();
                }
            }
        });

        alertbuilder.setView(LayView);
        Dialog = alertbuilder.create();
        Dialog.show();
    }



    public void AlertForAPIRESPONSE(String Title, String Message, final OnlyMessage onlyMessage) {
        AlertDialog.Builder alertbuilder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View LayView = inflater.inflate(R.layout.alert_dialog_api_response, null);
        SFNFTextView TXTMessage = (SFNFTextView) LayView.findViewById(R.id.Message);
        TXTMessage.setText(Message);
        SFNFTextView TXTTitle = (SFNFTextView) LayView.findViewById(R.id.Title);
        TXTTitle.setText(Title);
        Button BTN_OK = (Button) LayView.findViewById(R.id.BTN_OK);

        BTN_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onlyMessage.OnOk(true);
                Dialog.dismiss();
            }
        });



        alertbuilder.setView(LayView);
        Dialog = alertbuilder.create();
        Dialog.show();
    }

}
