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
import android.view.View;
import android.widget.LinearLayout;

import com.shaheen.a5thgeneration.signupproject.Fragments.InboxFragment;
import com.shaheen.a5thgeneration.signupproject.Fragments.LivingInFragment;
import com.shaheen.a5thgeneration.signupproject.Fragments.SentFragment;
import com.shaheen.a5thgeneration.signupproject.Fragments.TravellingToFragment;

public class MessageActivity extends AppCompatActivity {

    FragmentTransaction transaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new SampleFragmentPagerAdapterMessage(getSupportFragmentManager()));
        if (transaction==null){
            transaction = getSupportFragmentManager().beginTransaction();
        }

        ////   screen visible ha ?
        // mje apki awaz a rhi ha... ap bolen


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
            public void onPageSelected(int position) {}
            @Override
            public void onPageScrollStateChanged(int state) {}
        });

    }
    public class SampleFragmentPagerAdapterMessage extends FragmentPagerAdapter {

        final int PAGE_COUNT = 2;

        public SampleFragmentPagerAdapterMessage(FragmentManager fm) {

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
                    InboxFragment inboxFragment = new InboxFragment();
                    return inboxFragment;


                case 1:
                    SentFragment sentFragment = new SentFragment();
                    return sentFragment;

            }
            return null;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return (position == 0)? "INBOX" : "SENT" ;
        }
    }

}

