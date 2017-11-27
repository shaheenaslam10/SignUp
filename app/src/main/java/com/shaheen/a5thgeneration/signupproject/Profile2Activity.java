package com.shaheen.a5thgeneration.signupproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shaheen.a5thgeneration.signupproject.DataModels.profile_data_upload;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class Profile2Activity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {

    EditText experinceBox;
    CheckBox check_travel,check_friendship,check_longterm;
    Button save_profile;
    String looking_travelPartner , looking_friendship,looking_longTerm;
    ProgressDialog progressDialog;
    String ResultString,myBase64Image;
    Bitmap scaledImage;
    String intent_gender,intent_interest,intent_birthdate,intent_location;

    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    FirebaseUser fire_user;

    TextView ethnicity_text,relation_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        Log.d("shani","gender..."+intent.getStringExtra("gender"));
        Log.d("shani","interest...."+intent.getStringExtra("interest"));
        Log.d("shani","birthdate...."+intent.getStringExtra("birthdate"));
        Log.d("shani","location...."+intent.getStringExtra("location"));
        Log.d("shani","uri...."+intent.getStringExtra("uri"));
        Log.d("shani","image...."+intent.getStringExtra("image"));


        intent_gender = intent.getStringExtra("gender");
        intent_interest = intent.getStringExtra("interest");
        intent_birthdate = intent.getStringExtra("birthdate");
        intent_location = intent.getStringExtra("location");


        Uri uri = Uri.parse(intent.getStringExtra("uri"));
        String imagePath = intent.getStringExtra("image");
        /// have to get bitmap

        ParcelFileDescriptor parcelFileDescriptor =
                null;
        try {
            parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();

             Bitmap yourSelectedImage =decodeSampledBitmapFromResource(fileDescriptor, 1000, 1000);
            Log.d("shani","bitmao in profile 2........"+yourSelectedImage);
            parcelFileDescriptor.close();


            scaledImage = modifyOrientation(yourSelectedImage,imagePath );
            myBase64Image = encodeToBase64(scaledImage, Bitmap.CompressFormat.PNG, 100);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

       // Spinner spin_require_ethnicit = (Spinner) findViewById(R.id.spin_require_ethnicit);
        //Spinner spin_require_relation = (Spinner) findViewById(R.id.spin_require_relation);
        final LinearLayout ethnic_linear = (LinearLayout)findViewById(R.id.ethnicity_linear);
        final LinearLayout relation_linear = (LinearLayout)findViewById(R.id.require_relation);

        relation_text = (TextView)findViewById(R.id.relation_text);
        ethnicity_text = (TextView)findViewById(R.id.ethnicity_text);

        ethnic_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context wrapper = new ContextThemeWrapper(Profile2Activity.this, R.style.PopupMenu);
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
                                break;
                            case "2. Irish":
                                ethnicity_text.setText(title.substring(3));
                                break;
                            case "3. Any other White Background":
                                ethnicity_text.setText(title.substring(3));

                                break;
                            case "1. White and Black Caribbean":
                                ethnicity_text.setText(title.substring(3));
                                break;
                            case "2. White and Black African":
                                ethnicity_text.setText(title.substring(3));
                                break;
                            case "3. White and Asian":
                                ethnicity_text.setText(title.substring(3));

                                break;
                            case "4. Any other Mixed Background":
                                ethnicity_text.setText(title.substring(3));
                                break;
                            case "1. Pakistani":
                                ethnicity_text.setText(title.substring(3));
                                break;
                            case "2. Indian":
                                ethnicity_text.setText(title.substring(3));

                                break;
                            case "3. Bangladeshi":
                                ethnicity_text.setText(title.substring(3));
                                break;
                            case "4. Any other Asian Background":
                                ethnicity_text.setText(title.substring(3));
                                break;
                            case "1. Caribbean":
                                ethnicity_text.setText(title.substring(3));

                                break;
                            case "2. African":
                                ethnicity_text.setText(title.substring(3));
                                break;
                            case "3. Any other Black Background":
                                ethnicity_text.setText(title.substring(3));
                                break;
                            case "1. Chinese":
                                ethnicity_text.setText(title.substring(3));
                                break;
                            case "2. Any other Ethnic Group":
                                ethnicity_text.setText(title.substring(3));
                                break;
                            case "6. Not Stated":
                                ethnicity_text.setText(title.substring(3));
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

                Context wrapper = new ContextThemeWrapper(Profile2Activity.this, R.style.PopupMenu);
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
                                break;
                            case "Divorced":
                                relation_text.setText(title);
                                break;
                            case "Widowed":
                                relation_text.setText(title);

                                break;
                            case "Separated":
                                relation_text.setText(title);
                                break;
                            case "Married But Looking":
                                relation_text.setText(title);
                                break;
                            case "In an Open Relationship":
                                relation_text.setText(title);

                                break;
                        }

                        return true;
                    }
                });

                popup.show(); //showing popup menu

            }
        });


      /*  spin_require_relation.setOnItemSelectedListener(this);

        List<String> relation = new ArrayList<String>();
        relation.add("---");
        relation.add("Single");
        relation.add("Divorced");
        relation.add("Widowed");
        relation.add("Separated");
        relation.add("Married But Looking");
        relation.add("In an Open Relationship");

        ArrayAdapter<String> dataAdapter_relation = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, relation);
        dataAdapter_relation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_require_relation.setAdapter(dataAdapter_relation);*/


        experinceBox = (EditText)findViewById(R.id.experinceBox);

        check_travel = (CheckBox)findViewById(R.id.check_travel);
        check_friendship = (CheckBox)findViewById(R.id.check_friendship);
        check_longterm = (CheckBox)findViewById(R.id.check_longterm);

        save_profile = (Button)findViewById(R.id.save_profile);
        save_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!check_travel.isChecked()  && !check_friendship.isChecked() && !check_longterm.isChecked() ){
                    Toast.makeText(Profile2Activity.this, "Please Select Travel For Check Box...", Toast.LENGTH_SHORT).show();
                }else if (experinceBox.getText().length()<6){
                    Toast.makeText(Profile2Activity.this, "Please Describe Your Experience...", Toast.LENGTH_SHORT).show();
                }else {

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


                    AsynchTaskUploadProfile profile_upload = new AsynchTaskUploadProfile();
                    profile_upload.execute();




                }
            }
        });



    }
    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {

        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

        String item = adapterView.getItemAtPosition(position).toString();

        if (adapterView.getId()==R.id.spin_month){

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return false;
    }

    public class AsynchTaskUploadProfile extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {


                Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                        "cloud_name", "dhfbugzaw",
                        "api_key", "114627725894716",
                        "api_secret", "jZShCVUaWUjkmU_8GdJGin2ab9g"));

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
                sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                System.out.println(sdf.format(new Date())); //-prints-> 2015-01-22T03:23:26Z
                //Map uploadResult = cloudinary.uploader().upload("data:image/png;base64,"+imageB64, ObjectUtils.asMap("timestamp","1493626250917 "));
                Map uploadResult = null;
                try {
                    uploadResult = cloudinary.uploader().upload("data:image/png;base64," + myBase64Image, ObjectUtils.emptyMap());
                    ResultString = uploadResult.get("url").toString();
                    Log.d("shani","response url......"+ResultString);


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NullPointerException n) {
                    n.printStackTrace();
                }



            fire_user = FirebaseAuth.getInstance().getCurrentUser();
            assert fire_user != null;
            String mail= fire_user.getEmail();

            DatabaseReference users = databaseReference.child("users");
            users.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot user_uids : dataSnapshot.getChildren()) {
                        Log.d("shani","in for loop :");
                        DatabaseReference user_id  = user_uids.getRef();
                        String userid_key = user_id.getKey();

                        Log.d("shani","user_ids from firebase  .......:"+user_id);
                        Log.d("shani","user_ids from from current user  .......:"+fire_user.getUid());

                        if (userid_key.equals(fire_user.getUid())){
                            Log.d("shani","in if condition true :");
                            DatabaseReference profile_data = user_id.child("profile_data");

                            profile_data_upload data = new profile_data_upload();
                            data.setGender(intent_gender);
                            data.setInterested_in(intent_interest);
                            data.setBirthdate(intent_birthdate);
                            data.setLocation(intent_location);
                            data.setLooking_for_travel_partner(looking_travelPartner);
                            data.setLooking_for_friendship(looking_friendship);
                            data.setLooking_for_longterm_relation(looking_longTerm);
                            data.setTravel_experince(experinceBox.getText().toString());
                            data.setImage_path(ResultString);

                            profile_data.setValue(data);

                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });


          /*  progressDialog.dismiss();
            Intent intent = new Intent(Profile2Activity.this,OwnerProfileActivity.class);
            startActivity(intent);
            finish();*/

            return null;
        }
        @Override
        protected void onPreExecute() {super.onPreExecute();
            progressDialog = ProgressDialog.show(Profile2Activity.this,"","Uploading Profile UsersData.....", true);}
        @Override
        protected void onPostExecute(String s) {super.onPostExecute(s);
        progressDialog.dismiss();}
        @Override
        protected void onProgressUpdate(String... values) {super.onProgressUpdate(values);}
    }

}
