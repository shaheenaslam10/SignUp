package com.shaheen.a5thgeneration.signupproject.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shaheen.a5thgeneration.signupproject.Adapters.MyAdapterLiving;
import com.shaheen.a5thgeneration.signupproject.Adapters.Myadapter;
import com.shaheen.a5thgeneration.signupproject.DataModels.SearchTripData;
import com.shaheen.a5thgeneration.signupproject.R;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LivingInFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LivingInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LivingInFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    DatabaseReference all_users;
    FirebaseUser fire_user;

    ArrayList<SearchTripData> arrayList = new ArrayList<>();
    RecyclerView recyclerView;
    MyAdapterLiving recyclerAdapter;
    RecyclerView.LayoutManager layoutManager;



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public LivingInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LivingInFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LivingInFragment newInstance(String param1, String param2) {
        LivingInFragment fragment = new LivingInFragment();
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

        View view = inflater.inflate(R.layout.fragment_living_in, container, false);

        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);

        AsynchTaskDownloadLivingIn asynchTaskDownloadLivingIn = new AsynchTaskDownloadLivingIn();
        asynchTaskDownloadLivingIn.execute();

        return view;
    }

    public class AsynchTaskDownloadLivingIn extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(final String... strings) {


            fire_user = FirebaseAuth.getInstance().getCurrentUser();
            assert fire_user != null;
            String mail= fire_user.getEmail();
            Log.d("shani","email ....."+mail);

            mAuth = FirebaseAuth.getInstance();
            databaseReference = FirebaseDatabase.getInstance().getReference();
            all_users = databaseReference.child("all_users");

            Log.d("shani","all_users ....."+all_users);

            all_users.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot uids : dataSnapshot.getChildren()) {

                        DatabaseReference uids_ref = uids.getRef();

                        final String  address = uids.child("address").getValue().toString();
                        String  age = uids.child("age").getValue().toString();
                        final String  profile_name = uids.child("profile_name").getValue().toString();
                        final String  image_url = uids.child("image_url").getValue().toString();
                       // String  my_trip = uids.child("my_trip").getValue().toString();



                        Log.d("shani","image url in main fragment ......."+image_url);
                        Log.d("shani","profile name in main fragment ......."+profile_name);

                        if (uids.child("my_trip").getValue()!=null){

                            DatabaseReference my_trip_ref = uids.child("my_trip").getRef();
                            Log.d("shani","trip is not null");
                            my_trip_ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    for (DataSnapshot push : dataSnapshot.getChildren()) {

                                        String get_flexibleDates = push.child("dates_flexible").getValue().toString();
                                        String get_fromDay = push.child("from_day").getValue().toString();
                                        String get_fromMonth = push.child("from_month").getValue().toString();
                                        String get_fromYear = push.child("from_year").getValue().toString();
                                        String get_toDay = push.child("to_day").getValue().toString();
                                        String get_toMonth = push.child("to_month").getValue().toString();
                                        String get_toYear = push.child("to_year").getValue().toString();
                                        String tag = push.child("tag").getValue().toString();
                                        String get_travelingTo = push.child("travelling_to").getValue().toString();
                                        String get_tripDescription = push.child("trip_description").getValue().toString();
                                        String get_whoPaying = push.child("who_paying").getValue().toString();


                                        String date = calculateDate(get_fromDay,get_fromMonth,get_fromYear,get_toDay,get_toMonth,get_toYear);

                                        Log.d("shani"," profile name  ........."+profile_name);
                                        Log.d("shani"," travellogn to  ........."+get_travelingTo);
                                        Log.d("shani"," description  ........."+get_tripDescription);

                                        SearchTripData searchTripData = new SearchTripData(profile_name,"null","null",get_whoPaying,image_url,get_tripDescription,address);
                                        arrayList.add(searchTripData);
                                    }

                                    recyclerAdapter = new MyAdapterLiving(arrayList, getActivity());
                                    recyclerView.setHasFixedSize(true);
                                    layoutManager = new LinearLayoutManager(getApplicationContext());
                                    recyclerView.setLayoutManager(layoutManager);
                                    recyclerView.setAdapter(recyclerAdapter);

                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {}
                            });
                        }else {
                            Log.d("shani","trip is null");
                        }

                    }



                }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });


            return null;
        }
        public String calculateDate(String fday,String fmonth,String fyear,String today,String tomonth,String toyear){
            String result=null;
            if (Integer.parseInt(fmonth)<Integer.parseInt(tomonth) && Integer.parseInt(fyear)<Integer.parseInt(toyear)){
                if (fmonth.equals("1")){
                    fmonth = "Jan";
                }else if (fmonth.equals("2")){
                    fmonth = "Feb";
                }else if (fmonth.equals("3")){
                    fmonth = "Mar";
                }else if (fmonth.equals("4")){
                    fmonth = "Apr";
                }else if (fmonth.equals("5")){
                    fmonth = "May";
                }else if (fmonth.equals("6")){
                    fmonth = "Jun";
                }else if (fmonth.equals("7")){
                    fmonth = "Jul";
                }else if (fmonth.equals("8")){
                    fmonth = "Aug";
                }else if (fmonth.equals("9")){
                    fmonth = "Sep";
                }else if (fmonth.equals("10")){
                    fmonth = "Oct";
                }else if (fmonth.equals("11")){
                    fmonth = "Nov";
                }else if (fmonth.equals("12")){
                    fmonth = "Dec";
                }
            }
            if (Integer.parseInt(fmonth)<Integer.parseInt(tomonth) && Integer.parseInt(fyear)<Integer.parseInt(toyear)){
                if (tomonth.equals("1")){
                    tomonth = "Jan";
                }else if (tomonth.equals("2")){
                    tomonth = "Feb";
                }else if (tomonth.equals("3")){
                    tomonth = "Mar";
                }else if (tomonth.equals("4")){
                    tomonth = "Apr";
                }else if (tomonth.equals("5")){
                    tomonth = "May";
                }else if (tomonth.equals("6")){
                    tomonth = "Jun";
                }else if (tomonth.equals("7")){
                    tomonth = "Jul";
                }else if (tomonth.equals("8")){
                    tomonth = "Aug";
                }else if (tomonth.equals("9")){
                    tomonth = "Sep";
                }else if (tomonth.equals("10")){
                    tomonth = "Oct";
                }else if (tomonth.equals("11")){
                    tomonth = "Nov";
                }else if (tomonth.equals("12")){
                    tomonth = "Dec";
                }
            }
            result = fmonth+" "+fday+" - "+tomonth+" "+today;

            return result;
        }
        @Override
        protected void onPreExecute() {super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(),"","Uploading Profile UsersData.....", true);}
        @Override
        protected void onPostExecute(String s) {super.onPostExecute(s);
            progressDialog.dismiss();}
        @Override
        protected void onProgressUpdate(String... values) {super.onProgressUpdate(values);}
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

   /* @Override
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
    }
*/
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
}
