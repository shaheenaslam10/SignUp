package com.shaheen.a5thgeneration.signupproject.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.shaheen.a5thgeneration.signupproject.DataBase.FilterData;
import com.shaheen.a5thgeneration.signupproject.MessageActivity;
import com.shaheen.a5thgeneration.signupproject.R;
import com.shaheen.a5thgeneration.signupproject.SearchActivity;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FiltersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FiltersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FiltersFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    ImageView back;
    EditText trip_destination;
    CrystalRangeSeekbar rangeSeekbar;
    TextView start_age,end_age,update_search;
    CheckBox male,female,extra_ticket,no_ticket,own_ticket;
    String st_tripDestination,st_male,st_female,st_extraTicket,st_ownTicket,st_noTicket,st_min_age,st_max_age;
    Realm realm;





    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FiltersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FiltersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FiltersFragment newInstance(String param1, String param2) {
        FiltersFragment fragment = new FiltersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filters, container, false);

        trip_destination = (EditText)view.findViewById(R.id.trip_destination);
        male = (CheckBox)view.findViewById(R.id.filter_male);
        female = (CheckBox)view.findViewById(R.id.filter_female);
        rangeSeekbar = (CrystalRangeSeekbar)view.findViewById(R.id.rangeSeekbar);
        start_age = (TextView)view.findViewById(R.id.start_age);
        end_age = (TextView)view.findViewById(R.id.end_age);
        update_search = (TextView)view.findViewById(R.id.update_search);
        extra_ticket = (CheckBox)view.findViewById(R.id.filter_extraTicket);
        no_ticket = (CheckBox)view.findViewById(R.id.filter_noTicket);
        own_ticket = (CheckBox)view.findViewById(R.id.filter_ownTicket);
        back = (ImageView)view.findViewById(R.id.back);

      /*  FragmentTransaction transaction;
        if (transaction==null){
            transaction = getActivity().getSupportFragmentManager().beginTransaction();
        }*/

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);*/

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
               // transaction.replace(R.id.profile_frag_main, editProfileFragment);
                //transaction.addToBackStack(null);
                //getActivity().getFragmentManager().popBackStack();
               // transaction.commit();

                FragmentManager fm = getActivity().getSupportFragmentManager();
                if(fm.getBackStackEntryCount()>0) {
                    fm.popBackStack();
                }

            }
        });
        trip_destination.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (trip_destination.getRight() - trip_destination.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here

                        trip_destination.setText("");
                        return true;
                    }
                }
                return false;
            }
        });

        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                start_age.setText(minValue.toString());
                end_age.setText(maxValue.toString());
                st_min_age = minValue.toString();
                st_max_age = maxValue.toString();
            }
        });

        update_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (realm!=null){

                    realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    realm.deleteAll();
                    realm.commitTransaction();
                    realm.close();
                }


                if (trip_destination.getText().toString().length()<1){
                    st_tripDestination = "null";
                }else {
                    st_tripDestination = trip_destination.getText().toString();
                }

                if (male.isChecked()){
                    st_male = "yes";
                }else {
                    st_male = "no";
                }
                if (female.isChecked()){
                    st_female = "yes";
                }else {
                    st_female = "no";
                }
                if (extra_ticket.isChecked()){
                    st_extraTicket = "yes";
                }else {
                    st_extraTicket = "no";
                }
                if (own_ticket.isChecked()){
                    st_ownTicket = "yes";
                }else {
                    st_ownTicket = "no";
                }
                if (no_ticket.isChecked()){
                    st_noTicket = "yes";
                }else {
                    st_noTicket = "no";
                }

                realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                final FilterData filterData = realm.createObject(FilterData.class);
                filterData.setTrip_destination(st_tripDestination);
                filterData.setMale(st_male);
                filterData.setFemal(st_female);
                filterData.setMin_age(st_min_age);
                filterData.setMax_age(st_max_age);
                filterData.setExtra_ticket(st_extraTicket);
                filterData.setOwn_ticket(st_ownTicket);
                filterData.setNo_ticket(st_noTicket);
                realm.commitTransaction();
                realm.close();


                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                //getActivity().finish();


            }
        });


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }*/

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    @Override
    public void onResume() {
        super.onResume();

        if(getView() == null){
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){



                    return true;
                }
                return false;
            }
        });
    }

}
