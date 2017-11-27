package com.shaheen.a5thgeneration.signupproject.Fragments;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.bumptech.glide.Glide;
import com.shaheen.a5thgeneration.signupproject.R;

/**
 * Created by Shani on 11/27/2017.
 */

public class TestFragmnet extends Fragment {

    ImageView img;

    View v;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment

        v=inflater.inflate(R.layout.fragment_first, container, false);

        img= (ImageView) v.findViewById(R.id.img);

//img.setImageDrawable(getResources().getDrawable(R.drawable.ic_search));
        Glide.with(getActivity()).load("https://firebasestorage.googleapis.com/v0/b/signupproject-f1396.appspot.com/o/images2%2F.jpeg?alt=media&token=4807aa92-1c94-495e-ab42-57231bdb6341").into(img);
        return v;
    }
}