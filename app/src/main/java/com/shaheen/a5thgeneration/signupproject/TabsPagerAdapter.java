package com.shaheen.a5thgeneration.signupproject;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.shaheen.a5thgeneration.signupproject.Fragments.LikeFragment;
import com.shaheen.a5thgeneration.signupproject.Fragments.MainFragment;
import com.shaheen.a5thgeneration.signupproject.Fragments.ProfileFragment;

/**
 * Created by Shani on 10/14/2017.
 */

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Top Rated fragment activity
                return new ProfileFragment();
            case 1:
                // Games fragment activity
                return new MainFragment();
            case 2:
                // Movies fragment activity
                return new LikeFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }
}
