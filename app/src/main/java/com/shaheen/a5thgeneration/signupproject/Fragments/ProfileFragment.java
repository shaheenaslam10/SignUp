package com.shaheen.a5thgeneration.signupproject.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shaheen.a5thgeneration.signupproject.Adapters.DashbaordFavouriteAdapter;
import com.shaheen.a5thgeneration.signupproject.DataModels.FavouriteModel;
import com.shaheen.a5thgeneration.signupproject.R;
import com.shaheen.a5thgeneration.signupproject.TripActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.facebook.FacebookSdk.getApplicationContext;


public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    CircleImageView profile_image;
    TextView name,address;

    ArrayList<FavouriteModel> arrayListFavourite = new ArrayList<>();
    DashbaordFavouriteAdapter recyclerAdapterFavourite;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView,recyclerViewFavourites;
    Button create_trip,editProfile;

    FirebaseAuth mAuth;
    DatabaseReference databaseReference,all_users;
    FirebaseUser fire_user;
    ProgressDialog progressDialog;
    Bitmap bitmap;
    FragmentTransaction transaction;
    EditProfileFragment editProfileFragment;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(int page) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profile_image = (CircleImageView)view.findViewById(R.id.profile_image);
        name = (TextView) view.findViewById(R.id.name);
        address = (TextView)view.findViewById(R.id.address);
        create_trip = (Button)view.findViewById(R.id.create_trip);
        editProfile = (Button)view.findViewById(R.id.editProfile);
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerViewMessages);
        recyclerViewFavourites = (RecyclerView)view.findViewById(R.id.recyclerViewFavourites);


        if (transaction==null){
            transaction = getActivity().getSupportFragmentManager().beginTransaction();
        }


        AsynchTaskUploadProfile asynchTaskUploadProfile = new AsynchTaskUploadProfile();
        asynchTaskUploadProfile.execute();

        editProfileFragment = new EditProfileFragment();


        create_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), TripActivity.class));
            }
        });
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.profile_frag_main, editProfileFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }

    public class AsynchTaskUploadProfile extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {


            fire_user = FirebaseAuth.getInstance().getCurrentUser();
            assert fire_user != null;
            String mail= fire_user.getEmail();
            Log.d("shani","email ....."+mail);


            mAuth = FirebaseAuth.getInstance();
            databaseReference = FirebaseDatabase.getInstance().getReference();

            all_users = databaseReference.child("all_users");
            all_users.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot uids : dataSnapshot.getChildren()) {

                        DatabaseReference uids_ref = uids.getRef();
                        String u_name = uids.child("profile_name").getValue().toString();
                        String st_address = uids.child("address").getValue().toString();
                        String image_url = uids.child("image_url").getValue().toString();

                        Log.d("shani","prifile image before if .......    "+image_url);

                        Log.d("shani","uid before if  .........  "+uids.toString());
                        if (uids.getKey().equals(fire_user.getUid())){

                            Log.d("shani","matched    ");

                            name.setText(u_name);
                            address.setText(st_address);
                            Log.d("shani","profile image .......    "+image_url);
                            Glide.with(getApplicationContext()).load(image_url).asBitmap().into(profile_image);
                           // Picasso.with(getApplicationContext()).load(image_url).resize(100,100).into(profile_image);
                        }else {

                            FavouriteModel favouriteModel = new FavouriteModel(image_url,u_name);
                            arrayListFavourite.add(favouriteModel);
                        }

                        recyclerAdapterFavourite = new DashbaordFavouriteAdapter(arrayListFavourite);
                        recyclerViewFavourites.setHasFixedSize(true);
                        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                        recyclerViewFavourites.setLayoutManager(layoutManager);
                        recyclerViewFavourites.setAdapter(recyclerAdapterFavourite);


                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



            return null;
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

  /*  @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    /* @Override
     public void onDetach() {
         super.onDetach();
         mListener = null;
     }*/
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
