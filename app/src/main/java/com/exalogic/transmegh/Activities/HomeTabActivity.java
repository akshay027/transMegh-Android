package com.exalogic.transmegh.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.exalogic.transmegh.Adapters.HomePageAdapter;
import com.exalogic.transmegh.R;

public class HomeTabActivity extends Fragment {


    private SectionsPagerAdapter sectionsPagerAdapter;
    private HomePageAdapter adapter;
    private ViewPager mViewPager;

    public static HomeTabActivity newInstance() {
        HomeTabActivity fragment = new HomeTabActivity();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home_tab, container, false);

        sectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());

        mViewPager = (ViewPager) view.findViewById(R.id.container);
        final TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                try{
                tabLayout.addTab(tabLayout.newTab().setText("Pickup"));
                tabLayout.addTab(tabLayout.newTab().setText("Drop"));

                tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
                tabLayout.setSelectedTabIndicatorColor(Color.GRAY);
                tabLayout.setSelectedTabIndicatorHeight(2);
                tabLayout.setTabTextColors(Color.GRAY,Color.WHITE);

                /*SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                Date d = new Date();
                String dayOfTheWeek = sdf.format(d);*/

                adapter = new HomePageAdapter(getChildFragmentManager(), tabLayout.getTabCount());
                mViewPager.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        mViewPager.setCurrentItem(tab.getPosition());


                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {


                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
            }
        });
        return view;
    }


    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }


        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {

            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Class TimeTable";
                case 1:
                    return "Teacher TimeTable";

            }
            return null;
        }
    }
}
