package com.happywannyan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.R;

import java.util.ArrayList;

/**
 * Created by su on 9/11/17.
 */

public class CustomListAdapter extends BaseAdapter {
    private Context context; //context
    private ArrayList<String> items; //data source of the list adapter

    //public constructor
    public CustomListAdapter(Context context, ArrayList<String> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size(); //returns total of items in the list
    }

    @Override
    public Object getItem(int position) {
        return items.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.single_list_text1, parent, false);
        }

        // get current item to be displayed
        String currentItem = (String) getItem(position);

        // get the TextView for item name and item description
        SFNFTextView TXT_item = (SFNFTextView) convertView.findViewById(R.id.TXT_item);

        //sets the text for item name and item description from the current item object
        TXT_item.setText(items.get(position));

        // returns the view for the current row
        return convertView;
    }
}