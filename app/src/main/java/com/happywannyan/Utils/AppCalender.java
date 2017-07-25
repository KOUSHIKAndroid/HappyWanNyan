package com.happywannyan.Utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;

import com.happywannyan.R;
import com.happywannyan.SitterBooking.BookingFragmnetOne;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by su on 6/28/17.
 */

public class AppCalender extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    private static final String VIEWID = "viewid";
    public static AppCalender newInstance(int startdate) {
        AppCalender fragment = new AppCalender();
        Bundle args = new Bundle();
        args.putInt(VIEWID, startdate);
        fragment.setArguments(args);
        return fragment;
    }

    public interface OnDateSelect {
        void Ondate(Calendar date,int viewid);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme);
    }

    public OnDateSelect onDateSelect;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker

        onDateSelect=(OnDateSelect)getActivity();
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
        dialog.getDatePicker().setMinDate(c.getTimeInMillis());
        // Create a new instance of DatePickerDialog and return it
        return dialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
      Calendar calendar=Calendar.getInstance();
        calendar.set(year,month,day);
        onDateSelect.Ondate(calendar,getArguments().getInt(VIEWID));

    }
}

