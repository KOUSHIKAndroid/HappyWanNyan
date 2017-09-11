package com.happywannyan.SitterBooking;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.happywannyan.Adapter.CustomListAdapter;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.R;
import com.happywannyan.Utils.Loger;

import java.util.ArrayList;

/**
 * Created by su on 9/11/17.
 */

public class NewCardAddActivity extends AppCompatActivity {
    boolean defaultValue = false;
    PopupWindow popupWindow;
    SFNFTextView tv_month,tv_year;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card);

        ((AppCompatCheckBox) findViewById(R.id.checkbox_card_default)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                defaultValue = isChecked;
            }
        });
        findViewById(R.id.Card_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCardValidation();
            }
        });

        findViewById(R.id.tv_month).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow!=null) {
                    popupWindow.dismiss();
                }
                showDialogMonth(view);
            }
        });
        findViewById(R.id.tv_year).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow!=null) {
                    popupWindow.dismiss();
                }
                showDialogYear(view);
            }
        });
        tv_month= (SFNFTextView) findViewById(R.id.tv_month);
        tv_year= (SFNFTextView) findViewById(R.id.tv_year);
    }

    public void checkCardValidation() {

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
                if (((SFNFTextView) findViewById(R.id.tv_month)).getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please choose month", Toast.LENGTH_SHORT).show();
                } else {
                    if (((SFNFTextView) findViewById(R.id.tv_year)).getText().toString().trim().equals("")) {
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

                                Loger.MSG("finish", "Yes");

                            }
                        }
                    }
                }
            }
        }
    }

    private void showDialogMonth(View v) {
        ArrayList<String> monthArrayList=new ArrayList<>();
        monthArrayList.add("01");
        monthArrayList.add("02");
        monthArrayList.add("03");
        monthArrayList.add("04");
        monthArrayList.add("05");
        monthArrayList.add("06");
        monthArrayList.add("07");
        monthArrayList.add("08");
        monthArrayList.add("09");
        monthArrayList.add("10");
        monthArrayList.add("11");
        monthArrayList.add("12");

        popupWindow = new PopupWindow(NewCardAddActivity.this);
        // Closes the popup window when touch outside.
        popupWindow.setOutsideTouchable(true);
        // Removes default background.
//        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        View dailogView = getLayoutInflater().inflate(R.layout.dialog_show_list, null);

        final ListView listView = (ListView) dailogView.findViewById(R.id.listView);
        CustomListAdapter customListAdapter=new CustomListAdapter(NewCardAddActivity.this,monthArrayList);

        listView.setLayoutParams( new ListView.LayoutParams( 60, 300) );
        listView.setAdapter(customListAdapter);
        // some other visual settings
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // ListView Clicked item value
                String  itemValueMonth    = (String) listView.getItemAtPosition(position);
                tv_month.setText(itemValueMonth);
                Loger.MSG("itemValueMonth",""+itemValueMonth);
                popupWindow.dismiss();
            }
        });

        popupWindow.setFocusable(false);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        // set the list view as pop up window content
        popupWindow.setContentView(dailogView);
        popupWindow.showAsDropDown(v, -5, 0);

    }

    private void showDialogYear(View v) {
        ArrayList<String> monthArrayList=new ArrayList<>();
        monthArrayList.add("01");
        monthArrayList.add("02");
        monthArrayList.add("03");
        monthArrayList.add("04");
        monthArrayList.add("05");
        monthArrayList.add("06");
        monthArrayList.add("07");
        monthArrayList.add("08");
        monthArrayList.add("09");
        monthArrayList.add("10");
        monthArrayList.add("11");
        monthArrayList.add("12");

        popupWindow = new PopupWindow(NewCardAddActivity.this);
        // Closes the popup window when touch outside.
        popupWindow.setOutsideTouchable(true);
        // Removes default background.
//        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        View dailogView = getLayoutInflater().inflate(R.layout.dialog_show_list, null);

        final ListView listView = (ListView) dailogView.findViewById(R.id.listView);
        CustomListAdapter customListAdapter=new CustomListAdapter(NewCardAddActivity.this,monthArrayList);

        listView.setLayoutParams( new ListView.LayoutParams( 60, 300) );
        listView.setAdapter(customListAdapter);
        // some other visual settings
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // ListView Clicked item value
                String  itemValueMonth    = (String) listView.getItemAtPosition(position);
                tv_year.setText(itemValueMonth);
                Loger.MSG("itemValueMonth",""+itemValueMonth);
                popupWindow.dismiss();
            }
        });

        popupWindow.setFocusable(false);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        // set the list view as pop up window content
        popupWindow.setContentView(dailogView);
        popupWindow.showAsDropDown(v, -5, 0);

    }

}
