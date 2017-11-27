package com.shaheen.a5thgeneration.signupproject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.facebook.Profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shaheen.a5thgeneration.signupproject.Fragments.BlankFragment;
import com.shaheen.a5thgeneration.signupproject.Fragments.EditProfileFragment;
import com.shaheen.a5thgeneration.signupproject.Fragments.FiltersFragment;
import com.shaheen.a5thgeneration.signupproject.Fragments.LikeFragment;
import com.shaheen.a5thgeneration.signupproject.Fragments.MainFragment;
import com.shaheen.a5thgeneration.signupproject.Fragments.ProfileFragment;
import com.shaheen.a5thgeneration.signupproject.Fragments.TestFragmnet;
import com.shaheen.a5thgeneration.signupproject.Valoidators.Validations;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    PopupMenu popup;
    FragmentTransaction transaction;
    FragmentTransaction transaction2;
    RelativeLayout container;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


      /*  if (transaction == null) {
            transaction = getSupportFragmentManager().beginTransaction();
        }
        if (transaction2 == null) {
            transaction2 = getSupportFragmentManager().beginTransaction();
            // Toast.makeText(this, "transaction2 alled", Toast.LENGTH_SHORT).show();
        }*/

        container = (RelativeLayout) findViewById(R.id.container);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MainFragment filtersFragment = new MainFragment();
        fragmentTransaction.add(R.id.container, filtersFragment);
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

      /*  ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager()));
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        int[] imageResId = {
                R.drawable.profil,
                R.drawable.home,
                R.drawable.premum };

        for (int i = 0; i < imageResId.length; i++) {
            tabLayout.getTabAt(i).setIcon(imageResId[i]);
        }

        View root = tabLayout.getChildAt(0);
        if (root instanceof LinearLayout) {
            ((LinearLayout) root).setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(getResources().getColor(R.color.purple));
            drawable.setSize(2, 1);
            ((LinearLayout) root).setDividerPadding(10);
            ((LinearLayout) root).setDividerDrawable(drawable);
        }

        viewPager.setCurrentItem(1);
        viewPager.setOffscreenPageLimit(3);


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

        *//*tabsStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private int currentPageSelected = 0;
            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {

                if (position==0){
                    BlankFragment blankFragment = new BlankFragment();
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.blank_container, blankFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    popup.show();
                }
            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
            }
        });*//*

     *//*   Context wrapper = new ContextWrapper(MainActivity.this);
       popup = new PopupMenu(wrapper,tabLayout );

        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.profile_click, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                String title = item.getTitle().toString();
                ProfileFragment profileFragment = new ProfileFragment();
                EditProfileFragment editProfileFragment = new EditProfileFragment();


                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back


                switch (title){
                    case "Dashboard":
                        transaction2 = getSupportFragmentManager().beginTransaction();
                        transaction2.replace(R.id.blank_container, profileFragment);
                        transaction2.addToBackStack(null);
                        transaction2.commit();
                        break;
                    case "Edit Profile":
                        transaction2 = getSupportFragmentManager().beginTransaction();
                        transaction2.replace(R.id.blank_container, editProfileFragment);
                        transaction2.addToBackStack(null);
                        transaction2.commit();
                        break;

                }

                return true;
            }
        });
*//*





        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

         Intent intent = getIntent();
        String  provid = intent.getStringExtra("from");
        String  phone = intent.getStringExtra("phone");
        Log.d("shani","value of from ......"+provid);
        Profile profile = Profile.getCurrentProfile();
        if (provid != null){


        }
        if (provid == null){
            if (profile != null){
                Log.d("shani","current profile first name ......."+profile.getFirstName());
                Log.d("shani","current profile id ......."+profile.getId());
                Log.d("shani","current profile id ......."+profile.getProfilePictureUri(130,140));
            }

        }
        if (phone != null){
           // provider.setText("SignUp From Phone Number");
        }


    }


    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider {
        final int PAGE_COUNT = 3;
        private int tabIcons[] = {R.drawable.profil, R.drawable.home, R.drawable.premum};
       *//* private final int[] ICONS = { R.drawable.ic_launcher_gplus, R.drawable.ic_launcher_gmail,
                R.drawable.ic_launcher_gmaps, R.drawable.ic_launcher_chrome };*//*

		private final int[] SWITCH_ICONS = {R.drawable.profil, R.drawable.home, R.drawable.premum};


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
                   *//* ProfileFragment profileFragment = new ProfileFragment();
                    return profileFragment;*//*
                    ProfileFragment profileFragment = new ProfileFragment();
                    return profileFragment;


                case 1:
                    MainFragment mainFragment = new MainFragment();
                    return mainFragment;
                case 2:
                    LikeFragment likeFragment = new LikeFragment();
                    return likeFragment;
            }
            return null;
        }

        @Override
        public int getPageIconResId(int position) {
            return SWITCH_ICONS[position];
        }
    }
*/
    }

}
