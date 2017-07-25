package com.happywannyan.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.happywannyan.Activities.CalenderActivity;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.POJO.SetGetCalender;
import com.happywannyan.POJO.SuperCalender;
import com.happywannyan.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class Calendar_Adapter extends RecyclerView.Adapter<Calendar_Adapter.MyViewHolder> {
    ArrayList<SetGetCalender> calenderArrayList;
    Context context;

   public int startPosition=-1;
    int endPostition=-1;
   public int night=0;


    Calendar calendar;



    public Calendar_Adapter(ArrayList<SetGetCalender> calenderArrayList, Context context) {
        this.calenderArrayList = calenderArrayList;
        this.context=context;
        calendar=Calendar.getInstance(Locale.getDefault());
    }

    @Override
    public Calendar_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.form_calender_child_view, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final Calendar_Adapter.MyViewHolder holder, final int position) {

        holder.day.setText(calenderArrayList.get(position).getDay());

        holder.day.setTextColor(Color.parseColor("#A4A4A4"));
        holder.day.setBackgroundColor(Color.WHITE);

        if(!calenderArrayList.get(position).getDay().equals("")) {
//            Log.i("currentAdapterYear",""+ApplicationClass.yearValue);
//            Log.i("currentAdapterMonth",""+ApplicationClass.monthValue);
//            Log.i("currentAdapterDate",calenderArrayList.get(position).getDay());

//            Log.i("currentYear",""+(int)calendar.get(Calendar.YEAR));
//            Log.i("monthStringValue",""+(int)calendar.get(Calendar.MONTH));
//            Log.i("currentDate",""+(int)calendar.get(Calendar.DATE));

            if (((CalenderActivity)context).yearValue== (int)calendar.get(Calendar.YEAR)) {
                if (((CalenderActivity)context).monthValue == (int)calendar.get(Calendar.MONTH)) {

                    if (Integer.parseInt(calenderArrayList.get(position).getDay()) == (int)calendar.get(Calendar.DATE)) {
                        //Log.i("EnterInCurrentDate","yes");
                        holder.day.setBackgroundResource(R.drawable.text_view_calander_background_rectangle);
                    }
                    else if (Integer.parseInt(calenderArrayList.get(position).getDay()) >(int) calendar.get(Calendar.DATE)) {
                        holder.day.setTextColor(Color.parseColor("#000000"));
                    }
                    else {
                        holder.day.setTextColor(Color.parseColor("#A4A4A4"));
                    }
                }
                else if(((CalenderActivity)context).monthValue> (int)calendar.get(Calendar.MONTH)){
                    holder.day.setTextColor(Color.parseColor("#000000"));
                }
                else {
                    holder.day.setTextColor(Color.parseColor("#A4A4A4"));
                }
            }
            else if(((CalenderActivity)context).yearValue > (int)calendar.get(Calendar.YEAR)){
                holder.day.setTextColor(Color.parseColor("#000000"));
            }
            else {
                holder.day.setTextColor(Color.parseColor("#A4A4A4"));
            }


/////////////////////////////selection view change//////////////////////////////////////
            if (calenderArrayList.get(position).isSelected()){

                holder.day.setTextColor(Color.WHITE);
                holder.day.setBackgroundColor(Color.parseColor("#C44C56"));

            }

            if(calenderArrayList.get(position).isStartdate())
            {
                holder.day.setBackgroundResource(R.drawable.start_date_picture);
            }

            if(calenderArrayList.get(position).isEnddate()){
                holder.day.setBackgroundResource(R.drawable.end_date_picture);
            }




            ///////////////////////////end/////////////////////////////////////////
        }

        holder.totalView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!calenderArrayList.get(position).getDay().equals("")){

//                    Log.i("currentAdapterYear",""+((CalenderActivity)context).yearValue);
//                    Log.i("currentAdapterMonth",""+((CalenderActivity)context).monthValue);
//                    Log.i("currentAdapterDate",calenderArrayList.get(position).getDay());

//                    Log.i("currentYear",""+(int)calendar.get(Calendar.YEAR));
//                    Log.i("monthStringValue",""+(int)calendar.get(Calendar.MONTH));
//                    Log.i("currentDate",""+(int)calendar.get(Calendar.DATE));

                    if (((CalenderActivity)context).yearValue == (int)calendar.get(Calendar.YEAR)) {
                        if (((CalenderActivity)context).monthValue == (int)calendar.get(Calendar.MONTH)) {

                            if (Integer.parseInt(calenderArrayList.get(position).getDay()) >=(int) calendar.get(Calendar.DATE)) {

                                CleckTask(holder,position);
                            }
                            else {
                                Toast.makeText(context,"You can't select before current date",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else if(((CalenderActivity)context).monthValue> (int)calendar.get(Calendar.MONTH)){

                            CleckTask(holder,position);
                        }
                        else {
                            Toast.makeText(context,"You can't select before current date",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if(((CalenderActivity)context).yearValue > (int)calendar.get(Calendar.YEAR)){

                        CleckTask(holder,position);
                    }
                    else {
                        Toast.makeText(context,"You can't select before current date",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void CleckTask(MyViewHolder holder, int position) {

        if(!((CalenderActivity)context).firstclick && !((CalenderActivity)context).secondclick)
        {
            calenderArrayList.get(position).setSelected(true);
            calenderArrayList.get(position).setStartdate(true);
            ((CalenderActivity)context).firstclick=true;
            startPosition=position;
        }else  if(((CalenderActivity)context).firstclick && !((CalenderActivity)context).secondclick){
            calenderArrayList.get(position).setEnddate(true);
            calenderArrayList.get(position).setSelected(true);
            ((CalenderActivity)context).secondclick=true;

            boolean startcolor=false;

            for(SuperCalender scal:((CalenderActivity)context).ArrayCalender)
            {

                for(SetGetCalender dd: scal.getMonthBoject())
                {   if(dd.isStartdate())
                        startcolor=true;

                    if(startcolor)
                        dd.setSelected(true);

                    if( dd.isEnddate()) {
                        startcolor=false;
                        break;
                    }

                }


            }

            if(((CalenderActivity)context).samepage && position<startPosition)
            {
                calenderArrayList.get(position).setStartdate(true);
                calenderArrayList.get(position).setEnddate(false);
                calenderArrayList.get(startPosition).setEnddate(true);
                calenderArrayList.get(startPosition).setStartdate(false);

                for(SetGetCalender dd: calenderArrayList)
                {   if(dd.isStartdate())
                    startcolor=true;

                    if(startcolor)
                        dd.setSelected(true);

                    if( dd.isEnddate()) {
                        startcolor=false;
                        break;
                    }

                }
            }

            // if same month call a loop for selected dates

        }
        else if(((CalenderActivity)context).firstclick && ((CalenderActivity)context).secondclick){
            ((CalenderActivity)context).firstclick=false;
            ((CalenderActivity)context).secondclick=false;
            for(SuperCalender scal:((CalenderActivity)context).ArrayCalender)
            {

                for(SetGetCalender dd: scal.getMonthBoject())
                {
                    dd.setEnddate(false);
                    dd.setStartdate(false);
                    dd.setSelected(false);
                }


            }
            startPosition=position;
            calenderArrayList.get(position).setSelected(true);
            calenderArrayList.get(position).setStartdate(true);
            ((CalenderActivity)context).firstclick=true;
            ((CalenderActivity)context).samepage=true;
        }

        notifyDataSetChanged();


        ((CalenderActivity)context).ViewLabel();
    }

    @Override
    public int getItemCount() {
        return calenderArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        SFNFTextView day;
        View totalView;

        public MyViewHolder(View itemView) {
            super(itemView);
            day= (SFNFTextView) itemView.findViewById(R.id.day);
            totalView=itemView;
        }
    }



}