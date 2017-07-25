package com.happywannyan.Activities.profile.fragmentPagerAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.happywannyan.Activities.profile.profilepagerFragments.ProfileFragAbout;
import com.happywannyan.Activities.profile.profilepagerFragments.ProfileFragImages;
import com.happywannyan.Activities.profile.profilepagerFragments.ProfileFragReview;
import com.happywannyan.Activities.profile.profilepagerFragments.ProfileFragService;


/**
 * Created by bodhidipta on 22/05/17.
 */

public class ProfileFragPagerAdapter extends FragmentStatePagerAdapter {

    public ProfileFragPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return super.isViewFromObject(view, object);
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ProfileFragAbout();
            case 1:
                return new ProfileFragService();
            case 2:
                return new ProfileFragReview();
            case 3:
                return new ProfileFragImages();
            default:
                return null;
        }
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return 4;
    }
}
