package com.happywannyan.Utils.helper;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.DatePicker;

import com.happywannyan.R;

import java.util.Calendar;

/**
 * Created by su on 7/12/17.
 */

public  class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {



    public interface DateSelect{
        void OnDateSelected(int year,int month,int day);

    }

    DateSelect dateselect;
    public void DatePickerFragment(DateSelect Date) {
        this.dateselect=Date;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it

        DatePickerDialog datepic= new DatePickerDialog(getActivity(), R.style.DatePickerDialogTheme, this, year, month, day);
        datepic.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
        return datepic;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        dateselect.OnDateSelected(year,month,day);
    }

}