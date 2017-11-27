package com.shaheen.a5thgeneration.signupproject;

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
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {

    Button next1;
    String gender,interest;
    ToggleButton gender_male,interest_men;
    private static final int SELECTED_PICTURE = 1011;
    CircleImageView add_profile_image;
    Bitmap scaledBitmap;
    String day,month,year;
    EditText location;
    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        location = (EditText) findViewById(R.id.location);
        next1 = (Button)findViewById(R.id.next1);
        add_profile_image = (CircleImageView)findViewById(R.id.add_profile_image);
        gender_male = (ToggleButton) findViewById(R.id.toggleMale);
        interest_men = (ToggleButton) findViewById(R.id.toggleMen);

        gender = gender_male.getText().toString();
        interest = interest_men.getText().toString();

        add_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, SELECTED_PICTURE);

            }
        });

        next1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("shani","clicked.....");

                if (location.getText().toString().length()<6){
                    Toast.makeText(ProfileActivity.this, "Please Enter Location..", Toast.LENGTH_SHORT).show();
                } else if (filePath==null){
                    Toast.makeText(ProfileActivity.this, "Please Select Profile Image..", Toast.LENGTH_SHORT).show();
                }else {

                    Log.d("shani","clicked in else.....");
                    String date = day+"-"+month+"-"+year;


                    Intent intent = new Intent(ProfileActivity.this,Profile2Activity.class);
                    intent.putExtra("gender",gender);
                    intent.putExtra("interest",interest);
                    intent.putExtra("birthdate",date);
                    intent.putExtra("location",location.getText().toString());
                   intent.putExtra("image",filePath);
                   intent.putExtra("uri",uri.toString());
                    startActivity(intent);
                }

            }
        });

        Spinner spinner_months = (Spinner) findViewById(R.id.spin_month);
        Spinner spinner_days = (Spinner) findViewById(R.id.spin_date);
        Spinner spinner_years = (Spinner) findViewById(R.id.spin_year);

        // Spinner click listener
        spinner_months.setOnItemSelectedListener(this);
        spinner_days.setOnItemSelectedListener(this);
        spinner_years.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> months = new ArrayList<String>();
        months.add("January");
        months.add("February");
        months.add("March");
        months.add("April");
        months.add("May");
        months.add("June");
        months.add("July");
        months.add("August");
        months.add("September");
        months.add("October");
        months.add("November");
        months.add("December");
        List<String> days = new ArrayList<String>();
        days.add("1");
        days.add("2");
        days.add("3");
        days.add("4");
        days.add("5");
        days.add("6");
        days.add("7");
        days.add("8");
        days.add("9");
        days.add("10");
        days.add("11");
        days.add("13");
        days.add("14");
        days.add("15");
        days.add("16");
        days.add("17");
        days.add("18");
        days.add("19");
        days.add("20");
        days.add("21");
        days.add("22");
        days.add("23");
        days.add("24");
        days.add("25");
        days.add("26");
        days.add("27");
        days.add("28");
        days.add("29");
        days.add("30");
        days.add("31");

        List<String> years = new ArrayList<String>();
        years.add("1980");
        years.add("1981");
        years.add("1982");
        years.add("1983");
        years.add("1984");
        years.add("1985");
        years.add("1986");
        years.add("1987");
        years.add("1988");
        years.add("1989");
        years.add("1990");
        years.add("1991");
        years.add("1992");
        years.add("1993");
        years.add("1994");
        years.add("1995");
        years.add("1996");
        years.add("1997");
        years.add("1998");
        years.add("1999");
        years.add("2000");
        years.add("2001");
        years.add("2002");
        years.add("2003");
        years.add("2004");
        years.add("2005");
        years.add("2006");
        years.add("2007");
        years.add("2008");
        years.add("2009");
        years.add("2010");
        years.add("2011");
        years.add("2012");
        years.add("2013");
        years.add("2014");
        years.add("2015");
        years.add("2016");
        years.add("2017");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter_months = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, months);
        dataAdapter_months.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_months.setAdapter(dataAdapter_months);

        ArrayAdapter<String> dataAdapter_days = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, days);
        dataAdapter_days.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_days.setAdapter(dataAdapter_days);

        ArrayAdapter<String> dataAdapter_years = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);
        dataAdapter_years.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_years.setAdapter(dataAdapter_years);


        ((RadioGroup) findViewById(R.id.gender_group)).setOnCheckedChangeListener(ToggleListenerGender);
        ((RadioGroup) findViewById(R.id.interest_group)).setOnCheckedChangeListener(ToggleListenerInterest);

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

                    Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex=cursor.getColumnIndex(projection[0]);
                    filePath=cursor.getString(columnIndex);
                    cursor.close();




                    Bitmap yourSelectedImage= BitmapFactory.decodeFile(filePath);
                    Log.d("shani","bitmao in profile........"+yourSelectedImage);

                    ParcelFileDescriptor parcelFileDescriptor =
                            null;
                    try {
                        parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
                        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();

                        yourSelectedImage =decodeSampledBitmapFromResource(fileDescriptor, 1000, 1000);
                        parcelFileDescriptor.close();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    Drawable d=new BitmapDrawable(yourSelectedImage);

//                    circleImageView.setBackground(d);

                    try {
                        scaledBitmap = modifyOrientation(yourSelectedImage,filePath);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        String item = parent.getItemAtPosition(position).toString();


        if (parent.getId()==R.id.spin_month){

            month = item;
        }
        if (parent.getId()==R.id.spin_date){

            day = item;
        }
        if (parent.getId()==R.id.spin_year){

            year = item;
        }

    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
