package com.happywannyan.Activities.profile.fragmentPagerAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.happywannyan.Activities.profile.profilepagerFragments.ProfileFragAboutFragment;
import com.happywannyan.Activities.profile.profilepagerFragments.ProfileFragImagesFragment;
import com.happywannyan.Activities.profile.profilepagerFragments.ProfileFragReviewFragment;
import com.happywannyan.Activities.profile.profilepagerFragments.ProfileFragServiceFragment;


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
                return new ProfileFragAboutFragment();
            case 1:
                return new ProfileFragServiceFragment();
            case 2:
                return new ProfileFragReviewFragment();
            case 3:
                return new ProfileFragImagesFragment();
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
