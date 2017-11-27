package com.shaheen.a5thgeneration.signupproject;

import android.graphics.drawable.GradientDrawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.astuetz.PagerSlidingTabStrip;
import com.shaheen.a5thgeneration.signupproject.Fragments.FiltersFragment;
import com.shaheen.a5thgeneration.signupproject.Fragments.LivingInFragment;
import com.shaheen.a5thgeneration.signupproject.Fragments.TravellingToFragment;



public class SearchActivity extends AppCompatActivity {

    FragmentTransaction transaction;
    ImageView filters;
    FrameLayout search_frame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        search_frame = (FrameLayout)findViewById(R.id.search_frame);
        filters = (ImageView)findViewById(R.id.filters);
        filters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("shani","filter clicked..");
               FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FiltersFragment filtersFragment = new FiltersFragment();
                fragmentTransaction.replace(R.id.search_frame, filtersFragment);
                //fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager()));
        if (transaction==null){
            transaction = getSupportFragmentManager().beginTransaction();
        }


        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(2);

        View root = tabLayout.getChildAt(0);
        if (root instanceof LinearLayout) {
            ((LinearLayout) root).setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(getResources().getColor(R.color.purple));
            drawable.setSize(2, 1);
            ((LinearLayout) root).setDividerPadding(10);
            ((LinearLayout) root).setDividerDrawable(drawable);
        }


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }
    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {

        final int PAGE_COUNT = 2;

        public SampleFragmentPagerAdapter(FragmentManager fm) {

            super(fm);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {


                case 0:
                    TravellingToFragment travellingToFragment = new TravellingToFragment();
                    return travellingToFragment;


                case 1:
                    LivingInFragment livingInFragment = new LivingInFragment();
                    return livingInFragment;

            }
            return null;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return (position == 0)? "Travelling To" : "Living In" ;
        }
    }

}
