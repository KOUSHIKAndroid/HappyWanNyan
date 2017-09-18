package com.happywannyan.Utils;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Window;
import android.widget.EditText;

import com.happywannyan.R;
import com.stripe.android.model.Card;

/**
 * Created by apple on 24/08/17.
 */

public class AddCreditCard {
    Context mContext;
    Dialog dialog;

    public AddCreditCard(Context mContext) {
        this.mContext = mContext;
        dialog = new Dialog(mContext);
    }

    public interface OnCradListener {
        void OnAddComplete(String data);

        void OnCancel();
    }

    public void AddNewOnClick(OnCradListener onCradListener) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.add_card_layout);

        Card card = new Card("4242424242424242", 12, 2018, "123");
        if (!card.validateCard()) {
            // Do not continue token creation.
        }

        EditText Month = (EditText) dialog.findViewById(R.id.month);
        EditText cardNumber = (EditText) dialog.findViewById(R.id.cardNumber);
        cardNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        dialog.show();


    }

}
