package com.shaheen.a5thgeneration.signupproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
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
import com.kyleduo.switchbutton.SwitchButton;
import com.mukesh.image_processing.ImageProcessor;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static android.R.attr.bitmap;

public class SettingsActivity extends AppCompatActivity{


    public static final int REQUEST_CODE = 1434 ;

    TextView seek_min_dis,seek_min_age,seek_max_age,save_settings;
    EditText username;
    LinearLayout photo_effects;
    SwitchButton showswitchButton,swipeswitchButton;
    CrystalSeekbar rangeSeekbar;
    CrystalRangeSeekbar crystalRangeSeekbar;
    ImageView setting_image;

    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    DatabaseReference all_users,all_users_uid;
    FirebaseUser fire_user;
    StorageReference mStorageRef;
    ByteArrayInputStream bs;
    DatabaseReference users;
    DatabaseReference settings;
    Uri downloadUrl;
    RadioGroup show_group;
    RadioButton show_women,show_men;

    String showMe,maxDistance_text,minAge_text,maxAge_text,username_text,show_me_on_sugar_vocay_txt = "no",swipe_with_frnds_txt = "no";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);



        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Settings");
        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }

                }

        );

        show_group = (RadioGroup)findViewById(R.id.show_group);
        show_women = (RadioButton)findViewById(R.id.show_women);
        show_men = (RadioButton)findViewById(R.id.show_men);

        username = (EditText)findViewById(R.id.username);
        seek_min_dis = (TextView)findViewById(R.id.seek_min_dis);
        save_settings = (TextView)findViewById(R.id.save_settings);
        seek_min_age = (TextView)findViewById(R.id.seek_min_age);
        seek_max_age = (TextView)findViewById(R.id.seek_max_age);
        setting_image = (ImageView) findViewById(R.id.setting_image);
        photo_effects = (LinearLayout)findViewById(R.id.photo_effects);
        showswitchButton = (SwitchButton)findViewById(R.id.show_switch);
        swipeswitchButton = (SwitchButton)findViewById(R.id.swipe_switch);
        rangeSeekbar = (CrystalSeekbar)findViewById(R.id.rangeSeekbar);
        crystalRangeSeekbar = (CrystalRangeSeekbar)findViewById(R.id.rangeSeekbar_range);



        fire_user = FirebaseAuth.getInstance().getCurrentUser();
        assert fire_user != null;
        String mail= fire_user.getEmail();
        Log.d("shani","email ....."+mail);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        users = databaseReference.child(fire_user.getUid());
        settings = users.child("settings");

        AsynchTaskDownloadSettings asynchTaskDownloadSettings = new AsynchTaskDownloadSettings();
        asynchTaskDownloadSettings.execute();
        progressDialog.dismiss();


        Intent inet = getIntent();

        if (inet.getStringExtra("from")!=null){
            String result = inet.getStringExtra("imagepath");
            Log.d("shani","activity result path image......"+result);
             Bitmap img = BitmapFactory.decodeFile(result);
             setting_image.setImageBitmap(img);
        }

       // username_text = username.getText().toString();
        showMe = "Women";
        show_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

                if (i==R.id.show_women){
                    showMe = show_women.getText().toString();
                }else {
                    showMe = show_men.getText().toString();
                }
            }
        });

        photo_effects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("shani"," start activity clicked");

                Bitmap image=((BitmapDrawable)setting_image.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] bytes = stream.toByteArray();
                Log.d("shani"," byte array put "+bytes);
               Intent intent = new Intent(SettingsActivity.this,ImageeffectActivity.class);
                intent.putExtra("input_image",bytes);
               startActivityForResult(intent,REQUEST_CODE);

            }
        });

        rangeSeekbar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                seek_min_dis.setText(value.toString());
                maxDistance_text = value.toString();
            }
        });
        crystalRangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                seek_min_age.setText(minValue.toString()+"  -");
                seek_max_age.setText("  "+maxValue.toString());
                minAge_text = minValue.toString();
                maxAge_text = maxValue.toString();
            }
        });
        swipeswitchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b){
                    swipeswitchButton.setBackColorRes(R.color.purple);
                    swipe_with_frnds_txt = "yes";
                }else {
                    swipeswitchButton.setBackColorRes(R.color.colorSwitchBack_light);
                    swipe_with_frnds_txt = "no";
                }
            }
        });
        showswitchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b){
                    showswitchButton.setBackColorRes(R.color.purple);
                    show_me_on_sugar_vocay_txt = "yes";
                }else {
                    showswitchButton.setBackColorRes(R.color.colorSwitchBack_light);
                    show_me_on_sugar_vocay_txt = "no";
                }
            }
        });
        save_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().length()<1){
                    Toast.makeText(getApplicationContext(), "Please Enter Username", Toast.LENGTH_SHORT).show();
                }else {

                    username_text = username.getText().toString();

                    AsynchTaskUploadSettings uploadSettings = new AsynchTaskUploadSettings();
                    uploadSettings.execute();

                }
            }
        });
    }
    public class AsynchTaskUploadSettings extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(final String... strings) {

            fire_user = FirebaseAuth.getInstance().getCurrentUser();
            assert fire_user != null;
            String mail= fire_user.getEmail();
            Log.d("shani","email ....."+mail);

            mAuth = FirebaseAuth.getInstance();
            databaseReference = FirebaseDatabase.getInstance().getReference();
            users = databaseReference.child(fire_user.getUid());
            settings = users.child("settings");
            all_users = databaseReference.child("all_users");
            all_users_uid = all_users.child(fire_user.getUid());


          /*  mStorageRef = FirebaseStorage.getInstance().getReference();
            StorageReference chilRef = mStorageRef.child("images2/"+mail+".jpeg");*/

          /*  UploadTask uploadTask = chilRef.putStream(bs);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {*/
                    // Handle unsuccessful uploads
                   // Log.d("shani","downloadURi exception ....."+exception);


                    settings.child("show_me").setValue(showMe);
                    settings.child("maximum_distance").setValue(maxDistance_text);
                    settings.child("age_from").setValue(minAge_text);
                    settings.child("age_to").setValue(maxAge_text);
                    settings.child("show_me_on_sugar_vocay").setValue(show_me_on_sugar_vocay_txt);
                    settings.child("swipe_with_friends").setValue(swipe_with_frnds_txt);
                    settings.child("profile_name").setValue(username_text);

                    all_users_uid.child("profile_name").setValue(username_text);

           /*     }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    downloadUrl = taskSnapshot.getDownloadUrl();
                    Log.d("shani","downloadURi ....."+downloadUrl);
                    String downloadimageAddress = downloadUrl.toString();

                    Log.d("shani","download url in testing ....."+downloadimageAddress);

                    settings.child("show_me").setValue(showMe);
                    settings.child("maximum_distance").setValue(maxDistance_text);
                    settings.child("age_from").setValue(minAge_text);
                    settings.child("age_to").setValue(maxAge_text);
                    settings.child("show_me_on_sugar_vocay").setValue(show_me_on_sugar_vocay_txt);
                    settings.child("swipe_with_friends").setValue(swipe_with_frnds_txt);
                    settings.child("profile_name").setValue(username_text);
                    settings.child("image_url").setValue(downloadimageAddress);
                }
            });*/





            return null;
        }
        @Override
        protected void onPreExecute() {super.onPreExecute();
            progressDialog = ProgressDialog.show(SettingsActivity.this,"","Uploading Profile UsersData.....", true);}
        @Override
        protected void onPostExecute(String s) {super.onPostExecute(s);
            progressDialog.dismiss();}
        @Override
        protected void onProgressUpdate(String... values) {super.onProgressUpdate(values);}
    }
    public class AsynchTaskDownloadSettings extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(final String... strings) {


            fire_user = FirebaseAuth.getInstance().getCurrentUser();
            assert fire_user != null;
            String mail= fire_user.getEmail();
            Log.d("shani","email ....."+mail);

            mAuth = FirebaseAuth.getInstance();
            databaseReference = FirebaseDatabase.getInstance().getReference();
            users = databaseReference.child(fire_user.getUid());
            settings = users.child("settings");



            settings.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (!dataSnapshot.child("show_me").getValue().toString().equals(null)){
                        String getShowMe = dataSnapshot.child("show_me").getValue().toString();
                        if (getShowMe.equals("Men")){
                            show_men.setChecked(true);
                        }else {
                            show_women.setChecked(true);
                        }
                    }


                    String getMaxDis = dataSnapshot.child("maximum_distance").getValue().toString();
                    int maxDisInt = Integer.parseInt(getMaxDis);
                    rangeSeekbar.setMinStartValue(maxDisInt);
                    rangeSeekbar.apply();

                    String getAgeFrom = dataSnapshot.child("age_from").getValue().toString();
                    String getAgeTo = dataSnapshot.child("age_to").getValue().toString();
                    int ageFromInt = Integer.parseInt(getAgeFrom);
                    int ageToInt = Integer.parseInt(getAgeTo);
                    float first = Float.valueOf(getAgeFrom);
                    float flll = Float.valueOf(getAgeTo);
                   // crystalRangeSeekbar.setLeft(ageFromInt);
                   // crystalRangeSeekbar.setRight(ageToInt);
                    crystalRangeSeekbar.setMinStartValue(first);
                    crystalRangeSeekbar.setMaxStartValue(flll);
                    crystalRangeSeekbar.apply();

                    String showMeOnSugerVocay = dataSnapshot.child("show_me_on_sugar_vocay").getValue().toString();
                    if (showMeOnSugerVocay.equals("no")){
                        showswitchButton.setChecked(false);
                    }else {
                        showswitchButton.setChecked(true);
                    }

                    String swipeWithFriends = dataSnapshot.child("swipe_with_friends").getValue().toString();
                    if (swipeWithFriends.equals("no")){
                        swipeswitchButton.setChecked(false);
                    }else {
                        swipeswitchButton.setChecked(true);
                    }

                    String getProfile = dataSnapshot.child("profile_name").getValue().toString();
                    username.setText(getProfile);
                    username_text = getProfile;

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });


            return null;
        }
        @Override
        protected void onPreExecute() {super.onPreExecute();
            progressDialog = ProgressDialog.show(SettingsActivity.this,"","Uploading Profile UsersData.....", true);}
        @Override
        protected void onPostExecute(String s) {super.onPostExecute(s);
            progressDialog.dismiss();}
        @Override
        protected void onProgressUpdate(String... values) {super.onProgressUpdate(values);}
    }
}
