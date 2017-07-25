package com.happywannyan.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.happywannyan.Adapter.Calendar_Adapter;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.POJO.SetGetCalender;
import com.happywannyan.POJO.SuperCalender;
import com.happywannyan.R;
import com.happywannyan.Utils.Loger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class CalenderActivity extends AppCompatActivity implements View.OnClickListener{


    public static Calendar APPcalendar;
    public static int currentDate ;
    public static int currentDay ;
    public static int currentMonth ;
    public static int currentYear ;

    public static String fisrtdayofmonth = "";
    public static String monthName = "";

    public static int yearValue;
    public static int monthValue;
    public static int maximumDaysOfMonth ; // 28


    SFNFTextView tv_month_year_header;
    SFNFTextView tv_date_limit;
    SFNFTextView TXT_OK;
    int day = 1;
    RecyclerView recyclerView;
    String[] positionPre = {}, positionNext = {};
    public ArrayList<SuperCalender> ArrayCalender;
    int StartSelectedMonth = 0;
    int StartSelcteedYear = 0;
    public boolean samepage = true;

    ImageView IMG_Tutorial;
    public int StartSelcetdDayposition = 0;


    public boolean firstclick = false;
    public boolean secondclick = false;


    SharedPreferences SharePrefernce;
    int EndSelectedMonth = 0;
    int EndSelcteedYear = 0;
    int EndSelcetdDayPosition = 0;


    Calendar_Adapter mAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calender_activity);

        APPcalendar = Calendar.getInstance(Locale.getDefault());
        maximumDaysOfMonth = APPcalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        currentDate = APPcalendar.get(Calendar.DATE);
        currentDay = APPcalendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        currentMonth = APPcalendar.get(Calendar.MONTH);
        currentYear = APPcalendar.get(Calendar.YEAR);
        SharePrefernce= PreferenceManager.getDefaultSharedPreferences(this);
        if(SharePrefernce.getString("SHULD","N").equals("Y"))
        {
            findViewById(R.id.RL_Tutorial).setVisibility(View.GONE);
        }else {
            findViewById(R.id.RL_Tutorial).setVisibility(View.VISIBLE);
        }
        IMG_Tutorial=(ImageView)findViewById(R.id.IMG_Tutorial);
        IMG_Tutorial.setImageResource(R.drawable.calender_tutorail);

        // Get the background, which has been compiled to an AnimationDrawable object.
        AnimationDrawable frameAnimation = (AnimationDrawable) IMG_Tutorial.getDrawable();

        // Start the animation (looped playback by default).
        frameAnimation.start();


        tv_month_year_header = (SFNFTextView) findViewById(R.id.tv_month_year_header);
        tv_date_limit = (SFNFTextView) findViewById(R.id.tv_date_limit);
        TXT_OK = (SFNFTextView) findViewById(R.id.TXT_OK);
        ArrayCalender = new ArrayList<>();

        Calendar calendar = (Calendar) APPcalendar.clone();
        calendar.set(Calendar.MONTH, currentMonth);
        calendar.set(Calendar.YEAR, currentYear);
        calendar.set(Calendar.DATE, 1);

        monthValue = calendar.get(Calendar.MONTH);
        Log.i("time", "" + monthValue);

        positionPre = calendar.getTime().toString().split(" ");
        maximumDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        monthName = positionPre[1];
        Log.i("monthString", "" + monthName);
        yearValue = Integer.parseInt(positionPre[5]);
        Log.i("yearString", "" + yearValue);
        fisrtdayofmonth = positionPre[0];


        tv_month_year_header.setText(monthName + " " + yearValue);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 7));


        //create date and set in adapter////////////////////
        SuperCalender superCalender = new SuperCalender();
        superCalender.setMonth(monthValue);
        superCalender.setYear(yearValue);
        superCalender.setMonthBoject(dateCreation(fisrtdayofmonth));
        ArrayCalender.add(superCalender);

        CallView();
        findViewById(R.id.img_previous_date).setVisibility(View.GONE);
        ////set time interval and no of nights in bottom layout
        tv_date_limit.setText(monthName);

        TXT_OK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String StartDate = "";
                String EndDate = "";
                for (SuperCalender calender : ArrayCalender) {
                    for (SetGetCalender data : calender.getMonthBoject()) {
                        if (data.isStartdate()) {
                            StartDate = data.getDay() + "-" + (calender.getMonth() + 1) + "-" + calender.getYear();
//                            break;
                        }
                        if (data.isEnddate()) {
                            EndDate = data.getDay() + "-" + (calender.getMonth() + 1) + "-" + calender.getYear();
                            break;
                        }
                    }
                }

                Intent intent = new Intent();
                intent.putExtra("startdate", StartDate);
                intent.putExtra("enddate", EndDate);
                setResult(RESULT_OK, intent);
                finish();

            }
        });


        findViewById(R.id.IMG_Back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.img_previous_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar pre = (Calendar) APPcalendar.clone();
                currentMonth--;

                if (currentMonth == 0) {
                    currentMonth = 12;
                    currentYear--;
                }
                pre.set(Calendar.MONTH, currentMonth);
                pre.set(Calendar.YEAR, currentYear);
                pre.set(Calendar.DATE, 1);

                monthValue = pre.get(Calendar.MONTH);
                Log.i("time", "" + monthValue);

                positionPre = pre.getTime().toString().split(" ");
                maximumDaysOfMonth = pre.getActualMaximum(Calendar.DAY_OF_MONTH);
                monthName = positionPre[1];
                yearValue = Integer.parseInt(positionPre[5]);
                fisrtdayofmonth = positionPre[0];

                Log.i("currentDate", "" + pre.get(Calendar.DATE));
                Log.i("currentDay", "" + pre.getActualMinimum(Calendar.DAY_OF_MONTH));
                Log.i("currentMonth", "" + pre.get(Calendar.MONTH));
                Log.i("currentYear", "" + pre.get(Calendar.YEAR));
                Log.i("maximumDaysOfMonth", "" + maximumDaysOfMonth);
                Log.i("fisrtDay", fisrtdayofmonth);
                Log.i("month", monthName);
                Log.i("year", "" + yearValue);

                tv_month_year_header.setText(monthName + " " + yearValue);


                if (firstclick && !secondclick) {
                    for (SuperCalender sp : ArrayCalender) {
                        for (SetGetCalender data : sp.getMonthBoject()) {
                            if (data.isStartdate()) {
                                data.setStartdate(false);
                                data.setSelected(false);
                                data.setEnddate(false);
                                secondclick = false;
                                firstclick = false;
                                break;

                            }
                        }
                    }
                }


                CallView();

                for (int i = 0; i < positionPre.length; i++) {
                    Log.i("positionPre[" + i + "]", positionPre[i]);
                }
                Log.i("maximumDaysOfMonth", "" + maximumDaysOfMonth);
                //Toast.makeText(context, "Swiped Right", Toast.LENGTH_SHORT).show();
//                    dateCreation(fisrtdayofmonth);

                ////set time interval and no of nights in bottom layout
                tv_date_limit.setText(monthName);

                Calendar tat = Calendar.getInstance();
                if (tat.get(Calendar.MONTH) == monthValue
                        && tat.get(Calendar.YEAR) == yearValue) {
                    findViewById(R.id.img_previous_date).setVisibility(View.GONE);
                }


            }
        });
        findViewById(R.id.img_next_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.img_previous_date).setVisibility(View.VISIBLE);
                Calendar next = (Calendar) APPcalendar.clone();
                if (currentMonth > 11) {
                    currentMonth = 1;
                    currentYear++;
                } else {
                    currentMonth++;
                }

                next.set(Calendar.MONTH, currentMonth);
                next.set(Calendar.YEAR, currentYear);
                next.set(Calendar.DATE, 1);


                monthValue = next.get(Calendar.MONTH);
                Log.i("time", "" + monthValue);

                positionNext = next.getTime().toString().split(" ");
                fisrtdayofmonth = positionNext[0];
                monthName = positionNext[1];
                yearValue = Integer.parseInt(positionNext[5]);
                maximumDaysOfMonth = next.getActualMaximum(Calendar.DAY_OF_MONTH);

                Log.i("currentDate", "" + next.get(Calendar.DATE));
                Log.i("currentDay", "" + next.getActualMinimum(Calendar.DAY_OF_MONTH));
                Log.i("currentMonth", "" + next.get(Calendar.MONTH));
                Log.i("currentYear", "" + next.get(Calendar.YEAR));
                Log.i("maximumDaysOfMonth", "" + maximumDaysOfMonth);
                Log.i("fisrtDay", fisrtdayofmonth);
                Log.i("month", monthName);
                Log.i("year", "" + yearValue);

                tv_month_year_header.setText(monthName + " " + yearValue);

                for (int i = 0; i < positionNext.length; i++) {
                    Log.i("positionNext[" + i + "]", positionNext[i]);
                }
                Log.i("maximumDaysOfMonth", "" + maximumDaysOfMonth);
                //Toast.makeText(context, "Swiped Left", Toast.LENGTH_SHORT).show();
                dateCreation(fisrtdayofmonth);

                SuperCalender superCalender = new SuperCalender();
                superCalender.setMonth(monthValue);
                superCalender.setYear(yearValue);
                superCalender.setMonthBoject(dateCreation(fisrtdayofmonth));
                AddDataToMainArray(superCalender);
                if (firstclick)
                    samepage = false;

                ////set time interval and no of nights in bottom layout
                tv_date_limit.setText(monthName);
            }
        });


    }

    private void AddDataToMainArray(SuperCalender superCalender) {
        boolean yes = true;
        for (SuperCalender data : ArrayCalender) {
            if (data.getYear() == yearValue &&
                    data.getMonth() == monthValue) {
                yes = false;
                break;
            }
        }


        if (yes) {
            ArrayCalender.add(superCalender);
        }

        CallView();

    }

    private void CallView() {
        for (SuperCalender superCalender1 : ArrayCalender) {
            if (superCalender1.getMonth() == monthValue &&
                    superCalender1.getYear() == yearValue) {

                mAdapter = new Calendar_Adapter(superCalender1.getMonthBoject(), this);
                recyclerView.setAdapter(mAdapter);
                Loger.MSG("@@ TTTT", "YESS");
                break;
            }
        }
    }


    public ArrayList<SetGetCalender> dateCreation(String s) {
        ArrayList<SetGetCalender> tempArray = new ArrayList<>();

        Log.i("dataCreation", s);
        if (s.equalsIgnoreCase("sun")) {
            for (int i = 0; i < maximumDaysOfMonth; i++) {
                SetGetCalender sg = new SetGetCalender();
                sg.setSelected(false);
                sg.setDay("" + day);
                tempArray.add(sg);

                day++;
            }
        } else if (s.equalsIgnoreCase("mon")) {
            for (int i = 0; i < 1; i++) {
                SetGetCalender sg = new SetGetCalender();
                sg.setSelected(false);
                sg.setDay("");
                tempArray.add(sg);
            }
            for (int i = 0; i < maximumDaysOfMonth; i++) {
                SetGetCalender sg = new SetGetCalender();
                sg.setSelected(false);
                sg.setDay("" + day);
                tempArray.add(sg);
                day++;
            }
        } else if (s.equalsIgnoreCase("tue")) {
            for (int i = 0; i < 2; i++) {
                SetGetCalender sg = new SetGetCalender();
                sg.setSelected(false);
                sg.setDay("");
                tempArray.add(sg);
            }
            for (int i = 0; i < maximumDaysOfMonth; i++) {
                SetGetCalender sg = new SetGetCalender();
                sg.setSelected(false);
                sg.setDay("" + day);
                tempArray.add(sg);
                day++;
            }
        } else if (s.equalsIgnoreCase("wed")) {
            for (int i = 0; i < 3; i++) {
                SetGetCalender sg = new SetGetCalender();
                sg.setSelected(false);
                sg.setDay("");
                tempArray.add(sg);
            }
            for (int i = 0; i < maximumDaysOfMonth; i++) {
                SetGetCalender sg = new SetGetCalender();
                sg.setSelected(false);
                sg.setDay("" + day);
                tempArray.add(sg);
                day++;
            }
        } else if (s.equalsIgnoreCase("thu")) {
            for (int i = 0; i < 4; i++) {

                SetGetCalender sg = new SetGetCalender();
                sg.setSelected(false);
                sg.setDay("");
                tempArray.add(sg);
            }
            for (int i = 0; i < maximumDaysOfMonth; i++) {
                SetGetCalender sg = new SetGetCalender();
                sg.setSelected(false);
                sg.setDay("" + day);
                tempArray.add(sg);
                day++;
            }
        } else if (s.equalsIgnoreCase("fri")) {
            for (int i = 0; i < 5; i++) {
                SetGetCalender sg = new SetGetCalender();
                sg.setSelected(false);
                sg.setDay("");
                tempArray.add(sg);
            }
            for (int i = 0; i < maximumDaysOfMonth; i++) {
                SetGetCalender sg = new SetGetCalender();
                sg.setSelected(false);
                sg.setDay("" + day);
                tempArray.add(sg);
                day++;
            }
        } else if (s.equalsIgnoreCase("sat")) {
            for (int i = 0; i < 6; i++) {
                SetGetCalender sg = new SetGetCalender();
                sg.setSelected(false);
                sg.setDay("");
                tempArray.add(sg);
            }
            for (int i = 0; i < maximumDaysOfMonth; i++) {
                SetGetCalender sg = new SetGetCalender();
                sg.setSelected(false);
                sg.setDay("" + day);
                tempArray.add(sg);
                day++;
            }
        }
        day = 1;

        return tempArray;

//        for(int p=0;p<calenderArrayList.size();p++){
//            Log.i("p",""+calenderArrayList.get(p).getDay());
//        }
//        if(StartSelectedMonth==currentMonth )
//        {
//            for(int i=0;i<calenderArrayList.size();i++){
//                if(StartSelcetdDayposition<=i ){
//                    calenderArrayList.get(i).setSelected(true);
//                }
//            }
//        }
//        mAdapter= new Calendar_Adapter(calenderArrayList,this);
//        recyclerView.setAdapter(mAdapter);
//        if(StartSelectedMonth<currentMonth){
//            mAdapter.startPosition=0;
//        }else if(StartSelectedMonth==currentMonth ){
//            ClickCount=1;
//            mAdapter.startPosition=StartSelcetdDayposition;
//        }
//        mAdapter.notifyDataSetChanged();

    }


    public void setLimitAndNoOfNight(String dayinterval, int noOfNights) {
        tv_date_limit.setText(monthName + " " + dayinterval);
    }

    public void SetSTARTDATE(int position) {
        StartSelcetdDayposition = position;
        StartSelectedMonth = currentMonth;
        StartSelcteedYear = currentYear;
    }

    public void SetEndate(int position) {
        EndSelcetdDayPosition = position;
        EndSelectedMonth = currentMonth;
        EndSelcteedYear = currentYear;
    }


    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();


//        super.onBackPressed();
    }

    public void ViewLabel() {
        String StartDate = "";
        String EndDate = "";
        for (SuperCalender calender : ArrayCalender) {
            for (SetGetCalender data : calender.getMonthBoject()) {
                if (data.isStartdate()) {
                    StartDate = data.getDay() + "-" + (calender.getMonth() + 1) + "-" + calender.getYear();
//                            break;
                }
                if (data.isEnddate()) {
                    EndDate = data.getDay() + "-" + (calender.getMonth() + 1) + "-" + calender.getYear();
                    break;
                }
            }
        }

        if(EndDate.equals(""))
        tv_date_limit.setText(StartDate);
        else
            tv_date_limit.setText(StartDate+ "  to  "+EndDate);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.BTN_Dismiss:
                findViewById(R.id.RL_Tutorial).setVisibility(View.GONE);
                break;
            case R.id.IMG_TUTO:
                findViewById(R.id.RL_Tutorial).setVisibility(View.VISIBLE);
                break;
            case R.id.BTN_Nver:
                SharedPreferences.Editor editor=SharePrefernce.edit();
                editor.putString("SHULD","Y").commit();
                findViewById(R.id.RL_Tutorial).setVisibility(View.GONE);
                break;
        }
    }
}
