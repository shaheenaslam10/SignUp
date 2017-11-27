package com.shaheen.a5thgeneration.signupproject.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shaheen.a5thgeneration.signupproject.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    Button next1;
    String gender,interest;
    ToggleButton gender_male,interest_men;
    private static final int SELECTED_PICTURE = 1011;
    CircleImageView add_profile_image;
    Bitmap scaledBitmap;
    EditText location;
    Uri uri;

    EditText experinceBox;
    CheckBox check_travel,check_friendship,check_longterm;
    Button save_profile;
    String looking_travelPartner , looking_friendship,looking_longTerm;
    RadioGroup gender_group,interest_group;
    ProgressDialog progressDialog;
    String ResultString,myBase64Image;
    Bitmap scaledImage;

    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    FirebaseUser fire_user;

    TextView ethnicity_text,relation_text,location_city,location_country,bith;

    ToggleButton toggleFemale,toggleMale,toggleMen,toggleWomen,toggleBoth;

    String ethnicity,relation,birth_date = null,age,birth_day,birth_month,birth_year,location_address,st_city,st_country,st_address,travel_experience,mail;

    StorageReference mStorageRef;
    ByteArrayInputStream bs;
    DatabaseReference all_users,all_users_uid,users,profile;

    Uri downloadUrl;
    String get_day,get_month,get_year;
    Bitmap bitmap;





    private OnFragmentInteractionListener mListener;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditProfileFragment newInstance(String param1, String param2) {
        EditProfileFragment fragment = new EditProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        location = (EditText) view.findViewById(R.id.location);
        next1 = (Button)view.findViewById(R.id.next1);
        add_profile_image = (CircleImageView)view.findViewById(R.id.add_profile_image);

        experinceBox = (EditText)view.findViewById(R.id.experinceBox);
        check_travel = (CheckBox)view.findViewById(R.id.check_travel);
        check_friendship = (CheckBox)view.findViewById(R.id.check_friendship);
        check_longterm = (CheckBox)view.findViewById(R.id.check_longterm);
        save_profile = (Button)view.findViewById(R.id.save_profile);

        location_country = (TextView)view.findViewById(R.id.location_country);
        location_city = (TextView)view.findViewById(R.id.location_city);
        bith = (TextView)view.findViewById(R.id.birth_date);

        gender_group = (RadioGroup)view.findViewById(R.id.gender_group);
        interest_group = (RadioGroup)view.findViewById(R.id.interest_group);
        gender_male = (ToggleButton) view.findViewById(R.id.toggleMale);
        interest_men = (ToggleButton) view.findViewById(R.id.toggleMen);
        toggleFemale = (ToggleButton)view.findViewById(R.id.toggleFemale);
        toggleWomen = (ToggleButton)view.findViewById(R.id.toggleWomen);
        toggleBoth = (ToggleButton)view.findViewById(R.id.toggleBoth);

        gender_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // ((RadioGroup)view.getParent()).check(view.getId());
                gender_group.check(R.id.toggleMale);

            }
        });
        toggleFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RadioGroup)view.getParent()).check(view.getId());
            }
        });
        interest_men.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RadioGroup)view.getParent()).check(view.getId());
            }
        });
        toggleWomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RadioGroup)view.getParent()).check(view.getId());
            }
        });
        toggleBoth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RadioGroup)view.getParent()).check(view.getId());
            }
        });
        bith.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putInt("DATE",1);
                DialogFragment newFragment = new DatePickerFragmentBirth();
                newFragment.setArguments(bundle);
                newFragment.show(getActivity().getFragmentManager(), "datePicker");
            }
        });

        fire_user = FirebaseAuth.getInstance().getCurrentUser();
        assert fire_user != null;
        if (fire_user.getProviders().contains("[phone]")){
            mail = fire_user.getPhoneNumber();
        }/*else if (fire_user.getProviders().contains("email")){
            mail = fire_user.getEmail();
        }else {

        }*/


        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        users = databaseReference.child(fire_user.getUid());
        profile = users.child("profile");
        all_users = databaseReference.child("all_users");
        all_users_uid = all_users.child(fire_user.getUid());

        AsynchTaskDownloadProfile asynchTaskDownloadProfile = new AsynchTaskDownloadProfile();
        asynchTaskDownloadProfile.execute();
        progressDialog.dismiss();

        AsynchLocation asynchLocation = new AsynchLocation();
        asynchLocation.execute();
        progressDialog.dismiss();





        gender = null;
        interest = null;

        add_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, SELECTED_PICTURE);

            }
        });



        ((RadioGroup) view.findViewById(R.id.gender_group)).setOnCheckedChangeListener(ToggleListenerGender);
        ((RadioGroup) view.findViewById(R.id.interest_group)).setOnCheckedChangeListener(ToggleListenerInterest);



        final LinearLayout ethnic_linear = (LinearLayout)view.findViewById(R.id.ethnicity_linear);
        final LinearLayout relation_linear = (LinearLayout)view.findViewById(R.id.require_relation);

        relation_text = (TextView)view.findViewById(R.id.relation_text);
        ethnicity_text = (TextView)view.findViewById(R.id.ethnicity_text);

        ethnic_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context wrapper = new ContextThemeWrapper(getActivity(), R.style.PopupMenu);
                PopupMenu popup = new PopupMenu(wrapper, ethnic_linear);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.ethnicit_menu, popup.getMenu());


                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        String title = item.getTitle().toString();

                        switch (title){
                            case "1. British":
                                ethnicity_text.setText(title.substring(3));
                                ethnicity = title.substring(3);
                                break;
                            case "2. Irish":
                                ethnicity_text.setText(title.substring(3));
                                ethnicity = title.substring(3);
                                break;
                            case "3. Any other White Background":
                                ethnicity_text.setText(title.substring(3));
                                ethnicity = title.substring(3);

                                break;
                            case "1. White and Black Caribbean":
                                ethnicity_text.setText(title.substring(3));
                                ethnicity = title.substring(3);
                                break;
                            case "2. White and Black African":
                                ethnicity_text.setText(title.substring(3));
                                ethnicity = title.substring(3);
                                break;
                            case "3. White and Asian":
                                ethnicity_text.setText(title.substring(3));
                                ethnicity = title.substring(3);

                                break;
                            case "4. Any other Mixed Background":
                                ethnicity_text.setText(title.substring(3));
                                ethnicity = title.substring(3);
                                break;
                            case "1. Pakistani":
                                ethnicity_text.setText(title.substring(3));
                                ethnicity = title.substring(3);
                                break;
                            case "2. Indian":
                                ethnicity_text.setText(title.substring(3));
                                ethnicity = title.substring(3);

                                break;
                            case "3. Bangladeshi":
                                ethnicity_text.setText(title.substring(3));
                                ethnicity = title.substring(3);
                                break;
                            case "4. Any other Asian Background":
                                ethnicity_text.setText(title.substring(3));
                                ethnicity = title.substring(3);
                                break;
                            case "1. Caribbean":
                                ethnicity_text.setText(title.substring(3));
                                ethnicity = title.substring(3);

                                break;
                            case "2. African":
                                ethnicity_text.setText(title.substring(3));
                                ethnicity = title.substring(3);
                                break;
                            case "3. Any other Black Background":
                                ethnicity_text.setText(title.substring(3));
                                ethnicity = title.substring(3);
                                break;
                            case "1. Chinese":
                                ethnicity_text.setText(title.substring(3));
                                ethnicity = title.substring(3);
                                break;
                            case "2. Any other Ethnic Group":
                                ethnicity_text.setText(title.substring(3));
                                ethnicity = title.substring(3);
                                break;
                            case "6. Not Stated":
                                ethnicity_text.setText(title.substring(3));
                                ethnicity = title.substring(3);
                                break;

                        }

                        return true;
                    }
                });

                popup.show(); //showing popup menu

            }
        });
        relation_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context wrapper = new ContextThemeWrapper(getActivity(), R.style.PopupMenu);
                PopupMenu popup = new PopupMenu(wrapper, relation_linear);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.relation_menu, popup.getMenu());


                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        String title = item.getTitle().toString();

                        switch (title){
                            case "Single":
                                relation_text.setText(title);
                               relation = title;
                                break;
                            case "Divorced":
                                relation_text.setText(title);
                                relation = title;
                                break;
                            case "Widowed":
                                relation_text.setText(title);
                                relation = title;

                                break;
                            case "Separated":
                                relation_text.setText(title);
                                relation = title;
                                break;
                            case "Married But Looking":
                                relation_text.setText(title);
                                relation = title;
                                break;
                            case "In an Open Relationship":
                                relation_text.setText(title);
                                relation = title;
                                break;
                        }

                        return true;
                    }
                });

                popup.show(); //showing popup menu

            }
        });


        Log.d("shani","age............................................................."+age);


       // profile.child("age").setValue(age);

        save_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 if (add_profile_image.getDrawable()==null && filePath==null){
                    Toast.makeText(getActivity(), "Please Select Profile Image..", Toast.LENGTH_SHORT).show();
                } else if (gender == null){
                     Toast.makeText(getActivity(), "Please Select Gender..", Toast.LENGTH_SHORT).show();
                 } else if (interest == null){
                     Toast.makeText(getActivity(), "Please Select Interest..", Toast.LENGTH_SHORT).show();
                 }else if (birth_date == null){
                     Toast.makeText(getActivity(), "Please select Birthday..", Toast.LENGTH_SHORT).show();
                 }else if (location.getText().toString().length()<6){
                     Toast.makeText(getActivity(), "Please Enter Location..", Toast.LENGTH_SHORT).show();
                 }else if (ethnicity==null || ethnicity.contains("--")){
                     Toast.makeText(getActivity(), "Please Select Ethnicity ..", Toast.LENGTH_SHORT).show();
                 } else if (relation==null || relation.contains("--")){
                     Toast.makeText(getActivity(), "Please Select Relationship ..", Toast.LENGTH_SHORT).show();
                 }else if (!check_travel.isChecked()  && !check_friendship.isChecked() && !check_longterm.isChecked() ){
                    Toast.makeText(getActivity(), "Please Select Travel For Check Box...", Toast.LENGTH_SHORT).show();
                }else if (experinceBox.getText().length()<6){
                    Toast.makeText(getActivity(), "Please Describe Your Experience...", Toast.LENGTH_SHORT).show();
                }else {

                   /*  if (filePath==null){

                         BitmapDrawable drawable = (BitmapDrawable) add_profile_image.getDrawable();
                         bitmap = drawable.getBitmap();
                         ByteArrayOutputStream bos = new ByteArrayOutputStream();
                         bitmap.compress(Bitmap.CompressFormat.JPEG, 0 *//*ignored for PNG*//*, bos);

                        *//* int byteSize = bitmap.getRowBytes() * bitmap.getHeight();
                         ByteBuffer byteBuffer = ByteBuffer.allocate(byteSize);
                         bitmap.copyPixelsToBuffer(byteBuffer);

                         byte[] byteArray = byteBuffer.array();
                          bs = new ByteArrayInputStream(byteArray);*//*

                         byte[] bitmapdata = bos.toByteArray();
                         bs = new ByteArrayInputStream(bitmapdata);

                     }*/


                    if (check_travel.isChecked()){
                        looking_travelPartner = "yes";
                    }else {
                        looking_travelPartner = "null";
                    }
                    if (check_friendship.isChecked()){
                        looking_friendship = "yes";
                    }else {
                        looking_friendship = "null";
                    }
                    if (check_longterm.isChecked()){
                        looking_longTerm = "yes";
                    }else {
                        looking_longTerm = "null";
                    }

                    location_address = location.getText().toString()+" , "+location_city.getText().toString()+" , "+location_country.getText().toString();
                     travel_experience = experinceBox.getText().toString();

                     st_address = location.getText().toString();
                     st_city = location_city.getText().toString();
                     st_country = location_country.getText().toString();



                    AsynchTaskUploadProfile profile_upload = new AsynchTaskUploadProfile();
                    profile_upload.execute();

                     progressDialog.dismiss();

                }
            }
        });




        return view;
    }

    final RadioGroup.OnCheckedChangeListener ToggleListenerGender = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(final RadioGroup radioGroup, final int i) {
            for (int j = 0; j < radioGroup.getChildCount(); j++) {
                final ToggleButton view = (ToggleButton) radioGroup.getChildAt(j);

                if (view.getId() == i){
                    view.setChecked(true);
                    view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.purple));
                    view.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                    gender = view.getText().toString();
                }else {
                    view.setChecked(false);
                    view.setBackgroundResource(R.drawable.border);
                    view.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                }

            }
        }
    };
    final RadioGroup.OnCheckedChangeListener ToggleListenerInterest = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(final RadioGroup radioGroup, final int i) {
            for (int j = 0; j < radioGroup.getChildCount(); j++) {
                final ToggleButton view = (ToggleButton) radioGroup.getChildAt(j);
                if (view.getId() == i){

                    view.setChecked(true);
                    view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.purple));
                    view.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                    interest = view.getText().toString();

                }else {
                    view.setChecked(false);
                    view.setBackgroundResource(R.drawable.border);
                    view.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                }

            }
        }
    };
    public void onToggleGender(View view) {
        ((RadioGroup)view.getParent()).check(view.getId());

        // app specific stuff ..
    }
    public void onToggleInterest(View view) {
        ((RadioGroup)view.getParent()).check(view.getId());

        // app specific stuff ..
    }

    String filePath;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case SELECTED_PICTURE:
                if(resultCode==RESULT_OK){
                    uri=data.getData();

                    Log.d("shani","uri ....."+uri);

                    String[]projection={MediaStore.Images.Media.DATA};

                    Cursor cursor = getApplicationContext().getContentResolver().query(uri, projection, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex=cursor.getColumnIndex(projection[0]);
                    filePath=cursor.getString(columnIndex);
                    cursor.close();


                    Bitmap yourSelectedImage= BitmapFactory.decodeFile(filePath);
                    Log.d("shani","bitmao in profile........"+yourSelectedImage);


                    if (yourSelectedImage.getHeight()>1200 || yourSelectedImage.getWidth()>1200){
                        ParcelFileDescriptor parcelFileDescriptor = null;
                        try {
                            parcelFileDescriptor = getApplicationContext().getContentResolver().openFileDescriptor(uri, "r");
                            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();

                            yourSelectedImage =decodeSampledBitmapFromResource(fileDescriptor, 1200, 1200);
                            parcelFileDescriptor.close();

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    Drawable d=new BitmapDrawable(yourSelectedImage);

//                    circleImageView.setBackground(d);

                    try {
                        scaledBitmap = modifyOrientation(yourSelectedImage,filePath);

                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100 /*ignored for PNG*/, bos);
                        byte[] bitmapdata = bos.toByteArray();

                        bs = new ByteArrayInputStream(bitmapdata);


                      /*  File file = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");
                        FileOutputStream fo = new FileOutputStream(file);
                        fo.write(bitmapdata);
                        fo.flush();
                        fo.close();*/


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // Picasso.with(this).load(filePath).centerCrop().into(add_profile_image);
                    add_profile_image.setImageBitmap(scaledBitmap);
                    Log.i("Path", filePath);
                }
                break;

            default:
                break;
        }

    }
    public static Bitmap decodeSampledBitmapFromResource(FileDescriptor fileDescriptor, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fileDescriptor,null,options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
    }
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
    public static Bitmap modifyOrientation(Bitmap bitmap, String image_absolute_path) throws IOException {
        ExifInterface ei = new ExifInterface(image_absolute_path);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotate(bitmap, 90);

            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotate(bitmap, 180);

            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotate(bitmap, 270);

            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                return flip(bitmap, true, false);

            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                return flip(bitmap, false, true);

            default:
                return bitmap;
        }
    }
    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public class AsynchTaskDownloadProfile extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {


            users = databaseReference.child(fire_user.getUid());
            profile = users.child("profile");

            Log.d("shani","email ....."+mail);

            fire_user.getProviderId();
            Log.d("shani","privider id........."+fire_user.getProviderId());
            Log.d("shani","get id token true........."+fire_user.getIdToken(true));
            Log.d("shani","privider s........."+fire_user.getProviders());
            Log.d("shani","privider data........."+fire_user.getProviderData());


            Log.d("shani","phoneNumber ......"+ fire_user.getPhoneNumber());
            Log.d("shani","photourl ......"+ fire_user.getPhotoUrl());



                    Log.d("shani","location address testing ....."+location_address);


            if (profile == null){
                // if empty
            }else {
                profile.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                            Log.d("shani","profiledata....."+dataSnapshot);
                            Log.d("shani","profiledata address....."+dataSnapshot.child("address").getValue());

                            location.setText(dataSnapshot.child("address").getValue().toString());
                            location_city.setText(dataSnapshot.child("city").getValue().toString());
                            location_country.setText(dataSnapshot.child("country").getValue().toString());

                            String get_age = dataSnapshot.child("age").getValue().toString();
                            Log.d("shani","get_age................................."+get_age);

                            String get_gender = dataSnapshot.child("gender").getValue().toString();
                            if (get_gender.equals("Male")){
                               // gender_male.setChecked(true);
                                gender_group.check(R.id.toggleMale);
                            }else {
                                //toggleFemale.setChecked(true);
                                gender_group.check(R.id.toggleFemale);
                            }

                            String interested_in = dataSnapshot.child("interest_in").getValue().toString();
                            if (interested_in.equals("Both")){
                               // toggleBoth.setChecked(true);
                                interest_group.check(R.id.toggleBoth);
                            }else if (interested_in.equals("Women")){
                               // toggleWomen.setChecked(true);
                                interest_group.check(R.id.toggleWomen);
                            }else {
                               // toggleMen.setChecked(true);
                                interest_group.check(R.id.toggleMen);
                            }

                            String get_experience = dataSnapshot.child("travel_experience").getValue().toString();
                            experinceBox.setText(get_experience);

                            get_day = dataSnapshot.child("day").getValue().toString();
                            get_month = dataSnapshot.child("month").getValue().toString();
                            get_year = dataSnapshot.child("year").getValue().toString();


                        Log.d("shani","day  ...."+dataSnapshot.child("day").getValue());
                        Log.d("shani","month  ...."+dataSnapshot.child("month").getValue());
                        Log.d("shani","year  ...."+dataSnapshot.child("year").getValue());

                            age = String.valueOf(getAge(Integer.parseInt(get_day),Integer.parseInt(get_month),Integer.parseInt(get_year)));

                            Log.d("shani","day of birth within ...."+dataSnapshot.child("day").getValue());
                            birth_date = get_day+"   /   "+get_month+"   /   "+get_year;

                            bith.setText(new StringBuilder()
                                    .append(get_day).append("\t/\t").append(get_month).append("\t/\t").append(get_year).append(" "));

                            String get_freindship = dataSnapshot.child("friendship").getValue().toString();
                            String get_long_term_relation = dataSnapshot.child("long_term_relationship").getValue().toString();
                            String get_travel_partner = dataSnapshot.child("travel_partner").getValue().toString();

                            if (get_freindship.equals("yes")){

                                check_friendship.setChecked(true);
                            }
                            if (get_long_term_relation.equals("yes")){

                                check_longterm.setChecked(true);
                            }
                            if (get_travel_partner.equals("yes")){

                                check_travel.setChecked(true);
                            }


                            String get_ethnicity = dataSnapshot.child("ethnicity").getValue().toString();
                            String get_relationship = dataSnapshot.child("relationship").getValue().toString();

                            ethnicity_text.setText(get_ethnicity);
                            ethnicity = get_ethnicity;
                            relation_text.setText(get_relationship);
                            relation = get_relationship;

                        String get_imagePath = dataSnapshot.child("image_url").getValue().toString();

                        Glide.with(getApplicationContext()).load(get_imagePath).asBitmap().into(add_profile_image);

                        Log.d("shani","profiledata image url....."+dataSnapshot.child("image_url").getValue().toString());


                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
            }




            Log.d("shani","day of birth end ...."+get_day);


            return null;
        }
        @Override
        protected void onPreExecute() {super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(),"","Retrieving Profile UsersData.....", true);}
        @Override
        protected void onPostExecute(String s) {super.onPostExecute(s);
            progressDialog.dismiss();}
        @Override
        protected void onProgressUpdate(String... values) {super.onProgressUpdate(values);}
    }

    public class AsynchTaskUploadProfile extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {


            fire_user = FirebaseAuth.getInstance().getCurrentUser();
            assert fire_user != null;
            String mail= fire_user.getEmail();
            Log.d("shani","email ....."+mail);

            fire_user.getProviderId();
            Log.d("shani","privider id........."+fire_user.getProviderId());
            Log.d("shani","get id token true........."+fire_user.getIdToken(true));
            Log.d("shani","privider s........."+fire_user.getProviders());
            Log.d("shani","privider data........."+fire_user.getProviderData());


            Log.d("shani","phoneNumber ......"+ fire_user.getPhoneNumber());
            Log.d("shani","photourl ......"+ fire_user.getPhotoUrl());

            if (filePath == null){

                profile.child("gender").setValue(gender);
                profile.child("interest_in").setValue(interest);
                // DatabaseReference birth_date = profile.child("birth_date");
                profile.child("day").setValue(get_day);
                profile.child("month").setValue(get_month);
                profile.child("year").setValue(get_year);
                profile.child("age").setValue(age);
                profile.child("address").setValue(st_address);
                profile.child("city").setValue(st_city);
                profile.child("country").setValue(st_country);
                // DatabaseReference require = profile.child("required");
                profile.child("ethnicity").setValue(ethnicity);
                profile.child("relationship").setValue(relation);
                //DatabaseReference looking_for = profile.child("looking_for");
                profile.child("travel_partner").setValue(looking_travelPartner);
                profile.child("friendship").setValue(looking_friendship);
                profile.child("long_term_relationship").setValue(looking_longTerm);
                profile.child("travel_experience").setValue(travel_experience);

                all_users_uid.child("age").setValue(age);
                all_users_uid.child("address").setValue(location_address);

            }else {

                mStorageRef = FirebaseStorage.getInstance().getReference();
                StorageReference chilRef = mStorageRef.child("images2/"+mail+".jpeg");

                UploadTask uploadTask = chilRef.putStream(bs);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Log.d("shani","downloadURi exception ....."+exception);

                        profile.child("gender").setValue(gender);
                        profile.child("interest_in").setValue(interest);
                        // DatabaseReference birth_date = profile.child("birth_date");
                        profile.child("day").setValue(get_day);
                        profile.child("month").setValue(get_month);
                        profile.child("year").setValue(get_year);
                        profile.child("age").setValue(age);
                        profile.child("address").setValue(st_address);
                        profile.child("city").setValue(st_city);
                        profile.child("country").setValue(st_country);
                        // DatabaseReference require = profile.child("required");
                        profile.child("ethnicity").setValue(ethnicity);
                        profile.child("relationship").setValue(relation);
                        //DatabaseReference looking_for = profile.child("looking_for");
                        profile.child("travel_partner").setValue(looking_travelPartner);
                        profile.child("friendship").setValue(looking_friendship);
                        profile.child("long_term_relationship").setValue(looking_longTerm);
                        profile.child("travel_experience").setValue(travel_experience);

                        all_users_uid.child("age").setValue(age);
                        all_users_uid.child("address").setValue(location_address);


                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        downloadUrl = taskSnapshot.getDownloadUrl();
                        Log.d("shani","downloadURi ....."+downloadUrl);
                        String downloadimageAddress = downloadUrl.toString();

                        Log.d("shani","download url in testing ....."+downloadimageAddress);
                        Log.d("shani","location address testing ....."+location_address);

                        profile.child("gender").setValue(gender);
                        profile.child("interest_in").setValue(interest);
                        // DatabaseReference birth_date = profile.child("birth_date");
                        profile.child("day").setValue(get_day);
                        profile.child("month").setValue(get_month);
                        profile.child("year").setValue(get_year);
                        profile.child("age").setValue(age);
                        profile.child("address").setValue(st_address);
                        profile.child("city").setValue(st_city);
                        profile.child("country").setValue(st_country);
                        // DatabaseReference require = profile.child("required");
                        profile.child("ethnicity").setValue(ethnicity);
                        profile.child("relationship").setValue(relation);
                        //DatabaseReference looking_for = profile.child("looking_for");
                        profile.child("travel_partner").setValue(looking_travelPartner);
                        profile.child("friendship").setValue(looking_friendship);
                        profile.child("long_term_relationship").setValue(looking_longTerm);
                        profile.child("travel_experience").setValue(travel_experience);
                        profile.child("image_url").setValue(downloadimageAddress);

                        all_users_uid.child("age").setValue(age);
                        all_users_uid.child("image_url").setValue(downloadimageAddress);
                        all_users_uid.child("address").setValue(location_address);
                        all_users_uid.child("gender").setValue(gender);
                    }
                });
            }







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

    public int getAge (int _year, int _month, int _day) {

        GregorianCalendar cal = new GregorianCalendar();
        int y, m, d, a;

        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH);
        d = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(_year, _month, _day);
        a = y - cal.get(Calendar.YEAR);
        if ((m < cal.get(Calendar.MONTH))
                || ((m == cal.get(Calendar.MONTH)) && (d < cal
                .get(Calendar.DAY_OF_MONTH)))) {
            --a;
        }
        if(a < 0)
            throw new IllegalArgumentException("Age < 0");
        return a;
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
    public class AsynchLocation extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {


            OkHttpClient client;

            //for increase time to get response from server

            OkHttpClient.Builder builder = new OkHttpClient.Builder();


            client = builder.build();


            Request request = new Request.Builder()
                    .url("http://ip-api.com/json")
                    .build();


            try {
                Response response = client.newCall(request).execute();

                if (response.isSuccessful()) {

                    final String json_string = response.body().string();
                    Log.e("shani", "Lender wifis post response suuccessfull ====  : " + json_string);
                    JSONObject jsonobj = new JSONObject(json_string);

                    String status = jsonobj.getString("status");
                    final String country = jsonobj.getString("country");
                    final String city = jsonobj.getString("city");

                    Log.e("shani","status ....."+status);
                    Log.e("shani","country ....."+country);
                    Log.e("shani","city ....."+city);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           location_country.setText(country);
                           location_city.setText(city);
                        }
                    });

                } else {
                    Log.e("shani", "Lender wifis get response unsuccessful : " + response.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }
    class DatePickerFragmentBirth extends DialogFragment implements DatePickerDialog.OnDateSetListener {


        static final int BIRTH_DATE = 3;

        private int mChosenDate;

        int cur = 0;


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog;

            Bundle bundle = this.getArguments();
            if(bundle != null){
                mChosenDate = bundle.getInt("DATE",1);
            }

            if (get_day!=null || get_month!=null || get_year!=null){
                datePickerDialog = new DatePickerDialog(getActivity(), this, Integer.parseInt(get_year), Integer.parseInt(get_month)-1, Integer.parseInt(get_day));
            }else {
                datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
            }

           // datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                    return datePickerDialog;



        }


        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {

            age = String.valueOf(getAge(year,month,day));

                Log.d("Date fin","Date3 : " + new StringBuilder().append(month + 1)
                        .append("/").append(day).append("/").append(year)
                        .append(" "));

                bith.setText(new StringBuilder()
                       .append(day).append("\t/\t").append(month + 1).append("\t/\t").append(year).append(" "));
           birth_date = day+"   /   "+month+"   /   "+year;

            birth_day = String.valueOf(day);
            birth_month = String.valueOf(month);
            birth_year = String.valueOf(year);

            get_day = birth_day;
            get_month = birth_month;
            get_year = birth_year;


        }


    }
}
