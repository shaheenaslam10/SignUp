package com.shaheen.a5thgeneration.signupproject.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.shaheen.a5thgeneration.signupproject.UsersData;
import com.shaheen.a5thgeneration.signupproject.FeedbackActivity;
import com.shaheen.a5thgeneration.signupproject.LoginOptionsActivity;
import com.shaheen.a5thgeneration.signupproject.MessageActivity;
import com.shaheen.a5thgeneration.signupproject.R;
import com.shaheen.a5thgeneration.signupproject.SearchActivity;
import com.shaheen.a5thgeneration.signupproject.SettingsActivity;
import com.shaheen.a5thgeneration.signupproject.TripActivity;
import com.shaheen.a5thgeneration.signupproject.Valoidators.Validations;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MainFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    TextView mytrip, messages, search, more;
    ImageView information;
    public static final String ARG_PAGE = "ARG_PAGE";
    Dialog dialog;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();


    public MyAppAdapter myAppAdapter;
    public MyAppAdapter.ViewHolder viewHolder;
    private ArrayList<UsersData> array = new ArrayList<>();
    ;
    private SwipeFlingAdapterView flingContainer;

    ProgressDialog progressDialog;
    TextView no_image;

    FirebaseAuth mAuth;
    DatabaseReference databaseReference, all_users;
    FirebaseUser fire_user;


    public MainFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        Toolbar myToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(myToolbar);

        mytrip = (TextView) view.findViewById(R.id.mytrip);
        messages = (TextView) view.findViewById(R.id.message);
        search = (TextView) view.findViewById(R.id.search);
        more = (TextView) view.findViewById(R.id.more);
        no_image = (TextView) view.findViewById(R.id.no_image);
        flingContainer = (SwipeFlingAdapterView) view.findViewById(R.id.frame);
        //  information = (ImageView) view.findViewById(R.id.information);

        no_image.setVisibility(View.INVISIBLE);
        flingContainer.setVisibility(View.VISIBLE);


        FirebaseStorage storage = FirebaseStorage.getInstance();


        Log.d("shani", "mAuth....................." + mAuth);
        Log.d("shani", "DatabaseReference....................." + databaseReference);

        mytrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), TripActivity.class);
                startActivity(intent);
            }
        });
        messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MessageActivity.class));
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Context wrapper = new ContextThemeWrapper(getContext(), R.style.PopupMenu);
                PopupMenu popup = new PopupMenu(wrapper, more);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.more_menu, popup.getMenu());


                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        String title = item.getTitle().toString();

                        switch (title) {
                            case "Settings":
                                startActivity(new Intent(getActivity(), SettingsActivity.class));
                                break;
                            case "Feedback":
                                startActivity(new Intent(getActivity(), FeedbackActivity.class));
                                break;
                            case "Logout":

                                if (Validations.checkConnection(getActivity())) {
                                    fAuth.signOut();

                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    if (user == null) {

                                        Log.d("shani", "user is nul");
                                        startActivity(new Intent(getActivity(), LoginOptionsActivity.class));
                                        // progressDialog.dismiss();

                                    } else {
                                       /* progressDialog.dismiss();*/
                                        Toast.makeText(getActivity(), "Logout failed", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "No Internet", Toast.LENGTH_SHORT).show();
                                }
                                break;
                        }

                        return true;
                    }
                });

                popup.show(); //showing popup menu

            }
        });


        /*array.add(new UsersData("https://firebasestorage.googleapis.com/v0/b/signupproject-f1396.appspot.com/o/images2%2F.jpeg?alt=media&token=d5f926b1-95c8-4b8c-880c-b5aad02484b6", "name"));
        myAppAdapter = new MyAppAdapter(array, getActivity());
        flingContainer.setAdapter(myAppAdapter);*/

       /* AsynchTaskDownloadMainContent asynchTaskDownloadMainContent = new AsynchTaskDownloadMainContent();
        asynchTaskDownloadMainContent.execute();*/




        return view;
/// shan bhai awaz ka scene ni hota is ma? hota h boblo aygi awaz

    }


    public class MyAppAdapter extends BaseAdapter {


        public ArrayList<UsersData> parkingList;
        public Context context;

        private MyAppAdapter(ArrayList<UsersData> apps, Context context) {
            this.parkingList = apps;
            this.context = context;
            // this.notifyDataSetChanged();
            Log.d("shani", "array list in adapter constructor ......." + parkingList.size());
        }

        @Override
        public int getCount() {
            Log.d("shani", "getItem Count" + parkingList.size());
            // return parkingList.size();
            return parkingList.size();
        }

        @Override
        public Object getItem(int position) {
            Log.d("shani","getItem "+parkingList.get(position));
            return position;
        }

        @Override
        public long getItemId(int position) {
            Log.d("shani", "getItem id" + position);
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View rowView = convertView;
            Log.d("shani", "getView rowView" + rowView);


            if (rowView == null) {

                Log.d("shani", "getView in if ");
                LayoutInflater inflater = getActivity().getLayoutInflater();
                rowView = inflater.inflate(R.layout.swipable_item, parent, false);
            }
                // configure view holder
                viewHolder = new ViewHolder();
                // viewHolder.DataText = (TextView) rowView.findViewById(R.id.bookText);
                viewHolder.infoImage = (ImageView) rowView.findViewById(R.id.information);
                viewHolder.background = (FrameLayout) rowView.findViewById(R.id.background);
                viewHolder.cardImage = (ImageView) rowView.findViewById(R.id.cardImage);
                rowView.setTag(viewHolder);


                viewHolder.infoImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Toast.makeText(context, "array.get(0).getDescription()..........."+array.get(0).getDescription(), Toast.LENGTH_SHORT).show();

                        progressDialog = ProgressDialog.show(getActivity(), "Loading", "Please wait", false, false, null);
                        showDialog(array.get(0).getImagePath());
                    }
                });


           /*  else {
                viewHolder = (ViewHolder) convertView.getTag();
                Log.d("shani", "getView in else");
            }*/
            //viewHolder.DataText.setText(parkingList.get(position).getDescription() + "");


            String imgURL = (parkingList.get(position).getImagePath());
            Log.d("shani", "parkingList.get(position)........" + parkingList.get(position).getImagePath());
            Glide.with(getApplicationContext()).load(imgURL).fitCenter().into(viewHolder.cardImage);
            //Picasso.with(getActivity()).load(parkingList.get(position).getImagePath()).resize(370,550).placeholder(R.drawable.man).into(viewHolder.cardImage);

            return rowView;
        }

        public class ViewHolder {
            public FrameLayout background;
            public ImageView cardImage;
            public ImageView infoImage;


        }
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

    public void showDialog(String imagePath) {
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.user_information);

        ImageView dial_image = (ImageView) dialog.findViewById(R.id.image);
        Glide.with(getActivity()).load(imagePath).into(dial_image);

        FloatingActionButton fab = (FloatingActionButton) dialog.findViewById(R.id.fab);

        progressDialog.dismiss();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.more_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return false;
    }

    public class AsynchTaskDownloadMainContent extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {


            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(), "", "Retrieving Profile UsersData.....", true);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("shani", "array list ..... image ,...." + array.size());


            progressDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }




    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        all_users = databaseReference.child("all_users");
        all_users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot uids : dataSnapshot.getChildren()) {
                    DatabaseReference uids_ref = uids.getRef();

                    String address = uids.child("address").getValue().toString();
                    String age = uids.child("age").getValue().toString();
                    String profile_name = uids.child("profile_name").getValue().toString();
                    String image_url = uids.child("image_url").getValue().toString();

                    Log.d("shani", "image url in main fragment ......." + image_url);
                    Log.d("shani", "profile name in main fragment ......." + profile_name);
                    Log.d("shani", "array list in main fragment in valuelistener  ......." + array.size());

                    array.add(new UsersData(image_url, profile_name));


                    int orientation = getActivity().getResources().getConfiguration().orientation;

                   /* if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

                    } else {
                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                    }*/

                    Log.d("shani", "image url in main fragment ......." + image_url);


                }


                // UsersAdapter usersAdapter=new UsersAdapter(array,getApplicationContext());

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myAppAdapter = new MyAppAdapter(array, getApplicationContext());
                      myAppAdapter.notifyDataSetChanged();

                        flingContainer.setAdapter(myAppAdapter);
                    }
                });

                Log.d("shani", "outer my ********r  ......." + array.size());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                array.remove(0);
                myAppAdapter.notifyDataSetChanged();

                Toast.makeText(getActivity(), "Image Disliked...", Toast.LENGTH_SHORT).show();
                Log.d("shani", "Card left Swiped........" + dataObject);
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
            }

            @Override
            public void onRightCardExit(Object dataObject) {

                array.remove(0);

                myAppAdapter.notifyDataSetChanged();
                // Log.d("shani", "myAppAdapter.getItem(0)........."+myAppAdapter.getItem(0));
                // Log.d("shani", "myAppAdapter.getItemCount........."+myAppAdapter.getCount());
                // Log.d("shani", "myAppAdapter.getItemID........."+myAppAdapter.getItemId(0));
                Toast.makeText(getActivity(), "Image Liked...", Toast.LENGTH_SHORT).show();
                Log.d("shani", "Card Right Swiped........" + dataObject);
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                //   Toast.makeText(getActivity(), "List is About to Empty...."+itemsInAdapter, Toast.LENGTH_SHORT).show();
                if (itemsInAdapter == 0) {
                    no_image.setVisibility(View.VISIBLE);
                    flingContainer.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onScroll(float scrollProgressPercent) {

                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.background).setAlpha(0);
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });

        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {

                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.background).setAlpha(0);

                Log.d("shani", "flingContainer.setOnclick......... itemPosition..." + itemPosition + ".........dataObject......." + dataObject);

                //myAppAdapter.notifyDataSetChanged();

            }
        });
    }
}
