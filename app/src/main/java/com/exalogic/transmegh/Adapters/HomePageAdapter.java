package com.exalogic.transmegh.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.exalogic.transmegh.Activities.DropFragment;
import com.exalogic.transmegh.Activities.PickupFragment;

/**
 * Created by Exalogic on 3/15/2017.
 */

public class HomePageAdapter extends FragmentPagerAdapter {
    private final int tabCount;

    public HomePageAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new PickupFragment();
            case 1:
                return new DropFragment();

        }
        return null;
    }

    @Override
    public int getCount() {
        return this.tabCount;
    }
}


