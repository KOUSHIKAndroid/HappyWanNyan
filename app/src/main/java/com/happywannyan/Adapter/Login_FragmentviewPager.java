package com.happywannyan.Adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.LinkedList;


/**
 * Created by su on 5/3/17.
 */

public class Login_FragmentviewPager extends FragmentStatePagerAdapter {
    LinkedList<Fragment> listItems;
    //String[] planets;


    public Login_FragmentviewPager(Context context, FragmentManager fm, LinkedList<Fragment> list) {
        super(fm);
        listItems = list;
        //planets= context.getResources().getStringArray(R.array.Tilte_list);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        super.restoreState(state, loader);
    }

    @Override
    public Fragment getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

//    @Override
//    public CharSequence getPageTitle(int position) {
//
//        return planets[position];
//    }

}