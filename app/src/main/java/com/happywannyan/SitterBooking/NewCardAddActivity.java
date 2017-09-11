package com.happywannyan.SitterBooking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.R;
import com.happywannyan.Utils.Loger;

/**
 * Created by su on 9/11/17.
 */

public class NewCardAddActivity extends AppCompatActivity {
    boolean defaultValue = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card);

        ((AppCompatCheckBox) findViewById(R.id.checkbox_card_default)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                defaultValue=isChecked;
            }
        });
        findViewById(R.id.Card_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCardValidation();
            }
        });
    }

    public void checkCardValidation(){

        if (((EditText) findViewById(R.id.edtxt_card_name)).getText().toString().trim().equals("")) {
            ((TextInputLayout) findViewById(R.id.input_txt_card_name)).setErrorEnabled(true);
            ((TextInputLayout) findViewById(R.id.input_txt_card_name)).setError("Please Enter card holder name");
        } else {
            ((TextInputLayout) findViewById(R.id.input_txt_card_name)).setErrorEnabled(false);
            if (((EditText) findViewById(R.id.edtxt_card_number)).getText().toString().trim().equals("")) {
                ((TextInputLayout) findViewById(R.id.input_txt_card_number)).setErrorEnabled(true);
                ((TextInputLayout) findViewById(R.id.input_txt_card_number)).setError("Please Enter card holder number");
            } else if (((EditText) findViewById(R.id.edtxt_card_number)).getText().toString().trim().length() < 14) {
                ((TextInputLayout) findViewById(R.id.input_txt_card_number)).setErrorEnabled(true);
                ((TextInputLayout) findViewById(R.id.input_txt_card_number)).setError("Please Enter correct card number");
            } else {
                ((TextInputLayout) findViewById(R.id.input_txt_card_number)).setErrorEnabled(false);
                if (((SFNFTextView) findViewById(R.id.tv_month)).getText().toString().trim().equals("00")) {
                    Toast.makeText(getApplicationContext(), "Please choose month", Toast.LENGTH_SHORT).show();
                } else {
                    if (((SFNFTextView) findViewById(R.id.tv_year)).getText().toString().trim().equals("00")) {
                        Toast.makeText(getApplicationContext(), "Please choose year", Toast.LENGTH_SHORT).show();
                    } else {

                        if (((EditText) findViewById(R.id.edt_cvv)).getText().toString().trim().equals("")) {
                            ((TextInputLayout) findViewById(R.id.input_cvv)).setErrorEnabled(true);
                            ((TextInputLayout) findViewById(R.id.input_cvv)).setError("Please enter CVV");
                        } else {
                            ((TextInputLayout) findViewById(R.id.input_cvv)).setErrorEnabled(false);

                            if (((EditText) findViewById(R.id.edt_cvv)).getText().toString().trim().length() < 3) {
                                ((TextInputLayout) findViewById(R.id.input_cvv)).setErrorEnabled(true);
                                ((TextInputLayout) findViewById(R.id.input_cvv)).setError("Please enter correct CVV");
                            } else {
                                ((TextInputLayout) findViewById(R.id.input_cvv)).setErrorEnabled(false);
                                ///////////////submit task////////////////////////////////////

                                Loger.MSG("finish","Yes");

                            }
                        }
                    }
                }
            }
        }
    }
}
